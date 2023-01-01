package pt.isec.a2019133504.amov_22_23.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.isec.a2019133504.amov_22_23.Data.Player
import pt.isec.a2019133504.amov_22_23.R

class LeaderboardAdapter(private val map: Map<String, Player>, private val context: Context) :
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
        val inflater = LayoutInflater.from(context)

        if (convertView == null) {
            val rowView: View = inflater.inflate(R.layout.leaderboard_item, parent, false)
            val textViewUsername = rowView.findViewById<View>(R.id.leaderboardusername) as TextView
            val textViewTime = rowView.findViewById<View>(R.id.leaderboardusertime) as TextView
            val textViewPontos = rowView.findViewById<View>(R.id.leaderboarduserpontos) as TextView
            val imageView = rowView.findViewById<View>(R.id.leaderboardimg) as ImageView
            val player = getItem(position)
            if (player == null) return rowView
            textViewUsername.setText(player.nome)
            textViewPontos.setText("Pontos:" +player.Pontos)
            textViewTime.setText("Time:" +player.Timestamp.toString())
            imageView.setImageBitmap(player.Imagem)
            return rowView
        }else {
            val textViewUsername = convertView.findViewById<View>(R.id.leaderboardusername) as TextView
            val textViewTime = convertView.findViewById<View>(R.id.leaderboardusertime) as TextView
            val textViewPontos = convertView.findViewById<View>(R.id.leaderboarduserpontos) as TextView
            val imageView = convertView.findViewById<View>(R.id.leaderboardimg) as ImageView
            val player = getItem(position)
            if (player == null) return convertView
            textViewUsername.setText(player.nome)
            textViewPontos.setText("Pontos:" +player.Pontos)
            textViewTime.setText("Time:" +player.Timestamp.toString())
            imageView.setImageBitmap(player.Imagem)
            return convertView
        }
    }
}