package utility

class Stack<T> {
	val elements: MutableList<T> = mutableListOf()

	val isEmpty
		get() = elements.isEmpty()

	val size
		get() = elements.size

	fun peek() = elements.last()

	fun pop(): T {
		if (isEmpty)
			throw NoSuchElementException()

		return elements.removeAt(size - 1)
	}

	fun push(obj: T) = elements.add(obj)

	override fun toString(): String = elements.toString()


	constructor()
	constructor(vararg elements: T) {
		this.elements.addAll(elements)
	}
}