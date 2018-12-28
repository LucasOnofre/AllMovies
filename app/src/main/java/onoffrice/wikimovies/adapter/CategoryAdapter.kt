package onoffrice.wikimovies.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import onoffrice.wikimovies.R
import onoffrice.wikimovies.model.Genre
import java.util.ArrayList

interface CategoryInterface{
    fun onCategorySelected(genre: Genre?)
}


class CategoryAdapter(private var context: Context?, private var categoryList: ArrayList<Genre>? = null, private var listener:CategoryInterface): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {

            view = LayoutInflater.from(context).inflate(R.layout.category_list, parent, false)
        }

        val genreItem = this.getItem(position) as Genre

        val propTxt = view?.findViewById(R.id.category_item) as TextView
        propTxt.text = genreItem.name


        view?.setOnClickListener { Toast.makeText(context, categoryList?.get(position)?.name, Toast.LENGTH_SHORT).show()
        listener.onCategorySelected(genreItem)}

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