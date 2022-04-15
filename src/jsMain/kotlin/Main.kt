import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.jetbrains.compose.web.attributes.rowspan
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

var inGame by mutableStateOf(true)
var currentPlayer = 1
var clickCounter = 0
val displayChar = mutableStateListOf(' ', 'O', 'X', '?')

var buttonColors = (0..2).map { (0..2).map { Color.transparent } as MutableList } as MutableList

fun swapPlayers() {
    val bufferedChar = displayChar[1]
    displayChar[1] = displayChar[2]
    displayChar[2] = bufferedChar
}

class Field(row: Int, col: Int) {
    val id = row * 3 + col
    var content by mutableStateOf(0)
        private set
    fun setContent() {
        if (currentPlayer == 1) {
            content = 1
            currentPlayer = 2
            console.log("$id\t $content")
        }
        else if (currentPlayer == 2) {
            content = 2
            currentPlayer = 1
            console.log("$id\t $content")
        }
    }
}

val field: SnapshotStateList<SnapshotStateList<Field>> = mutableStateListOf()
fun buildField() {
    while (field.size > 0) { field.removeAt(0) }
    for (r in 0..2) {
        field.add(mutableStateListOf())
        for (c in 0..2) {
            field[r].add(Field(r, c))
        }
    }
}

fun checkWin() {
    if ((0..2).all { field[it][it].content == 1 }) { win(1, "cross"); (0..2).forEach { buttonColors[it][it] = Color.cyan } }
    if ((0..2).all { field[it][it].content == 2 }) { win(2, "cross"); (0..2).forEach { buttonColors[it][it] = Color.cyan } }
    for (n in 0..2) {
        if ((0..2).all { field[n][it].content == 1 }) { win(1, "row: ${n+1}"); (0..2).forEach { buttonColors[n][it] = Color.cyan } }
        if ((0..2).all { field[n][it].content == 2 }) { win(2, "row: ${n+1}"); (0..2).forEach { buttonColors[n][it] = Color.cyan } }
        if ((0..2).all { field[it][n].content == 1 }) { win(1, "col: ${n+1}"); (0..2).forEach { buttonColors[it][n] = Color.cyan } }
        if ((0..2).all { field[it][n].content == 2 }) { win(2, "col: ${n+1}"); (0..2).forEach { buttonColors[it][n] = Color.cyan } }
    }
    val crossFields = listOf(field[0][2].content, field[1][1].content, field[2][0].content)
    if (crossFields.all { it == 1 }) { win(1, "cross"); buttonColors[0][2] = Color.cyan; buttonColors[1][1] = Color.cyan; buttonColors[2][0] = Color.cyan }
    if (crossFields.all { it == 2 }) { win(2, "cross"); buttonColors[0][2] = Color.cyan; buttonColors[1][1] = Color.cyan; buttonColors[2][0] = Color.cyan }
    if (inGame && clickCounter >= 9) { inGame = false; console.log("No Player has won the game\n Impasse"); buttonColors = (0..2).map { (0..2).map { Color.tomato } as MutableList } as MutableList }
}

fun win(winner: Int, comment: String) {
    inGame = false
    console.log("Player $winner has won the game\n In: $comment")
    //window.alert("Player $winner has won the game\n In: $comment")
}

fun resetGame() {
    buildField()
    currentPlayer = 1
    clickCounter = 0
    buttonColors = (0..2).map { (0..2).map { Color.transparent } as MutableList } as MutableList
    inGame = true
}

fun main() {
    console.log("%c Welcome in Tic-tac-toe! ", "color: white; font-weight: bold; background-color: black;")
    buildField()

    renderComposable(rootElementId = "TicTacToe_root") {
        Div({ style { padding(25.px) } }) {
            Table({ style { property("aspect-ratio", "3"); width(75.percent); property("margin", "auto") } }) {
                Tr {
                    Td({ style { width(33.percent); border(5.px, LineStyle.Solid, Color.black); textAlign("center") } }) {
                        Button({
                            style { property("aspect-ratio", "2"); width(90.percent); backgroundColor(Color.transparent); padding(0.px); border(1.px, LineStyle.Solid, Color.white) }
                        }) {
                            Text("Player vs Player")
                        }
                    }
                    Td({ style { width(33.percent); border(5.px, LineStyle.Solid, Color.black); textAlign("center") } }) {
                        Button({
                            style { property("aspect-ratio", "2"); width(90.percent); backgroundColor(Color.transparent); padding(0.px); border(1.px, LineStyle.Solid, Color.white) }
                        }) {
                            Text("2")
                        }
                    }
                    Td({ style { width(33.percent); border(5.px, LineStyle.Solid, Color.black); textAlign("center") } }) {
                        Button({
                            style { property("aspect-ratio", "2"); width(90.percent); backgroundColor(Color.tomato); padding(0.px); border(1.px, LineStyle.Solid, Color.white) }
                            onClick { resetGame() }
                        }) {
                            Text("Reset")
                        }
                    }
                }
                Tr {
                    Td({ style { width(33.percent); border(5.px, LineStyle.Solid, Color.black); textAlign("center") } }) {
                        Button({
                            style { property("aspect-ratio", "2"); width(90.percent); backgroundColor(Color.transparent); padding(0.px); border(1.px, LineStyle.Solid, Color.white) }
                        }) {
                            Text("4")
                        }
                    }
                    Td({ style { width(33.percent); border(5.px, LineStyle.Solid, Color.black); textAlign("center") } }) {
                        Button({
                            style { property("aspect-ratio", "2"); width(90.percent); backgroundColor(Color.transparent); padding(0.px); border(1.px, LineStyle.Solid, Color.white) }
                            onClick { swapPlayers() }
                        }) {
                            Text("Swap")
                        }
                    }
                    Td({ style { width(33.percent); border(5.px, LineStyle.Solid, Color.black); textAlign("center") } }) {
                        Button({
                            style { property("aspect-ratio", "2"); width(90.percent); backgroundColor(Color.transparent); padding(0.px); border(1.px, LineStyle.Solid, Color.white) }
                        }) {
                            Text("6")
                        }
                    }
                }
            }
            Table({ style { property("aspect-ratio", "1"); width(75.percent); property("margin", "auto") } }) {
                for (i in 0..2) {
                    Tr {
                        for (j in 0..2) {
                            Td({
                                style {
                                    width(33.percent)
                                    border(5.px, LineStyle.Solid, Color.black)
                                    if (i == 0) { property("border-top-color", "transparent") }
                                    if (j == 0) { property("border-left-color", "transparent") }
                                    if (i == 2) { property("border-bottom-color", "transparent") }
                                    if (j == 2) { property("border-right-color", "transparent") }
                                    textAlign("center")
                                }
                            }) {
                                //Text("$i, $j")
                                Button({
                                    style { property("aspect-ratio", "1"); width(97.percent); backgroundColor(buttonColors[i][j]); padding(0.px); border(1.px, LineStyle.Solid, Color.white) }
                                    if (inGame && field[i][j].content == 0) {
                                        onClick {
                                            clickCounter += 1
                                            field[i][j].setContent()
                                            checkWin()
                                        }
                                    }
                                }) {
                                    Text("${displayChar[field[i][j].content]}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

