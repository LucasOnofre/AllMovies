package onoffrice.wikimovies.fragment.search_fragment

import onoffrice.wikimovies.model.Movie

interface SearchFragmentContract{

    interface Model{

        interface ResponseResult{

            fun onSucess(movies: ArrayList<Movie>)
            fun onFailure(error:Throwable)
        }

        fun requestMovies(page: Int, query:String, onFinishedListener: SearchFragmentContract.Model.ResponseResult)

    }
    interface View{

        fun updateList(movies:ArrayList<Movie>)
        fun onResponseError(error: Throwable)

    }
    interface Presenter{

        fun bindTo(view: SearchFragmentView)
        fun destroy()
        fun requestData(query:String)
        fun requestMoreData(page: Int,query:String)


    }

}