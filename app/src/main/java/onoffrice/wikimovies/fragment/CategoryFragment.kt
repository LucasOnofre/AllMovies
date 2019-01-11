package onoffrice.wikimovies.fragment


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
import onoffrice.wikimovies.model.Genre
import java.util.*

class CategoryFragment : BaseFragment() {

    private var gson           : Gson? = Gson()
    private var genres         : Array<Genre>?     = null
    private var categoryList   : ListView?         = null
    private var genresArrayList: ArrayList<Genre>? = null


    private val genreClickListener = object: CategoryInterface {override fun onCategorySelected(genre: Genre?) {openCategoryList(genre)} }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

       var view = inflater.inflate(R.layout.fragment_category, container, false)

        populateGenres()
        setUpViews(view)

        return view
    }

    private fun populateGenres() {

        genres = arrayOf(

                Genre(18    , "Action"),
                Genre(12    , "Adventure"),
                Genre(16    , "Animation"),
                Genre(35    , "Comedy"),
                Genre(80    , "Crime"),
                Genre(99    , "Documentary"),
                Genre(10751 , "Family"),
                Genre(14    , "Fantasy"),
                Genre(36    , "History"),
                Genre(27    , "Horror"),
                Genre(10402 , "Music"),
                Genre(9648  , "Mystery"),
                Genre(10749 , "Romance"),
                Genre(878   , "Science Fiction"),
                Genre(10770 , "TV Movie"),
                Genre(53    , "Thriller"),
                Genre(10752 , "War"),
                Genre(37    , "Western")
        )
    }


    private fun setUpViews(view: View) {

        categoryList          = view.findViewById(R.id.listView)

        //Convert's the array in arrayList
        genresArrayList       = genres?.toCollection(ArrayList())
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

        openFragment(CategoryMovieListFragment())
    }
}
