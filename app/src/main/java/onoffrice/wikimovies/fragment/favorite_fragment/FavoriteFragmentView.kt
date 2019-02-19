package onoffrice.wikimovies.fragment.favorite_fragment


import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.extension.getPreferenceKey
import onoffrice.wikimovies.extension.getPreferences
import onoffrice.wikimovies.extension.parseJson
import onoffrice.wikimovies.fragment.base_fragment.BaseFragment
import onoffrice.wikimovies.fragment.movie_detail_fragment.MovieDetailFragmentView
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.MovieInterface
import onoffrice.wikimovies.model.MovieLongClickInterface

class FavoriteFragmentView : BaseFragment() {

    private var layout           : AppBarLayout?              = null
    private var adapter          : MoviesAdapter?             = null
    private var isLoading        : Boolean                    = true
    private var progressBar      : ProgressBar?               = null
    private var recyclerList     : RecyclerView?              = null
    private var emptyMessage     : TextView?                  = null
    private var bottomNavigation : BottomNavigationView?      = null

    //  Initializations
    private var listMovies : ArrayList<Movie> = ArrayList()


    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object : MovieInterface {
        override fun onMovieSelected(movie: Movie?) {
            openPopulatedFragment(movie,"movieJson",MovieDetailFragmentView())
        }
    }

    /**
     * Implementing interface to handle the Long click on the movie
     */
    private val movieLongClicListener = object : MovieLongClickInterface {
        override fun onMovieLongClickSelected(view: View, movie: Movie?) {
            openDropMenu(view, movie)
            var handler = Handler()
            handler.postDelayed(
                    {getFavorites();
                        updateList()
                    },2000)
        }
    }

    private fun updateList() {

        adapter?.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{

        var view = inflater.inflate(R.layout.fragment_favorite, container, false)

        setUpViews(view)
        setupToolbar(view,"Favorites")
        getFavorites()
        checkList()
        setAdapter()

        return view
    }

    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(view: View) {

        layout            = view.findViewById(R.id.appBarLayout)
        progressBar       = view.findViewById(R.id.progressBar)
        recyclerList      = view.findViewById(R.id.listaFavoritos)
        bottomNavigation  = view.findViewById(R.id.bottomNavigation)
        emptyMessage      = view.findViewById(R.id.emptyFavMessage)

        progressBar?.visibility = View.VISIBLE

    }



    private fun setAdapter() {
        //Set's the adapter

        adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener,movieLongClicListener) }

        recyclerList?.adapter = adapter

        setGridLayout(recyclerList)
    }

    /**
     * Get the favorite movies from shared preferences
     */
    private fun getFavorites() {

        val json = context?.getPreferences()?.getPreferenceKey("favoriteMovieList")
        json?.parseJson<Array<Movie>>()?.let {
            listMovies = it.toCollection(ArrayList())

            progressBar?.visibility = View.GONE
            isLoading = false
        }
    }

    private fun checkList(){

         if (!listMovies.isEmpty()) {
            recyclerList?.adapter?.notifyDataSetChanged()


        } else {
            recyclerList?.visibility = View.GONE
            emptyMessage?.visibility = View.VISIBLE

        }
    }
}
