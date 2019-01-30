package onoffrice.wikimovies.fragment.home_fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MovieInterface
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.fragment.BaseFragment
import onoffrice.wikimovies.fragment.MovieDetailFragment
import onoffrice.wikimovies.model.Movie

class HomeFragmentView : BaseFragment(), HomeFragmentContract.View {

    private var page                                     = 1
    private var layout           : AppBarLayout?         = null
    private var isLoading                                = false
    private var movieBanner      : ImageView?            = null
    private var progressBar      : ProgressBar?          = null
    private var recyclerList     : RecyclerView?         = null
    private var bottomNavigation : BottomNavigationView? = null
    private var movieBannerTittle: TextView?             = null

    private var gson = Gson()
    private var adapter: MoviesAdapter?      = null
    private var listMovies: ArrayList<Movie> = ArrayList()
    private var homeFragmentPresenter        = HomeFragmentPresenter()

    private val TAG = "HomeFragmentView"

    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object: MovieInterface{ override fun onMovieSelected(movie: Movie?) { openDetailMovieFragment(movie) } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_home, container, false)

            // Get's the views
            setUpViews(rootView!!)

            //Set's the adapter
            setAdapter()

            //Links the view to the presenter
            homeFragmentPresenter.bindTo(this)

            //Make's the request by the presenter
            homeFragmentPresenter.requestDataFromServer()

            setInfiniteScroll()
        }

        return rootView
    }

    override fun showProgress() {
        progressBar?.visibility  = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar?.visibility  = View.INVISIBLE
    }

    /**
     * Set's the data on the list
     */
    override fun setDataToRecyclerView(movieArrayList: List<Movie>) {

        listMovies.addAll(movieArrayList)
        setGridLayout(recyclerList)

        adapter?.notifyDataSetChanged()

        checkFirstPage()
    }

    /**
     * Check's if it is the first page to set the bannerBar
     */
    private fun checkFirstPage() {
        if (page == 1) {
            setBannerBar(listMovies)
        }
    }

    /**
     * Handle's the error from the request
     */
    override fun onResponseError(throwable: Throwable) {
        Log.e(TAG, throwable.message)
        Toast.makeText(context,"Unexpected error, please try again", Toast.LENGTH_LONG).show()
    }

    /**
     * Destroy's the link between the presenter and the view
     */
    override fun onDestroy() {
        super.onDestroy()
        homeFragmentPresenter.destroy()
    }

    private fun setAdapter() {
        recyclerList?.adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener) }
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

        editor?.putString("movieJson", movieJson)
        editor?.commit()

        openFragment(MovieDetailFragment())
    }

    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(view: View) {
        layout            = view.findViewById(R.id.appBarLayout)
        progressBar       = view.findViewById(R.id.progressBar)
        movieBanner       = view.findViewById(R.id.movieBanner)
        recyclerList      = view.findViewById(R.id.lista)
        bottomNavigation  = view.findViewById(R.id.bottomNavigation)

        setupToolbar(rootView!!)
    }

    /**
     * Set's the banner bar, an image of the top ranked movie on the list
     */
    private fun setBannerBar(movies: ArrayList<Movie>) {
        Picasso.get().load(resources.getString(R.string.base_url_images) + movies[0].posterPath).into(movieBanner)
        movieBannerTittle?.text = movies[0].title
        movies.removeAt(0)
    }

    /**
     * Make's new requests when user scrolls the list to the last item
     */
    private fun setInfiniteScroll() {
        recyclerList?.addOnScrollListener(object:RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //direction = 1 = list ends
                if (!recyclerView.canScrollVertically(1 ) && !isLoading){
                    isLoading = true
                    page++
                    homeFragmentPresenter.getMoreData(page)
                    isLoading = false
                }
            }
        })
    }
}
