package extensions

import definition.constant.EventConstants
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.events.Event

fun Element.removeAllChildren() {
	while (firstChild != null) {
		removeChild(requireNotNull(firstChild))
	}
}

fun Node.addOnClickListener(listener: (event: Event) -> Unit) {
	addEventListener(EventConstants.CLICK, listener)
}

typealias ElementInit = (element: Element) -> Unit

fun Document.createDiv(init: ElementInit? = null): Element {
	return createElement("div", init)
}

fun Document.createButton(init: ElementInit? = null): Element {
	return createElement("button", init)
}

fun Document.createInput(init: ElementInit? = null): Element {
	return createElement("input", init)
}

fun Document.createTitle1(init: ElementInit? = null): Element {
	return createElement("h1", init)
}

fun Document.createTitle2(init: ElementInit? = null): Element {
	return createElement("h2", init)
}

fun Document.createTitle3(init: ElementInit? = null): Element {
	return createElement("h3", init)
}

fun Document.createElement(localName: String, init: ElementInit? = null): Element {
	return createElement(localName).apply { init?.invoke(this) }
}
