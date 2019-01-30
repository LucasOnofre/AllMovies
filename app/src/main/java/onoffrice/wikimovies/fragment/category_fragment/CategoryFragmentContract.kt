package onoffrice.wikimovies.fragment.category_fragment

import onoffrice.wikimovies.model.Genre

interface CategoryFragmentContract{

    interface Model{

        interface Data{

            fun populatedListData(genres:Array<Genre>)
        }

        fun populateGenres(listener: CategoryFragmentContract.Model.Data)
    }

    interface View{

        fun setGenres(genres: Array<Genre>)
    }

    interface Presenter{

        fun bindTo(view: CategoryFragmentView)
        fun destroy()
        fun getGenres()
    }

}