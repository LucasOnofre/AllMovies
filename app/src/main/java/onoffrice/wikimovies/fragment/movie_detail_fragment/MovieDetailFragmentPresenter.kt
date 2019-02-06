
package onoffrice.wikimovies.fragment.movie_detail_fragment

import onoffrice.wikimovies.model.Movie

class MovieDetailFragmentPresenter: MovieDetailFragmentContract.Presenter,MovieDetailFragmentContract.Model.RequestResult{

    private var view: MovieDetailFragmentView? = null
    private val model:MovieDetailFragmentModel = MovieDetailFragmentModel()


    override fun bindTo(view: MovieDetailFragmentView) {

        this.view = view
    }

    override fun destroy() {
        view = null
    }

    override fun requestSimilarMovies(movieId:Int) {

        model.requestSimilarMovies(1,movieId,this)
    }

    override fun requestMoreSimilarMovies(page: Int,movieId:Int) {

        model.requestSimilarMovies(page,movieId,this)
    }


    override fun onSucess(movies: ArrayList<Movie>) {

        view?.updateFavoriteList(movies)

    }

    override fun onError(error: Throwable) {
        view?.onResponseError(error)
    }

    override fun isFavorite(favoriteMovieList: ArrayList<Movie>, movie: Movie):Boolean {

        for (movieinList in favoriteMovieList) {
            if (movieinList.id == movie.id) {
                return true
                break
            }
        }
        return false
    }

    override fun favoriteMovie(moviesFavoriteList: ArrayList<Movie>, movie: Movie): ArrayList<Movie>  {

        moviesFavoriteList.add(movie)
        movie.isFavorite = true

        return  moviesFavoriteList
    }

    override fun unFavoriteMovie(moviesFavoriteList: ArrayList<Movie>, movie: Movie): ArrayList<Movie>  {

        val movieSelected = moviesFavoriteList.indexOfFirst { it.id == movie.id }

        if(movieSelected != -1)
            movie?.isFavorite = false

        moviesFavoriteList.removeAt(movieSelected)

        return  moviesFavoriteList
    }
}