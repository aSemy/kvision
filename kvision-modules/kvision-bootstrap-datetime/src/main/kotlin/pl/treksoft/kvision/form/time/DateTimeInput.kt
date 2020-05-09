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
package pl.treksoft.kvision.form.time

import com.github.snabbdom.VNode
import pl.treksoft.jquery.jQuery
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.core.StringBoolPair
import pl.treksoft.kvision.form.FormInput
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Icon
import pl.treksoft.kvision.html.icon
import pl.treksoft.kvision.html.span
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.types.toDateF
import pl.treksoft.kvision.types.toStringF
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.set
import kotlin.js.Date

internal const val DEFAULT_STEPPING = 5

/**
 * Basic date/time chooser component.
 *
 * @constructor
 * @param value date/time input value
 * @param format date/time format (default YYYY-MM-DD HH:mm)
 * @param classes a set of CSS class names
 */
@Suppress("TooManyFunctions")
open class DateTimeInput(
    value: Date? = null, format: String = "YYYY-MM-DD HH:mm",
    classes: Set<String> = setOf()
) : SimplePanel(classes + "input-group" + "date"), FormInput {

    private var initialized = false

    val input = TextInput(value = value?.toStringF(format))
    private lateinit var icon: Icon
    private val addon = Div(classes = setOf("input-group-append")) {
        span(classes = setOf("input-group-text", "datepickerbutton")) {
            icon = icon(getIconClass(format))
        }
    }

    init {
        addInternal(input)
        addInternal(addon)
    }

    /**
     * Date/time input value.
     */
    var value
        get() = input.value?.toDateF(format)
        set(value) {
            input.value = value?.toStringF(format)
            refreshState()
        }

    /**
     * Date/time format.
     */
    var format by refreshOnUpdate(format) { refreshDatePicker() }

    /**
     * The placeholder for the date/time input.
     */
    var placeholder
        get() = input.placeholder
        set(value) {
            input.placeholder = value
        }

    /**
     * The name attribute of the generated HTML input element.
     */
    override var name
        get() = input.name
        set(value) {
            input.name = value
        }

    /**
     * Determines if the field is disabled.
     */
    override var disabled
        get() = input.disabled
        set(value) {
            input.disabled = value
        }

    /**
     * Determines if the text input is automatically focused.
     */
    var autofocus
        get() = input.autofocus
        set(value) {
            input.autofocus = value
        }

    /**
     * Determines if the date/time input is read-only.
     */
    var readonly
        get() = input.readonly
        set(value) {
            input.readonly = value
        }

    /**
     * The size of the input.
     */
    override var size
        get() = input.size
        set(value) {
            input.size = value
        }

    /**
     * The validation status of the input.
     */
    override var validationStatus
        get() = input.validationStatus
        set(value) {
            input.validationStatus = value
            refresh()
        }

    /**
     * Days of the week that should be disabled. Multiple values should be comma separated.
     */
    var daysOfWeekDisabled by refreshOnUpdate(arrayOf<Int>()) { refreshDatePicker() }

    /**
     * Determines if *Clear* button should be visible.
     */
    var showClear by refreshOnUpdate(true) { refreshDatePicker() }

    /**
     * Determines if *Close* button should be visible.
     */
    var showClose by refreshOnUpdate(true) { refreshDatePicker() }

    /**
     * Determines if *Today* button should be visible.
     */
    var showTodayButton by refreshOnUpdate(true) { refreshDatePicker() }

    /**
     * The increment used to build the hour view.
     */
    var stepping by refreshOnUpdate(DEFAULT_STEPPING) { refreshDatePicker() }

    /**
     * Prevents date selection before this date.
     */
    var minDate: Date? by refreshOnUpdate { refreshDatePicker() }

    /**
     * Prevents date selection after this date.
     */
    var maxDate: Date? by refreshOnUpdate { refreshDatePicker() }

    /**
     * Shows date and time pickers side by side.
     */
    var sideBySide by refreshOnUpdate(false) { refreshDatePicker() }

    /**
     * An array of enabled dates.
     */
    var enabledDates by refreshOnUpdate(arrayOf<Date>()) { refreshDatePicker() }

    /**
     * An array of disabled dates.
     */
    var disabledDates by refreshOnUpdate(arrayOf<Date>()) { refreshDatePicker() }

    /**
     * Allow date picker for readonly component.
     */
    var ignoreReadonly by refreshOnUpdate(false) { refreshDatePicker() }

    private fun refreshState() {
        if (initialized) getElementJQueryD().data("DateTimePicker").date(value)
    }

    private fun getIconClass(format: String): String {
        return if (format.contains("YYYY") || format.contains("MM") || format.contains("DD")) {
            "fas fa-calendar-alt"
        } else {
            "fas fa-clock"
        }
    }

    override fun getSnClass(): List<StringBoolPair> {
        val cl = super.getSnClass().toMutableList()
        validationStatus?.let {
            cl.add(it.className to true)
        }
        return cl
    }

    protected open fun refreshDatePicker() {
        if (initialized) {
            getElementJQueryD()?.data("DateTimePicker").destroy()
        }
        initDateTimePicker()
        icon.icon = getIconClass(format)
    }

    /**
     * Open date/time chooser popup.
     */
    open fun showPopup() {
        if (initialized) getElementJQueryD()?.data("DateTimePicker").show()
    }

    /**
     * Hides date/time chooser popup.
     */
    open fun hidePopup() {
        if (initialized) getElementJQueryD()?.data("DateTimePicker").hide()
    }

    /**
     * Toggles date/time chooser popup.
     */
    open fun togglePopup() {
        if (initialized) getElementJQueryD()?.data("DateTimePicker").toggle()
    }

    @Suppress("UnsafeCastFromDynamic")
    override fun afterInsert(node: VNode) {
        this.initDateTimePicker()
        this.initEventHandlers()
        initialized = true
    }

    override fun afterDestroy() {
        if (initialized) {
            getElementJQueryD()?.data("DateTimePicker")?.destroy()
            initialized = false
        }
    }

    private fun initDateTimePicker() {
        val language = I18n.language
        val self = this
        getElementJQueryD()?.datetimepicker(obj {
            this.useCurrent = false
            this.format = format
            this.stepping = stepping
            this.showClear = showClear
            this.showClose = showClose
            this.showTodayButton = showTodayButton
            this.sideBySide = sideBySide
            this.ignoreReadonly = ignoreReadonly
            if (minDate != null) this.minDate = minDate
            if (maxDate != null) this.maxDate = maxDate
            if (daysOfWeekDisabled.isNotEmpty()) this.daysOfWeekDisabled = daysOfWeekDisabled
            if (enabledDates.isNotEmpty()) this.enabledDates = enabledDates
            if (disabledDates.isNotEmpty()) this.disabledDates = disabledDates
            this.locale = language
            this.icons = obj {
                this.time = "far fa-clock"
                this.date = "far fa-calendar"
                this.up = "fas fa-arrow-up"
                this.down = "fas fa-arrow-down"
                this.previous = "fas fa-chevron-left"
                this.next = "fas fa-chevron-right"
                this.today = "fas fa-calendar-check"
                this.clear = "far fa-trash-alt"
                this.close = "far fa-times-circle"
            }
            this.tooltips = obj {
                this.today = ""
                this.clear = ""
                this.close = ""
                this.selectMonth = ""
                this.prevMonth = ""
                this.nextMonth = ""
                this.selectYear = ""
                this.prevYear = ""
                this.nextYear = ""
                this.selectDecade = ""
                this.prevDecade = ""
                this.nextDecade = ""
                this.prevCentury = ""
                this.nextCentury = ""
                this.pickHour = ""
                this.incrementHour = ""
                this.decrementHour = ""
                this.pickMinute = ""
                this.incrementMinute = ""
                this.decrementMinute = ""
                this.pickSecond = ""
                this.incrementSecond = ""
                this.decrementSecond = ""
                this.togglePeriod = ""
                this.selectTime = ""
            }
            this.keyBinds = obj {
                enter = {
                    self.togglePopup()
                }
            }
        })
    }

    private fun initEventHandlers() {
        this.getElementJQuery()?.on("dp.change") { e, _ ->
            val moment = e.asDynamic().date
            @Suppress("UnsafeCastFromDynamic")
            if (moment) {
                this.value = moment.toDate()
            } else {
                this.value = null
            }
            @Suppress("UnsafeCastFromDynamic")
            this.dispatchEvent("change", obj { detail = e })
        }
        this.getElementJQuery()?.on("dp.error") { e, _ ->
            this.value = null
            @Suppress("UnsafeCastFromDynamic")
            this.dispatchEvent("change", obj { detail = e })
        }
        this.getElementJQuery()?.on("dp.show") { e, _ ->
            val inTabulator = this.getElementJQuery()?.closest(".tabulator-cell")?.length == 1
            if (inTabulator) {
                val datepicker = jQuery("body").find(".bootstrap-datetimepicker-widget:last")
                val position = datepicker.offset()
                val parent = datepicker.parent()
                val parentPos = parent.offset()
                val width = datepicker.width()
                val parentWid = parent.width()
                datepicker.appendTo("body")
                @Suppress("UnsafeCastFromDynamic")
                datepicker.css(obj {
                    this.position = "absolute"
                    this.top = position.top
                    this.bottom = "auto"
                    this.left = position.left
                    this.right = "auto"
                })
                if (parentPos.left.toInt() + parentWid.toInt() < position.left.toInt() + width.toInt()) {
                    var newLeft = parentPos.left.toInt()
                    newLeft += parentWid.toInt() / 2
                    newLeft -= width.toInt() / 2
                    @Suppress("UnsafeCastFromDynamic")
                    datepicker.css(obj { this.left = newLeft })
                }
            }
            @Suppress("UnsafeCastFromDynamic")
            this.dispatchEvent("showBsDateTime", obj { detail = e })
        }
        this.getElementJQuery()?.on("dp.hide") { e, _ ->
            @Suppress("UnsafeCastFromDynamic")
            this.dispatchEvent("hideBsDateTime", obj { detail = e })
        }
    }

    /**
     * Get value of date/time input control as String
     * @return value as a String
     */
    fun getValueAsString(): String? {
        return value?.toStringF(format)
    }

    /**
     * Makes the input element focused.
     */
    override fun focus() {
        input.focus()
    }

    /**
     * Makes the input element blur.
     */
    override fun blur() {
        input.blur()
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Container.dateTimeInput(
    value: Date? = null, format: String = "YYYY-MM-DD HH:mm",
    classes: Set<String>? = null,
    className: String? = null,
    init: (DateTimeInput.() -> Unit)? = null
): DateTimeInput {
    val dateTimeInput = DateTimeInput(value, format, classes ?: className.set).apply { init?.invoke(this) }
    this.add(dateTimeInput)
    return dateTimeInput
}
