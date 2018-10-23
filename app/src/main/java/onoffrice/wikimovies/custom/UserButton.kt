package onoffrice.wikimovies.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import onoffrice.wikimovies.R


class UserButton constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        inflate(context, R.layout.user_button, this)

        val imageView: ImageView  = findViewById(R.id.image)
        val textView:  TextView   = findViewById(R.id.text)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.userButton)
        imageView.setImageDrawable(attributes.getDrawable(R.styleable.userButton_image))
        textView.text = attributes.getString(R.styleable.userButton_text)
        attributes.recycle()

    }

}