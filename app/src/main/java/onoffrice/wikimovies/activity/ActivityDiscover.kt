package onoffrice.wikimovies.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MovieListAdapter
import onoffrice.wikimovies.model.Genre
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.MovieListGenre
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.ArrayList

class ActivityDiscover : AppCompatActivity() {

    var recyclerList:RecyclerView?              = null
    var listMovies:   ArrayList<Movie>          = ArrayList()
    var listSections: ArrayList<MovieListGenre> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

        recyclerList = findViewById(R.id.lista)

        val linearLayout            = LinearLayoutManager(this@ActivityDiscover)
        linearLayout.orientation    = LinearLayoutManager.VERTICAL
        recyclerList?.layoutManager = linearLayout

        requestMovies()
    }



    /**
     * Return a movie list from the Discover, passing page as a param for the request
     * Also its done a recursive function
     */
    fun requestMovies(count:Int=1){

        var page = count
        RequestMovies(this).getDiscoverMovies(page).enqueue(object : retrofit2.Callback<Result> {

            override fun onResponse(call: Call<Result>, response: Response<Result>?) {
                response?.body()?.movies?.let { movies ->
                    listMovies.addAll(movies)
                }

                if (page == 25) {
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

    private fun filterGenres(genres:List<Genre>){

        for (genre in genres){
            val movies =  listMovies.filter {
                it.genres!!.contains(genre!!.id)
            }

            if (!movies.isEmpty()){
                listSections.add(MovieListGenre(genre, movies))
            }
        }
        recyclerList?.adapter = MovieListAdapter(this@ActivityDiscover, listSections)
    }


    /**
     * Return a movie list from the Discover
     */
    fun requestGenres(){
        RequestMovies(this).getGenres().enqueue(object : retrofit2.Callback<ResultGenre> {
            override fun onResponse(call: Call<ResultGenre>, response: Response<ResultGenre>) {
                var removeCategoryList = response.body()?.genres?.filter { it.name != "TV Movie" }
                removeCategoryList?.let { genres ->
                    filterGenres(genres)
                }?:run {
                    Toast.makeText(this@ActivityDiscover, "Not possible to load list", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultGenre>, t: Throwable) {
                Toast.makeText(this@ActivityDiscover, "Not possible to load list", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

class ResultGenre {

    @SerializedName("genres")
    var genres:List<Genre>? = null
}
