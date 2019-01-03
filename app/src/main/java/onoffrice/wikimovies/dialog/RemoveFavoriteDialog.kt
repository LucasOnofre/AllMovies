package onoffrice.wikimovies.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import onoffrice.wikimovies.R

class RemoveFavoriteDialog : DialogFragment() {

    var btnNegative: Button? = null
    var btnPositive: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var view: View? = activity?.layoutInflater?.inflate(R.layout.remove_favorite_dialog, null)

        btnNegative = view?.findViewById(R.id.btnNegative)
        btnPositive = view?.findViewById(R.id.btnPositive)


        var alertDialog = AlertDialog.Builder(context!!)
        alertDialog.setView(view)

        return alertDialog.create()
    }
}