package ui.assignments.connectfour.ui

import javafx.animation.ScaleTransition
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import ui.assignments.connectfour.controller.Circle
import ui.assignments.connectfour.controller.StartController
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Player
import javafx.util.Duration

// draw message board
class ShowDrawMessage(private val model: Model):Pane(), ChangeListener<Boolean> {
    init{
        val label = Label(" This is a Draw! ").apply {
            style = "-fx-font: 52 arial;"
        }
        prefWidth = 400.0
        isVisible = false
        model.onGameDraw.addListener(this)
        padding = Insets(10.0,20.0,10.0,20.0)
        children.add(label)
        background = Background(
            BackgroundFill(
                Color.LIGHTGOLDENRODYELLOW,
                CornerRadii.EMPTY, Insets.EMPTY)
        );

    }

    override fun changed(observable: ObservableValue<out Boolean>?, oldValue: Boolean?, newValue: Boolean?) {
        if (newValue == true){
            isVisible = true
            val animation = ScaleTransition(Duration.millis(1000.0), this).apply{
                byX = 1.2
                byY = 1.2
            }
            animation.play()
        }

    }
}
// success message board
class ShowSuccessMessage(private val model: Model):Pane(), ChangeListener<Player> {
    var label = Label().apply {
        style = "-fx-font: 60 arial;"
    }
    init{
        prefWidth = 400.0
        isVisible = false
        model.onGameWin.addListener(this)
        padding = Insets(10.0,20.0,10.0,20.0)
        children.add(label)
        background = Background(
            BackgroundFill(
                Color.LIGHTGOLDENRODYELLOW,
                CornerRadii.EMPTY, Insets.EMPTY)
        );

    }


    override fun changed(observable: ObservableValue<out Player>?, oldValue: Player?, newValue: Player?) {
        if (newValue != null){
            isVisible = true
            label.text = " Player $newValue Win! "
            val animation = ScaleTransition(Duration.millis(1000.0), this).apply{
                byX = 1.2
                byY = 1.2

            }
            animation.play()

        }
    }
}

// game board
class BoardPane(private val model: Model):AnchorPane(), ChangeListener<Player>{
    init{
        var gridNode = ImageView(javaClass.getResource("/ui/assignments/connectfour/grid_8x7.png").toString())

        gridNode.setFitHeight(600.0)
        gridNode.setFitWidth(720.0)
        prefHeight = 800.0
// add two messages
        children.add(0, ShowDrawMessage(model).apply {
            AnchorPane.setTopAnchor(this, 300.0)
            AnchorPane.setLeftAnchor(this, 300.0)
        })
        children.add(0, ShowSuccessMessage(model).apply {
            AnchorPane.setTopAnchor(this, 300.0)
            AnchorPane.setLeftAnchor(this, 300.0)
        })

//         add game board img
        children.add(0, (gridNode).apply {
            AnchorPane.setRightAnchor(this, 140.0)
            AnchorPane.setBottomAnchor(this, 32.0)

        })
//         add the start button
        children.add(0, StartController(model).apply {
            AnchorPane.setTopAnchor(this, 32.0)
            AnchorPane.setLeftAnchor(this, 250.0)
        })

        model.onNextPlayer.addListener(this)
    }

    // add new player circle whenever there is a player switch
    override fun changed(observable: ObservableValue<out Player>?, oldValue: Player?, newValue: Player?) {
        if (newValue != Player.NONE || newValue != oldValue){

            val newCircle = Circle(model,this)

            children.add(0, newCircle)
        }

    }
}
// the overall layout class
class OverallView(private val model: Model):VBox() {
    init{
        val player1 = Label("Player # 1").apply{
            prefWidth =880.0
            style = "-fx-font: 20 arial;"
        }

        val player2 = Label("Player # 2").apply{
            minWidth = 100.0
            style = "-fx-font: 20 arial;"
        }
        val playerContainer = HBox(player1,player2).apply{
            padding = Insets(10.0,20.0,10.0,20.0)
        }

        val boardPane = BoardPane(model)
        children.addAll(playerContainer, boardPane)

    }
}
