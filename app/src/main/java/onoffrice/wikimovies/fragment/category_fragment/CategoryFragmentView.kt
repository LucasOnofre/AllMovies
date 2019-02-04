package onoffrice.wikimovies.fragment.category_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.gson.Gson
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.CategoryAdapter
import onoffrice.wikimovies.adapter.CategoryInterface
import onoffrice.wikimovies.fragment.BaseFragment
import onoffrice.wikimovies.fragment.category_movie_list_fragment.CategoryMovieListFragmentView
import onoffrice.wikimovies.model.Genre
import kotlin.collections.ArrayList

class CategoryFragmentView : BaseFragment(), CategoryFragmentContract.View {

    private var categoryList   : ListView?         = null

    private var gson           : Gson?             = Gson()
    private val presenter                          = CategoryFragmentPresenter()
    private var genresArrayList: ArrayList<Genre>? = ArrayList()


    private val genreClickListener = object: CategoryInterface {
        override fun onCategorySelected(genre: Genre?) {
            openCategoryList(genre)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

       var view = inflater.inflate(R.layout.fragment_category, container, false)

        presenter.bindTo(this)
        presenter.getGenres()

        setUpViews(view)


        return view
    }

    override fun setGenres(genres: Array<Genre>) {

        genresArrayList  = genres.toCollection(ArrayList())
    }

    private fun setUpViews(view: View) {

        categoryList          = view.findViewById(R.id.listView)
        categoryList?.adapter = CategoryAdapter(context,genresArrayList,genreClickListener)

    }

    /**
     * Convert's the genre in a Json and save on shared preferences
     */
    private fun openCategoryList(genre:Genre?){
        val preferences = context?.getSharedPreferences("WikiMoviesPref", Context.MODE_PRIVATE)
        val editor = preferences?.edit()

        var genreJson = gson?.toJson(genre)

        editor?.putString("categoryChosen",genreJson )
        editor?.commit()

        openFragment(CategoryMovieListFragmentView())
    }
}
