package pt.isec.a2019133504.amov_22_23.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.isec.a2019133504.amov_22_23.Data.Player
import pt.isec.a2019133504.amov_22_23.R
import java.time.Instant
import java.time.Instant.now
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

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

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null)
            return processView(position, convertView)
        val inflater = LayoutInflater.from(context)
        val rowView: View = inflater.inflate(R.layout.leaderboard_item, parent, false)
        return processView(position, rowView)
    }

    private fun processView(position: Int, rowView: View) : View {
        val textViewUsername = rowView.findViewById<View>(R.id.leaderboardusername) as TextView
        val textViewTime = rowView.findViewById<View>(R.id.leaderboardusertime) as TextView
        val textViewPontos = rowView.findViewById<View>(R.id.leaderboarduserpontos) as TextView
        val imageView = rowView.findViewById<View>(R.id.leaderboardimg) as ImageView
        val player = getItem(position)
        if (player == null) return rowView
        textViewUsername.setText(player.nome)
        imageView.setImageBitmap(player.Imagem)
        textViewPontos.setText(context.getString(R.string.Pontos, player.Pontos))
        if (player.Lost)
            textViewTime.setText(context.getString(R.string.lost))
        else{
            val tempo = now().until(player.Timestamp, ChronoUnit.SECONDS)
            textViewTime.setText(context.getString(R.string.Tempo,  if (tempo>=0) tempo else 0 ))
        }
        return rowView
    }
}