package onoffrice.wikimovies.model

import com.google.gson.annotations.SerializedName

class MovieVideoInfoList {

    @SerializedName("results")
    var movieVideoList: ArrayList<MovieVideoInfo> = ArrayList()

}
