package onoffrice.wikimovies.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Movie

class MovieDetailFragment : BaseFragment() {

    private var movieBanner         : ImageView?            = null
    private var movieName           : TextView?             = null
    private var movieDescript       : TextView?             = null
    private var gson                : Gson?                 = Gson()
    private var movie               : Movie?                = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        getDeviceData()
        setUpViews(view)
        configureToolbar(view)

        return view
    }

    /**
     * Get the Selected movie saved on preferences that is a json and transform it in to A movie again
     */
    private fun  getDeviceData(){

        val preferences = context?.getSharedPreferences("WikiMoviesPref",Context.MODE_PRIVATE)
        val movieJson            = preferences?.getString("movieJson","")

        //Transform the json into a movie
        movie = gson?.fromJson(movieJson, Movie::class.java)

    }

    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(view: View) {
        movieBanner         = view.findViewById(R.id.movieBanner)
        movieName           = view.findViewById(R.id.movieTittle)
        movieDescript       = view.findViewById(R.id.movieDescript)

        setInfo(movie, view)

    }

    private fun setInfo(movie: Movie?, container: View) {

        val urlImageBanner      = this.resources.getString(R.string.base_url_images) + movie?.backdropPath
        Picasso.get().load(urlImageBanner).into(movieBanner)

        movieName?.text = movie?.title

        movieDescript?.text = movie?.overview

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

}
