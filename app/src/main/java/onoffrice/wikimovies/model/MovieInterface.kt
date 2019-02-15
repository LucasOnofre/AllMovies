package onoffrice.wikimovies.model

import android.view.View

/**
 * Interface to get the click on the movie
 */
interface MovieInterface{
    fun onMovieSelected(movie:Movie?)
}
interface MovieLongClickInterface{
    fun onMovieLongClickSelected(view: View, movie:Movie?)
}