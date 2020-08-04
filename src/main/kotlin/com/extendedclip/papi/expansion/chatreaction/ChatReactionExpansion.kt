package com.extendedclip.papi.expansion.chatreaction;

import me.clip.chatreaction.ChatReaction;
import me.clip.chatreaction.ReactionAPI;
import me.clip.chatreaction.events.ReactionWinEvent;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChatReactionExpansion extends PlaceholderExpansion implements Listener, Cacheable, Configurable {

    Player getWinner;

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    @Override
    public String getAuthor() {
        return "kaliber";
    }

    @Override
    public String getIdentifier() {
        return "chatreaction";
    }

    @Override
    public String getRequiredPlugin() {
        return "ChatReaction";
    }

    @Override
    public String getVersion() {
        return "1.3.0";
    }

    @Override
    public void clear() {
        getWinner = null;
    }

    @Override
    public Map<String, Object> getDefaults() {
        Map<String, Object> config = new HashMap<>();
        config.put("time_limit", 30);
        return config;
    }

    @EventHandler
    public void onReactionWin(ReactionWinEvent event) {
        getWinner = event.getWinner();
    }

    @Override
    public String onRequest(OfflinePlayer p, String identifier) {
        if (p == null) {
            return "";
        }

        final long timeNow = Calendar.getInstance().getTimeInMillis();
        final long startTime = ReactionAPI.getStartTime();
        final long timeDifference = (Math.abs(timeNow - startTime)) / 1000;
        final int timeLimit = this.getInt("time_limit", 30);
        final Object result;

        switch (identifier.toLowerCase()) {
            case "wins":
                return String.valueOf(ReactionAPI.getWins(p));

            case "type":
                if (ReactionAPI.isStarted()) {
                    result = ChatReaction.isScrambled() ? "scramble" : "reaction";
                } else {
                    result = "none";
                }
                break;

            case "active_round":
                result = ReactionAPI.isStarted();
                break;

            case "display_word":
                result = ReactionAPI.getDisplayWord() != null ? ReactionAPI.getDisplayWord() : " ";
                break;

            case "reaction_word":
                result = ReactionAPI.getReactionWord() != null ? ReactionAPI.getReactionWord() : " ";
                break;
            case "latest_winner":
                result = getWinner != null ? getWinner.getName() : " ";
                break;


            case "start_time":
                result = ReactionAPI.getStartTime();
                break;

            case "time_in_seconds":
                if (!ReactionAPI.isStarted()) {
                    result = 0;
                    break;
                }

                result = timeDifference;
                break;

            case "time_remaining":
                if (!ReactionAPI.isStarted()) {
                    result = 0;
                    break;
                }

                result = timeLimit - timeDifference;
                break;

            default:
                return null;
        }
        return String.valueOf(result);
    }
}


