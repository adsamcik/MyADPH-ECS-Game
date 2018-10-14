package engine

import PIXI.Container
import kotlin.browser.document
import kotlin.browser.window

object Graphics {

	val pixi = PIXI.Application(window.innerWidth, window.innerHeight)

	val dynamicContainer = Container().apply {
		pixi.stage.addChild(this)
	}

	val staticContrainer = Container().apply {
		pixi.stage.addChild(this)
	}

	val uiContainer = Container().apply {
		pixi.stage.addChild(this)
	}

	init {
		document.body!!.appendChild(pixi.view)
	}
}