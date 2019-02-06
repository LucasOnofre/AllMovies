package onoffrice.wikimovies.fragment.search_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import onoffrice.wikimovies.R
import onoffrice.wikimovies.fragment.BaseFragment

class SearchFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_search, container, false)

        return view
    }


}
