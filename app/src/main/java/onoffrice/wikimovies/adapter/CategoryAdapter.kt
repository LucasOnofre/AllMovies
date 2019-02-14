package onoffrice.wikimovies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.CategoryInterface
import onoffrice.wikimovies.model.Genre
import java.util.*


class CategoryAdapter(
        private var context: Context?,
        private var categoryList: ArrayList<Genre>? = null,
        private var listener: CategoryInterface): BaseAdapter()
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {

            view = LayoutInflater.from(context).inflate(R.layout.category_list, parent, false)
        }

        val genreItem = this.getItem(position)

        val genreTitle = view?.findViewById(R.id.category_item) as TextView
        genreTitle.text        = genreItem?.name

        //Listener of the Genre list itens
        view.setOnClickListener {listener.onCategorySelected(genreItem)}

        return view
    }

    override fun getItem(position: Int): Genre? {
       return categoryList?.get(position)
}

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
      return categoryList?.size!!
    }
}