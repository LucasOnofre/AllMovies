package onoffrice.wikimovies.fragment.category_movie_list_fragment


import android.util.Log
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response

class CategoryMovieListFragmentModel:CategoryMovieListFragmentContract.Model{

    private val TAG = "CategoryMLFModel"

    /**
     * Make's the request using page and genreId as params
     */
    override fun requestMovies(
            page: Int, genreId: Int,
            onRequestListener: CategoryMovieListFragmentContract.Model.OnRequestResultListener)
    {
        RequestMovies().getGenresMovieList(page,genreId).enqueue(object : retrofit2.Callback<Result>{

            override fun onResponse(call: Call<Result>, response: Response<Result>?) {
                Log.i(TAG,"Request: ${call.request().url()}")
                onRequestListener.onSucess(response?.body()?.movies!!)
            }

            override fun onFailure(call: Call<Result>, error: Throwable) {
                onRequestListener.onFailure(error)
                Log.i(TAG,error.toString())
                Log.i(TAG,"Request: ${call.request().url()}")
            }
        })
    }
}