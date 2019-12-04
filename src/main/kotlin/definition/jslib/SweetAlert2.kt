package definition.jslib

import org.w3c.dom.Element
import kotlin.js.Json

@JsModule("sweetalert2")
@JsNonModule

external object Swal {
	fun fire(data: SwalData)
}

typealias OnSweetStateChange = (toast: Element) -> Unit
/**
 * Returns error message or null on success
 */
typealias InputValidator = (value: String) -> String?

data class SwalData(
	val title: String = "",
	val titleText: String = "",
	val html: String = "",
	val text: String = "",
	val icon: String? = undefined,
	val iconHtml: String? = undefined,
	val showClass: Json? = undefined,
	val hideClass: Json? = undefined,
	val footer: String = "",
	val input: String? = undefined,
	val grow: String? = undefined,
	val customClass: Json? = undefined,
	val timer: Int? = undefined,
	val timerProgressBar: Boolean = false,
	val animation: Boolean = true,
	val heightAuto: Boolean = true,
	val width: String? = undefined,
	val allowClickOutside: Boolean = true,
	val allowEscapeKey: Boolean = true,
	val allowEnterKey: Boolean = true,
	val toast: Boolean = false,
	val showConfirmButton: Boolean = true,
	val confirmButtonText: String = "OK",
	val showCancelButton: Boolean = false,
	val cancelButtonText: String = "Cancel",
	/**
	 * Validator for input field, may be async (Promise-returning) or sync.
	Returned (or resolved) value can be:

	a falsy value (undefined, null, false) for indicating success
	a string value (error message) for indicating failure

	 */
	val inputValidator: InputValidator? = undefined,
	val validationMessage: String? = undefined,
	val onBeforeOpen: OnSweetStateChange? = undefined,
	val onOpen: OnSweetStateChange? = undefined,
	val onClose: OnSweetStateChange? = undefined,
	val onAfterClose: (() -> Unit)? = undefined,
	/**
	 * Function to execute before confirm, may be async (Promise-returning) or sync.
	Returned (or resolved) value can be:

	false to prevent a popup from closing
	anything else to pass that value as the result.value of Swal.fire()
	undefined to keep the default result.value
	 */
	val preConfirm: ((value: String) -> dynamic)? = undefined
)