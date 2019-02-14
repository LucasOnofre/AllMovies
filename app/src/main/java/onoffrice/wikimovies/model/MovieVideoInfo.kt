package onoffrice.wikimovies.model

import com.google.gson.annotations.SerializedName

class MovieVideoInfo {

    @SerializedName("key")
    var key: String? = null

    @SerializedName("site")
    var site: String? = null

    @SerializedName("type")
    var type: String? = null

}
