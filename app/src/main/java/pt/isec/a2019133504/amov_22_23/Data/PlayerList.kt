package pt.isec.a2019133504.amov_22_23.Data

import androidx.lifecycle.MutableLiveData
import pt.isec.a2019133504.amov_22_23.Data.Messages.Message
import java.time.Instant
import java.time.Instant.now

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

    fun setBoardNr(BoardNr: Int) {
        for(p in _players.values)
            p.NrBoard = BoardNr
    }

    fun markBelowThreshold(threshold: Int, timeplayed : Long) {
        for (p in _players.values)
            if (!p.Lost && p.Pontos < threshold)
                p.hasLost(timeplayed)
    }

    fun allFinished(NrBoards: Int) : Boolean {
        val now = now()
        for(p in _players.values){
            if (p.Lost)
                continue

            if (p.NrBoard<NrBoards && p.Timestamp.isAfter(now))
                return false
        }
        return true
    }

    fun closeSockets(){
        for (p in _players.values)
            p.socket!!.close()
    }

    fun allLost() : Boolean {
        for(p in _players.values)
            if (!p.Lost)
                return false
        return true
    }
}