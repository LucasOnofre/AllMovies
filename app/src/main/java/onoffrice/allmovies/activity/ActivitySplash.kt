package onoffrice.allmovies.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import onoffrice.allmovies.R
import onoffrice.allmovies.extension.startActivity


class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setLogoAnimation()
        setDelayForActivity()
    }

    /**
     * Sets the handle that makes the transition delay
     **/
    private fun setDelayForActivity() {
        val handle = Handler()
        handle.postDelayed({ startActivity<ActivityDiscover>()
            finish() }, 3000)
    }

    /**
     * Set's the logo animation
     **/
    private fun setLogoAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_transition)
        animation.duration = 3000
        animation.fillAfter = true
        animation.repeatMode = Animation.REVERSE
        animation.repeatCount = 1
        logo_all_movies.startAnimation(animation)
    }
}