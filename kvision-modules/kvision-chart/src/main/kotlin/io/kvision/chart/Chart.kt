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
package io.kvision.chart

import io.kvision.ChartModule
import io.kvision.chart.js.ChartConfiguration
import io.kvision.core.Container
import io.kvision.core.Widget
import io.kvision.snabbdom.VNode

/**
 * Chart update modes.
 */
enum class UpdateMode(internal val mode: String) {
    ACTIVE("active"),
    HIDE("hide"),
    RESET("reset"),
    RESIZE("resize"),
    SHOW("show"),
    NONE("none")
}

/**
 * Chart component.
 *
 * @constructor
 * @param configuration chart configuration
 * @param chartWidth chart width in pixels
 * @param chartHeight chart height in pixels
 * @param className CSS class names
 * @param init an initializer extension function
 */
open class Chart(
    configuration: Configuration,
    chartWidth: Int? = null,
    chartHeight: Int? = null,
    className: String? = null,
    init: (Chart.() -> Unit)? = null
) : Widget(className) {

    /**
     * Chart configuration.
     */
    var configuration
        get() = chartCanvas.configuration
        set(value) {
            chartCanvas.configuration = value
        }

    /**
     * Native JS chart object.
     */
    val jsChart
        get() = chartCanvas.jsChart

    internal val chartCanvas: ChartCanvas = ChartCanvas(chartWidth, chartHeight, configuration)

    init {
        @Suppress("LeakingThis")
        init?.invoke(this)
    }

    override fun render(): VNode {
        return render("div", arrayOf(chartCanvas.renderVNode()))
    }

    /**
     * Returns chart configuration in the form of native JS object.
     */
    open fun getNativeConfig(): ChartConfiguration? {
        return chartCanvas.getNativeConfig()
    }

    /**
     * Resets the chart.
     */
    open fun reset() {
        chartCanvas.reset()
    }

    /**
     * Renders the chart.
     */
    open fun renderChart() {
        chartCanvas.renderChart()
    }

    /**
     * Stops the animation.
     */
    open fun stop() {
        chartCanvas.stop()
    }

    /**
     * Resizes the chart to the size of the container.
     */
    open fun resizeChart() {
        chartCanvas.resizeChart()
    }

    /**
     * Clears the chart.
     */
    open fun clearChart() {
        chartCanvas.clearChart()
    }

    /**
     * Updates the chart.
     */
    open fun update(updateMode: UpdateMode? = null) {
        chartCanvas.update(updateMode)
    }

    /**
     * Returns a base64 encoded string of the chart in its current state.
     */
    open fun toBase64Image(): String? {
        return chartCanvas.toBase64Image()
    }

    companion object {
        init {
            ChartModule.initialize()
        }

        fun registerPlugin(plugin: dynamic) {
            ChartModule.getConstructor().asDynamic().register(plugin)
        }

        fun unregisterPlugin(plugin: dynamic) {
            ChartModule.getConstructor().asDynamic().unregister(plugin)
        }
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Container.chart(
    configuration: Configuration,
    chartWidth: Int? = null,
    chartHeight: Int? = null,
    className: String? = null,
    init: (Chart.() -> Unit)? = null
): Chart {
    val chart = Chart(configuration, chartWidth, chartHeight, className, init)
    this.add(chart)
    return chart
}
