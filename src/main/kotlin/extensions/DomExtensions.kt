package extensions

import definition.constant.EventConstants
import org.w3c.dom.*
import org.w3c.dom.events.Event

fun Element.removeAllChildren() {
	while (firstChild != null) {
		removeChild(requireNotNull(firstChild))
	}
}

typealias OnClickListener = (event: Event) -> Unit

fun Node.addOnClickListener(listener: OnClickListener) {
	addEventListener(EventConstants.CLICK, listener)
}

typealias ElementInit<T> = (element: T) -> Unit

@Suppress("unchecked_cast")
inline fun <T : Element> Document.createElementTyped(
	localName: String,
	options: ElementCreationOptions? = null
): T {
	return if (options != null) {
		createElement(localName, options) as T
	} else {
		createElement(localName) as T
	}
}

fun Document.createDiv(parent: Node? = null, init: ElementInit<HTMLDivElement>? = null): HTMLDivElement {
	return createElementTyped("div", parent = parent, init = init)
}

fun Document.createSpan(parent: Node? = null, init: ElementInit<HTMLSpanElement>? = null): HTMLSpanElement {
	return createElementTyped("span", parent = parent, init = init)
}

fun Document.createButton(parent: Node? = null, init: ElementInit<HTMLButtonElement>? = null): HTMLButtonElement {
	return createElementTyped("button", parent = parent, init = init)
}

fun Document.createInput(parent: Node? = null, init: ElementInit<HTMLInputElement>? = null): HTMLInputElement {
	return createElementTyped("input", parent = parent, init = init)
}

fun Document.createTitle1(parent: Node? = null, init: ElementInit<HTMLTitleElement>? = null): HTMLTitleElement {
	return createElementTyped("h1", parent = parent, init = init)
}

fun Document.createTitle2(parent: Node? = null, init: ElementInit<HTMLTitleElement>? = null): HTMLTitleElement {
	return createElementTyped("h2", parent = parent, init = init)
}

fun Document.createTitle3(parent: Node? = null, init: ElementInit<HTMLTitleElement>? = null): HTMLTitleElement {
	return createElementTyped("h3", parent = parent, init = init)
}

@Suppress("UNCHECKED_CAST")
fun <T : Element> Document.createElementTyped(
	localName: String,
	parent: Node? = null,
	init: ElementInit<T>? = null
): T {
	val typed = createElementTyped<T>(localName)
	init?.invoke(typed)
	parent?.appendChild(typed)
	return typed
}

inline fun Node.requireParentElement() = requireNotNull(parentElement)