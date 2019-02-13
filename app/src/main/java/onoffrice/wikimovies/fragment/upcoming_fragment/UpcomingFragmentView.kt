package onoffrice.wikimovies.fragment.upcoming_fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import onoffrice.wikimovies.R

class UpcomingFragmentView : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_upcoming, container, false)

        return view
    }


}
