package ai.wandz.example

import ai.wandz.activate.WandzActivate
import ai.wandz.sdk.api.WandzClient

class App : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        //make sue to add the following line to your AndroidManifest.xml
        // <meta-data android:name="ai.wandz.sdk.client_id" android:value="%KEY_VALUE%" />
        WandzClient.start(applicationContext)

        //initialize the WandzActivate SDK
        WandzActivate.start(applicationContext)

    }
}