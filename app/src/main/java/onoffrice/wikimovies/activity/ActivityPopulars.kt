package onoffrice.wikimovies.activity

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.model.*
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.ArrayList

class ActivityPopulars : ActivityBase() {

    var         listMovies  : ArrayList<Movie>           = ArrayList()
    private var progressBar : ProgressBar?               = null
    private var recyclerList: RecyclerView?              = null

    var manager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_populars)

        setOrientationLayoutManager()
        setUpViews()
        requestMovies()
        setupToolbar("Popular")

        recyclerList?.layoutManager = manager
        progressBar?.visibility     = View.VISIBLE

    }

    private fun setUpViews() {
        progressBar  = findViewById(R.id.progressBar)
        recyclerList = findViewById(R.id.lista)
    }

    private fun setOrientationLayoutManager() {
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    /**
     * Return a movie list from the Discover, passing page as a param for the request
     * Also its done a recursive function
     */
    fun requestMovies(){

        RequestMovies(this).getPopularsMovies().enqueue(object : retrofit2.Callback<Result> {

            override fun onResponse(call: Call<Result>, response: Response<Result>?) {
                progressBar?.visibility = View.GONE
                response?.body()?.movies?.let { movies ->
                    listMovies.addAll(movies)
                    recyclerList?.adapter = MoviesAdapter(this@ActivityPopulars,listMovies)
                }

            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.i("Resposta: ", t.message)
            }
        })
    }
//
//    /**
//     * Return a movie list from the Discover
//     */
//    fun requestGenres(){
//        RequestMovies(this).getGenres().enqueue(object : retrofit2.Callback<ResultGenre> {
//            override fun onResponse(call: Call<ResultGenre>, response: Response<ResultGenre>) {
//
//                var removedSectionsList = filterSectionsGenres(response)
//                removedSectionsList?.let { genres ->
//
//                    filterGenres(genres)
//                }?:run {
//                    Toast.makeText(this@ActivityPopulars, "Not possible to load list", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResultGenre>, t: Throwable) {
//                Toast.makeText(this@ActivityPopulars, "Not possible to load list", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    /**
//     * Make's the filter to exclude some genres
//     */
//    private fun filterSectionsGenres(response: Response<ResultGenre>) =
//            response.body()?.genres?.filter { it.name != "TV Movie" && it.name != "Music" }
//
//    /**
//     * Make's the filter by genre with all the movies and send to adapter
//     */
//    private fun filterGenres(genres:List<Genre>){
//
//        for (genre in genres){
//            val movies =  listMovies.filter {
//                it.genres!!.contains(genre!!.id)
//            }
//
//            if (!movies.isEmpty()){
//                listSections.add(MovieListGenre(genre, movies))
//            }
//        }
//        recyclerList?.adapter = MovieListAdapter(this@ActivityPopulars, listSections)
//        progressBar?.visibility = View.GONE
//    }
}
