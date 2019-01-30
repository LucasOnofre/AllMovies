package onoffrice.wikimovies.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MovieInterface
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.custom.UserButton
import onoffrice.wikimovies.extension.*
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.Result
import onoffrice.wikimovies.request.RequestMovies
import retrofit2.Call
import retrofit2.Response


class MovieDetailFragment : BaseFragment() {

    private var page                                           = 1
    private var editor              :SharedPreferences.Editor? = null
    private var btnGoOut            :UserButton?               = null
    private var movieName           :TextView?                 = null
    private var isLoading                                      = true
    private var progressBar         :ProgressBar?              = null
    private var progressTxt         :TextView?                 = null
    private var btnFavorite         :UserButton?               = null
    private var preferences         :SharedPreferences?        = null
    private var movieBanner         :ImageView?                = null
    private var recyclerList        :RecyclerView?             = null
    private var movieDescript       :TextView?                 = null
    private var movieReleaseDate    :TextView?                 = null

    private var gson                :Gson?                     = Gson()
    private var movie               :Movie                     = Movie()
    private var listMovies          :ArrayList<Movie>          = ArrayList()
    private var favoriteMovieList   :ArrayList<Movie>          = ArrayList()


    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object: MovieInterface { override fun onMovieSelected(movie: Movie?) { openDetailMovieFragment(movie) } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        preferences             = context?.getPreferences()
        editor                  = context?.getPreferencesEditor()

        getSelectedMovie()
        setUpViews(view)
        setToolbarGoBackArrow(view, movie.title.toString())
        setAdapter()
        setInfiniteScroll()
        requestSimilarMovies(movieId = movie.id)

        return view
    }

    override fun onResume() {
        super.onResume()

        val json = preferences?.getPreferenceKey("favoriteMovieList")
        context?.parseJson<Array<Movie>>(json)?.let {
            favoriteMovieList = it.toCollection(ArrayList())
            getFavorites()
        }
    }

    override fun onPause() {
        super.onPause()
        saveFavoriteMovies(favoriteMovieList)
    }

    private fun getFavorites() {
        if (!favoriteMovieList.isEmpty()) {

            for (movieOnList in favoriteMovieList){
                if (movieOnList.id == movie?.id){
                    if (movieOnList.isFavorite){
                        favoriteMovie(movie)
                        break
                    }
                }
            }
        }
    }

    /**
     * Get the Selected movie saved on preferences that is a json and transform it into a movie again
     */
    private fun  getSelectedMovie(){

        val movieJson = preferences?.getPreferenceKey("movieJson")

        //Transform the json into a object '(movie)'
        gson?.fromJson(movieJson, Movie::class.java)?.let {
            movie = it
        }
    }

    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(view: View) {

        movieBanner         = view.findViewById(R.id.movieBanner)
        //movieName         = view.findViewById(R.id.movie_text)
        movieDescript       = view.findViewById(R.id.movieDescript)
        movieReleaseDate    = view.findViewById(R.id.movie_release_date)
        progressBar         = view.findViewById(R.id.circle_progress)
        progressTxt         = view.findViewById(R.id.progress_nota)
        recyclerList        = view.findViewById(R.id.lista)

        btnFavorite = view.findViewById(R.id.favorite_btn)
        btnGoOut    = view.findViewById(R.id.go_out_btn)

        btnFavorite?.setOnClickListener { getIsFavorite() }
        btnGoOut?.setOnClickListener    { openBrowser() }

        setInfo(movie)

    }

    private fun openBrowser() {

        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/#q=${movie.title} movie ${movie.releaseDate?.formatDateToYear()}")))
    }

    private fun getIsFavorite() {

        if (!movie.isFavorite)
            favoriteMovie(movie)

        else
            unFavoriteMovie(movie)

    }

    private fun unFavoriteMovie(movie:Movie) {

        btnFavorite?.imageParameter?.setImageResource(R.drawable.ic_favorite_border)
        btnFavorite?.textParameter?.text = "Favorite"

        movie?.isFavorite = false

        if (isFavorite()){
            val index = favoriteMovieList.indexOfFirst { it.id == movie.id }
            if(index != -1)
                favoriteMovieList.removeAt(index)
        }
    }

