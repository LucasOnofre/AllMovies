package onoffrice.wikimovies.model

import com.google.gson.annotations.SerializedName

class Genres{

    @SerializedName("genres")
    var genres: ArrayList<MovieCategory> = ArrayList()
}