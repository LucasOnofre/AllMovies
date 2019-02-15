package onoffrice.wikimovies.fragment.movie_detail_fragment

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.adapter.MoviesAdapter
import onoffrice.wikimovies.custom.UserButton
import onoffrice.wikimovies.extension.*
import onoffrice.wikimovies.fragment.base_fragment.BaseFragment
import onoffrice.wikimovies.model.Movie
import onoffrice.wikimovies.model.MovieInterface
import onoffrice.wikimovies.model.MovieLongClickInterface
import onoffrice.wikimovies.model.MovieVideoInfo


class MovieDetailFragmentView : BaseFragment(), MovieDetailFragmentContract.View {



    private var page                                           = 1
    private var editor              :SharedPreferences.Editor? = null
    private var toolbar             :CollapsingToolbarLayout?  = null
    private var btnGoOut            :UserButton?               = null
    private var isLoading           :Boolean                   = false
    private var movieName           :TextView?                 = null
    private var progressBar         :ProgressBar?              = null
    private var progressTxt         :TextView?                 = null
    private var btnFavorite         :UserButton?               = null
    private var preferences         :SharedPreferences?        = null
    private var movieBanner         :ImageView?                = null
    private var recyclerList        :RecyclerView?             = null
    private var movieDescript       :TextView?                 = null
    private var movieReleaseDate    :TextView?                 = null


    //Initializations
    private var gson                :Gson?                        = Gson()
    private var movie               :Movie                        = Movie()
    private var presenter           :MovieDetailFragmentPresenter = MovieDetailFragmentPresenter()
    private var listMovies          :ArrayList<Movie>             = ArrayList()
    private var favoriteMovieList   :ArrayList<Movie>             = ArrayList()

    /**
     * Implementing interface to handle the click on the movie
     */
    private val movieClickListener = object: MovieInterface {
        override fun onMovieSelected(movie: Movie?) {
            openDetailMovieFragment(movie)
        }
    }

