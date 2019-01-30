package onoffrice.wikimovies.fragment.home_fragment

import onoffrice.wikimovies.model.Movie

class HomeFragmentPresenter : HomeFragmentContract.Presenter, HomeFragmentContract.Model.OnRequestResultListener{

    private var model = HomeFragmentModel()
    private var view: HomeFragmentView? = null

    private val TAG = "HomeFragmentPresenter"

    override fun bindTo(view:HomeFragmentView){
        this.view = view
    }

    override fun destroy(){
        this.view = null
    }

    override fun requestDataFromServer() {

        if (view != null){ view?.showProgress() }
        
        model.requestMovies(1, this)
    }

    override fun getMoreData(page: Int) {

        model.requestMovies(page, this)
    }

    override fun onSucess(movieArrayList: List<Movie>) {

        view?.setDataToRecyclerView(movieArrayList)
        if (view != null){
            view?.hideProgress()
        }
    }

    override fun onFailure(error: Throwable) {
        view?.onResponseError(error)

        if (view != null){
            view?.hideProgress()
        }
    }
}