package onoffrice.wikimovies.adapter

import android.app.Activity
import android.content.res.Configuration
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_movie_item.view.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Movie
import android.util.DisplayMetrics
import android.view.*

class MoviesAdapter (private val contextActivity: Activity, private val movies:ArrayList<Movie>): RecyclerView.Adapter<MoviesAdapter.ViewHolderItem>() {

    private object VIEW_TYPES {
        const val header = 1
        const val normal = 0
    }

    override fun getItemViewType(position: Int): Int {

        return if (movies.get(position).isHeader)
            VIEW_TYPES.header
        else
            VIEW_TYPES.normal
    }

    /**
     * Return a movie list from the Discover
     */
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MoviesAdapter.ViewHolderItem {

        var view:View = LayoutInflater.from(contextActivity).inflate(R.layout.adapter_movie_item,parent,false)

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
        val movie         = movies.get(position)
        val urlImage      = contextActivity.resources.getString(R.string.base_url_images) + movie.posterPath

        Picasso.get().load(urlImage).into(holder.poster)

        if (!movie.isHeader) {
            getScreenSize(holder.poster)
        }

        //Listener that when clicked goes to the movie Info
        holder.itemView.setOnClickListener { Toast.makeText(contextActivity, movie.title, Toast.LENGTH_SHORT).show() }
    }

    /**
     * Create a viewHolder
     */
    class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val poster  = itemView.movie_poster!!
    }

    /**
     * Set's the widht and heigh of the movie poster according to the screen size
     */
    private fun getScreenSize(itemView: View) {

        var orientation = contextActivity.resources.configuration.orientation

        val displayMetrics = DisplayMetrics()
        contextActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width   = displayMetrics.widthPixels
        var height  = displayMetrics.heightPixels

        itemView.movie_poster.maxWidth  = (width/3)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE){

            itemView.movie_poster.layoutParams.height = (height * 3) /5

        }else
            itemView.movie_poster.layoutParams.height = (height * 2) /7
    }
}