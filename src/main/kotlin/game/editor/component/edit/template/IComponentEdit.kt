package game.editor.component.edit.template

import engine.component.IComponent
import engine.entity.Entity
import org.w3c.dom.Element
import kotlin.reflect.KClass

interface IComponentEdit<T : IComponent> : IObjectEdit<T>