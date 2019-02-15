package onoffrice.wikimovies.fragment.home_fragment

import onoffrice.wikimovies.model.Movie

class HomeFragmentPresenter : HomeFragmentContract.Presenter, HomeFragmentContract.Model.OnRequestResultListener{

    private var view: HomeFragmentView? = null
    private var model = HomeFragmentModel()

    /**
     * Make's the connection with the view
     */
    override fun bindTo(view:HomeFragmentView){
        this.view = view
    }

    /**
     * Destroy's the connection with the view
     */
    override fun destroy(){
        this.view = null
    }

    /**
     * Call's the method on model
     */
    override fun requestDataFromServer() {

        if (view != null){ view?.showProgress() }

        model.requestMovies(1, this)
    }

    /**
     * Call's the method on model passing page as param
     */
    override fun getMoreData(page: Int) {

        model.requestMovies(page, this)
    }

    /**
     * Send's the data of the result to the view
     */
    override fun onSucess(movieArrayList: ArrayList<Movie>) {

        view?.setDataToRecyclerView(movieArrayList)

        if (view != null){ view?.hideProgress() }
    }

    /**
     * Send's the error result to the view
     */
    override fun onFailure(error: Throwable) {
        view?.onResponseError(error)

        if (view != null){
            view?.hideProgress()
        }
    }
}