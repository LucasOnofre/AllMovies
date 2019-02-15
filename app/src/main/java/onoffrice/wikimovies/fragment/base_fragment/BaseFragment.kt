package onoffrice.wikimovies.fragment.base_fragment


import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
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

    protected var rootView:View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container
    }

    /**
     * Set's the default toolbar her title
     */
    protected fun setupToolbar(view: View, title: String? = ""){

        var toolbar:Toolbar?       = view.findViewById(R.id.toolbar)
        var titleSection:TextView? = view.findViewById(R.id.title_section)

        if (title == " "){

            titleSection?.let { titleSection?.text = title }
            return
        }

        toolbar.let {
            (activity as AppCompatActivity).setSupportActionBar(it)
            titleSection?.let { titleSection?.text = title }
        }
    }

    /**
     * Set's the toolbar of the fragments
     */
    protected fun setToolbarGoBackArrow(view: View, title:String) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        var collapsingToolbar:CollapsingToolbarLayout? = view.findViewById(R.id.collapsing_toolbar)

        toolbar.let {
            (activity as AppCompatActivity).setSupportActionBar(it)

            collapsingToolbar?.title =  title
            it.title = title
            it.setNavigationIcon(R.drawable.ic_arrow_back)
            it.setNavigationOnClickListener { fragmentManager?.popBackStackImmediate() }
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
