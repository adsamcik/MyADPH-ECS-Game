package extensions

inline fun <T> List<T>.forEachSince(startAtIndex: Int, action: (T) -> Unit) {
	for (i in startAtIndex until size)
		action(get(i))
}

inline fun <T> List<T>.forEachSinceIndexed(startAtIndex: Int, action: (index: Int, T) -> Unit) {
	for (i in startAtIndex until size)
		action(i, get(i))
}