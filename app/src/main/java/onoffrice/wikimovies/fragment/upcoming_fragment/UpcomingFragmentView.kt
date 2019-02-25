package onoffrice.wikimovies.fragment.upcoming_fragment


import android.os.Bundle
import android.support.constraint.ConstraintLayout
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
import com.squareup.picasso.Picasso
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.extension.checkConnection
import onoffrice.wikimovies.fragment.base_fragment.BaseFragment
import onoffrice.wikimovies.fragment.movie_detail_fragment.MovieDetailFragmentView
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.MovieInterface
import onoffrice.wikimovies.model.MovieLongClickInterface

class UpcomingFragmentView : BaseFragment(),UpcomingFragmentContract.View {

    private var page                                        = 1
    private var layout              : AppBarLayout?         = null
    private var adapter             : MoviesAdapter?        = null
    private var errorView           : ConstraintLayout?     = null
    private var isLoading           : Boolean               = false
    private var movieBanner         : ImageView?            = null
    private var progressBar         : ProgressBar?          = null
    private var recyclerList        : RecyclerView?         = null
    private var appBarLayout        : AppBarLayout?         = null
    private var bottomNavigation    : BottomNavigationView? = null
    private var movieBannerTittle   : TextView?             = null
    private var movieBannerSelected : Movie?                = null

    //  Initializations
    private var listMovies               : ArrayList<Movie>          = ArrayList()
    private var upcomingFragmentPresenter: UpcomingFragmentPresenter = UpcomingFragmentPresenter()

    private val TAG = "HomeFragmentView"

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

        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_upcoming, container, false)

            // Get's the views
            setUpViews(rootView!!)

            // Check's the scroll
            listenerScrollForToolbar()

            //Links the view to the presenter
            upcomingFragmentPresenter.bindTo(this)

            upcomingFragmentPresenter.checkNetworkConnection()

            //Make's the request by the presenter
            upcomingFragmentPresenter.requestData()

            //Set's the adapter
            setAdapter()

            setInfiniteScroll()
        }

        return rootView
    }

    override fun showProgress() {
        progressBar?.visibility  = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar?.visibility  = View.GONE
    }

    /**
     * Set's the data on the list
     */
    override fun setDataToRecyclerView(movieArrayList: ArrayList<Movie>) {

        listMovies.addAll(movieArrayList)

        adapter?.notifyDataSetChanged()

        checkFirstPage()
    }

    /**
     * Check's if it is the first page to set the bannerBar
     */
    private fun checkFirstPage() {
        if (page == 1) {
            setBannerBar(listMovies)
            movieBanner?.setOnClickListener {
                openPopulatedFragment(movieBannerSelected,"movieJson", MovieDetailFragmentView())
            }
        }
    }

    /**
     * Handle's the error from the request
     */
    override fun onResponseError(throwable: Throwable) {
        Log.e(TAG, throwable.message)
        showErrorView()
    }

    /**
     * Destroy's the link between the presenter and the view
     */
    override fun onDestroy() {
        super.onDestroy()
        upcomingFragmentPresenter.destroy()
    }

    private fun setAdapter() {
        adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener,movieLongClicListener) }
        recyclerList?.adapter = adapter
        setGridLayout(recyclerList)
    }


    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(view: View) {
        layout            = view.findViewById(R.id.appBarLayout)
        errorView         = view.findViewById(R.id.layout_error)
        progressBar       = view.findViewById(R.id.progressBar)
        movieBanner       = view.findViewById(R.id.movieBanner)
        recyclerList      = view.findViewById(R.id.lista)
        appBarLayout      = view.findViewById(R.id.appBarLayout)
        bottomNavigation  = view.findViewById(R.id.bottomNavigation)

    }

    /**
     * Check's the scroll to hide or show the toolbar title
     */
    private fun listenerScrollForToolbar() {
        appBarLayout?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            private var scrollRange = -1
            private var isShow = false

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    setupToolbar(rootView!!, "Upcoming")
                    isShow = true

                } else if (isShow) {
                    setupToolbar(rootView!!, " ")
                    isShow = false
                }
            }
        })
    }

    /**
     * Set's the banner bar, an image of the top ranked movie on the list
     */
    private fun setBannerBar(movies: ArrayList<Movie>) {
        Picasso.get().load(resources.getString(R.string.base_url_images) + movies[0].posterPath).into(movieBanner)
        movieBannerTittle?.text = movies[0].title
        movieBannerSelected     = movies[0]
        movies.removeAt(0)
    }

    /**
     * Make's new requests when user scrolls the list to the last item
     */
    private fun setInfiniteScroll() {
        recyclerList?.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //direction = 1 = list ends
                if (!recyclerView.canScrollVertically(1 ) && !isLoading){
                    isLoading = true
                    page++
                    upcomingFragmentPresenter.requestMoreData(page)
                    isLoading = false
                }
            }
        })
    }

    /**
     * Show's an error view if the request is failure
     */
    override fun showErrorView() {

        errorView?.visibility = View.VISIBLE
    }

    override fun hideErrorView(){

        errorView?.visibility = View.GONE

    }
}
