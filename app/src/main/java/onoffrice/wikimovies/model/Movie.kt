package onoffrice.wikimovies.model

import com.google.gson.annotations.SerializedName

class Movie{

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("genre_ids")
    var genres: List<Int>? = null

    @SerializedName("vote_average")
    var voteAverage:Float? = null

    @SerializedName("popularity")
    var popularity:Double? = null

    @SerializedName("poster_path")
    var posterPath:String? = null

    @SerializedName("backdrop_path")
    var backdropPath:String? = null

    @SerializedName("original_language")
    var originalLanguage:String? = null

    @SerializedName("original_title")
    var originalTitle:String? = null

    @SerializedName("overview")
    var overview:String? = null

    @SerializedName("release_date")
    var releaseDate:String? = null

}

class Genre {
    var id:Int? = null
    var name:String? = null
}

class MovieListGenre{
    var genre: Genre? = null
    var movies:List<Movie> = ArrayList()

    constructor(genre: Genre?, movies:List<Movie>) {
        this.genre  = genre
        this.movies = movies.filter {it.posterPath != null}
    }
}



class MovieCategory {

    @SerializedName("name")
    var name:String? = null

    @SerializedName("id")
    var id:Int? = null
}
