package ui.assignments.connectfour

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import ui.assignments.connectfour.ui.OverallView
import ui.assignments.connectfour.model.Model

class ConnectFourApp : Application() {
    override fun start(stage: Stage) {
        val view = OverallView(Model)
        val scene = Scene(view, 1000.0, 800.0)

        stage.title = "CS349 - A3 Connect Four - j65cui"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}