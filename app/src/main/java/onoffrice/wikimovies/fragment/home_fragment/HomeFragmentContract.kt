package onoffrice.wikimovies.fragment.home_fragment

import onoffrice.wikimovies.model.Movie

interface HomeFragmentContract {

    interface Model{

         interface OnRequestResultListener {

            fun onSucess(movieArrayList: ArrayList<Movie>)
            fun onFailure(error: Throwable)
        }

        fun requestMovies(page: Int, onFinishedListener: HomeFragmentContract.Model.OnRequestResultListener)

    }

    interface View{

        fun showProgress()
        fun hideProgress()
        fun setDataToRecyclerView(movieArrayList: ArrayList<Movie>)
        fun onResponseError(error: Throwable)

    }

    interface Presenter{

        fun bindTo(view:HomeFragmentView)
        fun destroy()
        fun requestDataFromServer()
        fun getMoreData(page: Int)
    }


}