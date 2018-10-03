package onoffrice.wikimovies.extension

import android.app.Activity
import android.content.Intent
import android.widget.Toast

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