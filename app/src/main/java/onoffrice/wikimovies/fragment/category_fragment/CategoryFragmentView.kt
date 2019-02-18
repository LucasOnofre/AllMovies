package onoffrice.wikimovies.fragment.category_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.CategoryAdapter
import onoffrice.wikimovies.fragment.base_fragment.BaseFragment
import onoffrice.wikimovies.fragment.category_movie_list_fragment.CategoryMovieListFragmentView
import onoffrice.wikimovies.model.CategoryInterface
import onoffrice.wikimovies.model.Genre

class CategoryFragmentView : BaseFragment(), CategoryFragmentContract.View {

    private var categoryList : ListView? = null

    //Initialiozations

    private val presenter      : CategoryFragmentPresenter  = CategoryFragmentPresenter()
    private var genresArrayList: ArrayList<Genre>?          = ArrayList()


    /**
     * Implementation of the genre Interface, that get's the genre selected
     */
    private val genreClickListener = object: CategoryInterface {
        override fun onCategorySelected(genre: Genre?) {
            openPopulatedFragment(genre,"categoryChosen",CategoryMovieListFragmentView())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

       var view = inflater.inflate(R.layout.fragment_category, container, false)

        presenter.bindTo(this)
        presenter.getGenres()

        setUpViews(view)

        return view
    }

    /**
     * Convert's the genre array into an arrayList
     */
    override fun setGenres(genres: ArrayList<Genre>) {

        genresArrayList  = genres
    }

    /**
     * Convert's the genre in a Json and save on shared preferences
     */
    private fun setUpViews(view: View) {

        categoryList          = view.findViewById(R.id.category_list)
        categoryList?.adapter = CategoryAdapter(context,genresArrayList,genreClickListener)

    }
}
