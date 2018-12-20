/*
 * Acorn - Decoupling navigation from Android
 * Copyright (C) 2018 Niek Haarman
 *
 * Acorn is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Acorn is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Acorn.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.nhaarman.acorn.samples.transitionanimation

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.transition.ChangeBounds
import androidx.transition.Slide
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.nhaarman.acorn.android.transition.Transition
import com.nhaarman.acorn.android.util.inflate
import com.nhaarman.acorn.samples.transitionanimation.album.AlbumViewController
import kotlinx.android.synthetic.main.album_scene.view.*
import kotlinx.android.synthetic.main.album_scene_contents.view.*
import kotlinx.android.synthetic.main.image_scene_contents.view.*

object ImageAlbumTransition : Transition {

    override fun execute(parent: ViewGroup, callback: Transition.Callback) {
        val root = parent.root

        root.inflate<View>(R.layout.album_scene_contents, true)

        callback.attach(AlbumViewController(parent))

        root.doOnPreDraw {
            root.albumRV.clickedView()?.let {
                it.translationZ = 100f
                ViewCompat.setTransitionName(it, "image_view")
            }
        }

        val transition = TransitionSet()
            .addTransition(
                ChangeBounds()
                    .addTarget("image_view")
            )
            .addTransition(
                Slide()
                    .addTarget(R.id.cardView)
            )

        transition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: androidx.transition.Transition) {
                root.albumRV.clickedView()?.let {
                    it.translationZ = 0f
                    ViewCompat.setTransitionName(it, null)
                }

                callback.onComplete(AlbumViewController(parent))
            }
        })

        ViewCompat.setTransitionName(root.imageView, "image_view")
        TransitionManager.beginDelayedTransition(parent, transition)

        root.removeView(root.imageView)
        root.removeView(root.cardView)
    }
}