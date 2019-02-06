package onoffrice.wikimovies.fragment.movie_detail_fragment

import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response


class MovieDetailFragmentModel:MovieDetailFragmentContract.Model{

    override fun requestSimilarMovies(
            page         : Int,
            movieId      : Int?,
            requestResult: MovieDetailFragmentContract.Model.RequestResult)
    {
        RequestMovies().getSimilarMovies(page, movieId).enqueue(object : retrofit2.Callback<Result> {

            override fun onResponse(call: Call<Result>, response: Response<Result>?) {

                response?.body()?.movies?.let {

                    movies -> requestResult.onSucess(movies)
                }
            }
            override fun onFailure(call: Call<Result>, error: Throwable) {
                requestResult.onError(error)
            }
        })
    }
}