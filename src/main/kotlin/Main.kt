import ecs.components.PhysicsEntityComponent
import ecs.eventsystem.ModifierEventSystem
import ecs.system.*
import engine.Core
import engine.Graphics
import engine.PhysicsEngine
import engine.physics.BodyBuilder
import engine.physics.Circle
import engine.physics.Rectangle
import engine.system.SystemManager
import game.levels.EntityCreator
import game.levels.definitions.Level1
import game.modifiers.ShapeModifierFactory
import jslib.Matter
import utility.Double2
import utility.Rgba
import kotlin.browser.window
import kotlin.random.Random

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

	Core.run()
}