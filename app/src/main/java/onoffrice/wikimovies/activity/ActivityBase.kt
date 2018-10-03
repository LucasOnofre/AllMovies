package onoffrice.wikimovies.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import onoffrice.wikimovies.R

open class ActivityBase : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

     protected fun setupToolbar(title:String){
         var toolbar:Toolbar?   = null
         var titleSection:TextView?    = null

         toolbar = findViewById(R.id.toolbar)
         titleSection   = findViewById(R.id.title_section)

         toolbar.let {
             setSupportActionBar(it)
             titleSection?.text = title
         }
     }
}
