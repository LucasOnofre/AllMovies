package onoffrice.wikimovies.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_movie_item.view.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Movie

class MovieAdapter (private val context:Context, private val movies:ArrayList<Movie>?): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    /**
     * Return a movie list from the Discover
     */
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.adapter_movie_item,parent,false)
        return ViewHolder(view)
    }

    /**
     * Return a movie list from the Discover
     */
    override fun getItemCount(): Int {
       return movies?.size!!
    }

    /**
     * Makes the bind for every item in the view
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val movie       = movies?.get(position)
        val urlImage    = context.resources.getString(R.string.base_url_images) + movie?.posterPath

        //Uses Picasso to load the web images
        Picasso.get().load(urlImage).networkPolicy(NetworkPolicy.OFFLINE).into(holder.poster)
    }

    /**
     * Create a viewHolder
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val poster  = itemView.movie_poster
    }

}
