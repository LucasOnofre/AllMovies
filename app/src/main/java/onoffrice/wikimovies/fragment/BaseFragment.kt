package onoffrice.wikimovies.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import onoffrice.wikimovies.R

open class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container
    }
    protected fun setupToolbar(title: String, container: View){
        var toolbar:Toolbar?       = container.findViewById(R.id.toolbar)
        var titleSection:TextView? = container.findViewById(R.id.title_section)

        toolbar.let {
            (activity as AppCompatActivity).setSupportActionBar(it)
            titleSection?.text = title
        }
    }
}
