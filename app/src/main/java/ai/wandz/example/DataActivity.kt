package ai.wandz.example

import ai.wandz.example.databinding.ActivityDataBinding
import ai.wandz.sdk.api.WandzClient
import ai.wandz.sdk.api.interfaces.IAffinityUpdateListener
import ai.wandz.sdk.api.interfaces.IWandzAIFeaturesListener
import ai.wandz.sdk.api.interfaces.IWandzAudiencesListener
import ai.wandz.sdk.api.interfaces.IWandzPredictionsListener
import ai.wandz.sdk.api.models.enums.AppSection
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class DataActivity : AppCompatActivity(), IAffinityUpdateListener {
    private lateinit var binding: ActivityDataBinding
    private val mapFeatures = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WandzClient.reportScreenEnteredEvent("data", null, this)

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

        registerToWandzPredictions()
        registerToWandzAiFeatures()
        registerToWandzAudiences()
        WandzClient.registerAffinityUpdateListener(this)


        // On every screen change a report needs to be send to describe the type of screen
        WandzClient.reportScreenEnteredEvent(AppSection.CATEGORY, "Jeans", this)

        // Custom AI features can be set using the following method
        WandzClient.setCustomAiFeature("custom", "Custom Value")

        // Affinities can be calculated using the following method
        WandzClient.calculateAffinities("xyz, veSt, test, Green")
        displayWandzAffinities()

    }

    private fun registerToWandzAiFeatures() {

        val listener = IWandzAIFeaturesListener { key, value ->
            if (key == null || value == null) {
                return@IWandzAIFeaturesListener
            }
            mapFeatures[key] = value.toString()
            val sortedFeatures = mapFeatures.toSortedMap()
            val toShow = StringBuilder()
            for ((index, feature) in sortedFeatures.entries.withIndex()) {
                toShow.append(index + 1).append(". ").append(feature.key).append(" = ").append(feature.value).append("\n")
            }
            binding.tvAiFeatures.text = toShow.toString()

            val sbEvents = StringBuilder()
            WandzClient.getClientEvents().forEach {
                sbEvents.append("• ").append(it.key).append("=").append(it.value).append("\n")
            }
            binding.tvEvents.text = sbEvents.toString()
        }
        // Register the listener if you wish to get notified on every feature change
        // Alternatively, you can use WandzClient.getAiFeatureValue method to get the current features
        WandzClient.registerAiFeaturesListeners(listener)
    }

    private fun registerToWandzPredictions() {
        val listener =  IWandzPredictionsListener { predictions ->
            val toShow = StringBuilder()
            for (prediction in predictions) {
                toShow.append("• ").append(prediction.displayName).append(" = ")
                    .append(prediction.prediction).append(" (")
                    .append(prediction.predictionScore).append(")").append("\n")
            }
            binding.tvPredictions.text = toShow.toString()
        }
        WandzClient.registerPredictionsListener(listener)
    }

    private fun registerToWandzAudiences() {
        val listener =  IWandzAudiencesListener { audiences ->
            val toShow = StringBuilder()
            for (audience in audiences) {
                toShow.append("• ").append(audience.name).append("\n")
            }
            binding.tvAudiences.text = toShow.toString()
        }
        WandzClient.registerAudiencesListeners(listener)
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

    override fun affinityUpdateCallback() {
        displayWandzAffinities()
    }
}