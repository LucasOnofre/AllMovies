package onoffrice.wikimovies.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MovieListAdapter
import onoffrice.wikimovies.model.*
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.ArrayList

class ActivityPopulars : ActivityBase() {

    var         listMovies  : ArrayList<Movie>           = ArrayList()
    private var listSections: ArrayList<MovieListGenre>  = ArrayList()
    private var progressBar : ProgressBar?               = null
    private var recyclerList: RecyclerView?              = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_populars)

        progressBar  = findViewById(R.id.progressBar)
        recyclerList = findViewById(R.id.lista)

        val linearLayout            = LinearLayoutManager(this@ActivityPopulars)
        linearLayout.orientation    = LinearLayoutManager.VERTICAL
        recyclerList?.layoutManager = linearLayout

        progressBar?.visibility = View.VISIBLE

        setupToolbar("Popular")
        requestMovies()
    }

    /**
     * Return a movie list from the Discover, passing page as a param for the request
     * Also its done a recursive function
     */
    fun requestMovies(count:Int=1){

        var page = count
        RequestMovies(this).getPopularsMovies(page).enqueue(object : retrofit2.Callback<Result> {

            override fun onResponse(call: Call<Result>, response: Response<Result>?) {
                response?.body()?.movies?.let { movies ->
                    listMovies.addAll(movies)
                }

                if (page == 10) {
                    requestGenres()
                }
                else{
                    page += 1
                    requestMovies(page)
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                //Resposta caso haja erro
            }
        })
    }

    /**
     * Return a movie list from the Discover
     */
    fun requestGenres(){
        RequestMovies(this).getGenres().enqueue(object : retrofit2.Callback<ResultGenre> {
            override fun onResponse(call: Call<ResultGenre>, response: Response<ResultGenre>) {

                var removedSectionsList = filterSectionsGenres(response)
                removedSectionsList?.let { genres ->

                    filterGenres(genres)
                }?:run {
                    Toast.makeText(this@ActivityPopulars, "Not possible to load list", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultGenre>, t: Throwable) {
                Toast.makeText(this@ActivityPopulars, "Not possible to load list", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Make's the filter to exclude some genres
     */
    private fun filterSectionsGenres(response: Response<ResultGenre>) =
            response.body()?.genres?.filter { it.name != "TV Movie" && it.name != "Music" }

    /**
     * Make's the filter by genre with all the movies and send to adapter
     */
    private fun filterGenres(genres:List<Genre>){

        for (genre in genres){
            val movies =  listMovies.filter {
                it.genres!!.contains(genre!!.id)
            }

            if (!movies.isEmpty()){
                listSections.add(MovieListGenre(genre, movies))
            }
        }
        recyclerList?.adapter = MovieListAdapter(this@ActivityPopulars, listSections)
        progressBar?.visibility = View.GONE
    }



}
