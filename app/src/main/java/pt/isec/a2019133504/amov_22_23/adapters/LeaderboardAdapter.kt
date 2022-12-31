package pt.isec.a2019133504.amov_22_23.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.isec.a2019133504.amov_22_23.Data.Player
import pt.isec.a2019133504.amov_22_23.R

class LeaderboardAdapter(data: ArrayList<Player>, context: Context) :
    ArrayAdapter<Player>(context, pt.isec.a2019133504.amov_22_23.R.layout.leaderboard_item, data){
    private var dataSet: ArrayList<Player>?
    init {
        dataSet = data
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val rowView: View = inflater.inflate(R.layout.leaderboard_item, parent, false);
        val textViewUsername = rowView.findViewById<View>(R.id.leaderboardusername) as TextView
        val textViewTime = rowView.findViewById<View>(R.id.leaderboardusertime) as TextView
        val textViewPontos = rowView.findViewById<View>(R.id.leaderboarduserpontos) as TextView
        val imageView = rowView.findViewById<View>(R.id.leaderboardimg) as ImageView

        val idk : Player = dataSet!!.get(position)
        if (idk == null)
            return rowView
        //TODO time
        textViewUsername.setText(idk.nome)
        textViewPontos.setText("Pontos:" +idk.Pontos)
        textViewTime.setText("Time:" +idk.Timestamp.toString())
        imageView.setImageBitmap(idk.Imagem)
        return rowView
    }
}