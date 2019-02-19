
package onoffrice.wikimovies.fragment.movie_detail_fragment

import android.content.SharedPreferences
import onoffrice.wikimovies.extension.favoriteMovie
import onoffrice.wikimovies.extension.getFavorites
import onoffrice.wikimovies.extension.unFavoriteMovie
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.MovieVideoInfo

class MovieDetailFragmentPresenter:
        MovieDetailFragmentContract.Presenter,
        MovieDetailFragmentContract.Model.RequestResult,
        MovieDetailFragmentContract.Model.ResultTrailers
{

    private var view : MovieDetailFragmentView? = null
    private val model: MovieDetailFragmentModel = MovieDetailFragmentModel()


    /**
     * Make's the connection with the view
     */
    override fun bindTo(view: MovieDetailFragmentView) {
        this.view = view
    }

    /**
     * Destroy's the connection with the view
     */
    override fun destroy() {
        view = null
    }

    /**
     * Make's a request to the similar movies method on Model
     */
    override fun requestSimilarMovies(movieId:Int) {

        model.requestSimilarMovies(1,movieId,this)
    }

    /**
     * Make's a request to the similar movies method on Model passing page as param
     */
    override fun requestMoreSimilarMovies(page: Int,movieId:Int) {

        model.requestSimilarMovies(page,movieId,this)
    }

    /**
     * Update's the favotite list in the view
     */
    override fun onSucess(movies: ArrayList<Movie>) {

        view?.updateFavoriteList(movies)

    }

    /**
     * Send's the error to the view
     */
    override fun onError(error: Throwable) {
        view?.onResponseError(error)
    }


    override fun getFavorites(pref: SharedPreferences?): ArrayList<Movie>? {

        return pref?.getFavorites()

    }


    /**
     * Check if the movie is favorited
     */
    override fun isFavorite(favoriteMovieList: ArrayList<Movie>, movie: Movie):Boolean {

        for (movieinList in favoriteMovieList) {
            if (movieinList.id == movie.id) {
                return true
            }
        }
        return false
    }

    /**
     * Favorite the movie
     */
    override fun favoriteMovie(moviesFavoriteList: ArrayList<Movie>, movie: Movie): ArrayList<Movie>  {

        return movie.favoriteMovie(moviesFavoriteList)
    }

    /**
     * Unfavorite the movie
     */
    override fun unFavoriteMovie(moviesFavoriteList: ArrayList<Movie>, movie: Movie): ArrayList<Movie> {

       return movie.unFavoriteMovie(moviesFavoriteList)
    }

    /**
     * Request the videos from the model method
     */
    override fun requestVideosFromMovie(movieId: Int) {

        model.requestVideosFromMovie(movieId,this)
    }

    /**
     * Check's if it's a trailer
     */
    override fun onSucessTrailers(videoInfo: ArrayList<MovieVideoInfo>) {

        checkIsTrailer(videoInfo)
    }

    /**
     * Check if is a trailer in the list, amd send's an Video item
     */
    private fun checkIsTrailer(videoInfo: ArrayList<MovieVideoInfo>){

        var isTrailer = false

        for (video in videoInfo) {
            if (video.type == "Trailer") {
                view?.updateMovieVideoPath(video)
                isTrailer = true
                break
            }
        }

        if (!isTrailer)
            view?.updateMovieVideoPath(videoInfo[0])
    }

    /**
     * Send's and error to the view
     */
    override fun onErrorTrailers(error: Throwable) {

        view?.onResponseErrorTrailer(error)

    }
}