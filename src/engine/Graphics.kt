package engine

import PIXI.Application
import PIXI.Container
import kotlin.browser.document
import kotlin.browser.window

object Graphics {

	val pixi = PIXI.Application(window.innerWidth, window.innerHeight, object {
		val antialias = true
	})

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