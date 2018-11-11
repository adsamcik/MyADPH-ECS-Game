import ecs.system.*
import engine.Core
import engine.system.SystemManager
import game.levels.definitions.Level1

fun initializeSystems() {
	SystemManager.registerSystems(
		Pair(EnergyRechargeSystem(), -50),
		//Pair(DevMoveSystem(), -1),
		Pair(KeyboardMoveSystem(), -1),
		Pair(UserTouchMoveSystem(), -1),
		Pair(ModifierUpdateSystem(), 0),
		Pair(RoundAndRoundWeGoSystem(), 0),
		//Pair(BoundSystem(), 50),
		Pair(PhysicsUpdateSystem(), 50),
		Pair(HealthUpdateSystem(), 90),
		Pair(RendererSystem(), 100),
		Pair(DisplayFollowSystem(), 100)
	)
}

fun main(args: Array<String>) {

	initializeSystems()
	Level1().load()

	Core.run()
}