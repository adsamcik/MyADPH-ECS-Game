package engine.physics

import utility.Double2

data class CollisionData(val firstCollider: ColliderShape, val secondCollider: ColliderShape, val point: Double2, val normal: Double2)