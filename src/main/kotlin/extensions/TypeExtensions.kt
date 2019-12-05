package extensions

inline val Any.isObject: Boolean get() {
	return jsTypeOf(this) === "object"
}