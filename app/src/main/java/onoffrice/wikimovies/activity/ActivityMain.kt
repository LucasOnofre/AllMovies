package onoffrice.wikimovies.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import onoffrice.wikimovies.R
import onoffrice.wikimovies.fragment.category_fragment.CategoryFragmentView
import onoffrice.wikimovies.fragment.favorite_fragment.FavoriteFragmentView
import onoffrice.wikimovies.fragment.home_fragment.HomeFragmentView
import onoffrice.wikimovies.fragment.search_fragment.SearchFragmentView
import onoffrice.wikimovies.fragment.upcoming_fragment.UpcomingFragmentView

class ActivityMain : AppCompatActivity() {

    private var bottomNavigation : BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViews()
        setBottomNavigation()
        openFragment(HomeFragmentView())
    }

    private fun setUpViews() {
        bottomNavigation  = findViewById(R.id.bottomNavigation)

    }

    /**
     * Get's the item clicked in the bottomNavigation - item
     **/
    private fun setBottomNavigation() {
        bottomNavigation?.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.action_home      -> openFragment(HomeFragmentView())

                R.id.action_search    -> openFragment(SearchFragmentView())

                R.id.action_upcoming  -> openFragment(UpcomingFragmentView())

                R.id.action_category  -> openFragment(CategoryFragmentView())

                R.id.action_favorites -> openFragment(FavoriteFragmentView())
            }
            true
        }
    }

    /**
     * Open the selected fragment and replace the container for him
     **/
    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