    private val movieLongClicListener = object : MovieLongClickInterface {
        override fun onMovieLongClickSelected(movie: Movie?) {

            val dropDownMenu = PopupMenu(context!!,view!!)

            dropDownMenu.menuInflater.inflate(R.menu.favorite_fragment_menu, dropDownMenu.menu)

            setMenuItemForLongClick(movie!!, dropDownMenu)

            dropDownMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.unFavorite -> {
                        Toast.makeText(context, "Deletado", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.favorite -> {
                        Toast.makeText(context, "Favoritado", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }

            dropDownMenu.show()

        }
        /**
         * Get's the menu item and shows only the right one according to the movie favorite status
         */
        fun setMenuItemForLongClick(movie: Movie, dropDownMenu: PopupMenu) {

            var menuItem: MenuItem? = if (movie.isFavorite) {
                dropDownMenu.menu.findItem(R.id.favorite)

            } else {
                dropDownMenu.menu.findItem(R.id.unFavorite)
            }
            menuItem?.isVisible = false
            menuItem?.isEnabled = false
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        preferences = context?.getPreferences()
        editor      = context?.getPreferencesEditor()

        getSelectedMovie()
        setUpViews(view)
        setToolbarGoBackArrow(view, movie.title.toString())
        setAdapter()
        setInfiniteScroll()

        presenter.bindTo(this)
        presenter.requestVideosFromMovie(movie.id!!)
        presenter.requestSimilarMovies(movieId = movie.id!!)

        return view
    }

    /**
     * Destroy the connection with the presenter
     */
    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()

    }
    /**
     * Update the movie Trailer with the result of the request
     */
    override fun updateMovieVideoPath(videoInfo: MovieVideoInfo) {

        movie.trailerVideo = videoInfo.key

    }

    override fun onResponseErrorTrailer(error: Throwable) {

        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show()
    }


    override fun updateFavoriteList(movies: ArrayList<Movie>) {

        listMovies.addAll(movies)

        if (!listMovies.isEmpty())
            recyclerList?.adapter?.notifyDataSetChanged()

        else
            hideSimilarMoviesLayout()
    }

    override fun onResponseError(error: Throwable) {

        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show()
        Log.i("Request: ", error.message)
    }

    /**
     * Get's the favorite's list on the Sharef Preferences
     */
    override fun onResume() {
        super.onResume()

        val json = preferences?.getPreferenceKey("favoriteMovieList")
        json?.parseJson<Array<Movie>>()?.let {
            favoriteMovieList = it.toCollection(ArrayList())
            getFavorites()
        }
    }

    /**
     * Save's the favorite movie list
     */
    override fun onPause() {
        super.onPause()
        saveFavoriteMovies(favoriteMovieList)
    }
    /**
     * Implementing interface to handle the click on the movie
     */
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
        gson?.fromJson(movieJson, Movie::class.java)?.let { movie = it }
    }

    /**
     * Set's the views and the progress bar
     */
    private fun setUpViews(view: View) {

        movieBanner         = view.findViewById(R.id.movieBanner)
        movieDescript       = view.findViewById(R.id.movieDescript)
        movieReleaseDate    = view.findViewById(R.id.movie_release_date)
        progressBar         = view.findViewById(R.id.circle_progress)
        progressTxt         = view.findViewById(R.id.progress_nota)
        recyclerList        = view.findViewById(R.id.lista)
        toolbar             = view.findViewById(R.id.collapsing_toolbar)

        btnFavorite = view.findViewById(R.id.favorite_btn)
        btnGoOut    = view.findViewById(R.id.go_out_btn)

        btnFavorite?.setOnClickListener { getIsFavorite() }
        btnGoOut?.setOnClickListener    { openTrailer() }

        setInfo(movie)

    }

    private fun openTrailer() {

        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${movie.trailerVideo}")))
    }

    /**
     * Check if it's favorite
     */
    private fun getIsFavorite() {

        if (!movie.isFavorite)
            favoriteMovie(movie)

        else
            unFavoriteMovie(movie)
    }

    private fun unFavoriteMovie(movie:Movie) {

        btnFavorite?.imageParameter?.setImageResource(R.drawable.ic_favorite_border)
        btnFavorite?.textParameter?.text = "Favorite"


        if (isFavorite()){
            presenter.unFavoriteMovie(favoriteMovieList,movie)
        }
    }

    private fun favoriteMovie(movie:Movie) {

        btnFavorite?.imageParameter?.setImageResource(R.drawable.ic_favorite)
        btnFavorite?.textParameter?.text = "Favorited"


        if (!isFavorite()){

            favoriteMovieList = presenter.favoriteMovie(favoriteMovieList,movie)

        }
    }

    /**
     * Verify if the movie is favorite
     */
    private fun isFavorite(): Boolean {

        return presenter.isFavorite(favoriteMovieList, movie)

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

    /**
     * Convert's the movie in a Json,
     * save on shared preferences and also open de Movie Detail Fragment
     */
    private fun openDetailMovieFragment(movie:Movie?){

        // Transform the movie into an Json to save in shared preferences
        var movieJson = gson?.toJson(movie)

        editor?.putString("movieJson",movieJson)
        editor?.commit()

        //Open's the given fragment
        openFragment(MovieDetailFragmentView())
    }

    /**
     * Set's the movie info in the fragment
     */

    private fun setInfo(movie: Movie?) {

        val date            = movie?.releaseDate?.formatDateToYear()
        val movieRate       = movie?.voteAverage?.toInt()
        val urlImageBanner  = this.resources.getString(R.string.base_url_images) + movie?.backdropPath

        //Load's the image using Picasso passing the local as parameter
        urlImageBanner.loadPicasso(movieBanner)

        movieName?.text         = movie?.title
        progressTxt?.text       = movie?.voteAverage.toString()
        movieDescript?.text     = movie?.overview
        movieReleaseDate?.text  = date

        movieRate?.let { progressBar?.circleAnimate(it)}

    }

    /**
     * Return a movie list from the Discover, passing page as a param for the request
     * Also its done a recursive function
     */

    private fun setAdapter() {

        recyclerList?.adapter = activity?.let { MoviesAdapter(it, listMovies, movieClickListener,movieLongClicListener) }

        //Set's the orientation of the list to Grid
        setGridLayout(recyclerList)
    }

    private fun hideSimilarMoviesLayout() {

        val params            = toolbar?.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags    = 0
        toolbar?.layoutParams = params

        layout_similar_movies.visibility = View.GONE

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
                    presenter.requestMoreSimilarMovies(page,movie.id!!)
                }
            }
        })
    }
}
