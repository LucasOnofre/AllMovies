package onoffrice.wikimovies.fragment


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
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

    protected fun setupToolbar(container: View){
        var toolbar:Toolbar?       = container.findViewById(R.id.toolbar)
        var titleSection:TextView? = container.findViewById(R.id.title_section)

        toolbar.let {
            (activity as AppCompatActivity).setSupportActionBar(it)
        }
    }

    protected fun openFragment(fragment:Fragment){

        fragmentManager?.beginTransaction()?.replace(R.id.container, fragment)?.addToBackStack(null)?.commit()
    }


    protected fun exitFragment(fragment:Fragment){
        activity?.supportFragmentManager?.beginTransaction()?.remove(fragment)?.commit()
    }

    /**
     * Set's the layout manager of the recycler view to Grid  and set`s the orientation
     */
    protected fun setGridLayout(recyclerList:RecyclerView?) {

        var manager: GridLayoutManager?

        val orientation = resources.configuration.orientation

        manager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(activity, 4, GridLayoutManager.VERTICAL, false)
        }
        else
            GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)

        recyclerList?.layoutManager = manager
    }
}
