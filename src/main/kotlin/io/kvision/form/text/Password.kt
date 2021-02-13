/*
 * Copyright (c) 2017-present Robert Jaros
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.kvision.form.text

import io.kvision.core.Container
import io.kvision.state.ObservableState
import io.kvision.state.bind

/**
 * Form field password component.
 *
 * @constructor
 * @param value text input value
 * @param name the name attribute of the generated HTML input element
 * @param label label text bound to the input element
 * @param rich determines if [label] can contain HTML code
 * @param init an initializer extension function
 */
open class Password(
    value: String? = null,
    name: String? = null,
    label: String? = null,
    rich: Boolean = false,
    init: (Password.() -> Unit)? = null
) : Text(
    TextInputType.PASSWORD,
    value, name, label, rich
) {

    init {
        @Suppress("LeakingThis")
        init?.invoke(this)
    }

}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Container.password(
    value: String? = null,
    name: String? = null,
    label: String? = null,
    rich: Boolean = false,
    init: (Password.() -> Unit)? = null
): Password {
    val password = Password(value, name, label, rich, init)
    this.add(password)
    return password
}

/**
 * DSL builder extension function for observable state.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun <S> Container.password(
    state: ObservableState<S>,
    value: String? = null,
    name: String? = null,
    label: String? = null,
    rich: Boolean = false,
    init: (Password.(S) -> Unit)
) = password(value, name, label, rich).bind(state, true, init)