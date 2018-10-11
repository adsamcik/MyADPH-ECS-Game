package utility

interface INode<T> {
	fun evaluate(value: T): Boolean

	fun and(node: INode<T>) = AndNode(this, node)
	fun or(node: INode<T>) = OrNode(this, node)
}

class ValueNodeIterator<T, J>(rootNode: INode<T>) : Iterator<IValueNode<T, J>> {
	private var stack = Stack(rootNode)

	override fun hasNext(): Boolean = !stack.isEmpty

	override fun next(): IValueNode<T, J> {
		val node = stack.pop()
		return when (node) {
			is IOperationNode<T> -> {
				stack.push(node.secondNode)
				stack.push(node.firstNode)
				next()
			}
			is IValueNode<*, *> -> node.unsafeCast<IValueNode<T, J>>()
			else -> throw Error("Node of type ${node::class.simpleName} was not recognized")
		}
	}

}

interface IOperationNode<T> : INode<T> {
	val firstNode: INode<T>
	val secondNode: INode<T>
}

data class AndNode<T>(override val firstNode: INode<T>, override val secondNode: INode<T>) : IOperationNode<T> {
	override fun evaluate(value: T) = firstNode.evaluate(value) && secondNode.evaluate(value)
}

data class OrNode<T>(override val firstNode: INode<T>, override val secondNode: INode<T>) : IOperationNode<T> {
	override fun evaluate(value: T) = firstNode.evaluate(value) || secondNode.evaluate(value)
}

interface IValueNode<J, T> : INode<J> {
	val value: T
}
