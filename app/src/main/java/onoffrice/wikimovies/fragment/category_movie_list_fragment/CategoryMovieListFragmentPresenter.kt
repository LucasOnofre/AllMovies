package onoffrice.wikimovies.fragment.category_movie_list_fragment

import onoffrice.wikimovies.model.Movie

class CategoryMovieListFragmentPresenter:CategoryMovieListFragmentContract.Presenter,CategoryMovieListFragmentContract.Model.OnRequestResultListener{

    var view: CategoryMovieListFragmentView? = null
    var model = CategoryMovieListFragmentModel()

    /**
     * Send's the result data to the view
     */
    override fun onSucess(movieArrayList: ArrayList<Movie>) {
        view?.setDataToList(movieArrayList)
        view?.hideProgress()
    }

    /**
     * Show's an error if the response is troubleled to the view
     */
    override fun onFailure(error: Throwable) {
        view?.onResponseError(error)
        view?.hideProgress()
    }

    /**
     * Make's the connection with the view
     */
    override fun bindTo(view: CategoryMovieListFragmentView) {
        this.view = view
    }

    /**
     * Destroy's the connection with the view
     */
    override fun destroy() {
        this.view = null
    }

    /**
     * Call's the method on the model passing the genreId
     */
    override fun requestMoviesModel(genreId:Int) {
        if (view != null){
            view?.showProgress()
        }

        model.requestMovies(1,genreId,this)
    }

    /**
     * Call's the method on the model passing the genreId and the page
     */
    override fun requestMoreMovies(page: Int,genreId:Int) {

        model.requestMovies(page,genreId,this)

    }
}