package onoffrice.wikimovies.fragment.category_movie_list_fragment


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.MovieInterface
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.extension.getPreferenceKey
import onoffrice.wikimovies.extension.parseJson
import onoffrice.wikimovies.fragment.base_fragment.BaseFragment
import onoffrice.wikimovies.fragment.movie_detail_fragment.MovieDetailFragmentView
import onoffrice.wikimovies.model.Genre
import onoffrice.wikimovies.model.Movie


class CategoryMovieListFragmentView : BaseFragment(), CategoryMovieListFragmentContract.View{

    private var page                                     = 1
    private var genre            :Genre?                 = null
    private var adapter          : MoviesAdapter?        = null
    private var isLoading                                = false
    private var progressBar      : ProgressBar?          = null
    private var recyclerList     : RecyclerView?         = null
    private var gson             : Gson?                 = Gson()
    private var listMovies       : ArrayList<Movie>      = ArrayList()
    private var presenter        : CategoryMovieListFragmentPresenter = CategoryMovieListFragmentPresenter()


    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object: MovieInterface{
        override fun onMovieSelected(movie: Movie?) {
            openDetailMovieFragment(movie)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_category_movie_list, container, false)

            setUpViews(rootView!!)

            getSelectedGenre()

            presenter.bindTo(this)
            presenter.requestMoviesModel(genre?.id!!)

            setToolbarGoBackArrow(rootView!!,genre?.name.toString())

            setAdapter()

            setInfiniteScroll()
        }

        return rootView
    }

    /**
     * Get's the genre Selected saved on preferences
     */
    private fun  getSelectedGenre(){
        val preferences = context?.getSharedPreferences("WikiMoviesPref", Context.MODE_PRIVATE)
        var genreSelected = (preferences?.getPreferenceKey("categoryChosen"))

        genre = genreSelected?.parseJson<Genre>()
        //gson?.fromJson(genreSelected, Genre::class.java)?.let { genre = it }
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

    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(view: View) {

        progressBar  = view.findViewById(R.id.progressBar)
        recyclerList = view.findViewById(R.id.category_list)


    }

    private fun setAdapter() {
        //Set's the adapter
        adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener) }
        recyclerList?.adapter = adapter

    }

    /**
     * Make's new requests when user scrolls the list to the last item
     */
    private fun setInfiniteScroll() {
        recyclerList?.addOnScrollListener(object:RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //direction = 1 = list ends
                if (!recyclerView.canScrollVertically(1) && !isLoading){
                    isLoading = true
                    page++
                    presenter.requestMoreMovies(page,genre?.id!!)
                    isLoading = false
                }
            }
        })
    }
    override fun showProgress() {

        progressBar?.visibility  = View.VISIBLE
    }

    override fun hideProgress() {

        progressBar?.visibility  = View.GONE

    }

    override fun setDataToList(movies: ArrayList<Movie>) {

        listMovies.addAll(movies)
        setGridLayout(recyclerList)
        adapter?.notifyDataSetChanged()

    }

    override fun onResponseError(error: Throwable) {
        Toast.makeText(context, error.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
