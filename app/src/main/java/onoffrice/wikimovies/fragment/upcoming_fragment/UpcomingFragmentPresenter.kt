package onoffrice.wikimovies.fragment.upcoming_fragment

import onoffrice.wikimovies.extension.checkConnection
import onoffrice.wikimovies.model.Movie

class UpcomingFragmentPresenter:
        UpcomingFragmentContract.Presenter,
        UpcomingFragmentContract.Model.OnRequestResultListener{

    var view:   UpcomingFragmentView? = null
    val model = UpcomingFragmentModel()

    /**
     * Make's the connection with the view
     */
    override fun bindTo(view: UpcomingFragmentView) {
        this.view = view
    }

    /**
     * Destroy's the connection with the view
     */
    override fun destroy() {

        view = null
    }

    /**
     * Send's the data result of the request to the view
     */
    override fun onSucess(movieArrayList: ArrayList<Movie>) {

        view?.setDataToRecyclerView(movieArrayList)

        if (view != null){ view?.hideProgress() }
    }

    /**
     * Send's the result error to the view
     */
    override fun onFailure(error: Throwable) {

        view?.onResponseError(error)

        if (view != null){
            view?.hideProgress()
        }
    }

    /**
     * Call's the method on the model to do the request
     */
    override fun requestData() {

        if (view != null){ view?.showProgress() }

        model.requestMovies(1,this)
    }

    /**
     * Call's the method on the model to do the request passing page as param
     */
    override fun requestMoreData(page: Int) {

        model.requestMovies(page,this)
    }

    /**
     * Check's the network connection
     * */
    override fun checkNetworkConnection() {

        val isConnected = view?.context?.checkConnection()

        if (isConnected != null && !isConnected){

            view?.showErrorView()

        }
    }
}