package onoffrice.wikimovies.fragment.category_fragment

import onoffrice.wikimovies.model.Genre

class CategoryFragmentPresenter: CategoryFragmentContract.Presenter, CategoryFragmentContract.Model.Data{

    private var view: CategoryFragmentView? = null
    private var model = CategoryFragmentModel()

    private val TAG = "CategoryFragmentPresenter"

    override fun bindTo(view: CategoryFragmentView) {
        this.view = view
    }

    override fun destroy() {
        this.view = null
    }

    override fun getGenres() {

        model.populateGenres(this)
    }

    override fun populatedListData(genres: Array<Genre>) {

        view?.setGenres(genres)
    }

}