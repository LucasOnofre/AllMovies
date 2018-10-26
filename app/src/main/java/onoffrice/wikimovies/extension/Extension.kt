package onoffrice.wikimovies.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.DisplayMetrics
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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

fun Activity.toast(message:String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

 fun String.convertDate(): String? {

    try {
        val date  = SimpleDateFormat("yyyy-MM-dd").parse(this)
        val dateFormat   = SimpleDateFormat("yyyy", Locale("pt", "BR"))

        return dateFormat.format(date)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

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

    return this?.getString(key,"0")
}

 fun Activity.getScreenSize(): Triple<Int, Int, Int> {
    var orientation = this.resources.configuration.orientation

    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)

    var width  = displayMetrics.widthPixels
    var height = displayMetrics.heightPixels

    return Triple(orientation, width, height)
}

