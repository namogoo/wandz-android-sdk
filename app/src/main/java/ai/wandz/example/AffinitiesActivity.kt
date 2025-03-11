package ai.wandz.example

import ai.wandz.example.databinding.ActivityAffinitiesBinding
import ai.wandz.example.databinding.ActivityEventsBinding

import ai.wandz.sdk.api.WandzClient
import ai.wandz.sdk.api.models.enums.EventType
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import br.tiagohm.codeview.Language
import br.tiagohm.codeview.Theme

class AffinitiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAffinitiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAffinitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WandzClient.reportScreenEnteredEvent("affinities", null, this)

        title = resources.getString(R.string.affinities)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnScan.setOnClickListener {
            binding.affinitiesInput.text?.let {
                WandzClient.calculateAffinities(it.toString())
            }
            showAffinitiesInUI()
        }

        showAffinitiesInUI()
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

    private fun showAffinitiesInUI() {
        val affinities = java.lang.StringBuilder()
        if (WandzClient.getAffinities() != null) {
            for (affinity in WandzClient.getAffinities()) {
                affinities.append("• ").append(affinity).append("\n")
            }
        }
        binding.affinitiesReport.text = affinities.toString()
    }

    private fun addCodeSample() {
        val kotlinCode = "\n" +
            "//Scan text to find and report user affinities\n" +
                "WandzClient.calculateAffinities(\"Some text to scan\")\n" +
                "\n" +
                "//Report a specific category type affinity\n" +
                "WandzClient.reportAffinity(AffinityType.CATEGORY, \"Summer\")\n" +
                "//Report a specific brand affinity\n" +
                "WandzClient.reportAffinity(AffinityType.Brand, \"Some Brand\")\n" +
                "//Report a specific color affinity\n" +
                "WandzClient.reportAffinity(AffinityType.Color, \"Blue\")\n";

        binding.includeCodeView.codeView
            .setTheme(Theme.TOMORROW_NIGHT_BLUE)
            .setCode(kotlinCode)
            .setLanguage(Language.KOTLIN)
            .setShowLineNumber(true)
            .setStartLineNumber(1)
            .apply()
    }
}