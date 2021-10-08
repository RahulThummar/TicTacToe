package com.rku.tictactoe

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_firstpage.*
import kotlinx.android.synthetic.main.activity_thirdpage.*
import kotlin.system.exitProcess

var isMyMove = isCodeMaker;

class ThirdPage : AppCompatActivity() {

    lateinit var player1: TextView
    lateinit var player2: TextView
    lateinit var box1Btn:Button
    lateinit var box2Btn:Button
    lateinit var box3Btn:Button
    lateinit var box4Btn:Button
    lateinit var box5Btn:Button
    lateinit var box6Btn:Button
    lateinit var box7Btn:Button
    lateinit var box8Btn:Button
    lateinit var box9Btn:Button
    lateinit var resetBtn:Button
    lateinit var turn:Button

    var player1Count = 0
    var player2Count = 0
    var player1List = ArrayList<Int>()
    var player2List = ArrayList<Int>()
    var emptyCells = ArrayList<Int>()
    var activeUser = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thirdpage)

        box1Btn = findViewById(R.id.idBtnBox1)
        box2Btn = findViewById(R.id.idBtnBox2)
        box3Btn = findViewById(R.id.idBtnBox3)
        box4Btn = findViewById(R.id.idBtnBox4)
        box5Btn = findViewById(R.id.idBtnBox5)
        box6Btn = findViewById(R.id.idBtnBox6)
        box7Btn = findViewById(R.id.idBtnBox7)
        box8Btn = findViewById(R.id.idBtnBox8)
        box9Btn = findViewById(R.id.idBtnBox9)
        resetBtn = findViewById(R.id.idBtnReset)
        player1 = findViewById(R.id.idPlayer1)
        player2 = findViewById(R.id.idPlayer2)
        turn = findViewById(R.id.idTurn)

        resetBtn.setOnClickListener {
            reset()
        }
        FirebaseDatabase.getInstance().reference.child("data").child(code)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    var data = snapshot.value
                    if (isMyMove == true) {
                        isMyMove = false
                        moveonline(data.toString(), isMyMove)
                    } else {
                        isMyMove = true
                        moveonline(data.toString(), isMyMove)
                    }


                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    reset()
                    errorMsg("Game Reset")
                }

            })
    }

    fun clickfun(view: View) {
        if (isMyMove) {
            val but = view as Button
            var cellOnline = 0
            when (but.id) {
                R.id.idBtnBox1 -> cellOnline = 1
                R.id.idBtnBox2 -> cellOnline = 2
                R.id.idBtnBox3 -> cellOnline = 3
                R.id.idBtnBox4 -> cellOnline = 4
                R.id.idBtnBox5 -> cellOnline = 5
                R.id.idBtnBox6 -> cellOnline = 6
                R.id.idBtnBox7 -> cellOnline = 7
                R.id.idBtnBox8 -> cellOnline = 8
                R.id.idBtnBox9 -> cellOnline = 9
                else -> {
                    cellOnline = 0
                }

            }
            playerTurn = false;
            Handler().postDelayed(Runnable { playerTurn = true }, 600)
            playnow(but, cellOnline)
            updateDatabase(cellOnline);

        } else {
            Toast.makeText(this, "Wait for your turn", Toast.LENGTH_LONG).show()
        }
    }

    fun playnow(buttonSelected: Button, currCell: Int) {
        val audio = MediaPlayer.create(this, R.raw.notification)

        buttonSelected.text = "X"
        emptyCells.remove(currCell)
        turn.text = "Turn : Player 2"
        buttonSelected.setTextColor(Color.parseColor("#EC0C0C"))
        player1List.add(currCell)
        emptyCells.add(currCell)
        audio.start()
        //Handler().postDelayed(Runnable { audio.pause() } , 500)
        buttonSelected.isEnabled = false
        Handler().postDelayed(Runnable { audio.release() }, 200)
        checkwinner()
        /*val checkWinner = checkwinner()
        if(checkWinner == 1) {
            Handler().postDelayed(Runnable { reset() }, 2000)
        }*/

        /*else
        {
            buttonSelected.text = "O"
            audio.start()
            buttonSelected.setTextColor(Color.parseColor("#D22BB804"))
            //Handler().postDelayed(Runnable { audio.pause() } , 500)
            activeUser = 1
            player2List.add(currCell)
            emptyCells.add(currCell)
            Handler().postDelayed(Runnable { audio.release() } , 200)
            buttonSelected.isEnabled = false
            val checkWinner  = checkwinner()
            if(checkWinner == 1)
                Handler().postDelayed(Runnable { reset() } , 4000)
        }*/

    }

    fun moveonline(data: String, move: Boolean) {
        val audio = MediaPlayer.create(this, R.raw.notification)

        if (move) {
            var buttonselected: Button?
            buttonselected = when (data.toInt()) {
                1 -> box1Btn
                2 -> box2Btn
                3 -> box3Btn
                4 -> box4Btn
                5 -> box5Btn
                6 -> box6Btn
                7 -> box7Btn
                8 -> box8Btn
                9 -> box9Btn
                else -> {
                    box1Btn
                }
            }
            buttonselected.text = "O"
            turn.text = "Turn : Player 1"
            buttonselected.setTextColor(Color.parseColor("#D22BB804"))
            player2List.add(data.toInt())
            emptyCells.add(data.toInt())
            audio.start()
            //Handler().postDelayed(Runnable { audio.pause() } , 500)
            Handler().postDelayed(Runnable { audio.release() }, 200)
            buttonselected.isEnabled = false
            checkwinner()
        }
    }

    fun updateDatabase(cellId: Int) {
        FirebaseDatabase.getInstance().reference.child("data").child(code).push().setValue(cellId);
    }

    fun checkwinner(): Int {
        val audio = MediaPlayer.create(this, R.raw.celebration)
        if ((player1List.contains(1) && player1List.contains(2) && player1List.contains(3)) || (player1List.contains(
                1
            ) && player1List.contains(4) && player1List.contains(7)) ||
            (player1List.contains(3) && player1List.contains(6) && player1List.contains(9)) || (player1List.contains(
                7
            ) && player1List.contains(8) && player1List.contains(9)) ||
            (player1List.contains(4) && player1List.contains(5) && player1List.contains(6)) || (player1List.contains(
                1
            ) && player1List.contains(5) && player1List.contains(9)) ||
            player1List.contains(3) && player1List.contains(5) && player1List.contains(7) || (player1List.contains(2) && player1List.contains(
                5
            ) && player1List.contains(8))
        ) {
            player1Count += 1
            buttonDisable()
            audio.start()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 1 Wins!!" + "\n\n" + "Do you want to play again")
            build.setPositiveButton("Ok") { dialog, which ->
                reset()
                audio.release()
            }
            build.setNegativeButton("Exit") { dialog, which ->
                audio.release()
                removeCode()
                exitProcess(1)

            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1


        } else if ((player2List.contains(1) && player2List.contains(2) && player2List.contains(3)) || (player2List.contains(
                1
            ) && player2List.contains(4) && player2List.contains(7)) ||
            (player2List.contains(3) && player2List.contains(6) && player2List.contains(9)) || (player2List.contains(
                7
            ) && player2List.contains(8) && player2List.contains(9)) ||
            (player2List.contains(4) && player2List.contains(5) && player2List.contains(6)) || (player2List.contains(
                1
            ) && player2List.contains(5) && player2List.contains(9)) ||
            player2List.contains(3) && player2List.contains(5) && player2List.contains(7) || (player2List.contains(2) && player2List.contains(
                5
            ) && player2List.contains(8))
        ) {
            player2Count += 1
            audio.start()
            buttonDisable()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 2 Wins!!" + "\n\n" + "Do you want to play again")
            build.setPositiveButton("Ok") { dialog, which ->
                reset()
                audio.release()
            }
            build.setNegativeButton("Exit") { dialog, which ->
                audio.release()
                removeCode()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1
        } else if (emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3) && emptyCells.contains(
                4
            ) && emptyCells.contains(5) && emptyCells.contains(6) && emptyCells.contains(7) &&
            emptyCells.contains(8) && emptyCells.contains(9)
        ) {

            val build = AlertDialog.Builder(this)
            build.setTitle("Game Draw")
            build.setMessage("Nobody Wins" + "\n\n" + "Do you want to play again")
            build.setPositiveButton("Ok") { dialog, which ->
                audio.release()
                reset()
            }
            build.setNegativeButton("Exit") { dialog, which ->
                audio.release()
                exitProcess(1)
                removeCode()
            }
            build.show()
            return 1

        }
        return 0
    }

    fun reset() {
        player1List.clear()
        player2List.clear()
        emptyCells.clear()
        activeUser = 1;
        for (i in 1..9) {
            var buttonselected: Button?
            buttonselected = when (i) {
                1 -> box1Btn
                2 -> box2Btn
                3 -> box3Btn
                4 -> box4Btn
                5 -> box5Btn
                6 -> box6Btn
                7 -> box7Btn
                8 -> box8Btn
                9 -> box9Btn
                else -> {
                    box1Btn
                }
            }
            buttonselected.isEnabled = true
            buttonselected.text = ""
            player1.text = "Player1 : $player1Count"
            textView2.text = "Player2 : $player2Count"
            isMyMove = isCodeMaker
            //startActivity(Intent(this,ThirdPage::class.java))
            if (isCodeMaker) {
                FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
            }


        }
    }

    fun buttonDisable() {
        for (i in 1..9) {
            val buttonSelected = when (i) {
                1 -> box1Btn
                2 -> box2Btn
                3 -> box3Btn
                4 -> box4Btn
                5 -> box5Btn
                6 -> box6Btn
                7 -> box7Btn
                8 -> box8Btn
                9 -> box9Btn
                else -> {
                    box1Btn
                }
            }
            if (buttonSelected.isEnabled == true)
                buttonSelected.isEnabled = false
        }
    }

    fun buttoncelldisable() {
        emptyCells.forEach {
            val buttonSelected = when (it) {
                1 -> box1Btn
                2 -> box2Btn
                3 -> box3Btn
                4 -> box4Btn
                5 -> box5Btn
                6 -> box6Btn
                7 -> box7Btn
                8 -> box8Btn
                9 -> box9Btn
                else -> {
                    box1Btn
                }

            }
            if (buttonSelected.isEnabled == true)
                buttonSelected.isEnabled = false;
        }
    }

    fun removeCode() {
        if (isCodeMaker) {
            FirebaseDatabase.getInstance().reference.child("codes").child(keyValue).removeValue()
        }
    }

    fun errorMsg(value: String) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
    }

    fun disableReset() {
        button10.isEnabled = false
        Handler().postDelayed(Runnable { button10.isEnabled = true }, 2200)
    }

    override fun onBackPressed() {
        removeCode()
        if (isCodeMaker) {
            FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
        }
        exitProcess(0)
    }
}