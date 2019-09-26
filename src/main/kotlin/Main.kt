import ecs.system.*
import ecs.system.render.PhysicsRenderSystem
import ecs.system.render.TransformRenderSystem
import engine.Core
import engine.physics.Physics
import engine.physics.engines.PlanckPhysicsEngine
import engine.system.SystemManager
import game.levels.LevelManager
import game.levels.definitions.Level1
import game.levels.definitions.Level2
import game.levels.definitions.Level3
import game.levels.definitions.Menu
import tests.TestRunner

fun initializeSystems() {
	SystemManager.registerSystems(
		Pair(EnergyRechargeSystem(), -50),
		//Pair(DevMoveSystem(), -1),
		Pair(KeyboardMoveSystem(), -1),
		Pair(UserTouchMoveSystem(), -1),
		Pair(ModifierUpdateSystem(), 0),
		Pair(RoundAndRoundWeGoSystem(), 0),
		Pair(SpawnerSystem(), 25),
		Pair(LifeTimeSystem(), 25),
		//Pair(BoundSystem(), 50),
		Pair(PhysicsUpdateSystem(), 50),
		Pair(HealthUpdateSystem(), 90),
		Pair(PhysicsRenderSystem(), 100),
		Pair(TransformRenderSystem(), 100),
		Pair(DisplayFollowSystem(), 100),
		Pair(UpdateUserInterfaceSystem(), 100)
	)
}

fun main() {
	//TODO Fix if systems are not initialized before core user is not rendered!!
	initializeSystems()
	TestRunner().run()

	Physics.engine = PlanckPhysicsEngine()

	LevelManager.addLevel(Menu())
	LevelManager.addLevel(Level1())
	LevelManager.addLevel(Level2())
	LevelManager.addLevel(Level3())

	Core.run()

	LevelManager.requestLevel(Menu.NAME)
}