
import ecs.eventsystem.ModifierEventSystem
import ecs.system.*
import engine.Core
import engine.physics.Physics
import engine.system.SystemManager
import game.levels.definitions.Level1

fun initializeSystems() {
	SystemManager.registerSystems(
		Pair(PhysicsUpdateSystem(), 90),
		Pair(UserKeyboardMoveSystem(), -1),
		Pair(UserTouchMoveSystem(), -1),
		Pair(RoundAndRoundWeGoSystem(), 0),
		//Pair(BoundSystem(), 50),
		Pair(RendererSystem(), 100),
		Pair(ModifierUpdateSystem(), 0),
		Pair(ModifierAddSystem(), -900),
		Pair(DisplayFollowSystem(), 100)
	)
}



lateinit var modifierEventSystem: ModifierEventSystem

fun main(args: Array<String>) {

	initializeSystems()
	Level1().load()

	modifierEventSystem = ModifierEventSystem(Physics.engine.eventManager)

	Core.run()
}