package ai.wandz.example.ui

import ai.wandz.example.R
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

class MainCardView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val titleText: TextView
    private val descriptionText: TextView
    private val cardImage: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.main_card_view, this, true)
        titleText = findViewById(R.id.titleText)
        descriptionText = findViewById(R.id.descriptionText)
        cardImage = findViewById(R.id.cardImage)
        val cardView = findViewById<MaterialCardView>(R.id.card_container)
        // Fully remove outline and shadows
        cardView.outlineProvider = null
        cardView.setBackgroundColor(Color.TRANSPARENT)
        cardView.cardElevation = 0f
        cardView.setUseCompatPadding(false)
        cardView.preventCornerOverlap = false

        // Apply the same fixes to MainCardView itself
        outlineProvider = null
        setBackgroundColor(Color.TRANSPARENT)
        cardElevation = 0f
        setUseCompatPadding(false)
        preventCornerOverlap = false

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MainCardView)

            val title = typedArray.getString(R.styleable.MainCardView_cardTitle)
            val description = typedArray.getString(R.styleable.MainCardView_cardDescription)
            val imageRes = typedArray.getResourceId(R.styleable.MainCardView_cardImage, -1)

            setCardData(title, description, imageRes)
            typedArray.recycle()
        }
    }

    fun setCardData(title: String?, description: String?, imageRes: Int) {
        titleText.text = title ?: "Default Title"
        descriptionText.text = description ?: "Default Description"
        if (imageRes != -1) {
            cardImage.setImageResource(imageRes)
        }
    }
}