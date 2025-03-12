package ai.wandz.example

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

class EventsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WandzClient.reportScreenEnteredEvent("events", null, this)

        title = resources.getString(R.string.events)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeMockEventsSpinner()
        showEventsInUI()
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

    private fun initializeMockEventsSpinner() {
        val items = ArrayList<String>()
        for (value in EventType.entries) {
            items.add(value.toString())
        }
        val sortedItems = ArrayList<String>()
        sortedItems.add("Select Event Type")
        sortedItems.addAll(items.sorted())
        val eventTypeAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sortedItems)
        binding.eventsSelector.adapter = eventTypeAdapter
        binding.eventsSelector.setSelection(0)
        binding.btnOob.isEnabled = false
        binding.eventsSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        binding.btnOob.setOnClickListener {
            var position = binding.eventsSelector.selectedItemPosition
            if (position > 0) {
                val selected = EventType.valueOfName(eventTypeAdapter.getItem(position))

                //event can be reported using the following method
                WandzClient.reportEvent(selected)

                binding.eventsSelector.setSelection(0)
                showEventsInUI()
            }
        }

        binding.btnCustom.isEnabled = false
        binding.btnCustom.setOnClickListener {
            //custom event can be reported using the following method
            WandzClient.reportEvent(binding.eventsCustom.text.toString())
            showEventsInUI()
        }
        binding.eventsCustom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnCustom.isEnabled = s.toString().trim().isNotEmpty()
            }
        })

    }

    private fun showEventsInUI() {
        val events = java.lang.StringBuilder()
        if (WandzClient.getClientEvents() != null) {
            for (event in WandzClient.getClientEvents().keys) {
                events.append("• ").append(event).append("=")
                    .append(WandzClient.getClientEvents()[event]).append("\n")
            }
        }
        binding.reportedEvents.text = events
    }

    private fun addCodeSample() {
        val kotlinCode = "\n" +
            "//Report an event \n" +
                "WandzClient.reportEvent(EventType.CLICK_HELP)\n" +
                "//reporting a custom event can be done using the following method\n" +
                "//alternatively, you can use the reportEvent method to report predefined events\n" +
                "WandzClient.reportEvent(\"Custom Event\")\n" +
                "//get all events\n" +
                "WandzClient.getClientEvents()\n"

        binding.includeCodeView.codeView
            .setTheme(Theme.TOMORROW_NIGHT_BLUE)
            .setCode(kotlinCode)
            .setLanguage(Language.KOTLIN)
            .setShowLineNumber(true)
            .setStartLineNumber(1)
            .apply()
    }
}