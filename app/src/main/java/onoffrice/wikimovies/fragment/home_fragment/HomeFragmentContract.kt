package onoffrice.wikimovies.fragment.home_fragment

import onoffrice.wikimovies.model.Movie

interface HomeFragmentContract {

    interface Model{

         interface OnRequestResultListener {

            fun onSucess(movieArrayList: List<Movie>)
            fun onFailure(error: Throwable)
        }

        fun requestMovies(page: Int, onFinishedListener: HomeFragmentContract.Model.OnRequestResultListener)

    }

    interface View{

        fun showProgress()
        fun hideProgress()
        fun setDataToRecyclerView(movieArrayList: List<Movie>)
        fun onResponseError(throwable: Throwable)

    }

    interface Presenter{

        fun bindTo(view:HomeFragmentView)
        fun destroy()
        fun requestDataFromServer()
        fun getMoreData(page: Int)
    }


}