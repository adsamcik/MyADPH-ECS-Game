package extensions

fun String.format(): String {
	return replace(Regex("([A-Z])([A-Z][a-z])"), "$1 $2")
		.replace(Regex("([a-z])([A-Z])"), "$1 $2")
		.replace(Regex("(\\D)(\\d)"), "$1 $2")
		.replace(Regex("(\\d)(\\D)"), "$1 $2")
}