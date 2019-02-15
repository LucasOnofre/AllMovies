package onoffrice.wikimovies.fragment.upcoming_fragment

import android.util.Log
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response

class UpcomingFragmentModel:UpcomingFragmentContract.Model{

    private val TAG = "UpcomingFragmentModel"

    override fun requestMovies(
            page: Int,
            onFinishedListener: UpcomingFragmentContract.Model.OnRequestResultListener)
    {
            RequestMovies().getUpcoming(page).enqueue(object : retrofit2.Callback<Result>{

                override fun onResponse(call: Call<Result>, response: Response<Result>?) {
                    val movies = response?.body()?.movies
                    onFinishedListener.onSucess(movies!!)
                    Log.i(TAG,"Request: ${call.request().url()}")
                }

                override fun onFailure(call: Call<Result>, error: Throwable) {
                    onFinishedListener.onFailure(error)
                    Log.i(TAG, error.message)
                    Log.i(TAG,"Request: ${call.request().url()}")
                }
            })
        }
    }