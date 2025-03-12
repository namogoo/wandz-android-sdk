package ai.wandz.example

import ai.wandz.example.databinding.ActivitySectionsBinding
import ai.wandz.sdk.api.WandzClient
import ai.wandz.sdk.api.models.enums.AppSection
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.tiagohm.codeview.Language
import br.tiagohm.codeview.Theme

class SectionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WandzClient.reportScreenEnteredEvent("Screens", null, this)

        title = resources.getString(R.string.sections)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeMockAppSectionSpinner()
        initializeUIListeners()
        addCodeSample()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed() // Handles the back navigation properly
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeMockAppSectionSpinner() {
        val items = ArrayList<String>()
        for (value in AppSection.entries) {
            items.add(value.toString())
        }
        val sortedItems = ArrayList<String>()
        sortedItems.add("Select Section Type")
        sortedItems.addAll(items.sorted())

        val appSectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sortedItems)
        binding.sectionTypeSelector.adapter = appSectionAdapter
        binding.sectionTypeSelector.setSelection(0)
    }

    private fun initializeUIListeners() {
        binding.sectionTypeSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    binding.btnOob.isEnabled = true
                } else {
                    binding.btnOob.isEnabled = false
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.btnOob.isEnabled = false
            }
        }
        binding.btnOob.isEnabled = false
        binding.btnOob.setOnClickListener{
            var position = binding.sectionTypeSelector.selectedItemPosition
            if (position > 0) {
                val selected = binding.sectionTypeSelector.selectedItem.toString()
                reportScreenEntered(selected)
                binding.sectionTypeSelector.setSelection(0)
            }
        }

        binding.sectionTypeCustom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnCustom.isEnabled = s.toString().trim().isNotEmpty()
            }
        })
        binding.btnCustom.isEnabled = false
        binding.btnCustom.setOnClickListener{
            val selected = binding.sectionTypeCustom.text.toString()
            reportScreenEntered(selected)
        }
    }

    private fun reportScreenEntered(selected: String) {
        if (selected.isNotEmpty()) {
            WandzClient.reportScreenEnteredEvent(selected, null, this@SectionsActivity)
            Toast.makeText(this, "Screen entered event reported: '$selected' section", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addCodeSample() {WandzClient.reportScreenEnteredEvent(AppSection.CATEGORY, "Jeans")
        WandzClient.getAdaptiveSearchSuggestions()
        WandzClient.reportScreenEnteredEvent(AppSection.HOME, null, this)
        val kotlinCode = "\n" +
            "//Report screen entered using AppSection enum, description &amp; current activity\n" +
                "WandzClient.reportScreenEnteredEvent(AppSection.CATEGORY, \"Jeans\", this)\n\n" +
                "//Report screen entered using AppSection enum only\n" +
                "WandzClient.reportScreenEnteredEvent(AppSection.HOME)\n\n" +
                "//Report screen entered using custom section only\n" +
                "WandzClient.reportScreenEnteredEvent(\"Other Section\")\n\n" +
                "//Report screen entered with description\n" +
                "WandzClient.reportScreenEnteredEvent(AppSection.CATEGORY, \"Jeans\")\n\n"
        binding.includeCodeView.codeView
            .setTheme(Theme.TOMORROW_NIGHT_BLUE)
            .setCode(kotlinCode)
            .setLanguage(Language.KOTLIN)
            .setShowLineNumber(true)
            .setStartLineNumber(9000)
            .apply()
    }
}