package onoffrice.wikimovies.fragment.category_movie_list_fragment

import onoffrice.wikimovies.model.Movie

interface CategoryMovieListFragmentContract{

    interface Model{

        interface OnRequestResultListener{

            fun onSucess(movieArrayList: ArrayList<Movie>)
            fun onFailure(error:Throwable)
        }
        fun requestMovies(
                page:Int
                ,genreId:Int
                ,onRequestListener:CategoryMovieListFragmentContract.Model.OnRequestResultListener
        )

    }

    interface View{

        fun showProgress()
        fun hideProgress()
        fun setDataToList(movies:ArrayList<Movie>)
        fun onResponseError(error: Throwable)
    }

    interface Presenter{

        fun bindTo(view: CategoryMovieListFragmentView)
        fun destroy()
        fun requestMoviesModel(genreId:Int)
        fun requestMoreMovies(page: Int,genreId:Int)
    }


}