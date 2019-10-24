package extensions

val Any.isObject: Boolean get() {
	console.log(this)
	return jsTypeOf(this) == "object"
}