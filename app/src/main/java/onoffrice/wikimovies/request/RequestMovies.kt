package onoffrice.wikimovies.request

import android.util.Log
import onoffrice.wikimovies.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.util.*

class RequestMovies{

    private val service:Service
    private val API_KEY  = "037b83eda5aad6b5c90898ea8ea94da3"
    private val params   = HashMap<String,Any?>()

    init {
        this.service = RetrofitClient.instance().create(Service::class.java)
    }

    /**
     * Return a movie list of Popular movies
     */
    fun getPopularsMovies(page: Int):Call<Result>{
        val endpoint = "movie/popular"
        params.put("api_key",API_KEY)
        params.put("page",page)
        Log.i("Request",endpoint + params)

        return service.getPopularsMovies(endpoint,params)
    }

    /**
     * Return the genres list
     */

    fun getGenresMovieList(page: Int, genreId:Int?):Call<Result>{
        val endpoint = "discover/movie"
        params.put("api_key",API_KEY)
        params.put("page",page)
        params.put("with_genres",genreId)
        Log.i("Request",endpoint + params)
        return service.getGenresMovieList(endpoint,params)
    }

    /**
     * Return the simmilar movies of a movie
     */

    fun getSimilarMovies(page: Int, movieId:Int?):Call<Result>{
        val endpoint = ("movie/$movieId/similar")

        params.put("api_key",API_KEY)
        params.put("page",page)
        return service.getSimilarMovies(endpoint,params)
    }

    /**
     * Return the result of the query
     */

    fun getSearchedMovie(page: Int, query:String?):Call<Result>{
        val endpoint = "search/movie"

        params.put("api_key",API_KEY)
        params.put("page",page)
        params.put("query",query)
        return service.getSimilarMovies(endpoint,params)
    }

    private interface Service {

        @GET
        fun getPopularsMovies(@Url url: String, @QueryMap params: HashMap<String, Any?>):Call<Result>

        @GET
        fun getGenresMovieList(@Url url: String, @QueryMap params: HashMap<String, Any?>):Call<Result>

        @GET
        fun getSimilarMovies(@Url url: String, @QueryMap params:HashMap<String, Any?>):Call<Result>

        fun getSearchedMovie(@Url url: String, @QueryMap params:HashMap<String, Any?>):Call<Result>
    }

}