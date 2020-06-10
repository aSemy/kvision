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

package pl.treksoft.kvision.onsenui.carousel

import com.github.snabbdom.VNode
import org.w3c.dom.events.MouseEvent
import pl.treksoft.kvision.html.Align
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.utils.set

/**
 * A carousel item component.
 *
 * @constructor Creates a carousel item component.
 * @param content the content of the component.
 * @param rich whether [content] can contain HTML code
 * @param align text align
 * @param classes a set of CSS class names
 * @param init an initializer extension function
 */
open class CarouselItem(
    content: String? = null,
    rich: Boolean = false,
    align: Align? = null,
    classes: Set<String> = setOf(),
    init: (CarouselItem.() -> Unit)? = null
) : Div(content, rich, align, classes) {

    init {
        @Suppress("LeakingThis")
        init?.invoke(this)
    }

    override fun render(elementName: String, children: Array<dynamic>): VNode {
        return super.render("ons-carousel-item", children)
    }

    /**
     * A convenient helper for easy setting onClick event handler.
     */
    open fun onClick(handler: CarouselItem.(MouseEvent) -> Unit): CarouselItem {
        this.setEventListener<CarouselItem> {
            click = { e ->
                self.handler(e)
            }
        }
        return this
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Carousel.carouselItem(
    content: String? = null,
    rich: Boolean = false,
    align: Align? = null,
    classes: Set<String>? = null,
    className: String? = null,
    init: (CarouselItem.() -> Unit)? = null
): CarouselItem {
    val carouselItem = CarouselItem(content, rich, align, classes ?: className.set, init)
    this.add(carouselItem)
    return carouselItem
}
