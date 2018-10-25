package jslib

import jslib.pixi.Transform
import jslib.planck.*

open external class planck {
	interface Body {
		val m_world: jslib.planck.World
		val m_awakeFlag: Boolean
		val m_autoSleepFlag: Boolean
		val m_bulletFlag: Boolean
		val m_fixedRotationFlag: Boolean
		val m_activeFlag: Boolean
		val m_islandFlag: Boolean
		val m_toiFlag: Boolean
		val m_userData: dynamic
		val m_type: jslib.planck.BodyType
		val m_mass: Number
		val m_invMass: Number
		// Rotational inertia about the center of mass.
		val m_I: Number
		val m_invI: Number
		// the body origin transform
		val m_xf: Transform
		// the swept motion for CCD
		val m_sweep: jslib.planck.Sweep
		// position and velocity correction
		val c_velocity: jslib.planck.Velocity
		val c_position: jslib.planck.Position
		val m_force: jslib.planck.Vec2
		val m_torque: Number
		val m_linearVelocity: jslib.planck.Vec2
		val m_angularVelocity: Number
		val m_linearDamping: Number
		val m_angularDamping: Number
		val m_gravityScale: Number
		val m_sleepTime: Number
		val m_jointList: jslib.planck.JointEdge?
		val m_contactList: jslib.planck.ContactEdge?
		val m_fixtureList: jslib.planck.Fixture?
		val m_prev: jslib.planck.Body?
		val m_next: jslib.planck.Body?

		fun isWorldLocked(): Boolean
		fun getWorld(): jslib.planck.World
		fun getNext(): jslib.planck.Body?
		fun setUserData(data: dynamic)
		fun getUserData(): dynamic
		fun getFixtureList(): jslib.planck.Fixture?
		fun getJointList(): jslib.planck.JointEdge?
		/**
		  * Warning: this list changes during the time step and you may miss some
		  * collisions if you don't use ContactListener.
		  */
		fun getContactList(): jslib.planck.ContactEdge?

		fun isStatic(): Boolean
		fun isDynamic(): Boolean
		fun isKinematic(): Boolean
		/**
		  * This will alter the mass and velocity.
		  */
		fun setStatic(): jslib.planck.Body

		fun setDynamic(): jslib.planck.Body
		fun setKinematic(): jslib.planck.Body
		fun isBullet(): Boolean
		fun setBullet(flag: Boolean): Unit
		fun isSleepingAllowed(): Boolean
		fun setSleepingAllowed(flag: Boolean): Unit
		fun isAwake(): Boolean
		fun setAwake(flag: Boolean): Unit
		fun isActive(): Boolean
		fun setActive(flag: Boolean): Unit
		fun isFixedRotation(): Boolean
		fun setFixedRotation(flag: Boolean): Unit
		fun getTransform(): Transform
		fun setTransform(position: jslib.planck.Vec2, angle: Number): Unit
		fun synchronizeTransform(): Unit
		fun synchronizeFixtures(): Unit
		fun advance(alpha: Number)
		fun getPosition(): jslib.planck.Vec2
		fun setPosition(p: jslib.planck.Vec2): Unit
		fun getAngle(): Number
		fun setAngle(angle: Number): Unit
		fun getWorldCenter(): jslib.planck.Vec2
		fun getLocalCenter(): jslib.planck.Vec2
		fun getLinearVelocity(): jslib.planck.Vec2
		fun getLinearVelocityFromWorldPoint(worldPoint: jslib.planck.Vec2): jslib.planck.Vec2
		fun getLinearVelocityFromLocalPoint(localPoint: jslib.planck.Vec2): jslib.planck.Vec2
		fun setLinearVelocity(v: jslib.planck.Vec2): Unit
		fun getAngularVelocity(): Number
		fun setAngularVelocity(w: Number): Unit
		fun getLinearDamping(): Number
		fun setLinearDamping(linearDamping: Number): Unit
		fun getAngularDamping(): Number
		fun setAngularDamping(angularDamping: Number): Unit
		fun getGravityScale(): Number
		fun setGravityScale(scale: Number): Unit
		fun getMass(): Number
		fun getInertia(): Number
		fun getMassData(data: jslib.planck.MassData)
		fun resetMassData()
		fun setMassData(massData: jslib.planck.MassData): Unit
		fun applyForce(force: jslib.planck.Vec2, point: jslib.planck.Vec2, wake: Boolean): Unit
		fun applyForceToCenter(force: jslib.planck.Vec2, wake: Boolean): Unit
		fun applyTorque(torque: Number, wake: Boolean): Unit
		fun applyLinearImpulse(impulse: jslib.planck.Vec2, point: jslib.planck.Vec2, wake: Boolean): Unit
		fun applyAngularImpulse(impulse: Number, wake: Boolean): Unit
		fun shouldCollide(that: jslib.planck.Body): Boolean
		fun createFixture(def: jslib.planck.FixtureDef): jslib.planck.Fixture
		fun createFixture(shape: jslib.planck.Shape, opt: jslib.planck.FixtureOpt): jslib.planck.Fixture
		fun createFixture(shape: jslib.planck.Shape, density: Number): jslib.planck.Fixture
		fun destroyFixture(fixture: jslib.planck.Fixture): Unit
		fun getWorldPoint(localPoint: jslib.planck.Vec2): jslib.planck.Vec2
		fun getWorldVector(localVector: jslib.planck.Vec2): jslib.planck.Vec2
		fun getLocalPoint(worldPoint: jslib.planck.Vec2): jslib.planck.Vec2
		fun getLocalVector(worldVector: jslib.planck.Vec2): jslib.planck.Vec2
	}
	
	open class World {
		constructor(init: PlanckExtensions.WorldInitObject)

		val m_solver: jslib.planck.Solver
		val m_broadPhase: jslib.planck.BroadPhase
		val m_contactList: CollisionEvent.PairData.Contact?
		val m_contactCount: Number
		val m_bodyList: jslib.planck.Body?
		val m_bodyCount: Number
		val m_jointList: jslib.planck.Joint?
		val m_jointCount: Number
		val m_stepComplete: Boolean
		val m_allowSleep: Boolean
		val m_gravity: jslib.planck.Vec2
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
		val addPair : (proxyA: jslib.planck.FixtureProxy, proxyB: jslib.planck.FixtureProxy) -> Unit

		fun getBodyList(): jslib.planck.Body?
		fun getJointList(): jslib.planck.Joint?
		fun getContactList(): CollisionEvent.PairData.Contact?
		fun getBodyCount(): Number
		fun getJointCount(): Number
		fun getContactCount(): Number
		fun setGravity(gravity: jslib.planck.Vec2): Unit
		fun getGravity(): jslib.planck.Vec2
		fun isLocked(): Boolean
		fun setAllowSleeping(flag: Boolean): Unit
		fun getAllowSleeping(): Boolean
		fun setWarmStarting(flag: Boolean): Unit
		fun getWarmStarting(): Boolean
		fun setContinuousPhysics(flag: Boolean): Unit
		fun getContinuousPhysics(): Boolean
		fun setSubStepping(flag: Boolean): Unit
		fun getSubStepping(): Boolean
		fun setAutoClearForces(flag: Boolean): Unit
		fun getAutoClearForces(): Boolean
		fun clearForces(): Unit
		fun queryAABB(aabb: jslib.planck.AABB, queryCallback: (fixture: jslib.planck.Fixture) -> Boolean): Unit
		fun rayCast(
			point1: jslib.planck.Vec2,
			point2: jslib.planck.Vec2,
			reportFixtureCallback: (fixture: jslib.planck.Fixture, point: jslib.planck.Vec2, normal: jslib.planck.Vec2, fraction: Number) -> Number
		): Unit

		fun getProxyCount(): Number
		fun getTreeHeight(): Number
		fun getTreeBalance(): Number
		fun getTreeQuality(): Number
		fun shiftOrigin(newOrigin: jslib.planck.Vec2): Unit
		fun createBody(def: jslib.planck.BodyDef): jslib.planck.Body
		fun createBody(position: jslib.planck.Vec2, angle: Number): jslib.planck.Body
		fun createBody(): jslib.planck.Body
		fun createDynamicBody(def: jslib.planck.BodyDef): jslib.planck.Body
		fun createDynamicBody(position: jslib.planck.Vec2, angle: Number): jslib.planck.Body
		fun createDynamicBody(): jslib.planck.Body
		fun createKinematicBody(def: jslib.planck.BodyDef): jslib.planck.Body
		fun createKinematicBody(position: jslib.planck.Vec2, angle: Number): jslib.planck.Body
		fun createKinematicBody(): jslib.planck.Body
		fun destroyBody(b: jslib.planck.Body): Boolean
		fun <T : jslib.planck.Joint> createJoint(joint: T): T?
		fun destroyJoint(joint: jslib.planck.Joint): Unit
		fun step(timeStep: Number, velocityIterations: Number, positionIterations: Number): Unit
		fun findNewContacts(): Unit

		fun updateContacts(): Unit
		fun destroyContact(contact: CollisionEvent.PairData.Contact): Unit

		val _listeners: dynamic // TODO
		//todo on funkce
		fun publish(name: String, arg1: dynamic, arg2: dynamic, arg3: dynamic): Number
		fun beginContact(contact: CollisionEvent.PairData.Contact): Unit
		fun endContact(contact: CollisionEvent.PairData.Contact): Unit
		fun preSolve(contact: CollisionEvent.PairData.Contact, oldManifold: jslib.planck.Manifold)
		fun postSolve(contact: CollisionEvent.PairData.Contact, impulse: jslib.planck.ContactImpulse)
	}


	open class Vec2 {
		constructor(x: Number, y: Number)

		val x: Number
		val y: Number

		override fun toString(): String
		fun clone(): jslib.planck.Vec2
		fun setZero(): jslib.planck.Vec2
		fun set(x: Number, y: Number): jslib.planck.Vec2
		fun set(value: jslib.planck.Vec2): jslib.planck.Vec2
		fun setCombine(a: Number, v: jslib.planck.Vec2, b: Number, w: jslib.planck.Vec2): jslib.planck.Vec2
		fun setMul(a: Number, v: jslib.planck.Vec2): jslib.planck.Vec2
		fun add(w: jslib.planck.Vec2): jslib.planck.Vec2
		fun addCombine(a: Number, v: jslib.planck.Vec2, b: Number, w: jslib.planck.Vec2): jslib.planck.Vec2
		fun addMul(a: Number, v: jslib.planck.Vec2): jslib.planck.Vec2
		fun sub(w: jslib.planck.Vec2): jslib.planck.Vec2
		fun subCombine(a: Number, v: jslib.planck.Vec2, b: Number, w: jslib.planck.Vec2): jslib.planck.Vec2
		fun subMul(a: Number, v: jslib.planck.Vec2): jslib.planck.Vec2
		fun mul(m: Number): jslib.planck.Vec2
		fun length(): Number
		fun lengthSquared(): Number
		fun normalize(): Number
		fun neg(): jslib.planck.Vec2
		fun clamp(max: Number): jslib.planck.Vec2
	}

	interface Shape {
		val m_type: String;
		val m_radius: number;

		fun isValid(shape: any): boolean;
		fun getRadius(): number;
		fun getType(): ShapeType;
		fun getChildCount(): number;
		fun testPoint(xf: Transform, p: Vec2): false;
		fun rayCast(output: RayCastOutput, input: RayCastInput, xf: Transform, childIndex?: number): boolean;
		fun computeAABB(aabb: AABB, xf: Transform, childIndex?: number): void;
		fun computeMass(massData: MassData, density?: number): void;
		fun computeDistanceProxy(proxy: DistanceProxy): void;
	}
	interface CircleShape extends Shape {
		val m_p: Vec2;

		getCenter(): Vec2;
		getVertex(index?: number): Vec2;
		getVertexCount(index?: number): 1;
	}
	interface EdgeShape extends Shape {
		m_type: 'edge';

		m_vertex1: Vec2;
		m_vertex2: Vec2;
		m_vertex0: Vec2;
		m_vertex3: Vec2;
		m_hasVertex0: boolean;
		m_hasVertex3: boolean;

		setNext(v3?: Vec2): EdgeShape;
		setPrev(v0?: Vec2): EdgeShape;
		// @private @internal
		// _set(v1: Vec2, v2: Vec2): EdgeShape;
	}
	interface PolygonShape extends Shape {

		m_centroid: Vec2;
		m_vertices: Vec2[];
		m_normals: Vec2[];
		m_count: number;

		getVertex(index: number): Vec2;
		validate(): void;

		// @private @internal
		// _set(vertices: Vec2[]): void;
		// _setAsBox(hx: number, hy: number, center: Vec2, angle?: number): void;
		// _setAsBox(hx: number, hy: number): void;
	}
	interface ChainShape extends Shape {
		m_type: 'chain';

		m_vertices: Vec2[];
		m_count: number;
		m_prevVertex: Vec2 | null;
		m_nextVertex: Vec2 | null;
		m_hasPrevVertex: boolean;
		m_hasNextVertex: boolean;

		// @private @internal
		// _createLoop(vertices: Vec2[]): ChainShape;
		// _createChain(vertices: Vec2[]): ChainShape;
		// _setPrevVertex(prevVertex: Vec2): void;
		// _setNextVertex(nextVertex: Vec2): void;
		getChildEdge(edge: EdgeShape, childIndex: number): void;
		getVertex(index: number): Vec2;
	}

	interface BodyType
	interface ContactImpulse
	interface ContactEdge
	interface Fixture
	interface Joint
	interface Manifold
	interface FixtureProxy
	interface FixtureDef
	interface BodyDef
	interface AABB
	interface Shape
	interface Solver
	interface MassData
	interface FixtureOpt
	interface JointEdge
	interface Position
	interface Velocity
	interface Sweep
	interface BroadPhase
}

open class PlanckExtensions {
	data class WorldInitObject(val gravity: Vec2)
}