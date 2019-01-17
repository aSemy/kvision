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
package pl.treksoft.kvision.form.spinner

import com.github.snabbdom.VNode
import pl.treksoft.jquery.JQuery
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.core.StringBoolPair
import pl.treksoft.kvision.core.StringPair
import pl.treksoft.kvision.core.Widget
import pl.treksoft.kvision.form.FormInput
import pl.treksoft.kvision.form.InputSize
import pl.treksoft.kvision.utils.obj

/**
 * Spinner buttons layout types.
 */
enum class ButtonsType {
    NONE,
    HORIZONTAL,
    VERTICAL
}

/**
 * Spinner force rounding types.
 */
enum class ForceType(internal val value: String) {
    NONE("none"),
    ROUND("round"),
    FLOOR("floor"),
    CEIL("cail")
}

internal const val DEFAULT_STEP = 1.0

/**
 * The basic component for spinner control.
 *
 * @constructor
 * @param value spinner value
 * @param min minimal value
 * @param max maximal value
 * @param step step value (default 1)
 * @param decimals number of decimal digits (default 0)
 * @param buttonsType spinner buttons type
 * @param forceType spinner force rounding type
 * @param classes a set of CSS class names
 */
@Suppress("TooManyFunctions")
open class SpinnerInput(
    value: Number? = null, min: Int? = null, max: Int? = null, step: Double = DEFAULT_STEP,
    decimals: Int = 0, buttonsType: ButtonsType = ButtonsType.VERTICAL,
    forceType: ForceType = ForceType.NONE,
    classes: Set<String> = setOf()
) : Widget(classes + "form-control"), FormInput {

    init {
        this.addSurroundingCssClass("input-group")
        if (buttonsType == ButtonsType.NONE) {
            this.addSurroundingCssClass("kv-spinner-btn-none")
        } else {
            this.removeSurroundingCssClass("kv-spinner-btn-none")
        }
        if (buttonsType == ButtonsType.VERTICAL) {
            this.addSurroundingCssClass("kv-spinner-btn-vertical")
        } else {
            this.removeSurroundingCssClass("kv-spinner-btn-vertical")
        }
        this.surroundingSpan = true
        this.refreshSpinner()
        this.setInternalEventListener<SpinnerInput> {
            change = {
                self.changeValue()
            }
        }
    }

    /**
     * Spinner value.
     */
    var value by refreshOnUpdate(value) { refreshState() }
    /**
     * The value attribute of the generated HTML input element.
     *
     * This value is placed directly in generated HTML code, while the [value] property is dynamically
     * bound to the spinner input value.
     */
    var startValue by refreshOnUpdate(value) { this.value = it; refresh() }
    /**
     * Minimal value.
     */
    var min by refreshOnUpdate(min) { refreshSpinner() }
    /**
     * Maximal value.
     */
    var max by refreshOnUpdate(max) { refreshSpinner() }
    /**
     * Step value.
     */
    var step by refreshOnUpdate(step) { refreshSpinner() }
    /**
     * Number of decimal digits value.
     */
    var decimals by refreshOnUpdate(decimals) { refreshSpinner() }
    /**
     * Spinner buttons type.
     */
    var buttonsType by refreshOnUpdate(buttonsType) { refreshSpinner() }
    /**
     * Spinner force rounding type.
     */
    var forceType by refreshOnUpdate(forceType) { refreshSpinner() }
    /**
     * The placeholder for the spinner input.
     */
    var placeholder: String? by refreshOnUpdate()
    /**
     * The name attribute of the generated HTML input element.
     */
    override var name: String? by refreshOnUpdate()
    /**
     * Determines if the field is disabled.
     */
    override var disabled by refreshOnUpdate(false)
    /**
     * Determines if the spinner is automatically focused.
     */
    var autofocus: Boolean? by refreshOnUpdate()
    /**
     * Determines if the spinner is read-only.
     */
    var readonly: Boolean? by refreshOnUpdate()
    /**
     * The size of the input.
     */
    override var size: InputSize? by refreshOnUpdate()

    private var siblings: JQuery? = null

    override fun render(): VNode {
        return render("input")
    }

    override fun getSnClass(): List<StringBoolPair> {
        val cl = super.getSnClass().toMutableList()
        size?.let {
            cl.add(it.className to true)
        }
        return cl
    }

    @Suppress("ComplexMethod")
    override fun getSnAttrs(): List<StringPair> {
        val sn = super.getSnAttrs().toMutableList()
        sn.add("type" to "text")
        startValue?.let {
            sn.add("value" to it.toString())
        }
        placeholder?.let {
            sn.add("placeholder" to translate(it))
        }
        name?.let {
            sn.add("name" to it)
        }
        autofocus?.let {
            if (it) {
                sn.add("autofocus" to "autofocus")
            }
        }
        readonly?.let {
            if (it) {
                sn.add("readonly" to "readonly")
            }
        }
        if (disabled) {
            sn.add("disabled" to "disabled")
            value?.let {
                sn.add("value" to it.toString())
            }
        }
        return sn
    }

    protected open fun changeValue() {
        val v = getElementJQuery()?.`val`() as String?
        if (v != null && v.isNotEmpty()) {
            this.value = v.toDoubleOrNull()
            this.value?.let {
                if (min != null && it.toInt() < min ?: 0) this.value = min
                if (max != null && it.toInt() > max ?: 0) this.value = max
            }
        } else {
            this.value = null
        }
    }

    @Suppress("UnsafeCastFromDynamic")
    override fun afterInsert(node: VNode) {
        getElementJQueryD()?.TouchSpin(getSettingsObj())
        siblings = getElementJQuery()?.parent(".bootstrap-touchspin")?.children("span")
        this.getElementJQuery()?.on("change") { e, _ ->
            if (e.asDynamic().isTrigger != null) {
                val event = org.w3c.dom.events.Event("change")
                this.getElement()?.dispatchEvent(event)
            }
        }
        this.getElementJQuery()?.on("touchspin.on.min") { e, _ ->
            this.dispatchEvent("onMinBsSpinner", obj { detail = e })
        }
        this.getElementJQuery()?.on("touchspin.on.max") { e, _ ->
            this.dispatchEvent("onMaxBsSpinner", obj { detail = e })
        }
        refreshState()
    }

    override fun afterDestroy() {
        siblings?.remove()
        siblings = null
    }

    /**
     * Returns the value of the spinner as a String.
     * @return value as a String
     */
    fun getValueAsString(): String? {
        return value?.toString()
    }

    /**
     * Change value in plus.
     */
    fun spinUp(): SpinnerInput {
        getElementJQueryD()?.trigger("touchspin.uponce")
        return this
    }

    /**
     * Change value in minus.
     */
    fun spinDown(): SpinnerInput {
        getElementJQueryD()?.trigger("touchspin.downonce")
        return this
    }

    private fun refreshState() {
        value?.let {
            getElementJQuery()?.`val`(it.toString())
        } ?: getElementJQueryD()?.`val`(null)
    }

    private fun refreshSpinner() {
        getElementJQueryD()?.trigger("touchspin.updatesettings", getSettingsObj())
    }

    private fun getSettingsObj(): dynamic {
        val verticalbuttons = buttonsType == ButtonsType.VERTICAL || buttonsType == ButtonsType.NONE
        return obj {
            this.min = min
            this.max = max
            this.step = step
            this.decimals = decimals
            this.verticalbuttons = verticalbuttons
            this.forcestepdivisibility = forceType.value
            if (verticalbuttons) {
                this.verticalup = "<i class=\"fa fa-caret-up\"></i>"
                this.verticaldown = "<i class=\"fa fa-caret-down\"></i>"
            }
            this.buttondown_class = "btn btn-default"
            this.buttonup_class = "btn btn-default"
        }
    }

    /**
     * Makes the input element focused.
     */
    open fun focus() {
        getElementJQuery()?.focus()
    }

    /**
     * Makes the input element blur.
     */
    open fun blur() {
        getElementJQuery()?.blur()
    }

    companion object {
        internal var counter = 0

        /**
         * DSL builder extension function.
         *
         * It takes the same parameters as the constructor of the built component.
         */
        fun Container.spinnerInput(
            value: Number? = null, min: Int? = null, max: Int? = null, step: Double = DEFAULT_STEP,
            decimals: Int = 0, buttonsType: ButtonsType = ButtonsType.VERTICAL,
            forceType: ForceType = ForceType.NONE, classes: Set<String> = setOf(),
            init: (SpinnerInput.() -> Unit)? = null
        ): SpinnerInput {
            val spinnerInput = SpinnerInput(value, min, max, step, decimals, buttonsType, forceType, classes).apply {
                init?.invoke(
                    this
                )
            }
            this.add(spinnerInput)
            return spinnerInput
        }
    }
}
