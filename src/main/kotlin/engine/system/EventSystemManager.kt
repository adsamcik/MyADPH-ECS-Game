package engine.system

import kotlin.reflect.KClass

object EventSystemManager {
	private val systems = mutableListOf<IBaseEventSystem>()

	fun registerSystems(vararg systems: IBaseEventSystem) {
		systems.forEach { system ->
			if (this.systems.any { it::class == system::class })
				throw RuntimeException("system ${system::class.simpleName} is already registered")

			registerSystem(system)
		}
	}

	fun tryRegisterSystems(vararg systems: IBaseEventSystem) {
		systems.forEach { system ->
			if (this.systems.any { it::class == system::class })
				return@forEach

			registerSystem(system)
		}
	}

	private fun registerSystem(system: IBaseEventSystem) {
		systems.add(system)
	}

	fun unregisterAllSystems() {
		systems.clear()
		TODO()
	}

	fun unregisterSystem(system: IBaseEventSystem) {
		unregisterSystem(system::class)
	}

	fun unregisterSystem(systemType: KClass<out IBaseEventSystem>) {
		val indexOf = systems.indexOfFirst { it::class == systemType }
		if (indexOf < 0)
			throw RuntimeException("system ${systemType.simpleName} is not registered")

		systems.removeAt(indexOf)
	}
}