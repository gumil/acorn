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

import com.nhaarman.acorn.navigation.StackNavigator
import com.nhaarman.acorn.presentation.Container
import com.nhaarman.acorn.presentation.Scene
import com.nhaarman.acorn.samples.transitionanimation.album.AlbumScene
import com.nhaarman.acorn.samples.transitionanimation.image.ImageScene
import com.nhaarman.acorn.state.NavigatorState
import com.nhaarman.acorn.state.SceneState
import kotlin.reflect.KClass

class TransitionAnimationNavigator(savedState: NavigatorState?) : StackNavigator(savedState), AlbumScene.Events {

    override fun initialStack(): List<Scene<out Container>> {
        return listOf(AlbumScene(this, null))
    }

    override fun onImageClicked(imageUrl: String) {
        push(ImageScene(imageUrl, null))
    }

    override fun instantiateScene(sceneClass: KClass<out Scene<*>>, state: SceneState?): Scene<out Container> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}