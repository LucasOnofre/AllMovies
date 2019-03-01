package onoffrice.wikimovies.request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{

    /**
     * Singleton to instanciate the Retrofit in all the application
     */
    fun instance(): Retrofit {

        val baseURL     = "https://api.themoviedb.org/3/"
        val retrofit     = Retrofit.Builder()
                                    .baseUrl(baseURL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
        return retrofit

    }
}
