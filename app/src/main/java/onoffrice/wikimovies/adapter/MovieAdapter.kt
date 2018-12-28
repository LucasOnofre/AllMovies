package onoffrice.wikimovies.adapter

import android.app.Activity
import android.content.res.Configuration
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.adapter_movie_item.view.*
import onoffrice.wikimovies.R
import onoffrice.wikimovies.extension.getScreenSize
import onoffrice.wikimovies.extension.loadPicasso
import onoffrice.wikimovies.model.Movie

interface MovieInterface{
    fun onMovieSelected(movie:Movie?)
}



class MoviesAdapter (private val contextActivity: Activity, private val movies:ArrayList<Movie>, private val listener:MovieInterface) : RecyclerView.Adapter<MoviesAdapter.ViewHolderItem>() {

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
        val movie = movies[position]
        val urlImage = contextActivity.resources.getString(R.string.base_url_images) + movie.posterPath

        // Load's the image using picasso and open in an ImageView parameter
        urlImage.loadPicasso(holder.poster)

        if (!movie.isHeader) {
            getScreenSize(holder.poster)
        }

        //Listener that when clicked goes to the detail Movie
        holder.itemView.setOnClickListener { listener?.onMovieSelected(movie) }


        holder.itemView.setOnLongClickListener {
            val dropDownMenu = PopupMenu(contextActivity, holder.itemView)
            if (movie.isFavorite) {

                dropDownMenu.menuInflater.inflate(R.menu.unfavorite_fragment_menu, dropDownMenu.menu)
                dropDownMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.unfavorite -> {
                            listener?.onMovieSelected(movie)
                            true}

                        else -> {false}
                    }
                }
            } else {
                dropDownMenu.menuInflater.inflate(R.menu.favorite_fragment_menu, dropDownMenu.menu)
                dropDownMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.favoritar -> {
                            listener?.onMovieSelected(movie)
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
            }
            dropDownMenu.show()
            true
        }
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

        //Get's the screen size and return a triple
        var (orientation, width, height) = contextActivity.getScreenSize()

        itemView.movie_poster.maxWidth  = (width / 3)

        if (orientation == Configuration.ORIENTATION_LANDSCAPE){

            itemView.movie_poster.layoutParams.height = (height * 3) / 5

        }else
            itemView.movie_poster.layoutParams.height = (height * 2) / 7
    }
}