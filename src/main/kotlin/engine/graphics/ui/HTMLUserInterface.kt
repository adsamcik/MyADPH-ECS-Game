package engine.graphics.ui

import engine.graphics.ui.HTMLButton.BUTTON_ITEM_STYLE
import engine.graphics.ui.HTMLButton.BUTTON_UI_BIG_STYLE
import engine.graphics.ui.HTMLButton.BUTTON_UI_STYLE
import extensions.*
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.dom.addClass

fun Node.addList(orientation: MenuOrientation, init: ElementInit<HTMLUListElement>): HTMLUListElement {
	val list = document.createElementTyped("ul", this, init)
	val className = when (orientation) {
		MenuOrientation.HORIZONTAL -> "horizontal-menu"
		MenuOrientation.VERTICAL -> "vertical-menu"
	}
	list.addClass(className)
	return list
}

fun HTMLUListElement.addListItem(init: ElementInit<HTMLLIElement>): HTMLLIElement {
	return document.createElementTyped("li", this, init)
}

inline fun Node.addItemButton(
	title: String,
	noinline onClickListener: OnClickListener,
	noinline init: ElementInit<HTMLButtonElement>? = null
): HTMLButtonElement = addButton(title, onClickListener, BUTTON_ITEM_STYLE, init)

inline fun Node.addUIButton(
	title: String,
	noinline onClickListener: OnClickListener,
	noinline init: ElementInit<HTMLButtonElement>? = null
): HTMLButtonElement = addButton(title, onClickListener, BUTTON_UI_STYLE, init)

inline fun Node.addUIBigButton(
	title: String,
	noinline onClickListener: OnClickListener,
	noinline init: ElementInit<HTMLButtonElement>? = null
): HTMLButtonElement = addButton(title, onClickListener, BUTTON_UI_BIG_STYLE, init)

fun Node.addButton(
	title: String,
	onClickListener: OnClickListener,
	className: String? = null,
	init: ElementInit<HTMLButtonElement>? = null
): HTMLButtonElement {
	val button = document.createButton(className, this, init)
	button.textContent = title
	button.addOnClickListener(onClickListener)
	return button
}

fun Document.createButton(
	className: String? = null,
	parent: Node? = null,
	init: (ElementInit<HTMLButtonElement>)? = null
): HTMLButtonElement {
	return createElementTyped("button", parent = parent, init = init).apply {
		if (className != null) this.className = className
	}
}

fun Document.createUIBigButton(text: String, init: (ElementInit<HTMLButtonElement>)? = null) =
	createButton(BUTTON_UI_BIG_STYLE, init = init).apply {
		textContent = text
	}

fun Document.createUIButton(text: String, init: (ElementInit<HTMLButtonElement>)? = null) =
	createButton(BUTTON_UI_STYLE, init = init).apply {
		textContent = text
	}

fun Document.createSelectionButton(text: String, init: (ElementInit<HTMLButtonElement>)? = null) =
	createButton(BUTTON_ITEM_STYLE, init = init).apply {
		textContent = text
	}

enum class MenuOrientation {
	HORIZONTAL,
	VERTICAL
}

object HTMLButton {
	const val BUTTON_UI_STYLE = "button-ui"
	const val BUTTON_ITEM_STYLE = "button-item"
	const val BUTTON_UI_BIG_STYLE = "button-ui-big"
}