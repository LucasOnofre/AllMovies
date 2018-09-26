package onoffrice.wikimovies.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response

class ActivityDiscover : AppCompatActivity() {

    private var call: retrofit2.Call<Result>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

        requestMovies()

    }

    /**
     * Return a movie list from the Discover
     */
    fun requestMovies(){

        call = RequestMovies(this).getDiscoverMovies()
        call?.enqueue(object:retrofit2.Callback<Result>{

           override fun onResponse(call: Call<Result>, response: Response<Result>) {
              //Resposta com sucesso
           }

           override fun onFailure(call: Call<Result>, t: Throwable) {
              //Resposta caso haja erro
           }
       })
    }
}
