package onoffrice.wikimovies.fragment.HomeFragment

import android.util.Log
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response

class HomeFragmentModel: HomeFragmentContract.Model {

    private val TAG = "HomeFragmentModel"

    /**
     * Return a movie list from the Discover, passing page as a param for the request
     * Also its done a recursive function
     */
     override fun requestMovies(page: Int, onFinishedListener: HomeFragmentContract.Model.OnFinishedListener) {

                 RequestMovies().getPopularsMovies(page).enqueue(object : retrofit2.Callback<Result>{

                     override fun onResponse(call: Call<Result>, response: Response<Result>?) {
                         val movies = response?.body()?.movies
                         onFinishedListener.onFinished(movies!!)
                     }

                     override fun onFailure(call: Call<Result>, error: Throwable) {
                         Log.i(TAG, error.message)
                         onFinishedListener.onFailure(error)
                     }
                 })
     }
}