package onoffrice.wikimovies.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Movie

class MovieDetailFragment : Fragment() {

    private var movieBanner      : ImageView?            = null
    private var movieTittle      : TextView?             = null
    private var movieDescription : TextView?             = null
    private var gson             : Gson?                 = null
    private var movie            :Movie?                 = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        getDeviceData()
        setUpViews(container)
        setInfo(movie)


        return view
    }


    private fun  getDeviceData(){
        val preferences= context?.getSharedPreferences("WikiMoviesPref", 0)
        val movieJson            = preferences?.getString("movieJson",null)

        movie = gson?.fromJson(movieJson, Movie::class.java)
    }

    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(container: View?) {
        movieBanner      = container?.findViewById(R.id.movieBanner)
        movieTittle      = container?.findViewById(R.id.movie_name)
        movieDescription = container?.findViewById(R.id.movie_description)

    }

    private fun setInfo(movie: Movie?) {

        TODO("Setar informações!!")
    }



}
