package ai.wandz.example

import ai.wandz.example.databinding.ActivityDataBinding
import ai.wandz.sdk.api.WandzClient
import ai.wandz.sdk.api.models.Audience
import ai.wandz.sdk.api.models.PredictionModel
import ai.wandz.sdk.api.models.WandzListener
import ai.wandz.sdk.api.models.enums.AffinityType
import ai.wandz.sdk.api.models.enums.AppSection
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList

class DataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataBinding
    private val mapAttributes = HashMap<String, String>()
    private var wandzListener: WandzListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        WandzClient.trackView("data", null, this)

        title = resources.getString(R.string.data)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listenToWandz()
        getAdaptiveSearchSuggestions()
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

    private fun listenToWandz() {
        //prediction is requested automatically when screen is entered
        //and can be requested manually using the following method
        WandzClient.requestPrediction()

        wandzListener = object : WandzListener() {
            override fun affinityDetected(
                affinityType: AffinityType?,
                value: String?,
                keyword: String?
            ) {
                affinityUpdateCallback()
                super.affinityDetected(affinityType, value, keyword)
            }
            override fun attributeUpdated(key: String, value: Any?) {
                attributeCallback(key, value)
                super.attributeUpdated(key, value)
            }
            override fun predictionsUpdated(predictions: ArrayList<PredictionModel>) {
                predictionsCallback(predictions)
                super.predictionsUpdated(predictions)
            }
            override fun audiencesUpdated(audiences: ArrayList<Audience>) {
                audienceCallback(audiences)
                super.audiencesUpdated(audiences)
            }
        }
        WandzClient.registerWandzListener(wandzListener)

        // On every screen change a report needs to be send to describe the type of screen
        WandzClient.trackView(AppSection.CATEGORY, "Jeans", this)

        // Custom attributes can be set using the following method
        WandzClient.setCustomAttribute("custom", "Custom Value")

        // Affinities can be calculated using the following method
        WandzClient.calculateAffinities("xyz, veSt, test, Green")
        displayWandzAffinities()

        audienceCallback(WandzClient.getAudiences())
    }

    private fun attributeCallback (key: String?, value: Any?) {
        if (key == null || value == null) {
            return
        }

        val sortedAttributes = WandzClient.getAllAttributes().toSortedMap()
        val toShow = StringBuilder()
        for ((index, attribute) in sortedAttributes.entries.withIndex()) {
            toShow.append(index + 1).append(". ").append(attribute.key).append(" = ").append(attribute.value).append("\n")
        }
        binding.tvAttributes.text = toShow.toString()

        val sbEvents = StringBuilder()
        WandzClient.getClientEvents().forEach {
            sbEvents.append("• ").append(it.key).append("=").append(it.value).append("\n")
        }
        binding.tvEvents.text = sbEvents.toString()
    }


    private fun predictionsCallback(predictions : ArrayList<PredictionModel>) {
        val toShow = StringBuilder()
        for (prediction in predictions) {
            toShow.append("• ").append(prediction.displayName).append(" = ")
                .append(prediction.prediction).append(" (")
                .append(prediction.predictionScore).append(")").append("\n")
        }
        binding.tvPredictions.text = toShow.toString()
    }

    private fun audienceCallback(audiences : ArrayList<Audience>) {
        val toShow = StringBuilder()
        for (audience in audiences) {
            toShow.append("• ").append(audience.name).append("\n")
        }
        binding.tvAudiences.text = toShow.toString()
    }

    private fun displayWandzAffinities() {
        val affinities = WandzClient.getAffinities()
        val toShow = StringBuilder()
        for (affinity in affinities) {
            toShow.append("• ").append(affinity).append("\n")
        }
        binding.tvAffinities.text = toShow.toString()
    }

    private fun getAdaptiveSearchSuggestions() {
        val suggestions = WandzClient.getAdaptiveSearchSuggestions()
        val toShow = StringBuilder()
        for (suggestion in suggestions) {
            toShow.append("• ").append(suggestion).append("\n")
        }
        binding.tvAdaptiveSearch.text = toShow.toString()
    }

    fun affinityUpdateCallback() {
        displayWandzAffinities()
    }
}