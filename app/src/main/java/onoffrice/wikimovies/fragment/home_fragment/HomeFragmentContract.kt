package onoffrice.wikimovies.fragment.HomeFragment

import onoffrice.wikimovies.model.Movie

interface HomeFragmentContract {


    interface Model{

         interface OnFinishedListener {

            fun onFinished(movieArrayList: List<Movie>)

            fun onFailure(t: Throwable)
        }

        fun requestMovies(page: Int, onFinishedListener: HomeFragmentContract.Model.OnFinishedListener)
    }

    interface View{

        //fun openDetailMovieFragment(movie: Movie)
        //fun openFragment()
        fun showProgress()
        fun hideProgress()

       // fun setBannerBar(movies: ArrayList<Movie>)

        fun setDataToRecyclerView(movieArrayList: List<Movie>)
        fun onResponseFailure(throwable: Throwable)

    }

    interface Presenter{

        //fun setBannerBar(movies: ArrayList<Movie>)
        fun bindTo(view:HomeFragment)
        fun destroy()
        fun requestDataFromServer()
        fun getMoreData(page: Int)
    }


}