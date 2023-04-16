package ui.assignments.connectfour.controller
import javafx.animation.Interpolator
import javafx.animation.ScaleTransition
import javafx.animation.TranslateTransition
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.util.Duration
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Piece
import ui.assignments.connectfour.model.Player
import java.util.*
import kotlin.math.floor
import kotlin.math.round


// start button
class StartController(model: Model) : Button("Click Here to Start Game!") {
    init{
        prefWidth = 500.0
        prefHeight = 80.0
        style = "-fx-font: 20 arial; -fx-text-fill: GREEN; "
        background = Background(
            BackgroundFill(Color.LIGHTGREEN,
            CornerRadii.EMPTY, Insets.EMPTY)
        );

        onAction = EventHandler {

            val animation = ScaleTransition(Duration.millis(1000.0), this).apply{
                toX = 0.0
                toY = 0.0

            }
            animation.play()
            val tm = Timer()
            tm.schedule(object : TimerTask() {
                override fun run() {
                    Platform.runLater {
                        isVisible = false
                        model.startGame()
                    }
                }
            }, 1000)


        }
    }
}
// the drag function is written with copying some of the class material
data class DragInfo(var target: Circle? = null, var anchorX: Double, var initialX: Double)
// player's circle
class Circle(private val model:Model, anchorPane:AnchorPane): ImageView(), ChangeListener<Piece?> {
    var inAction = true
    var player = Player.NONE
    init{
        model.onPieceDropped.addListener(this)
        y = 10.0
        player = model.onNextPlayer.value
//        generate circle according to player's type
        if (player == Player.ONE){

            image = Image(javaClass.getResource("/ui/assignments/connectfour/piece_red.png").toString())
            x = 10.0

        } else if (player == Player.TWO){
            image = Image(javaClass.getResource("/ui/assignments/connectfour/piece_yellow.png").toString())
            x = 900.0
        }
        fitHeight = 72.0
        fitWidth = 72.0
//        handle player movement
        var dragInfo = DragInfo(this, x, x)

        addEventFilter(MouseEvent.MOUSE_PRESSED) {

            dragInfo = DragInfo(this, it.sceneX, translateX)
        }
        addEventFilter(MouseEvent.MOUSE_DRAGGED) {
            if (inAction) {
                val newX = dragInfo.initialX + it.sceneX - dragInfo.anchorX
                if (it.sceneX <= 108 || it.sceneX >= 860) {

                    if (it.sceneX < 20.0) {
                        translateX = if (player == Player.TWO) {
                            -880.0
                        } else {
                            20.0
                        }

                    } else if (it.sceneX > 900.0) {
                        translateX = if (player == Player.TWO) {
                            0.0
                        } else {
                            900.0
                        }

                    } else {
                        translateX = newX
                    }
                } else {
                    translateX = if (player == Player.TWO) {
                        floor((newX - 108) / 90 + 1) * 90 + 60
                    } else {
                        floor((newX - 108) / 90 + 1) * 90 + 50
                    }
                }
            }
        }
//        handle drop event
            addEventFilter(MouseEvent.MOUSE_RELEASED) {
                if (inAction) {
                    var column = -1
                    if (player == Player.ONE) {
                        if (translateX in 140.0..770.0) {
                            column = (round(translateX - 140) / 90).toInt()
                        }

                    } else {
                        if (translateX <= -120 && translateX >= -750)
                            column = (round(750 + translateX) / 90).toInt()
                    }

                    if (column in 0..7) {
                        model.dropPiece(column)
                    } else {
                        val moveback = TranslateTransition(Duration.millis(1000.0), this).apply {
                            toX = dragInfo.initialX
                            interpolator = Interpolator.LINEAR
                            isAutoReverse = false
                        }
                        moveback.play()

                    }
                }
            }

        }

// if dropped find the dropping location and show animation for dropping
    override fun changed(observable: ObservableValue<out Piece?>?, oldValue: Piece?, newValue: Piece?) {
        if (newValue != null && inAction) {
            val depth = newValue.y

            val animation = TranslateTransition(Duration.millis(1000.0), this).apply{
                byY = depth * 85 + 123.0
                interpolator = Interpolator.EASE_IN
                isAutoReverse = false
            }
            animation.play()
            inAction = false

        }

    }

}