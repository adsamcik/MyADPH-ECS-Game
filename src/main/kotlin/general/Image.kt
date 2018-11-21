package general

import org.w3c.dom.HTMLImageElement
import kotlin.browser.document

object Image {
	fun newInstance() = document.createElement("img") as HTMLImageElement
	fun newInstance(src: String): HTMLImageElement {
		val instance = newInstance()
		instance.src = src
		return instance
	}

	fun newInstance(src: String, onLoad: (HTMLImageElement) -> Unit): HTMLImageElement {
		val instance = newInstance(src)
		instance.onload = {
			onLoad(instance)
		}
		return instance
	}
}