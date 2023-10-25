package com.example.simplecalculator

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    data class CustomButton(val label: String, val function: (View) -> Unit, val type: String)

    private val customButtons = listOf(
        CustomButton("CE", ::clearEntry, "operator"),
        CustomButton("C", ::clear, "operator"),
        CustomButton("BC", ::backspace, "operator"),
        CustomButton("/", ::operator, "operator"),
        CustomButton("7", ::add, "number"),
        CustomButton("8", ::add, "number"),
        CustomButton("9", ::add, "number"),
        CustomButton("×", ::operator, "operator"),
        CustomButton("4", ::add, "number"),
        CustomButton("5", ::add, "number"),
        CustomButton("6", ::add, "number"),
        CustomButton("−", ::operator, "operator"),
        CustomButton("1", ::add, "number"),
        CustomButton("2", ::add, "number"),
        CustomButton("3", ::add, "number"),
        CustomButton("+", ::operator, "operator"),
        CustomButton("+/-", ::voidfun, "operator"),
        CustomButton("0", ::add, "number"),
        CustomButton(".", ::voidfun, "operator"),
        CustomButton("=", ::calculate, "operator")
    )

    private var calculation = ""
    private var input = "0"
    private var tempResult = 0
    private var tempOper = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calculationText: TextView = findViewById(R.id.textView)
        val inputText: TextView = findViewById(R.id.textView2)
        inputText.text = input

        val buttonContainer = findViewById<GridLayout>(R.id.buttonContainer)

        for (operation in customButtons) {
            val button = Button(this)
            button.text = operation.label
            button.isAllCaps = false
            button.setTextColor(-0x1000000) // Black color
            button.height = 200

            if (operation.type == "number") {
                button.setBackgroundColor(-0x111112) // Light gray
            } else {
                button.setBackgroundColor(-0x222223) // Dark gray
            }

            val margin = 8
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.setMargins(margin, margin, margin, margin)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }

            button.layoutParams = params
            button.setOnClickListener { v -> operation.function(v) }
            buttonContainer.addView(button)
        }
    }

    fun add(view: View) {
        if (view is Button) {
            val label = view.text.toString()
            if (input == "0") {
                input = label
            } else {
                input += label
            }
            reshow()
        }
    }

    fun operator(view: View) {
        if (view is Button) {
            val operator = view.text.toString()

            // Check if the previous button pressed was also an operator
            if (!tempOper.isEmpty() && input.isEmpty()) {
                // If the previous button was an operator, update it
                calculation = calculation.dropLast(1) + operator
            } else {
                if (!tempOper.isEmpty()) {
                    // Calculate the result when an operator is pressed again
                    calculate()
                } else
                tempResult = input.toInt()
                input = ""
                calculation = tempResult.toString() + operator
            }

            tempOper = operator
            reshow()
        }
    }

    fun calculate(view: View) {
        if (!tempOper.isEmpty()) {
            calculate()
            // Set the input to the calculated result
            input = tempResult.toString()
            calculation = ""
            tempOper = ""
            reshow()
        }
    }
    fun calculate() {
        if (!tempOper.isEmpty()) {
            val secondNumber = input.toInt()
            when (tempOper) {
                "+" -> tempResult += secondNumber
                "×" -> tempResult *= secondNumber
                "−" -> tempResult -= secondNumber
                "/" -> tempResult /= secondNumber
            }
        }
    }

    fun clearEntry(view: View) {
        input = "0"
        reshow()
    }

    fun clear(view: View) {
        input = "0"
        calculation = ""
        tempResult = 0
        tempOper = ""
        reshow()
    }

    fun backspace(view: View) {
        if (input.isNotEmpty()) {
            input = input.dropLast(1) // Remove the last character
        }
        reshow()
    }

    fun reshow() {
        val calculationText: TextView = findViewById(R.id.textView)
        val inputText: TextView = findViewById(R.id.textView2)
        calculationText.text = calculation
        inputText.text = input
    }
    fun voidfun(view: View) {

    }
}
