import ecs.eventsystem.ModifierEventSystem
import ecs.system.*
import engine.Core
import engine.PhysicsEngine
import engine.system.SystemManager
import game.levels.definitions.Level1
import jslib.PlanckExtensions

fun initializeSystems() {
	SystemManager.registerSystems(
		Pair(UserKeyboardMoveSystem(), -1),
		Pair(UserTouchMoveSystem(), -1),
		Pair(RoundAndRoundWeGoSystem(), 0),
		//Pair(BoundSystem(), 50),
		Pair(RendererSystem(), 100),
		Pair(MatterEngineUpdateSystem(), -60),
		Pair(ModifierUpdateSystem(), 0),
		Pair(ModifierAddSystem(), -900),
		Pair(DisplayFollowSystem(), 100)
	)
}



lateinit var modifierEventSystem: ModifierEventSystem

fun main(args: Array<String>) {

	initializeSystems()
	Level1().load()

	modifierEventSystem = ModifierEventSystem(PhysicsEngine.eventManager)

	val world = jslib.planck.World(PlanckExtensions.WorldInitObject(gravity = jslib.planck.Vec2(0, -10)))
	val ground = world.createBody()
	ground.createFixture(jslib.planck.ed)

	Core.run()
}