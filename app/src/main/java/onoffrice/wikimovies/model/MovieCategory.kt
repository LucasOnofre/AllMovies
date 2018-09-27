package onoffrice.wikimovies.model

import com.google.gson.annotations.SerializedName

class MovieCategory {

    @SerializedName("name")
    var name:String? = null

    @SerializedName("id")
    var id:Int? = null
}
