package pt.isec.a2019133504.amov_22_23.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.PlayerScore
import pt.isec.a2019133504.amov_22_23.R

class PlayerScoreAdapter(private val data: List<PlayerScore>, private val context: Context) :
    BaseAdapter(){

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): PlayerScore {
        return data.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null)
            return processView(position, convertView)
        val inflater = LayoutInflater.from(context)
        val rowView: View = inflater.inflate(R.layout.listview_item_player, parent, false)
        return processView(position, rowView)
    }

    private fun processView(position: Int, rowView: View) : View {
        val textViewUser = rowView.findViewById<View>(R.id.SingleUsername) as TextView
        val textViewTime = rowView.findViewById<View>(R.id.SingleTempo) as TextView
        val textViewPontos = rowView.findViewById<View>(R.id.SinglePontos) as TextView
        val score = getItem(position)
        textViewUser.setText(context.getString(R.string.name,score.username))
        textViewPontos.setText(context.getString(R.string.Pontos, score.pontuacao))
        textViewTime.setText(context.getString(R.string.Tempo, score.Tempo))
        return rowView
    }
}