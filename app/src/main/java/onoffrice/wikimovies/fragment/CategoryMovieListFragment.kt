package onoffrice.wikimovies.fragment


import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.gson.Gson
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MovieInterface
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.extension.getPreferenceKey
import onoffrice.wikimovies.model.Genre
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response



class CategoryMovieListFragment : BaseFragment() {

    private var page                                     = 1
    private var isLoading                                = true
    private var progressBar      : ProgressBar?          = null
    private var recyclerList     : RecyclerView?         = null
    private var gson             : Gson?            = Gson()
    private var listMovies       : ArrayList<Movie> = ArrayList()


    private var genre:Genre? = null


    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object: MovieInterface{ override fun onMovieSelected(movie: Movie?) { openDetailMovieFragment(movie) } }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_category_movie_list, container, false)

            setUpViews(rootView!!)
            getSelectedGenre()
            configureToolbar(rootView!!,genre?.name.toString())
            requestMovies()
            setInfiniteScroll()

            setAdapter()
        }

        return rootView
    }

    /**
     * Get's the genre Selected saved on preferences
     */
    private fun  getSelectedGenre(){
        val preferences = context?.getSharedPreferences("WikiMoviesPref", Context.MODE_PRIVATE)
        var genreSelected = (preferences?.getPreferenceKey("categoryChosen"))

        gson?.fromJson(genreSelected, Genre::class.java)?.let { genre = it }

    }

    private fun configureToolbar(view: View, title:String) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(view)
        toolbar.let {
            (activity as AppCompatActivity).setSupportActionBar(it)

            it.title = title

            it.setNavigationIcon(R.drawable.ic_arrow_back)
            it.setNavigationOnClickListener { fragmentManager?.popBackStackImmediate() }
        }
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

        progressBar       = view.findViewById(R.id.progressBar)
        recyclerList      = view.findViewById(R.id.category_list)
        progressBar?.visibility  = View.VISIBLE

    }

    private fun setAdapter() {
        //Set's the adapter
        recyclerList?.adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener) }
        setGridLayout(recyclerList)
    }

    /**
     * Return a movie list of the selected genre
     * Also a recursive function
     */
    private fun requestMovies(page:Int = 1){
        activity?.let {
            RequestMovies(it).getGenresMovieList(page,genre?.id).enqueue(object : retrofit2.Callback<Result>{

                override fun onResponse(call: Call<Result>, response: Response<Result>?) {

                    progressBar?.visibility = View.GONE


                    response?.body()?.movies?.let { movies ->

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
