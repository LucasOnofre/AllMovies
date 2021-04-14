package onoffrice.wikimovies.fragment.search_fragment

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
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_search.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.fragment.base_fragment.BaseFragment
import onoffrice.wikimovies.fragment.movie_detail_fragment.MovieDetailFragmentView
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.MovieInterface
import onoffrice.wikimovies.model.MovieLongClickInterface


class SearchFragmentView : BaseFragment(), SearchFragmentContract.View {

    private var page                                         = 1
    private var query            : String?                   = null
    private var adapter          : MoviesAdapter?            = null
    private var isLoading                                    = false
    private var recyclerList     : RecyclerView?             = null
    private var searchView       :android.widget.SearchView? = null

    //  Initializations
    private val handler                                      = Handler()
    private var listMovies       : ArrayList<Movie>          = ArrayList()
    private var presenter        : SearchFragmentPresenter   = SearchFragmentPresenter()











    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object: MovieInterface {
        override fun onMovieSelected(movie: Movie?) {
            openPopulatedFragment(movie,"movieJson",MovieDetailFragmentView())
        }
    }

    /**
     * Implementing interface to handle the Long click on the movie
     */
    private val movieLongClicListener = object : MovieLongClickInterface {
        override fun onMovieLongClickSelected(view: View, movie: Movie?) {
            openDropMenu(view, movie)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(onoffrice.wikimovies.R.layout.fragment_search, container, false)


        setUpViews(view)
        setupSearchView(searchView!!)
        setAdapter()

        presenter.bindTo(this)

        searchViewListener(searchView!!)

        setInfiniteScroll()

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
        adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener,movieLongClicListener) }
        setGridLayout(recyclerList)
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
                    presenter.requestMoreData(page,query!!)
                    isLoading = false
                }
            }
        })
    }

    private fun searchViewListener(searchView: android.widget.SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {

                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newQuery: String): Boolean {

                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({filterQuery(newQuery)}, 250)

                return true
            }
        })
    }

    private fun filterQuery(newQuery: String) {

        query = newQuery

        if (!listMovies.isEmpty()){
            listMovies.clear()
            adapter?.notifyDataSetChanged()
        }

        if (!newQuery.isEmpty()){

            showList()
            presenter.requestData(newQuery)

        }else
            hideList()
    }

    private fun hideList() {
        layout_search_list.visibility    = View.GONE
        layout_search_message.visibility = View.VISIBLE
    }

    private fun showList() {
        layout_search_list.visibility    = View.VISIBLE
        layout_search_message.visibility = View.GONE
    }

    private fun setupSearchView(searchView: android.widget.SearchView) {
        val searchPlateId  = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val searchPlate = searchView.findViewById(searchPlateId) as EditText
        searchPlate.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        searchPlate.imeOptions = EditorInfo.IME_ACTION_SEARCH
    }


    /**
     * Update's the list with data of the presenter
     */
    override fun updateList(movies: ArrayList<Movie>) {

        listMovies.addAll(movies)
        adapter?.notifyDataSetChanged()

    }


    override fun onResponseError(error: Throwable) {

        Toast.makeText(context, error.toString(),Toast.LENGTH_LONG).show()
    }

    /**
     * Destroy's the link between the presenter and the view
     */
    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
