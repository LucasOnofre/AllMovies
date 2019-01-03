package onoffrice.wikimovies.fragment


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
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MovieInterface
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response



class HomeFragment : BaseFragment() {

    private var page                                     = 1
    private var layout           : AppBarLayout?         = null
    private var isLoading                                = true
    private var movieBanner      : ImageView?            = null
    private var progressBar      : ProgressBar?          = null
    private var recyclerList     : RecyclerView?         = null
    private var bottomNavigation : BottomNavigationView? = null
    private var movieBannerTittle: TextView?             = null

    private var gson             : Gson?            = Gson()
    private var listMovies       : ArrayList<Movie> = ArrayList()

    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object: MovieInterface{ override fun onMovieSelected(movie: Movie?) { openDetailMovieFragment(movie) } }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_home, container, false)

            setUpViews(rootView!!)
            requestMovies()
            setInfiniteScroll()
            setupToolbar("Popular", rootView!!)
            setAdapter()
        }

        return rootView
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
        //movieBannerTittle = view.findViewById(R.id.banner_movie_tittle)
        bottomNavigation  = view.findViewById(R.id.bottomNavigation)

        progressBar?.visibility  = View.VISIBLE

    }

    private fun setAdapter() {
        //Set's the adapter
        recyclerList?.adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener) }
        setGridLayout(recyclerList)
    }

    /**
     * Return a movie list from the Discover, passing page as a param for the request
     * Also its done a recursive function
     */
    private fun requestMovies(page:Int = 1){
        activity?.let {
            RequestMovies(it).getPopularsMovies(page).enqueue(object : retrofit2.Callback<Result>{

                override fun onResponse(call: Call<Result>, response: Response<Result>?) {

                    progressBar?.visibility = View.GONE
                    layout?.visibility      = View.VISIBLE

                    response?.body()?.movies?.let { movies -> if (page == 1){ setBannerBar(movies) }

                        listMovies.addAll(movies)
                        recyclerList?.adapter?.notifyDataSetChanged()
                        isLoading = false
                    }
                }
                override fun onFailure(call: Call<Result>, t: Throwable) {
                    Log.i("Error: ", t.message)
                }
            })
        }
    }

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
                    requestMovies(page)
                }
            }
        })
    }
}
