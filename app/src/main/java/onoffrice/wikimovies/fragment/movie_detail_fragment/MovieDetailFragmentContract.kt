package onoffrice.wikimovies.fragment.movie_detail_fragment

import android.content.SharedPreferences
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.MovieVideoInfo

interface MovieDetailFragmentContract {

    interface Model {

        interface RequestResult {

            fun onSucess(movies: ArrayList<Movie>)
            fun onError(error: Throwable)
        }

        interface ResultTrailers{

            fun onSucessTrailers(videoInfo: ArrayList<MovieVideoInfo>)
            fun onErrorTrailers(error: Throwable)
        }

        fun requestSimilarMovies(page:Int = 1,movieId:Int?,requestResult: RequestResult)
        fun requestVideosFromMovie(movieId: Int,resultTrailers: ResultTrailers)
    }

        interface View{

            fun updateFavoriteList(movies: ArrayList<Movie>)
            fun onResponseError(error: Throwable)
            fun updateMovieVideoPath(videoInfo:MovieVideoInfo)
            fun onResponseErrorTrailer(error: Throwable)

        }

        interface Presenter{

            fun bindTo(view: MovieDetailFragmentView)
            fun destroy()
            fun requestSimilarMovies(movieId:Int)
            fun requestVideosFromMovie(movieId: Int)
            fun requestMoreSimilarMovies(page: Int,movieId:Int)
            fun isFavorite(moviesFavoriteList: ArrayList<Movie>, movie: Movie): Boolean
            fun favoriteMovie(moviesFavoriteList: ArrayList<Movie>, movie: Movie): ArrayList<Movie>
            fun unFavoriteMovie(moviesFavoriteList: ArrayList<Movie>, movie: Movie): ArrayList<Movie>
            fun getFavorites(editor: SharedPreferences?): ArrayList<Movie>?

        }
}