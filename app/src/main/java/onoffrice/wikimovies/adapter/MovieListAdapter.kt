package onoffrice.wikimovies.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_movie_item.view.*
import kotlinx.android.synthetic.main.adapter_movie_list.view.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Movie
import android.support.v7.widget.LinearLayoutManager
import onoffrice.wikimovies.model.MovieListGenre

class MovieListAdapter (private val context:Context, private val moviesList:ArrayList<MovieListGenre>): RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    /**
     * Return a movie list from the Discover
     */
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_movie_list,parent,false)
        return ViewHolder(view)
    }

    /**
     * Return a movie list from the Discover
     */
    override fun getItemCount(): Int {
        return moviesList.size
    }

    /**
     * Makes the bind for every item in the view
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.HORIZONTAL
        holder.list.layoutManager = llm

        val movieGenre= moviesList.get(position)

        holder.title.text   = movieGenre.genre?.name
        holder.list.adapter = MoviesAdapter(context, movieGenre.movies)
    }

    /**
     * Create a viewHolder
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.list_title!!
        val list  = itemView.movie_item_list!!
    }

    class MoviesAdapter (private val context:Context, private val movies:List<Movie>): RecyclerView.Adapter<MoviesAdapter.ViewHolderItem>() {

        /**
         * Return a movie list from the Discover
         */
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MoviesAdapter.ViewHolderItem {
            val view = LayoutInflater.from(context).inflate(R.layout.adapter_movie_item,parent,false)
            return MoviesAdapter.ViewHolderItem(view)
        }

        /**
         * Return a movie list from the Discover
         */
        override fun getItemCount(): Int {
            return movies.size
        }

        /**
         * Makes the bind for every item in the view
         */
        override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
            val movie = movies.get(position)
            val urlImage = context.resources.getString(R.string.base_url_images) + movie.posterPath
            Picasso.get().load(urlImage).into(holder.poster)
        }


        /**
         * Create a viewHolder
         */
        class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val poster = itemView.movie_poster!!
        }

    }


}
