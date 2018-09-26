package onoffrice.wikimovies.request

import android.content.Context
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.util.HashMap

class RequestMovies(private val context:Context){

    private val service:Service

    init {
        this.service = RetrofitClient.instance(context).create(Service::class.java)
    }

    val API_KEY  = context.resources.getString(R.string.api_key)

    /**
     * Return a movie list from the Discover
     */
    fun getDiscoverMovies():Call<Result>{
        val endpoint = context.resources.getString(R.string.url_movies_discover)


        val params = HashMap<String,Any?>()
        params.put("api_key",API_KEY)

        return service.getDiscoverMovies(endpoint,params)

    }

    private interface Service {

        @GET
        fun getDiscoverMovies(@Url url: String, @QueryMap params:HashMap<String, Any?>):Call<Result>
    }

}