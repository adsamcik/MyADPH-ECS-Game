package game.levels.definitions

import ecs.eventsystem.CheckpointEventSystem
import ecs.eventsystem.DamageEventSystem
import ecs.eventsystem.DestroyDamageEventSystem
import ecs.eventsystem.ModifierEventSystem
import engine.entity.EntityManager
import engine.events.EventManager
import engine.physics.Physics
import engine.system.EventSystemManager
import engine.system.SystemManager
import game.editor.system.CheckpointInitializationSystem
import game.editor.system.PlayerInitializationSystem
import game.levels.Level

class CustomLevel(private val definition: String) : Level("custom") {
	override val isGameLevel: Boolean = true

	private fun initializeEventSystems() {
		val eventManager = Physics.engine.eventManager
		EventSystemManager.tryRegisterSystems(
			ModifierEventSystem(eventManager),
			DamageEventSystem(eventManager),
			CheckpointEventSystem(eventManager),
			DestroyDamageEventSystem(eventManager)
		)
	}

	private fun initializeSystems() {
		SystemManager.registerSystems(
			PlayerInitializationSystem() to -5000,
			CheckpointInitializationSystem() to -6000
		)
	}

	override fun loadLevel() {
		initializeSystems()
		initializeEventSystems()
		load(definition)
	}
}