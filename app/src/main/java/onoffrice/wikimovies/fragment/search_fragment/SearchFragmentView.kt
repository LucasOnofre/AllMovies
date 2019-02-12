package onoffrice.wikimovies.fragment.search_fragment


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MovieInterface
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.fragment.base_fragment.BaseFragment
import onoffrice.wikimovies.fragment.movie_detail_fragment.MovieDetailFragmentView
import onoffrice.wikimovies.model.Movie


class SearchFragment : BaseFragment(), SearchFragmentContract.View {

    private var page                                         = 1
    private var adapter          : MoviesAdapter?            = null
    private var isLoading                                    = false
    private var progressBar      : ProgressBar?              = null
    private var recyclerList     : RecyclerView?             = null
    private var gson             : Gson?                     = Gson()
    private var listMovies       : ArrayList<Movie>          = ArrayList()
    private val handler                                      = Handler()
    private var searchView       :android.widget.SearchView? = null

    private var presenter        : SearchFragmentPresenter   = SearchFragmentPresenter()


    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object: MovieInterface {
        override fun onMovieSelected(movie: Movie?) {
            openDetailMovieFragment(movie)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(onoffrice.wikimovies.R.layout.fragment_search, container, false)

//        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        setUpViews(view)
        setupSearchView(searchView!!)
        searchViewListener(searchView!!, handler)

        return view
    }

    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(view: View) {

        searchView   = view.findViewById(R.id.searchView)
        recyclerList = view.findViewById(R.id.lista)

    }

    private fun setAdapter() {
        //Set's the adapter
        adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener) }
        recyclerList?.adapter = adapter

    }

    private fun setInfiniteScroll() {
        recyclerList?.addOnScrollListener(object:RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //direction = 1 = list ends
                if (!recyclerView.canScrollVertically(1) && !isLoading){
                    isLoading = true
                    page++
                    //presenter.re(page,genre?.id!!)
                    isLoading = false
                }
            }
        })
    }

    private fun searchViewListener(searchView: android.widget.SearchView, handle: Handler) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {

                handle.postDelayed({
                    Toast.makeText(context, newQuery, Toast.LENGTH_LONG).show()
                }, 1500)

                return true
            }
        })
    }

    private fun setupSearchView(searchView: android.widget.SearchView) {
        val searchPlateId  = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val searchPlate = searchView.findViewById(searchPlateId) as EditText
        searchPlate.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        searchPlate.imeOptions = EditorInfo.IME_ACTION_SEARCH
    }

    /**
     * Convert's the movie in a Json,
     * save on shared preferences and also open de Movie Detail Fragment
     */
    private fun openDetailMovieFragment(movie:Movie?){
        val preferences = context?.getSharedPreferences("WikiMoviesPref", Context.MODE_PRIVATE)
        val editor = preferences?.edit()

        // Transform the movie into an Json to save in shared preferences
        var movieJson = gson?.toJson(movie)

        editor?.putString("movieJson",movieJson)
        editor?.commit()

        openFragment(MovieDetailFragmentView())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun updateList(movies: ArrayList<Movie>) {

    }

    override fun onResponseError(error: Throwable) {

        Toast.makeText(context, error.toString(),Toast.LENGTH_LONG).show()
    }
}
