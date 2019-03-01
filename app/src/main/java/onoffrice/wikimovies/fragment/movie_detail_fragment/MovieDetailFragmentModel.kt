package onoffrice.wikimovies.fragment.movie_detail_fragment

import android.util.Log
import onoffrice.wikimovies.model.MovieVideoInfoList
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailFragmentModel:MovieDetailFragmentContract.Model{

    private val TAG = "MovieDetailFModel"

    /**
     * Make's the request passing page and movieId as params
     */
    override fun requestSimilarMovies(
            page         : Int,
            movieId      : Int?,
            requestResult: MovieDetailFragmentContract.Model.RequestResult)
    {
        RequestMovies().getSimilarMovies(page, movieId).enqueue(object : retrofit2.Callback<Result> {

            override fun onResponse(call: Call<Result>, response: Response<Result>?) {
                response?.body()?.movies?.let {
                    movies -> requestResult.onSucess(movies)
                    Log.i(TAG,"Request: ${call.request().url()}")
                }
            }

            override fun onFailure(call: Call<Result>, error: Throwable) {
                requestResult.onError(error)
                Log.i(TAG,"Request: ${call.request().url()}")
            }
        })
    }

    /**
     * Make's the request passing the movieId
     */
    override fun requestVideosFromMovie(
            movieId: Int,
            resultTrailers: MovieDetailFragmentContract.Model.ResultTrailers)
    {
        RequestMovies().getVideosFromMovie(movieId).enqueue(object: Callback<MovieVideoInfoList>{

            override fun onResponse(call: Call<MovieVideoInfoList>, response: Response<MovieVideoInfoList>) {
                response.body()?.movieVideoList.let {
                    videos -> resultTrailers.onSucessTrailers(videos!!)
                    Log.i(TAG,"Request: ${call.request().url()}")
                }
            }

            override fun onFailure(call: Call<MovieVideoInfoList>, error: Throwable) {
                resultTrailers.onErrorTrailers(error)
                Log.i(TAG,"Request: ${call.request().url()}")
            }
        })
    }
}