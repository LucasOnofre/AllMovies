package onoffrice.wikimovies.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_movie_item.view.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Movie
import android.util.DisplayMetrics
import android.view.*




class MoviesAdapter (private val contextActivity: Activity, private val movies:ArrayList<Movie>): RecyclerView.Adapter<MoviesAdapter.ViewHolderItem>() {

    /**
     * Return a movie list from the Discover
     */
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MoviesAdapter.ViewHolderItem {
        val view = LayoutInflater.from(contextActivity).inflate(R.layout.adapter_movie_item,parent,false)
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
        val movie    = movies.get(position)
        val urlImage = contextActivity.resources.getString(R.string.base_url_images) + movie.posterPath
        Picasso.get().load(urlImage).into(holder.poster)

        getScreenSize(holder.poster)

        holder.itemView.setOnClickListener { Toast.makeText(contextActivity,movie.title, Toast.LENGTH_SHORT).show() }

    }

    /**
     * Create a viewHolder
     */
    class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val poster = itemView.movie_poster!!
    }

    fun getScreenSize(itemView: View) {
        val displayMetrics = DisplayMetrics()
        contextActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        var width  = displayMetrics.widthPixels

        itemView.movie_poster.maxWidth = (width/3)*2

    }
}