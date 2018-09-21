package onoffrice.allmovies.extension

import android.app.Activity
import android.content.Intent

/**
 * Open any activity just passing the destiny, in case, 'T'
 **/

inline fun<reified T:Activity>Activity.startActivity(){
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}