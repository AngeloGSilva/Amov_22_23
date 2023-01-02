package pt.isec.a2019133504.amov_22_23.Data

import android.os.Build
import androidx.lifecycle.MutableLiveData
import pt.isec.a2019133504.amov_22_23.Data.Messages.Message
import java.time.Instant

class PlayerList {
    private var _players : MutableMap<String, Player> = mutableMapOf()
    val players : Map<String, Player>
        get() = _players
    val playersLD = MutableLiveData(players)

    fun addPlayer(player: Player) {
        _players.put(player.uid, player)
        playersLD.postValue(players)
    }

    fun sendToAll(message : Message) {
        for(p in _players.values)
            p.sendMessage(message)
    }

    fun setTimestap(timestamp: Instant) {
        for(p in _players.values)
            p.Timestamp = timestamp
    }


}