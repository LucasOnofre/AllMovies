package onoffrice.wikimovies.fragment.category_fragment

import onoffrice.wikimovies.model.Genre

class CategoryFragmentPresenter: CategoryFragmentContract.Presenter, CategoryFragmentContract.Model.Data{

    private var view: CategoryFragmentView? = null
    private var model = CategoryFragmentModel()


    override fun bindTo(view: CategoryFragmentView) {
        this.view = view
    }

    override fun destroy() {
        this.view = null
    }

    /**
     * Call the method on the model that make's the request
     */
    override fun getGenres() {

        model.populateGenres(this)
    }

    /**
     * Bring's the result of the request to the view
     */
    override fun populateListData(genres: Array<Genre>) {

        view?.setGenres(genres.toCollection(ArrayList()))
    }

}