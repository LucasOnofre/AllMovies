package onoffrice.wikimovies.fragment.upcoming_fragment

import onoffrice.wikimovies.model.Movie

class UpcomingFragmentPresenter:
        UpcomingFragmentContract.Presenter,
        UpcomingFragmentContract.Model.OnRequestResultListener{

    private val TAG = "UpcomingFragmentPresenter"

    var view:   UpcomingFragmentView? = null
    val model = UpcomingFragmentModel()

    override fun bindTo(view: UpcomingFragmentView) {
        this.view = view
    }

    override fun destroy() {

        view = null
    }

    override fun onSucess(movieArrayList: ArrayList<Movie>) {

        view?.setDataToRecyclerView(movieArrayList)

        if (view != null){ view?.hideProgress() }
    }

    override fun onFailure(error: Throwable) {

        view?.onResponseError(error)

        if (view != null){
            view?.hideProgress()
        }
    }

    override fun requestDataFromServer() {

        model.requestMovies(1,this)
    }

    override fun getMoreData(page: Int) {

        model.requestMovies(page,this)
    }

}