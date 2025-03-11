package ai.wandz.example

import ai.wandz.example.databinding.ActivityMainBinding
import ai.wandz.sdk.api.WandzClient
import ai.wandz.sdk.api.interfaces.IAffinityUpdateListener
import ai.wandz.sdk.api.interfaces.IWandzAIFeaturesListener
import ai.wandz.sdk.api.interfaces.IWandzAudiencesListener
import ai.wandz.sdk.api.interfaces.IWandzPredictionsListener
import ai.wandz.sdk.api.models.enums.AppSection
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //report screen entered event to Wandz SDK
        //this event is used to track user navigation in the app
        WandzClient.reportScreenEnteredEvent(AppSection.HOME, null, this)

        binding.adaptiveSearchCard.setOnClickListener {
            adaptiveSearchDemo()
        }

        binding.interactionsCard.setOnClickListener {
            interactionsDemo()
        }

        binding.sectionsCard.setOnClickListener {
            screensDemo()
        }

        binding.eventsCard.setOnClickListener {
            eventsDemo()
        }

        binding.affinitiesCard.setOnClickListener {
            affinitiesDemo()
        }

        binding.dataCard.setOnClickListener {
            dataDemo()
        }

        binding.wandzAi.setOnClickListener() {
            //report button clicked event to Wandz SDK
            //this event is used to track user interaction with the app
            //you can use this event to track user interaction with any UI element in the app
            //custom event name can be any string
            WandzClient.reportEvent("wandz_ai_button_clicked")

            //open wandz.ai website in browser
            val wandzUri = Uri.parse("https://wandz.ai")
            startActivity(Intent(Intent.ACTION_VIEW, wandzUri))
        }
    }

    private fun adaptiveSearchDemo() {
        startActivity(Intent(this, AdaptiveSearchActivity::class.java))
    }

    private fun interactionsDemo() {
        startActivity(Intent(this, InteractionsActivity::class.java))
    }

    private fun screensDemo() {
        startActivity(Intent(this, SectionsActivity::class.java))
    }

    private fun eventsDemo() {
        startActivity(Intent(this, EventsActivity::class.java))
    }

    private fun affinitiesDemo() {
        startActivity(Intent(this, AffinitiesActivity::class.java))
    }

    private fun dataDemo() {
        startActivity(Intent(this, DataActivity::class.java))
    }
}