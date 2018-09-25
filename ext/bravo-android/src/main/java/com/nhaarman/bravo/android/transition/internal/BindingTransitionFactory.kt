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

package com.nhaarman.bravo.android.transition.internal

import com.nhaarman.bravo.android.presentation.ViewFactory
import com.nhaarman.bravo.android.transition.DefaultTransitionFactory
import com.nhaarman.bravo.android.transition.Transition
import com.nhaarman.bravo.android.transition.TransitionFactory
import com.nhaarman.bravo.navigation.TransitionData
import com.nhaarman.bravo.presentation.Scene

internal class BindingTransitionFactory(
    private val viewFactory: ViewFactory,
    private val bindings: Sequence<TransitionBinding>
) : TransitionFactory {

    private val delegate by lazy { DefaultTransitionFactory(viewFactory) }

    override fun transitionFor(previousScene: Scene<*>, newScene: Scene<*>, data: TransitionData?): Transition {
        return bindings
            .mapNotNull { it.transitionFor(previousScene, newScene, data) }
            .firstOrNull()
            ?: delegate.transitionFor(previousScene, newScene, data)
    }
}