    private fun favoriteMovie(movie:Movie) {

        btnFavorite?.imageParameter?.setImageResource(R.drawable.ic_favorite)
        btnFavorite?.textParameter?.text = "Favorited"

        movie.isFavorite = true

        if (!isFavorite()){ favoriteMovieList.add(movie) }
    }

    /**
     * Verify if the movie is favorite
     */
    private fun isFavorite(): Boolean {

        for (movieinList in favoriteMovieList) {
            if (movieinList.id == movie.id) {
                return true
                break
            }
        }
        return false
    }

    /**
     * Save's the list of favorite movies in the shared preferences
     */
    private fun saveFavoriteMovies(favoriteMovieList: ArrayList<Movie>) {

        // Transform the movie into an Json to save in shared preferences
        var favoritedList = gson?.toJson(favoriteMovieList)

        editor?.putString("favoriteMovieList", favoritedList)
        editor?.commit()
    }
//
//    private fun configureToolbar(view: View, title:String) {
//        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
//        setupToolbar(view)
//        toolbar.let {
//            (activity as AppCompatActivity).setSupportActionBar(it)
//            it.title = title
//            it.setNavigationIcon(R.drawable.ic_arrow_back)
//            it.setNavigationOnClickListener { fragmentManager?.popBackStackImmediate() }
//        }
//    }

    /**
     * Convert's the movie in a Json,
     * save on shared preferences and also open de Movie Detail Fragment
     */
    private fun openDetailMovieFragment(movie:Movie?){

        editor = context?.getPreferencesEditor()

        // Transform the movie into an Json to save in shared preferences
        var movieJson = gson?.toJson(movie)

        editor?.putString("movieJson",movieJson)
        editor?.commit()

        //Open's the given fragment
        openFragment(MovieDetailFragment())
    }

    /**
     * Set's the movie info in the fragment
     */

    private fun setInfo(movie: Movie?) {

        val date         = movie?.releaseDate?.formatDateToYear()
        val urlImageBanner= this.resources.getString(R.string.base_url_images) + movie?.backdropPath
        val movieRate       = movie?.voteAverage?.toInt()

        //Load's the image using Picasso passing the local as parameter
        urlImageBanner.loadPicasso(movieBanner)

        movieName?.text         = movie?.title
        progressTxt?.text       = movie?.voteAverage.toString()
        movieDescript?.text     = movie?.overview
        movieReleaseDate?.text  = date

        movieRate?.let { progressBar?.circleAnimate((it * 100)/10) }

    }

    /**
     * Return a movie list from the Discover, passing page as a param for the request
     * Also its done a recursive function
     */

    private fun setAdapter() {

        recyclerList?.adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener) }

        //Set's the orientation of the list to Grid
        setGridLayout(recyclerList)
    }

    /**
     * Return a movie list from the Discover, passing page as a param for the request
     * Also its done a recursive function
     */
    private fun requestSimilarMovies(page:Int = 1,movieId:Int?){

        activity?.let {
            RequestMovies().getSimilarMovies(page, movieId).enqueue(object : retrofit2.Callback<Result> {

                override fun onResponse(call: Call<Result>, response: Response<Result>?) {

                    response?.body()?.movies?.let { movies -> listMovies.addAll(movies)
                        if (!listMovies.isEmpty())
                            recyclerList?.adapter?.notifyDataSetChanged()
                        else
                            hideSimilarMoviesLayout()
                    }
                }
                override fun onFailure(call: Call<Result>, error: Throwable) {
                    Log.i("Error: ", error.message)
                }
            })
        }
    }

    private fun hideSimilarMoviesLayout() {

        divider3.visibility      = View.GONE
        divider4.visibility      = View.GONE
        semelhantes.visibility   = View.GONE
        recyclerList?.visibility = View.GONE
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
                    requestSimilarMovies(page,movie.id!!)
                }
            }
        })
    }
}
