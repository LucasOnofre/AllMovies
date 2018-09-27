package onoffrice.wikimovies.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_discover.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MovieAdapter
import onoffrice.wikimovies.model.Genres
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response

class ActivityDiscover : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

        requestMovies()
        requestGenres()

    }

    /**
     * Return a movie list from the Discover
     */
    fun requestMovies(){

        var call: Call<Result>?

        call = RequestMovies(this).getDiscoverMovies()
        call?.enqueue(object:retrofit2.Callback<Result>{

           override fun onResponse(call: Call<Result>, response: Response<Result>?) {
               setMovieList(response?.body()?.movies)

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

        var call: Call<Genres>?

        call = RequestMovies(this).getGenres()
        call?.enqueue(object : retrofit2.Callback<Genres> {

            override fun onResponse(call: Call<Genres>, response: Response<Genres>) {
                response.body()
                //Resposta com sucesso
            }

            override fun onFailure(call: Call<Genres>, t: Throwable) {
                //Resposta caso haja erro
            }
        })
    }



    private fun setMovieList(listMovies: ArrayList<Movie>?) {
        TODO("NECESSARIO FAZER UM ARRAY DE ARRAYS, POIS É UMA LISTA COM LISTAS DENTRO!!!!!!!!!!")

        var recyclerView = lista_descobrir
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MovieAdapter(this,listMovies)
    }
}
