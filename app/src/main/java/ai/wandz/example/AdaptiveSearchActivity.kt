package ai.wandz.example

import ai.wandz.example.databinding.ActivityAdaptiveSearchBinding
import ai.wandz.sdk.api.WandzClient
import ai.wandz.sdk.api.models.WandzListener
import ai.wandz.sdk.api.models.enums.AffinityType
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import br.tiagohm.codeview.Language
import br.tiagohm.codeview.Theme

class AdaptiveSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdaptiveSearchBinding
    private lateinit var autocompleteAdapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdaptiveSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        title = resources.getString(R.string.adaptive_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeSearch()

        addCodeSample()
    }

    private fun addCodeSample() {
        WandzClient.getAdaptiveSearchSuggestions()
        val kotlinCode = "\n" +
            "//Get a list of all adaptive search suggestions\n" +
                "WandzClient.getAdaptiveSearchSuggestions()\n" +
                "\n" +
                "//Get a list of a filtered adaptive search suggestions\n" +
                "WandzClient.getAdaptiveSearchSuggestions(searchTerm)\n" +
                "\n" +
                "//Sample code of AutoCompleteTextView\n" +
                "autocompleteAdapter = ArrayAdapter<String>(\n" +
                "            this,\n" +
                "            android.R.layout.simple_dropdown_item_1line,\n" +
                "            WandzClient.getAdaptiveSearchSuggestions()\n" +
                "        )\n" +
                "        binding.autocompleteTextView.setAdapter(autocompleteAdapter)";
        binding.includeCodeView.codeView
            .setTheme(Theme.TOMORROW_NIGHT_BLUE)
            .setCode(kotlinCode)
            .setLanguage(Language.KOTLIN)
            .setShowLineNumber(true)
            .setStartLineNumber(9000)
            .apply()
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

    private fun initializeSearch() {
        autocompleteAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            WandzClient.getAdaptiveSearchSuggestions()
        )
        binding.autocompleteTextView.setAdapter(autocompleteAdapter)
        binding.autocompleteTextView.threshold = 1

        val wandzListener = object : WandzListener() {
            override fun affinityDetected(
                affinityType: AffinityType?,
                value: String?,
                keyword: String?
            ) {
                refillAutocompleteSuggestions()
                super.affinityDetected(affinityType, value, keyword)
            }
        }
        WandzClient.registerWandzListener(wandzListener)
    }

    fun refillAutocompleteSuggestions() {
        autocompleteAdapter.clear()
        autocompleteAdapter.addAll(WandzClient.getAdaptiveSearchSuggestions())
        autocompleteAdapter.notifyDataSetChanged()
    }
}