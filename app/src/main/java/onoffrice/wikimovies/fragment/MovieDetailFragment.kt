package onoffrice.wikimovies.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MovieInterface
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.extension.*
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response
import java.util.*

class MovieDetailFragment : BaseFragment() {

    private var page                                            = 1
    private var isLoading                                       = true
    private var movieBanner         : ImageView?                = null
    private var movieName           : TextView?                 = null
    private var movieDescript       : TextView?                 = null
    private var movieReleaseDate    : TextView?                 = null
    private var movie               : Movie?                    = null
    private var gson                : Gson?                     = Gson()
    private var listMovies          : ArrayList<Movie>          = ArrayList()
    private var recyclerList        : RecyclerView?             = null
    private var editor              :SharedPreferences.Editor?  = null

    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object: MovieInterface { override fun onMovieSelected(movie: Movie?) { openDetailMovieFragment(movie) } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        getDeviceData()
        setUpViews(view)
        configureToolbar(view)
        setAdapter()
        setInfiniteScroll()
        requestSimilarMovies(movieId = movie?.id)

        return view
    }

    /**
     * Get the Selected movie saved on preferences that is a json and transform it in to A movie again
     */
    private fun  getDeviceData(){
        val preferences = context?.getPreferences()
        val movieJson             = preferences?.getPreferenceKey("movieJson")

        //Transform the json into a objct '(movie)'
        movie = gson?.fromJson(movieJson, Movie::class.java)

    }

    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(view: View) {

        movieBanner         = view.findViewById(R.id.movieBanner)
        movieName           = view.findViewById(R.id.movieTittle)
        movieDescript       = view.findViewById(R.id.movieDescript)
        movieReleaseDate    = view.findViewById(R.id.movie_release_date)
        recyclerList        = view.findViewById(R.id.lista)

        setInfo(movie, view)

    }

    private fun configureToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(view)
        toolbar.let {
            (activity as AppCompatActivity).setSupportActionBar(it)
            it.setNavigationIcon(R.drawable.ic_arrow_back)
            it.setNavigationOnClickListener { fragmentManager?.popBackStackImmediate() }
        }
    }

    /**
     * Convert's the movie in a Json,
     * save on shared preferences and also open de Movie Detail Fragment
     */
    private fun openDetailMovieFragment(movie:Movie?){

        editor = context?.getPreferencesEditor()

        // Transform the movie into an Json to save in shared preferences
        var movieJson = gson?.toJson(movie)

        editor?.putString("movieJson",movieJson)
        editor?.commit()

        //Open's the given fragment
        openFragment(MovieDetailFragment())
    }

    private fun setInfo(movie: Movie?, view: View) {

        val date         = movie?.releaseDate?.convertDate()
        val urlImageBanner= this.resources.getString(R.string.base_url_images) + movie?.backdropPath

        //Load's the image using Picasso passing the local as parameter
        urlImageBanner.loadPicasso(movieBanner)

        movieName?.text         = movie?.title
        movieReleaseDate?.text  = date
        movieDescript?.text     = movie?.overview

    }

    private fun setAdapter() {

        recyclerList?.adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener) }

        //Set's the orientation of the list to Grid
        setGridLayout(recyclerList)
    }

    /**
     * Return a movie list from the Discover, passing page as a param for the request
     * Also its done a recursive function
     */
    private fun requestSimilarMovies(page:Int = 1,movieId:Int?){

        activity?.let {
            RequestMovies(it).getSimilarMovies(page, movieId).enqueue(object : retrofit2.Callback<Result> {

                override fun onResponse(call: Call<Result>, response: Response<Result>?) {

                    response?.body()?.movies?.let { movies ->
                        listMovies.addAll(movies)
                        recyclerList?.adapter?.notifyDataSetChanged()
                    }
                }
                override fun onFailure(call: Call<Result>, error: Throwable) {
                    Log.i("Error: ", error.message)
                }
            })
        }
    }

    /**
     * Make's new requests when user scrolls the list to the last item
     */
    private fun setInfiniteScroll() {
        recyclerList?.addOnScrollListener(object:RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //direction = 1 = list ends
                if (!recyclerView.canScrollVertically(1 ) && !isLoading){
                    isLoading = true
                    page++
                    requestSimilarMovies(page,movie?.id!!)
                }
            }
        })
    }
}
