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
import pt.isec.a2019133504.amov_22_23.Data.FirebaseData.MultiplayerScore
import pt.isec.a2019133504.amov_22_23.Data.Player
import pt.isec.a2019133504.amov_22_23.R
import java.time.Instant
import java.time.Instant.now
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

class MultiplayerScoresAdapter(private val data: List<MultiplayerScore>, private val context: Context) :
    BaseAdapter(){

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): MultiplayerScore {
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
        val rowView: View = inflater.inflate(R.layout.multiplayergame_item, parent, false)
        return processView(position, rowView)
    }

    private fun processView(position: Int, rowView: View) : View {
        val textViewTime = rowView.findViewById<View>(R.id.LeaderMultytime) as TextView
        val textViewPontos = rowView.findViewById<View>(R.id.LeaderMultyPontos) as TextView
        val mpScore = getItem(position)
        textViewPontos.setText(context.getString(R.string.Pontos, mpScore.TotalScore))
        textViewTime.setText(context.getString(R.string.Tempo, mpScore.Tempo))
        return rowView
    }
}