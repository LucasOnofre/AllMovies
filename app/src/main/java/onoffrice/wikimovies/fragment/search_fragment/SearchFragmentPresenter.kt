package onoffrice.wikimovies.fragment.search_fragment

import onoffrice.wikimovies.model.Movie

class SearchFragmentPresenter:SearchFragmentContract.Presenter, SearchFragmentContract.Model.ResponseResult{

    private var view: SearchFragmentView? = null
    private var model = SearchFragmentModel()

    override fun bindTo(view: SearchFragmentView) {

        this.view = view
    }

    override fun destroy() {

        view = null
    }

    override fun requestData(query: String) {
        model.requestMovies(1,query,this)
    }

    override fun getMoreData(page: Int, query: String) {

        model.requestMovies(page,query,this)
    }

    override fun onSucess(movies: ArrayList<Movie>) {

        view?.updateList(movies)

    }

    override fun onFailure(error: Throwable) {

        view?.onResponseError(error)

    }
}