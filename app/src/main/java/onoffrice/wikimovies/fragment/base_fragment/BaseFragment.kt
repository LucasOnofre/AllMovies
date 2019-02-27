package onoffrice.wikimovies.fragment.base_fragment


import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import onoffrice.wikimovies.R
import onoffrice.wikimovies.extension.*
import onoffrice.wikimovies.fragment.home_fragment.HomeFragmentView
import onoffrice.wikimovies.model.Movie

open class BaseFragment : Fragment() {

    protected var rootView:View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container
    }

    /**
     * Set's the default toolbar her title
     */
    protected fun setupToolbar(view: View, title: String? = ""){

        var toolbar:Toolbar?       = view.findViewById(R.id.toolbar)
        var titleSection:TextView? = view.findViewById(R.id.title_section)

        if (title == " "){

            titleSection?.let { titleSection.text = title }
            return
        }

        toolbar.let {
            (activity as AppCompatActivity).setSupportActionBar(it)
            titleSection?.let { titleSection.text = title }
        }
    }

    /**
     * Set's the toolbar of the fragments
     */
    protected fun setToolbarGoBackArrow(view: View, title:String) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        var collapsingToolbar:CollapsingToolbarLayout? = view.findViewById(R.id.collapsing_toolbar)

        toolbar.let {
            (activity as AppCompatActivity).setSupportActionBar(it)

            collapsingToolbar?.title =  title
            it.title = title
            it.setNavigationIcon(R.drawable.ic_arrow_back)
            it.setNavigationOnClickListener { openFragment(HomeFragmentView()) }
        }
    }


    protected fun openFragment(fragment:Fragment){
        fragmentManager?.beginTransaction()?.replace(R.id.container, fragment)?.addToBackStack(null)?.commit()
    }

    protected fun exitFragment(fragment:Fragment){
        activity?.supportFragmentManager?.beginTransaction()?.remove(fragment)?.commit()
    }

    /**
     * Set's the layout manager of the recycler view to Grid  and set`s the orientation
     */
    protected fun setGridLayout(recyclerList:RecyclerView?) {

        var manager: GridLayoutManager?
        val orientation = resources.configuration.orientation

        manager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(activity, 4, GridLayoutManager.VERTICAL, false)
        }
        else
            GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)

        recyclerList?.layoutManager = manager
    }

    /**
     * Open's an fragment received by params, and saves as a String Json the object
     * that is also a param
     */
    inline fun<reified T> openPopulatedFragment(obj:T ,keyEditor:String,fragment:Fragment){


        var gson = Gson()
        var editor = context?.getPreferencesEditor()

        // Transform the object in a Json to save in shared preferences as a String
        var json = gson.toJson(obj)

        editor?.putString(keyEditor,json)
        editor?.commit()

        //Open's the given fragment
        fragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.enter_anim,R.anim.exit_anim)
                ?.replace(R.id.container, fragment)
                ?.addToBackStack(null)?.commit()
    }

    fun openDropMenu(view:View, movie: Movie?){

        val dropDownMenu = PopupMenu(context!!,view)
        var favoriteList:ArrayList<Movie>

        context?.getPreferences()?.getFavorites().let {
            favoriteList = it!!
        }

        dropDownMenu.menuInflater.inflate(R.menu.favorite_fragment_menu, dropDownMenu.menu)

        favoriteList.checkFavorite(movie)

        setMenuItemForLongClick(movie!!, dropDownMenu)

        dropDownMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.unFavorite -> {
                    Toast.makeText(context, "Unfavorited", Toast.LENGTH_SHORT).show()
                    movie.unFavoriteMovie(favoriteList)
                    favoriteList.saveFavoriteMovies(context!!)
                    true
                }

                R.id.favorite -> {
                    movie.favoriteMovie(favoriteList)
                    Toast.makeText(context, "Favorited", Toast.LENGTH_SHORT).show()
                    favoriteList.saveFavoriteMovies(context!!)

                    true
                }
                else -> {
                    false
                }
            }
        }
        dropDownMenu.show()
    }

    private fun setMenuItemForLongClick(movie: Movie, dropDownMenu: PopupMenu) {

        var menuItem: MenuItem? = if (movie.isFavorite) {
            dropDownMenu.menu.findItem(R.id.favorite)

        } else {
            dropDownMenu.menu.findItem(R.id.unFavorite)
        }
        menuItem?.isVisible = false
        menuItem?.isEnabled = false
    }
}
