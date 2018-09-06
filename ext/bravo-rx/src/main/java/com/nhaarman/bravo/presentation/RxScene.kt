/*
 * Bravo - Decoupling navigation from Android
 * Copyright (C) 2018 Niek Haarman
 *
 * Bravo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bravo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Bravo.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.nhaarman.bravo.presentation

import android.support.annotation.CallSuper
import arrow.core.Option
import arrow.core.toOption
import com.nhaarman.bravo.presentation.RxScene.Event.Attached
import com.nhaarman.bravo.presentation.RxScene.Event.Detached
import com.nhaarman.bravo.state.SceneState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject

/**
 * An abstract [Scene] implementation which provides a basic Rx implementation.
 *
 * Implementers of this class gain access to the [view] property to get notified
 * of view changes, and a [disposables] property is provided for easy clearing
 * of [Disposable]s.
 *
 * @see SaveableScene
 */
abstract class RxScene<V : RestorableContainer>(
    savedState: SceneState?
) : BaseSaveableScene<V>(savedState) {

    /**
     * A disposable container which will be cleared when this Scene receives a
     * call to [onStop].
     * Note that the instance will be _cleared_ not disposed, so this instance
     * can be reused multiple times.
     *
     * This container should only be used in the [onStart] method. When used in
     * other methods like [attach], memory leaks may occur.
     *
     * @see [CompositeDisposable.clear]
     */
    protected val disposables = CompositeDisposable()

    /**
     * A disposable container which will be disposed when this Scene receives a
     * call to [onDestroy].
     * Note that the instance will be _disposed_: Disposables added after
     * [onDestroy] is called will immediately be disposed.
     *
     * @see [CompositeDisposable.dispose]
     */
    private val sceneDisposables = CompositeDisposable()

    private val eventsSubject = BehaviorSubject.createDefault<Event<V>>(Detached)

    /**
     * Publishes a stream of optional [V] instances that are attached to this
     * Scene.
     */
    protected val view: Observable<Option<V>> = eventsSubject
        .map { event ->
            when (event) {
                is Attached<V> -> event.v.toOption()
                is Detached -> Option.empty()
            }
        }
        .replay(1).autoConnect()

    @CallSuper
    override fun attach(v: V) {
        super.attach(v)
        eventsSubject.onNext(Attached(v))
    }

    @CallSuper
    override fun detach(v: V) {
        eventsSubject.onNext(Detached)
        super.detach(v)
    }

    @CallSuper
    override fun onStop() {
        disposables.clear()
    }

    @CallSuper
    override fun onDestroy() {
        sceneDisposables.dispose()
    }

    @Suppress("unused")
    private sealed class Event<out V> {
        class Attached<V>(val v: V) : Event<V>()
        object Detached : Event<Nothing>()
    }

    /**
     * A utility function to combine an arbitrary stream of [T]'s with the
     * currently attached [V] instance.
     */
    protected fun <T> Observable<T>.combineWithLatestView(): Observable<Pair<T, V?>> {
        return Observables.combineLatest(this, view) { t, v -> t to v.orNull() }
    }

    /**
     * A utility function to switchMap the stream of attached [V] instances to
     * streams that [V] provides.
     */
    protected fun <R> Observable<Option<V>>.whenAvailable(f: (V) -> Observable<R>): Observable<R> {
        return switchMap { v -> v.orNull()?.let(f) ?: Observable.empty() }
    }

    /**
     * Returns an [Observable] that automatically connects at most once to the
     * receiving [ConnectableObservable] when the first Observer subscribes to it.
     * The connection will automatically be disposed of when [scene] is destroyed.
     *
     * This can be used to keep an Observable alive for as long as the Scene
     * lives.
     *
     * @see [ConnectableObservable.autoConnect].
     */
    protected fun <T> ConnectableObservable<T>.autoConnect(scene: RxScene<V>): Observable<T> {
        return autoConnect(1) { scene.sceneDisposables += it }
    }
}
