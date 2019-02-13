package onoffrice.wikimovies.fragment.favorite_fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.MovieInterface
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.extension.getPreferenceKey
import onoffrice.wikimovies.extension.parseJson
import onoffrice.wikimovies.fragment.base_fragment.BaseFragment
import onoffrice.wikimovies.fragment.movie_detail_fragment.MovieDetailFragmentView
import onoffrice.wikimovies.model.Movie

class FavoriteFragmentView : BaseFragment() {

    private var layout           : AppBarLayout?              = null
    private var isLoading                                     = true
    private var preferences      : SharedPreferences?         = null
    private var progressBar      : ProgressBar?               = null
    private var recyclerList     : RecyclerView?              = null
    private var emptyMessage     : TextView?                  = null
    private var bottomNavigation : BottomNavigationView?      = null
    private var editor           : SharedPreferences.Editor?  = null

    private var gson       : Gson?            = Gson()
    private var listMovies : ArrayList<Movie> = ArrayList()


    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object : MovieInterface {
        override fun onMovieSelected(movie: Movie?) {
            openDetailMovieFragment(movie)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{

        var view = inflater.inflate(R.layout.fragment_favorite, container, false)

        setUpViews(view)
        getPreferecences()
        setupToolbar(view,"Favorites")
        getFavorites()
        setAdapter()

        return view
    }


    private fun getPreferecences() {

        preferences = context?.getSharedPreferences("WikiMoviesPref", Context.MODE_PRIVATE)
        editor      = preferences?.edit()
    }

    /**
     * Convert's the movie in a Json,
     * save on shared preferences and also open de Movie Detail Fragment
     */
    private fun openDetailMovieFragment(movie: Movie?) {


        // Transform the movie into an Json to save in shared preferences
        var movieJson = gson?.toJson(movie)

        editor?.putString("movieJson", movieJson)
        editor?.commit()

        openFragment(MovieDetailFragmentView())
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
        recyclerList?.adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener) }
        setGridLayout(recyclerList)
    }

    /**
     * Get the favorite movies from shared preferences
     */
    private fun getFavorites() {

        val json = preferences?.getPreferenceKey("favoriteMovieList")
        json?.parseJson<Array<Movie>>()?.let {
            listMovies = it.toCollection(ArrayList())
            progressBar?.visibility = View.GONE
            isLoading = false

            if (!listMovies.isEmpty()) {
                recyclerList?.adapter?.notifyDataSetChanged()


            }else{
                recyclerList?.visibility  = View.GONE
                emptyMessage?.visibility  = View.VISIBLE

            }
        }
    }
}
