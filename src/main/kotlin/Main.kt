import ecs.system.*
import engine.Core
import engine.system.SystemManager
import game.levels.LevelManager
import game.levels.definitions.Level1
import game.levels.definitions.Level2
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

fun main(args: Array<String>) {
	TestRunner().run()

	initializeSystems()
	LevelManager.addLevel(Level1())
	LevelManager.addLevel(Level2())

	Core.run()

	LevelManager.requestLevel(2)
}