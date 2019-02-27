package onoffrice.wikimovies.extension

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.DisplayMetrics
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import onoffrice.wikimovies.model.Movie
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Open any activity just passing the destiny, in case, 'T'
 **/

inline fun<reified T:Activity>Activity.startActivity(){
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

/**
* Exibe Toast message de Activities
**/

fun Context.toast(message:String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

 fun String.formatDateToYear(): String? {

    try {
        val date   = SimpleDateFormat("yyyy-MM-dd").parse(this)
        val dateFormat   = SimpleDateFormat("yyyy", Locale("pt", "BR"))

        return dateFormat.format(date)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

/**
 * Load's the String URL in the Image View param
 */
fun String.loadPicasso(local:ImageView?){
    Picasso.get().load(this).into(local)
}

fun Context.getPreferences():SharedPreferences{
    return this?.getSharedPreferences("WikiMoviesPref", Context.MODE_PRIVATE)
}

fun Context.getPreferencesEditor():SharedPreferences.Editor?{
    val preferences = this?.getSharedPreferences("WikiMoviesPref", Context.MODE_PRIVATE)
    return preferences?.edit()
}

fun SharedPreferences.getPreferenceKey(key:String): String? {
    return this?.getString(key,"")
}

/**
 * Convert's the object param to Json
 */
inline fun<reified T> String.parseJson():T?{
    try{
        return Gson().fromJson(this, T::class.java)
    }
    catch (exception:Exception){
        exception.stackTrace
    }
    return null
}

 fun Activity.getScreenSize(): Triple<Int, Int, Int> {
    var orientation = this.resources.configuration.orientation

    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)

    var width  = displayMetrics.widthPixels
    var height = displayMetrics.heightPixels

    return Triple(orientation, width, height)
}

/**
 * Animate the ProgressBar
 */
fun ProgressBar.circleAnimate(rate:Int){
    this?.postDelayed({
        val animation = ObjectAnimator.ofInt(this, "progress",rate*(100/10))
        animation.duration = 500
        animation.interpolator = LinearInterpolator()
        animation.start()
    }, 100)
}

/**
 * Favorite the movie
 */
fun Movie.favoriteMovie(moviesFavoriteList: ArrayList<Movie>): ArrayList<Movie> {

    moviesFavoriteList.add(this)
    this.isFavorite = true


    return moviesFavoriteList
}

/**
 * Unfavorite the movie
 */
fun Movie.unFavoriteMovie(moviesFavoriteList: ArrayList<Movie>): ArrayList<Movie> {

    val movieSelected = moviesFavoriteList.indexOfFirst { it.id == this.id }

    if(movieSelected != -1)
        this.isFavorite = false

    moviesFavoriteList.removeAt(movieSelected)

    return  moviesFavoriteList
}

/**
 * Check's if the movie is favorited
 * and if it's, change the value of isFavorite for true
 */
 fun ArrayList<Movie>.checkFavorite(movie: Movie?) {

    for (movieItem in this) {
        if (movieItem.id == movie?.id){
            movie?.isFavorite = true
        }
    }
}

/**
 * Save's the list of favorite movies in the shared preferences
 */
fun ArrayList<Movie>.saveFavoriteMovies(context:Context) {

    val gson = Gson()
    val editor = context.getPreferencesEditor()

    // Transform the movie into an Json to save in shared preferences
    var favoritedList = gson.toJson(this)

    editor?.putString("favoriteMovieList", favoritedList)
    editor?.commit()
}

fun SharedPreferences.getFavorites():ArrayList<Movie>{

    var favoriteMovies:ArrayList<Movie> = ArrayList()
    val json = this.getPreferenceKey("favoriteMovieList")

    json?.parseJson<Array<Movie>>()?.let {
         favoriteMovies =  it.toCollection(ArrayList())
    }

    return favoriteMovies
}


/**
 * Check's the network connection
 */
fun Context.checkConnection():Boolean{

    var connectionManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    var activeNetwork: NetworkInfo?
            = connectionManager.activeNetworkInfo

    return activeNetwork != null && activeNetwork.isConnected

}
