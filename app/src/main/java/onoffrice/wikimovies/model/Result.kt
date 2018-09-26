package onoffrice.wikimovies.model

import com.google.gson.annotations.SerializedName


class Result{

    @SerializedName("results")
    var movies: ArrayList<Movie> = ArrayList()
}