package jslib

import jslib.pixi.Transform


typealias ShapeType = String

open external class planck {

	interface Body {
		val m_world: World
		val m_awakeFlag: Boolean
		val m_autoSleepFlag: Boolean
		val m_bulletFlag: Boolean
		val m_fixedRotationFlag: Boolean
		val m_activeFlag: Boolean
		val m_islandFlag: Boolean
		val m_toiFlag: Boolean
		val m_userData: dynamic
		val m_type: BodyType
		val m_mass: Number
		val m_invMass: Number
		// Rotational inertia about the center of mass.
		val m_I: Number
		val m_invI: Number
		// the body origin transform
		val m_xf: Transform
		// the swept motion for CCD
		val m_sweep: Sweep
		// position and velocity correction
		val c_velocity: Velocity
		val c_position: Position
		val m_force: Vec2
		val m_torque: Number
		val m_linearVelocity: Vec2
		val m_angularVelocity: Number
		val m_linearDamping: Number
		val m_angularDamping: Number
		val m_gravityScale: Number
		val m_sleepTime: Number
		val m_jointList: JointEdge?
		val m_contactList: ContactEdge?
		val m_fixtureList: Fixture?
		val m_prev: Body?
		val m_next: Body?

		fun isWorldLocked(): Boolean
		fun getWorld(): World
		fun getNext(): Body?
		fun setUserData(data: dynamic)
		fun getUserData(): dynamic
		fun getFixtureList(): Fixture?
		fun getJointList(): JointEdge?
		/**
		 * Warning: this list changes during the time step and you may miss some
		 * collisions if you don't use ContactListener.
		 */
		fun getContactList(): ContactEdge?

		fun isStatic(): Boolean
		fun isDynamic(): Boolean
		fun isKinematic(): Boolean
		/**
		 * This will alter the mass and velocity.
		 */
		fun setStatic(): Body

		fun setDynamic(): Body
		fun setKinematic(): Body
		fun isBullet(): Boolean
		fun setBullet(flag: Boolean)
		fun isSleepingAllowed(): Boolean
		fun setSleepingAllowed(flag: Boolean)
		fun isAwake(): Boolean
		fun setAwake(flag: Boolean)
		fun isActive(): Boolean
		fun setActive(flag: Boolean)
		fun isFixedRotation(): Boolean
		fun setFixedRotation(flag: Boolean)
		fun getTransform(): Transform
		fun setTransform(position: Vec2, angle: Number)
		fun synchronizeTransform()
		fun synchronizeFixtures()
		fun advance(alpha: Number)
		fun getPosition(): Vec2
		fun setPosition(p: Vec2)
		fun getAngle(): Number
		fun setAngle(angle: Number)
		fun getWorldCenter(): Vec2
		fun getLocalCenter(): Vec2
		fun getLinearVelocity(): Vec2
		fun getLinearVelocityFromWorldPoint(worldPoint: Vec2): Vec2
		fun getLinearVelocityFromLocalPoint(localPoint: Vec2): Vec2
		fun setLinearVelocity(v: Vec2)
		fun getAngularVelocity(): Number
		fun setAngularVelocity(w: Number)
		fun getLinearDamping(): Number
		fun setLinearDamping(linearDamping: Number)
		fun getAngularDamping(): Number
		fun setAngularDamping(angularDamping: Number)
		fun getGravityScale(): Number
		fun setGravityScale(scale: Number)
		fun getMass(): Number
		fun getInertia(): Number
		fun getMassData(data: MassData)
		fun resetMassData()
		fun setMassData(massData: MassData)
		fun applyForce(force: Vec2, point: Vec2, wake: Boolean = definedExternally)
		fun applyForceToCenter(force: Vec2, wake: Boolean)
		fun applyTorque(torque: Number, wake: Boolean)
		fun applyLinearImpulse(impulse: Vec2, point: Vec2, wake: Boolean)
		fun applyAngularImpulse(impulse: Number, wake: Boolean)
		fun shouldCollide(that: Body): Boolean
		fun createFixture(def: FixtureDef): Fixture
		fun createFixture(shape: Shape, opt: FixtureOpt): Fixture
		fun createFixture(shape: Shape, density: Number = definedExternally): Fixture
		fun destroyFixture(fixture: Fixture)
		fun getWorldPoint(localPoint: Vec2): Vec2
		fun getWorldVector(localVector: Vec2): Vec2
		fun getLocalPoint(worldPoint: Vec2): Vec2
		fun getLocalVector(worldVector: Vec2): Vec2
	}

	open class World {
		constructor(def: planck.WorldDef)
		constructor(gravity: Vec2)
		constructor()

		val m_solver: Solver
		val m_broadPhase: BroadPhase
		val m_contactList: Contact?
		val m_contactCount: Number
		val m_bodyList: Body?
		val m_bodyCount: Number
		val m_jointList: Joint?
		val m_jointCount: Number
		val m_stepComplete: Boolean
		val m_allowSleep: Boolean
		val m_gravity: Vec2
		val m_clearForces: Boolean
		val m_newFixture: Boolean
		val m_locked: Boolean
		val m_warmStarting: Boolean
		val m_continuousPhysics: Boolean
		val m_subStepping: Boolean
		val m_blockSolve: Boolean
		val m_velocityIterations: Number
		val m_positionIterations: Number
		val m_t: Number
		val addPair: (proxyA: FixtureProxy, proxyB: FixtureProxy) -> Unit

		fun getBodyList(): Body?
		fun getJointList(): Joint?
		fun getContactList(): Contact?
		fun getBodyCount(): Number
		fun getJointCount(): Number
		fun getContactCount(): Number
		fun setGravity(gravity: Vec2)
		fun getGravity(): Vec2
		fun isLocked(): Boolean
		fun setAllowSleeping(flag: Boolean)
		fun getAllowSleeping(): Boolean
		fun setWarmStarting(flag: Boolean)
		fun getWarmStarting(): Boolean
		fun setContinuousPhysics(flag: Boolean)
		fun getContinuousPhysics(): Boolean
		fun setSubStepping(flag: Boolean)
		fun getSubStepping(): Boolean
		fun setAutoClearForces(flag: Boolean)
		fun getAutoClearForces(): Boolean
		fun clearForces()
		fun queryAABB(aabb: AABB, queryCallback: (fixture: Fixture) -> Boolean)
		fun rayCast(
			point1: Vec2,
			point2: Vec2,
			reportFixtureCallback: (fixture: Fixture, point: Vec2, normal: Vec2, fraction: Number) -> Number
		)

		fun getProxyCount(): Number
		fun getTreeHeight(): Number
		fun getTreeBalance(): Number
		fun getTreeQuality(): Number
		fun shiftOrigin(newOrigin: Vec2)
		fun createBody(def: BodyDef): Body
		fun createBody(position: Vec2, angle: Number = definedExternally): Body
		fun createBody(): Body
		fun createDynamicBody(def: BodyDef): Body
		fun createDynamicBody(position: Vec2, angle: Number = definedExternally): Body
		fun createDynamicBody(): Body
		fun createKinematicBody(def: BodyDef): Body
		fun createKinematicBody(position: Vec2, angle: Number = definedExternally): Body
		fun createKinematicBody(): Body
		fun destroyBody(b: Body): Boolean
		fun <T : Joint> createJoint(joint: T): T?
		fun destroyJoint(joint: Joint)
		fun step(timeStep: Number, velocityIterations: Number = definedExternally, positionIterations: Number = definedExternally)
		fun findNewContacts()

		fun updateContacts()
		fun destroyContact(contact: Contact)

		/*val _listeners: dynamic // TODO

		fun publish(name: String, arg1: dynamic, arg2: dynamic, arg3: dynamic): Number
		fun beginContact(contact: Contact)
		fun endContact(contact: Contact)
		fun preSolve(contact: Contact, oldManifold: Manifold)
		fun postSolve(contact: Contact, impulse: ContactImpulse)*/


		//begin-contact
		//end-contact
		fun on(name: String, listener: (contact: Contact) -> Unit): World

		fun off(name: String, listener: (contact: Contact) -> Unit): World
	}


	open class Vec2(x: Number, y: Number) {

		val x: Number
		val y: Number

		override fun toString(): String
		fun clone(): Vec2
		fun setZero(): Vec2
		fun set(x: Number, y: Number): Vec2
		fun set(value: Vec2): Vec2
		fun setCombine(a: Number, v: Vec2, b: Number, w: Vec2): Vec2
		fun setMul(a: Number, v: Vec2): Vec2
		fun add(w: Vec2): Vec2
		fun addCombine(a: Number, v: Vec2, b: Number, w: Vec2): Vec2
		fun addMul(a: Number, v: Vec2): Vec2
		fun sub(w: Vec2): Vec2
		fun subCombine(a: Number, v: Vec2, b: Number, w: Vec2): Vec2
		fun subMul(a: Number, v: Vec2): Vec2
		fun mul(m: Number): Vec2
		fun length(): Number
		fun lengthSquared(): Number
		fun normalize(): Number
		fun neg(): Vec2
		fun clamp(max: Number): Vec2
	}

	abstract class Shape {
		val m_type: ShapeType
		val m_radius: Number

		fun isValid(shape: dynamic): Boolean
		fun getRadius(): Number
		fun getType(): ShapeType
		fun getChildCount(): Number
		fun testPoint(xf: Transform, p: Vec2): Boolean
		fun rayCast(
			output: RayCastOutput,
			input: RayCastInput,
			xf: Transform,
			childIndex: Number = definedExternally
		): Boolean

		fun computeAABB(aabb: AABB, xf: Transform, childIndex: Number = definedExternally)
		fun computeMass(massData: MassData, density: Number = definedExternally)
		fun computeDistanceProxy(proxy: DistanceProxy)
	}

	open class CircleShape : Shape {
		val m_p: Vec2

		fun getCenter(): Vec2
		fun getVertex(index: Number): Vec2
		fun getVertexCount(index: Number): Int
	}

	open class EdgeShape : Shape {
		val m_vertex1: Vec2
		val m_vertex2: Vec2
		val m_vertex0: Vec2
		val m_vertex3: Vec2
		val m_hasVertex0: Boolean
		val m_hasVertex3: Boolean

		fun setNext(v3: Vec2 = definedExternally): EdgeShape
		fun setPrev(v0: Vec2 = definedExternally): EdgeShape
	}

	open class PolygonShape : Shape {
		val m_centroid: Vec2
		val m_vertices: Array<Vec2>
		val m_normals: Array<Vec2>
		val m_count: Number

		fun getVertex(index: Number): Vec2
		fun validate()
	}

	open class ChainShape : Shape {
		val m_vertices: Array<Vec2>
		val m_count: Number
		val m_prevVertex: Vec2?
		val m_nextVertex: Vec2?
		val m_hasPrevVertex: Boolean
		val m_hasNextVertex: Boolean

		fun getChildEdge(edge: EdgeShape, childIndex: Number)
		fun getVertex(index: Number): Vec2
	}

	class Chain(vertices: Array<Vec2>, loop: Boolean = definedExternally) : ChainShape
	class Polygon(vertices: Array<Vec2>) : PolygonShape
	class Edge(v1: Vec2, v2: Vec2) : EdgeShape
	class Circle : CircleShape {
		constructor(position: Vec2, radius: Number = definedExternally)
		constructor(radius: Number = definedExternally)
	}

	class Box(hx: Number, hy: Number, center: Vec2 = definedExternally, angle: Number = definedExternally) :
		PolygonShape

	interface Contact {
		val m_nodeA: ContactEdge
		val m_nodeB: ContactEdge
		val m_fixtureA: Fixture
		val m_fixtureB: Fixture
		val m_indexA: Number
		val m_indexB: Number
		val m_evaluateFcn: (manifold: Manifold, xfA: Transform, fixtureA: Fixture, indexA: Number, xfB: Transform, fixtureB: Fixture, indexB: Number) -> Unit
		val m_manifold: Manifold
		val m_prev: Contact?
		val m_next: Contact?
		val m_toi: Number
		val m_toiCount: Number
		val m_toiFlag: Boolean
		val m_friction: Number
		val m_restitution: Number
		val m_tangentSpeed: Number
		val m_enabledFlag: Boolean
		val m_islandFlag: Boolean
		val m_touchingFlag: Boolean
		val m_filterFlag: Boolean
		val m_bulletHitFlag: Boolean
		val v_points: Array<VelocityConstraintPoint>
		val v_normal: Vec2
		val v_normalMass: Mat22
		val v_K: Mat22
		val v_pointCount: Number
		val v_tangentSpeed: Number?
		val v_friction: Number?
		val v_restitution: Number?
		val v_invMassA: Number?
		val v_invMassB: Number?
		val v_invIA: Number?
		val v_invIB: Number?
		val p_localPoints: Array<Vec2>
		val p_localNormal: Vec2
		val p_localPoint: Vec2
		val p_localCenterA: Vec2
		val p_localCenterB: Vec2
		val p_type: Manifold.Type
		val p_radiusA: Number?
		val p_radiusB: Number?
		val p_pointCount: Number?
		val p_invMassA: Number?
		val p_invMassB: Number?
		val p_invIA: Number?
		val p_invIB: Number?

		//fun initConstraint(step: {warmStarting: Boolean, dtRatio: Number})
		fun getManifold(): Manifold

		fun getWorldManifold(worldManifold: WorldManifold?): WorldManifold
		fun setEnabled(flag: Boolean)
		fun isEnabled(): Boolean
		fun isTouching(): Boolean
		fun getNext(): Contact?
		fun getFixtureA(): Fixture
		fun getFixtureB(): Fixture
		fun getChildIndexA(): Number
		fun getChildIndexB(): Number
		fun flagForFiltering()
		fun setFriction(friction: Number)
		fun getFriction(): Number
		fun resetFriction()
		fun setRestitution(restitution: Number)
		fun getRestitution(): Number
		fun resetRestitution()
		fun setTangentSpeed(speed: Number)
		fun getTangentSpeed(): Number
		fun evaluate(manifold: Manifold, xfA: Transform, xfB: Transform)
		//fun update(listener?: {beginContact(contact: Contact), endContact(contact: Contact), oreSolve(contact: Contact, oldManifold: Manifold)})
		//fun solvePositionConstraint(step: dynamic): Number
		//fun solvePositionConstraintTOI(step: dynamic, toiA?: Body | null, toiB?: Body | null): Number
		//fun _solvePositionConstraint(step: dynamic, toi: Boolean, toiA?: Body | null, toiB?: Body | null): Number
		//fun initVelocityConstraint(step: {blockSolve: Boolean})
		fun warmStartConstraint(step: dynamic)

		fun storeConstraintImpulses(step: dynamic)
		//fun solveVelocityConstraint(step: {blockSolve: Boolean})
	}

	open class RayCastInput {
		var p1: Vec2
		var p2: Vec2
		var maxFraction: Number
	}

	open class RayCastOutput {
		val normal: Vec2
		val fraction: Number
	}


	interface DistanceProxy {
		val m_buffer: Array<Vec2>
		val m_vertices: Array<Vec2>
		val m_count: Number
		val m_radius: Number

		fun getVertexCount(): Number
		fun getVertex(index: Number): Vec2
		fun getSupport(d: Vec2): Number
		fun getSupportVertex(d: Vec2): Vec2
		fun set(shape: Shape, index: Number)
	}

	interface MassData {
		var mass: Number
		var center: Vec2
		var I: Number
	}

	interface Fixture {
		val m_body: Body;
		val m_friction: Number;
		val m_restitution: Number;
		val m_density: Number;
		val m_isSensor: Boolean;
		val m_filterGroupIndex: Number;
		val m_filterCategoryBits: Number;
		val m_filterMaskBits: Number;
		val m_shape: Shape;
		val m_next: Fixture?;
		val m_proxies: Array<FixtureProxy>;
		val m_proxyCount: Number;
		val m_userData: dynamic;

		fun getType(): ShapeType;
		fun getShape(): Shape;
		fun isSensor(): Boolean;
		fun setSensor(sensor: Boolean): Unit;
		fun getUserData(): dynamic;
		fun setUserData(data: dynamic): Unit;
		fun getBody(): Body;
		fun getNext(): Fixture?;
		fun getDensity(): Number;
		fun setDensity(density: Number): Unit;
		fun getFriction(): Number;
		fun setFriction(friction: Number): Unit;
		fun getRestitution(): Number;
		fun setRestitution(restitution: Number): Unit;
		fun testPoint(p: Vec2): Boolean;
		fun rayCast(output: RayCastOutput, input: RayCastInput, childIndex: Number): Boolean;// is childIndex optional?
		fun getMassData(massData: MassData): Unit;
		fun getAABB(childIndex: Number): AABB;
		fun createProxies(broadPhase: BroadPhase, xf: Transform): Unit;//TODO
		fun destroyProxies(broadPhase: BroadPhase): Unit;
		fun synchronize(broadPhase: BroadPhase, xf1: Transform, xf2: Transform): Unit;
		//fun setFilterData(filter: { groupIndex: Number, categoryBits: Number, maskBits: Number }): Unit;
		fun getFilterGroupIndex(): Number;
		fun getFilterCategoryBits(): Number;
		fun getFilterMaskBits(): Number;
		fun refilter(): Unit;
		fun shouldCollide(that: Fixture): Boolean;
	}

	interface BodyType
	interface ContactImpulse
	interface ContactEdge
	interface Joint
	interface Manifold {
		interface Type
	}

	interface WorldDef
	interface FixtureProxy
	interface FixtureDef
	interface BodyDef
	interface AABB
	interface Solver
	interface FixtureOpt
	interface JointEdge
	interface Position
	interface Velocity
	interface Sweep
	interface BroadPhase
	interface VelocityConstraintPoint
	interface WorldManifold

	interface Mat22 {
		var ex: Vec2
		var ey: Vec2
		override fun toString(): String
		fun set(a: Mat22)
		fun set(a: Vec2, b: Vec2)
		fun set(a: Number, b: Number, c: Number, d: Number)
		fun setIdentity()
		fun setZero()
		fun getInverse(): Mat22
		fun solve(v: Vec2): Vec2
	}
}

open class PlanckExtensions {
	data class WorldInitObject(val gravity: planck.Vec2)

	object World {
		const val EVENT_BEGIN_CONTACT = "begin-contact"
		const val EVENT_END_CONTACT = "end-contact"
	}
}

fun planck.World.onBeginContact(listener: (contact: planck.Contact) -> Unit): planck.World {
	on(PlanckExtensions.World.EVENT_BEGIN_CONTACT, listener)
	return this
}

fun planck.World.offBeginContact(listener: (contact: planck.Contact) -> Unit): planck.World {
	off(PlanckExtensions.World.EVENT_BEGIN_CONTACT, listener)
	return this
}

fun planck.World.onEndContact(listener: (contact: planck.Contact) -> Unit): planck.World {
	on(PlanckExtensions.World.EVENT_END_CONTACT, listener)
	return this
}

fun planck.World.offEndContact(listener: (contact: planck.Contact) -> Unit): planck.World {
	off(PlanckExtensions.World.EVENT_END_CONTACT, listener)
	return this
}
