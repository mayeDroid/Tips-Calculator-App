package com.example.tipcalculator

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENTAGE = 15

class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount: EditText
    private lateinit var textViewTotalAmt: TextView
    private lateinit var textViewBase: TextView
    private lateinit var textViewPercentage: TextView
    private lateinit var textViewTipsAmt: TextView
    private lateinit var seekBarTips: SeekBar
    private lateinit var textViewPecentageDescriptn: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.etBaseAmount)
        textViewBase = findViewById(R.id.textViewBase)
        textViewTipsAmt = findViewById(R.id.textViewTipsAmt)
        textViewTotalAmt = findViewById(R.id.textViewTotalAmt)
        textViewPercentage = findViewById(R.id.textViewPercentage)
        seekBarTips = findViewById(R.id.seekBarTips)
        textViewPecentageDescriptn = findViewById(R.id.textViewPecentageDescriptn)

// we want the at startup the initial percentage should be 15%
        seekBarTips.progress = INITIAL_TIP_PERCENTAGE
        textViewPercentage.text = "$INITIAL_TIP_PERCENTAGE%"
        updateTipsDescription(INITIAL_TIP_PERCENTAGE)

        seekBarTips.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")

                textViewPercentage.text = progress.toString()  // "$progress"
                calculateTipsAndTotal()
                updateTipsDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                calculateTipsAndTotal()
            }

        })

    }

    private fun updateTipsDescription(tipsPercent: Int) {
        val tipsDescription = when (tipsPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..30 -> "Very good"
            else -> "Amazing"
        }

        textViewPecentageDescriptn.text = tipsDescription

        // we want to transform the colors of the tips description

        val color = ArgbEvaluator().evaluate(
            tipsPercent.toFloat() / seekBarTips.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        textViewPecentageDescriptn.setTextColor(color)

    }

    private fun calculateTipsAndTotal() {
        // 1. we want to get the base value and tip percentage
        if (etBaseAmount.text.isEmpty()) {   // without this block if we clear the editText the app will crash cos it could not convert the null to string
            textViewTipsAmt.text = ""
            textViewTotalAmt.text = ""
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipsPercent = seekBarTips.progress

        // 2. compute the tips and total
        val tipAmount = baseAmount * tipsPercent / 100
        val totalAmount = baseAmount + tipAmount

        // 3. update the UI
        textViewTipsAmt.text = "%.2f".format(tipAmount)  // this rounds it up to two decimal places
        textViewTotalAmt.text = "%.2f".format(totalAmount)

    }
}