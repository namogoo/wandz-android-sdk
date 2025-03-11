package ai.wandz.example

import ai.wandz.activate.WandzActivate
import ai.wandz.activate.models.ClientInteractionRecord
import ai.wandz.example.databinding.ActivityInteractionsBinding
import ai.wandz.sdk.api.WandzClient
import ai.wandz.sdk.api.models.Interaction
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.tiagohm.codeview.Language
import br.tiagohm.codeview.Theme

class InteractionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInteractionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInteractionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WandzClient.reportScreenEnteredEvent("Interactions Screen", null, this)

        // Allow interactions when the Interaction screen is visible
        WandzActivate.allowInteractions(true)

        title = resources.getString(R.string.interactions)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.forceInteractionButton.setOnClickListener {
            WandzActivate.getLastInteractionDisplayed().let {
                WandzActivate.showInteraction(it)
            }
        }
        addCodeSample()
        setInteractionListener()
    }

    private fun setInteractionListener() {
        WandzActivate.setWandzInteractionsListener { interaction: Interaction, clientInteractionRecord: ClientInteractionRecord ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Interaction")
            val msg = StringBuilder("Interaction GUID: " + interaction.guid + "\n\n")
            msg.append("Client Interaction Type: ").append(clientInteractionRecord.type)
                .append("\n")
            for (key in clientInteractionRecord.values.keys) {
                msg.append(key).append(" = ").append(clientInteractionRecord.values[key])
                    .append("\n")
            }
            builder.setMessage(msg.toString())
            builder.setPositiveButton(
                "OK"
            ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            builder.show()
        }
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

    private fun addCodeSample() {
        WandzClient.getAdaptiveSearchSuggestions()
        val kotlinCode = "\n"+
            "//Re display the last interaction\n" +
                "WandzActivate.getLastInteractionDisplayed().let {\n" +
                "    WandzActivate.showInteraction(it)\n" +
                "}\n" +
                "//Register to interaction action\n" +
                "WandzActivate.setWandzInteractionsListener { interaction: Interaction, clientInteractionRecord: ClientInteractionRecord ->\n" +
                "...\n" +
                "}\n" +
                "//Allow / Prevent interactions programmatically\n" +
                "WandzActivate.allowInteractions(true)\n" +
                "WandzActivate.allowInteractions(false)\n"
        binding.includeCodeView.codeView
            .setTheme(Theme.TOMORROW_NIGHT_BLUE)
            .setCode(kotlinCode)
            .setLanguage(Language.KOTLIN)
            .setShowLineNumber(true)
            .setStartLineNumber(9000)
            .apply()
    }
}