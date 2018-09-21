package onoffrice.allmovies.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import onoffrice.allmovies.R
import onoffrice.allmovies.extension.startActivity


class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handle = Handler()
        handle.postDelayed({
            startActivity<DiscoverActivity>()
        }, 2000)
    }
}