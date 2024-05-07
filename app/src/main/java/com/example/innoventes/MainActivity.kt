package com.example.innoventes

import MainViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViewModel()

        val panEditText: EditText = findViewById(R.id.panEditText)
        val nextButton: Button = findViewById(R.id.nextButton)
        val noPanButton: Button = findViewById(R.id.no_pan_button)

        val editTextDay = findViewById<EditText>(R.id.editTextDay)
        val editTextMonth = findViewById<EditText>(R.id.editTextMonth)
        val editTextYear = findViewById<EditText>(R.id.editTextYear)

        // Observe the LiveData to enable/disable the Next button
        viewModel.isNextButtonEnabled.observe(
            this,
        ) { isEnabled ->
            nextButton.isEnabled = isEnabled
        }
        setupEditTextListeners(editTextDay, editTextMonth, editTextYear,panEditText)

        noPanButton.setOnClickListener {
            finish()
        }
        nextButton.setOnClickListener {
            showToast("Details submitted successfully")
            finish();
        }
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun setupEditTextListeners(vararg editTexts: EditText) {
        editTexts.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 2 && index < editTexts.size - 1 && editText != editTexts[2] && editText != editTexts[3]) {
                        editTexts[index + 1].requestFocus()
                    }
                    if (editTexts.all { it.text.isNotEmpty() }) {
                        val isBirthdateValid = " ${editTexts[0].text}/${editTexts[1].text}/${editTexts[2].text}"
                        val isPanValid = editTexts[3].text.toString()
                        viewModel.validateInputs(isBirthdateValid, isPanValid)
                    }

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
