package onoffrice.wikimovies.activity

import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.extension.startActivity

class ActivitySplash : ActivityBase(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setLogoAnimation()
        setDelayForActivity()
    }

    private fun setDelayForActivity() {
        val handle = Handler()
        handle.postDelayed({ startActivity<ActivityMain>()
            finish() }, 4000)
    }

    private fun setLogoAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_transition)
        animation.repeatCount    = 1
        animation.duration       = 2000
        animation.fillAfter      = true
        animation.repeatMode     = Animation.REVERSE
        logo_wiki_movie.startAnimation(animation)
    }
}