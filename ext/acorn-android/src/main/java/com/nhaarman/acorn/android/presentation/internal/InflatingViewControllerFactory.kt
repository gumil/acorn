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

package com.nhaarman.acorn.android.presentation.internal

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.nhaarman.acorn.android.presentation.ViewController
import com.nhaarman.acorn.android.presentation.ViewControllerFactory
import com.nhaarman.acorn.android.util.inflate
import com.nhaarman.acorn.presentation.Scene

internal class InflatingViewControllerFactory<V : View>(
    @LayoutRes private val layoutResId: Int,
    private val wrapper: (V) -> ViewController
) : ViewControllerFactory {

    override fun supports(scene: Scene<*>): Boolean {
        return true
    }

    override fun viewControllerFor(scene: Scene<*>, parent: ViewGroup): ViewController {
        return parent
            .inflate<V>(layoutResId)
            .let { view -> wrapper.invoke(view) }
    }
}