package onoffrice.wikimovies.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import onoffrice.wikimovies.R


class UserButton constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var imageParameter: ImageView
    var textParameter : TextView

    init {

        //Set's the layout parameters
        gravity     = Gravity.CENTER
        orientation = LinearLayout.VERTICAL

        //Instantiate and Set's the view's parameters
        textParameter  = TextView(context)
        imageParameter = ImageView(context)

        imageParameter.layoutParams = LinearLayout.LayoutParams(60, 60)
        textParameter .layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        textParameter .setPadding(0,5,0,0)

        //Add's the view's in the layout
        addView(imageParameter)
        addView(textParameter)

        //Get the parameters on the XML
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.UserButton)
        textParameter .setTextColor(Color.WHITE)
        //imageParameter.scaleType = ImageView.ScaleType.FIT_XY
        imageParameter.setImageDrawable(attributes.getDrawable(R.styleable.UserButton_image))
        textParameter.text = attributes.getString(R.styleable.UserButton_text)

        attributes.recycle()
    }
}


