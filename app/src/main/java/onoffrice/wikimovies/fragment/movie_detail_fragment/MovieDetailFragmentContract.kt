package onoffrice.wikimovies.fragment.movie_detail_fragment

import onoffrice.wikimovies.model.Movie

interface MovieDetailFragmentContract {

    interface Model {

        interface RequestResult {

            fun onSucess(movies: ArrayList<Movie>)
            fun onError(error: Throwable)
        }

        fun requestSimilarMovies(page:Int = 1,movieId:Int?,requestResult: RequestResult)
    }

        interface View{

            fun updateFavoriteList(movies: ArrayList<Movie>)
            fun onResponseError(error: Throwable)

        }

        interface Presenter{

            fun bindTo(view: MovieDetailFragmentView)
            fun destroy()
            fun requestSimilarMovies(movieId:Int)
            fun requestMoreSimilarMovies(page: Int,movieId:Int)
            fun isFavorite(moviesFavoriteList: ArrayList<Movie>, movie: Movie): Boolean
            fun favoriteMovie(moviesFavoriteList: ArrayList<Movie>, movie: Movie): ArrayList<Movie>
            fun unFavoriteMovie(moviesFavoriteList: ArrayList<Movie>, movie: Movie): ArrayList<Movie>

        }
}