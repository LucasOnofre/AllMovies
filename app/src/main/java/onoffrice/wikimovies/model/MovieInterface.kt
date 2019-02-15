package onoffrice.wikimovies.model

/**
 * Interface to get the click on the movie
 */
interface MovieInterface{
    fun onMovieSelected(movie:Movie?)
}
interface MovieLongClickInterface{
    fun onMovieLongClickSelected(movie:Movie?)
}