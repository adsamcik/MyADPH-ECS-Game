package utility

interface INode<T> {
	fun evaluate(value: T): Boolean

	fun and(node: INode<T>) = AndNode(this, node)
	fun or(node: INode<T>) = OrNode(this, node)
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

interface IValueNode<T, J> : INode<J> {
	val value: T
}
