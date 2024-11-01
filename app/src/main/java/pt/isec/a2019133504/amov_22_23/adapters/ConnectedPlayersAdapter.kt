package pt.isec.a2019133504.amov_22_23.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.isec.a2019133504.amov_22_23.Data.Player
import pt.isec.a2019133504.amov_22_23.R
import java.time.Instant
import java.time.temporal.ChronoUnit


class ConnectedPlayersAdapter (private val map: Map<String, Player>, private val context: Context) :
BaseAdapter(){
    override fun getCount(): Int {
        return map.size
    }

    override fun getItem(p0: Int): Player? {
        val keys = map.keys.toTypedArray()
        return map[keys[p0]]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null)
            return processView(position, convertView)
        val inflater = LayoutInflater.from(context)
        val rowView: View = inflater.inflate(R.layout.listview_item, parent, false)
        return processView(position, rowView)
    }

    private fun processView(position: Int, rowView: View) : View {
        val textView = rowView.findViewById<View>(R.id.tv1) as TextView
        val imageView = rowView.findViewById<View>(R.id.ivImg) as ImageView
        val player = getItem(position)
        if (player == null) return rowView
        textView.setText(player.nome)
        imageView.setImageBitmap(player.Imagem)
        return rowView
    }
}
