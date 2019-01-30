package onoffrice.wikimovies.fragment.HomeFragment

import onoffrice.wikimovies.model.Movie

class HomeFragmentPresenter : HomeFragmentContract.Presenter, HomeFragmentContract.Model.OnFinishedListener{

    private var model = HomeFragmentModel()
    private var view:HomeFragment? = null

    private val TAG = "HomeFragmentPresenter"


    override fun bindTo(view:HomeFragment){
        this.view = view
    }

    override fun destroy(){
        this.view = null
    }

    override fun requestDataFromServer() {

        if (view != null){
            view?.showProgress()
        }
        model.requestMovies(1, this)
    }

    override fun getMoreData(page: Int) {

        model.requestMovies(page, this)
    }

    override fun onFinished(movieArrayList: List<Movie>) {

        view?.setDataToRecyclerView(movieArrayList)
        if (view != null){
            view?.hideProgress()
        }
    }

    override fun onFailure(error: Throwable) {
        view?.onResponseFailure(error)

        if (view != null){
            view?.hideProgress()
        }
    }
}