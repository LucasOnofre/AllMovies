package onoffrice.wikimovies.request

import android.content.Context
import onoffrice.wikimovies.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient{

    /**
     * Singleton for instanciate the Retrofit in all the application
     */
    fun instance(context: Context): Retrofit {

        val baseURL = context.resources.getString(R.string.base_url_movie_api)
        val retrofit     = Retrofit.Builder()
                                    .baseUrl(baseURL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
        return retrofit

    }
}
