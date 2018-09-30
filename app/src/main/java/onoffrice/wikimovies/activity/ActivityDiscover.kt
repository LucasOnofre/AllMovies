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

   var  listMovies: ArrayList<Movie> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

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
                response?.body()?.movies?.let {
                    listMovies.addAll(it)
                }

                if (page == 3)
                    setMovieList(listMovies)
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

        RequestMovies(this).getGenres().enqueue(object : retrofit2.Callback<Genres> {
            override fun onResponse(call: Call<Genres>, response: Response<Genres>) {
                response.body()
                //Resposta com sucesso
            }

            override fun onFailure(call: Call<Genres>, t: Throwable) {
                //Resposta caso haja erro
            }
        })
    }

    /**
     * Send the list of movies to adapter
     */

    private fun setMovieList(listMovies: ArrayList<Movie>?) {

        TODO("Melhorar lógica do Layout Manager para as listas")

        val layoutManager2  = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val layoutManager3  = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val layoutManager4  = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val layoutManager5  = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val layoutManager6  = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val layoutManager7  = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val layoutManager8  = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val layoutManager9  = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val layoutManager10 = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

//        val arrayOfRecyclerView = arrayOf(
//                lista_favoritos,
//                lista_descobrir,
//                lista_acao,
//                lista_animacao,
//                lista_comedia,
//                lista_fantasia,
//                lista_ficcao,
//                lista_romance,
//                lista_terror)
//
//        for (item in arrayOfRecyclerView){
//            item.layoutManager = layoutManager
//            item.adapter
//        }



        lista_favoritos .layoutManager   = layoutManager2
        lista_favoritos .adapter         = MovieAdapter(this,listMovies)
        lista_descobrir.layoutManager    = layoutManager3
        lista_descobrir.adapter          = MovieAdapter(this,listMovies)
        lista_acao.layoutManager         = layoutManager4
        lista_acao.adapter               = MovieAdapter(this,listMovies)
        lista_animacao.layoutManager     = layoutManager5
        lista_animacao.adapter           = MovieAdapter(this,listMovies)
        lista_comedia.layoutManager      = layoutManager6
        lista_comedia.adapter            = MovieAdapter(this,listMovies)
        lista_fantasia.layoutManager     = layoutManager7
        lista_fantasia.adapter           = MovieAdapter(this,listMovies)
        lista_ficcao.layoutManager       = layoutManager8
        lista_ficcao.adapter             = MovieAdapter(this,listMovies)
        lista_romance.layoutManager      = layoutManager9
        lista_romance.adapter            = MovieAdapter(this,listMovies)
        lista_terror.layoutManager       = layoutManager10
        lista_terror.adapter             = MovieAdapter(this,listMovies)
}
}
