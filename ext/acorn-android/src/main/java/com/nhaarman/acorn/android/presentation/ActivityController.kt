/*
 *    Copyright 2018 Niek Haarman
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.nhaarman.acorn.android.presentation

import android.app.Activity
import android.content.Intent
import com.nhaarman.acorn.presentation.Container
import com.nhaarman.acorn.presentation.Scene

/**
 * A [Container] specialization that can be used to dispatch [Scene]s as Activities.
 */
interface ActivityController : Container {

    /**
     * Creates the [Intent] that can be used to start the [Activity].
     */
    fun createIntent(): Intent

    /**
     * Called when the [Activity] started with the [Intent] provided by
     * [createIntent] finishes.
     */
    fun onResult(resultCode: Int, data: Intent?)
}
