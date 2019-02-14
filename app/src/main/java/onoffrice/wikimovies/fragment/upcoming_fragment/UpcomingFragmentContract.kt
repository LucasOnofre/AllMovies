package onoffrice.wikimovies.fragment.upcoming_fragment

import onoffrice.wikimovies.model.Movie

interface UpcomingFragmentContract {

    interface Model{

        interface OnRequestResultListener {

            fun onSucess(movieArrayList: ArrayList<Movie>)
            fun onFailure(error: Throwable)
        }

        fun requestMovies(page: Int, onFinishedListener: UpcomingFragmentContract.Model.OnRequestResultListener)

    }

    interface View{

        fun showProgress()
        fun hideProgress()
        fun setDataToRecyclerView(movieArrayList: ArrayList<Movie>)
        fun onResponseError(error: Throwable)

    }

    interface Presenter{

        fun bindTo(view: UpcomingFragmentView)
        fun destroy()
        fun requestDataFromServer()
        fun getMoreData(page: Int)
    }

}