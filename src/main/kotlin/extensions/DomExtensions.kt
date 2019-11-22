package extensions

import definition.constant.EventConstants
import org.w3c.dom.*
import org.w3c.dom.events.Event

fun Element.removeAllChildren() {
	while (firstChild != null) {
		removeChild(requireNotNull(firstChild))
	}
}

fun Node.addOnClickListener(listener: (event: Event) -> Unit) {
	addEventListener(EventConstants.CLICK, listener)
}

typealias ElementInit<T> = (element: T) -> Unit

@Suppress("unchecked_cast")
fun <T: Element> Document.createElementTyped(localName: String, options: ElementCreationOptions? = null): T {
	return if(options != null) {
		createElement(localName, options) as T
	} else {
		createElement(localName) as T
	}
}

fun Document.createDiv(init: ElementInit<HTMLDivElement>? = null): HTMLDivElement {
	return createElement("div", init)
}

fun Document.createButton(init: ElementInit<HTMLButtonElement>? = null): HTMLButtonElement {
	return createElement("button", init)
}

fun Document.createInput(init: ElementInit<HTMLInputElement>? = null): HTMLInputElement {
	return createElement("input", init)
}

fun Document.createTitle1(init: ElementInit<HTMLTitleElement>? = null): HTMLTitleElement {
	return createElement("h1", init)
}

fun Document.createTitle2(init: ElementInit<HTMLTitleElement>? = null): HTMLTitleElement {
	return createElement("h2", init)
}

fun Document.createTitle3(init: ElementInit<HTMLTitleElement>? = null): HTMLTitleElement {
	return createElement("h3", init)
}

@Suppress("UNCHECKED_CAST")
fun <T : Element> Document.createElement(localName: String, init: ElementInit<T>? = null): T {
	val typed = createElement(localName) as? T
	requireNotNull(typed)
	init?.invoke(typed)
	return typed
}
