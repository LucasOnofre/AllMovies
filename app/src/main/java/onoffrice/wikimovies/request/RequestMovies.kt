package onoffrice.wikimovies.request

import android.content.Context
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.model.ResultGenre
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.util.*

class RequestMovies(private val context:Context){

    private val service:Service

    init {
        this.service = RetrofitClient.instance(context).create(Service::class.java)
    }

    private val API_KEY  = context.resources.getString(R.string.api_key)
    private val params = HashMap<String,Any?>()

    /**
     * Return a movie list of Popular movies
     */
    fun getPopularsMovies(page: Int):Call<Result>{
        val endpoint = context.resources.getString(R.string.url_movies_popular)
        params.put("api_key",API_KEY)
        params.put("page",page)

        return service.getPopularsMovies(endpoint,params)
    }

    /**
     * Return the genres list
     */

    fun getGenres():Call<ResultGenre>{
        val endpoint = context.resources.getString(R.string.url_genres)
        params.put("api_key",API_KEY)
        return service.getGenres(endpoint,params)
    }

    /**
     * Return the simmilar movies of a movie
     */

    fun getSimilarMovies(page: Int, movieId:Int?):Call<Result>{
        val endpoint = (context.resources.getString(R.string.url_movie) + movieId + "/similar")

        params.put("api_key",API_KEY)
        params.put("page",page)
        return service.getSimilarMovies(endpoint,params)
    }

    private interface Service {

        @GET
        fun getPopularsMovies(@Url url: String, @QueryMap params: HashMap<String, Any?>):Call<Result>

        @GET
        fun getGenres(@Url url: String, @QueryMap params:HashMap<String, Any?>):Call<ResultGenre>

        @GET
        fun getSimilarMovies(@Url url: String, @QueryMap params:HashMap<String, Any?>):Call<Result>
    }

}