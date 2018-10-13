package onoffrice.wikimovies.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import onoffrice.wikimovies.R
import onoffrice.wikimovies.fragment.CategoryFragment
import onoffrice.wikimovies.fragment.FavoriteFragment
import onoffrice.wikimovies.fragment.HomeFragment
import onoffrice.wikimovies.fragment.SearchFragment

class ActivityMain : AppCompatActivity() {

    private var bottomNavigation : BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViews()
        setBottomNavigation()
        homeFragment()

    }
    private fun setUpViews() {
        bottomNavigation  = findViewById(R.id.bottomNavigation)
    }

    //Get's the item clicked in the bottomNavigation - item
    private fun setBottomNavigation() {
        bottomNavigation?.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.action_home      -> homeFragment()

                R.id.action_search    -> searchFragment()

                R.id.action_category  -> categoryFragment()

                R.id.action_favorites -> favoritesFragment()
            }
            true
        }
    }

    private fun homeFragment() {
        //setupToolbar("Populars")
        val homeFragment = HomeFragment()
        openFragment(homeFragment)
    }

    private fun searchFragment() {
        //setupToolbar("Search")
        val searchFragment = SearchFragment()
        openFragment(searchFragment)
    }

    private fun categoryFragment() {
        //setupToolbar("Categorys")
        val categoryFragment = CategoryFragment()
        openFragment(categoryFragment)
    }

    private fun favoritesFragment() {
       // setupToolbar("Favorites")
        val favoritesFragment = FavoriteFragment()
        openFragment(favoritesFragment)
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
