package onoffrice.wikimovies.fragment.category_movie_list_fragment

import onoffrice.wikimovies.model.Movie

class CategoryMovieListFragmentPresenter:CategoryMovieListFragmentContract.Presenter,CategoryMovieListFragmentContract.Model.OnRequestResultListener{

    var view: CategoryMovieListFragmentView? = null
    var model = CategoryMovieListFragmentModel()

    private val TAG = "CategoryMovieListFragmentPresenter"

    override fun onSucess(movieArrayList: ArrayList<Movie>) {

        view?.setDataToList(movieArrayList)
        view?.hideProgress()
    }

    override fun onFailure(error: Throwable) {
        view?.onResponseError(error)
        view?.hideProgress()
    }

    override fun bindTo(view: CategoryMovieListFragmentView) {

        this.view = view
    }

    override fun destroy() {
        this.view = null
    }

    override fun requestMoviesModel(genreId:Int) {
        if (view != null){

            view?.showProgress()
        }
        model.requestMovies(1,genreId,this)
    }

    override fun requestMoreMovies(page: Int,genreId:Int) {

        model.requestMovies(page,genreId,this)

    }
}