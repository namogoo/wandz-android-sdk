package ai.wandz.example

import ai.wandz.activate.WandzActivate
import ai.wandz.example.databinding.ActivitySplashBinding
import ai.wandz.sdk.api.WandzClient
import ai.wandz.sdk.api.models.enums.AppSection
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        fadeInAnimation()


        // Do not allow interactions in the splash screen
        // This is to prevent interactions from being shown before the main screen
        WandzActivate.allowInteractions(false)

        //report screen entered event to Wandz SDK
        //this event is used to track user navigation in the app
        WandzClient.reportScreenEnteredEvent(AppSection.SPLASH)

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

        //make sure to report the referrer if available
        reportReferrerIfAvailable()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))

            finish()
        }, 3000) // 3 seconds delay
    }

    private fun fadeInAnimation() {
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.splashScreenView.startAnimation(fadeInAnimation)
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30+ (Android 11+)
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Fallback for older versions
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }

    private fun reportReferrerIfAvailable() {
        val referrer = intent.getStringExtra("referrer")
        if (referrer != null) {
            val referrerUri = intent.data?.toString()
            WandzClient.reportReferrer(referrer, referrerUri)
        }
    }
}