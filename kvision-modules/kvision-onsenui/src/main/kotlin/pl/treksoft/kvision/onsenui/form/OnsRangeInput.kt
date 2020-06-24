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

package pl.treksoft.kvision.onsenui.form

import com.github.snabbdom.VNode
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.core.StringPair
import pl.treksoft.kvision.form.range.RangeInput
import pl.treksoft.kvision.utils.set

/**
 * OnsenUI range input component.
 *
 * @constructor Creates a range input component.
 * @param value number input value
 * @param min minimal value (default 0)
 * @param max maximal value (default 100)
 * @param step step value (default 1)
 * @param inputId the ID of the input element
 * @param classes a set of CSS class names
 * @param init an initializer extension function
 */
open class OnsRangeInput(
    value: Number? = null,
    min: Number = 0,
    max: Number = 100,
    step: Number = DEFAULT_STEP,
    inputId: String? = null,
    classes: Set<String> = setOf(),
    init: (OnsRangeInput.() -> Unit)? = null
) : RangeInput(value, min, max, step, classes + "kv-ons-form-control") {

    /**
     * The ID of the input element.
     */
    var inputId: String? by refreshOnUpdate(inputId)

    /**
     * A modifier attribute to specify custom styles.
     */
    var modifier: String? by refreshOnUpdate()

    init {
        @Suppress("LeakingThis")
        init?.invoke(this)
    }

    override fun render(): VNode {
        return render("ons-range")
    }

    override fun getSnAttrs(): List<StringPair> {
        val sn = super.getSnAttrs().toMutableList()
        inputId?.let {
            sn.add("input-id" to it)
        }
        modifier?.let {
            sn.add("modifier" to it)
        }
        return sn
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Container.onsRangeInput(
    value: Number? = null,
    min: Number = 0,
    max: Number = 100,
    step: Number = DEFAULT_STEP,
    inputId: String? = null,
    classes: Set<String>? = null,
    className: String? = null,
    init: (OnsRangeInput.() -> Unit)? = null
): OnsRangeInput {
    val onsRangeInput =
        OnsRangeInput(value, min, max, step, inputId, classes ?: className.set, init)
    this.add(onsRangeInput)
    return onsRangeInput
}
