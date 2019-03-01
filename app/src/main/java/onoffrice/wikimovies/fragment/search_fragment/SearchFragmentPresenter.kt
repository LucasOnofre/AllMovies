package onoffrice.wikimovies.fragment.search_fragment

import onoffrice.wikimovies.model.Movie

class SearchFragmentPresenter:SearchFragmentContract.Presenter, SearchFragmentContract.Model.ResponseResult{

    private var view: SearchFragmentView? = null
    private var model = SearchFragmentModel()

    /**
     * Make's the connection with the view
     */
    override fun bindTo(view: SearchFragmentView) {

        this.view = view
    }

    /**
     * Destroy's the connection with the view
     */
    override fun destroy() {

        view = null
    }

    /**
     * Call's the method on the model to request data
     */
    override fun requestData(query: String) {
        model.requestMovies(1,query,this)
    }

    /**
     * Call's the method on the model to request more data
     */
    override fun requestMoreData(page: Int, query: String) {

        model.requestMovies(page,query,this)
    }

    /**
     * Send's the data result to the view
     */
    override fun onSucess(movies: ArrayList<Movie>) {

        view?.updateList(movies)

    }

    /**
     * Send's the error to the view
     */
    override fun onFailure(error: Throwable) {

        view?.onResponseError(error)
    }
}