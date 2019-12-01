package game.modifiers.factory.template

import engine.entity.Entity
import game.modifiers.IModifierData

//PATTERN Abstract Factory
//so modifierLogic can be recreated as many time as needed with separate internal states
interface IModifierFactory {
	fun build(sourceEntity: Entity): IModifierData
}