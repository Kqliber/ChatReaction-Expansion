package com.extendedclip.papi.expansion.chatreaction

import me.clip.chatreaction.ChatReaction
import me.clip.chatreaction.ReactionAPI
import me.clip.chatreaction.events.ReactionWinEvent
import me.clip.placeholderapi.expansion.Cacheable
import me.clip.placeholderapi.expansion.Configurable
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*
import kotlin.math.abs

class ChatReactionExpansion : PlaceholderExpansion(), Listener, Cacheable, Configurable {

    var getWinner: Player? = null

    override fun getAuthor(): String {
        return "kaliber"
    }

    override fun getIdentifier(): String {
        return "chatreaction"
    }

    override fun getRequiredPlugin(): String {
        return "ChatReaction"
    }

    override fun getVersion(): String {
        return "1.3.0"
    }

    override fun clear() {
        getWinner = null
    }

    override fun getDefaults(): Map<String, Any> {
        val config: MutableMap<String, Any> = HashMap()
        config["time_limit"] = 30
        return config
    }

    @EventHandler
    fun onReactionWin(event: ReactionWinEvent) {
        getWinner = event.winner
    }

    override fun onRequest(p: OfflinePlayer?, identifier: String): String? {
        if (p == null) {
            return ""
        }

        val timeNow = Calendar.getInstance().timeInMillis
        val startTime = ReactionAPI.getStartTime()
        val timeDifference = abs(timeNow - startTime) / 1000
        val timeLimit = getInt("time_limit", 30)
        lateinit var result: Any

        when (identifier.toLowerCase()) {
            "wins" -> return ReactionAPI.getWins(p).toString()
            "type" -> result = if (ReactionAPI.isStarted()) {
                if (ChatReaction.isScrambled()) "scramble" else "reaction"
            }
            else {
                "none"
            }

            "active_round" -> result = ReactionAPI.isStarted()
            "display_word" -> result = if (ReactionAPI.getDisplayWord() != null) ReactionAPI.getDisplayWord() else " "
            "reaction_word" -> result = if (ReactionAPI.getReactionWord() != null) ReactionAPI.getReactionWord() else " "
            "latest_winner" -> result = if (getWinner != null) getWinner!!.name else " "
            "start_time" -> result = ReactionAPI.getStartTime()
            "time_in_seconds" -> result = if (!ReactionAPI.isStarted()) {
                0
            }
            else {
                timeDifference
            }
            "time_remaining" -> {
                result = if (!ReactionAPI.isStarted()) {
                    0
                }
                else {
                    timeLimit - timeDifference
                }
            }
            else -> return null
        }
        return result.toString()
    }
}
