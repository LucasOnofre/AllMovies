package onoffrice.wikimovies.fragment.search_fragment

import android.util.Log
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response

class SearchFragmentModel:SearchFragmentContract.Model {

    private val TAG = "SearchFragmentModel"

    override fun requestMovies(
            page: Int,query: String,
            onFinishedListener: SearchFragmentContract.Model.ResponseResult)
    {
        RequestMovies().getSearchedMovie(page,query).enqueue(object : retrofit2.Callback<Result> {

            override fun onResponse(call: Call<Result>, response: Response<Result>?) {
                onFinishedListener.onSucess(response?.body()?.movies!!)
                Log.i(TAG,"Request: ${call.request().url()}")
            }

            override fun onFailure(call: Call<Result>, error: Throwable) {
                onFinishedListener.onFailure(error)
                Log.i(TAG,"Request: ${call.request().url()}")
            }
        })
    }

}