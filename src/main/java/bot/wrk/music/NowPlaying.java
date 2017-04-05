/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 *
 * @author Goupil
 */
public class NowPlaying implements EventListener {

    private TextChannel channel;
    private GuildMusicManager musicManager;
    private JDA jda;
    private String idMessageNowPlaying;
    private NowPlayingThread npThread;

    public NowPlaying(TextChannel channel, GuildMusicManager musicManager, JDA jda) {
        this.channel = channel;
        this.musicManager = musicManager;
        this.jda = jda;
        idMessageNowPlaying = "";
    }

    public void showNowPlaying() {
        AudioTrack currentTrack = musicManager.player.getPlayingTrack();
        if (currentTrack != null) {
            sendNowPlaying();
            npThread = new NowPlayingThread(this);
            npThread.start();
        } else {
            channel.sendMessage("The player is not currently playing anything!").queue();
        }
    }

    public void sendNowPlaying() {
        AudioTrack currentTrack = musicManager.player.getPlayingTrack();

        String title = currentTrack.getInfo().title;
        String position = getTimestamp(currentTrack.getPosition());
        String duration = getTimestamp(currentTrack.getDuration());

        String msg = String.format("**Playing:** %s\n**Time:** [%s / %s]", title, position, duration);
        Message theMessage = channel.sendMessage(msg).complete();
        idMessageNowPlaying = theMessage.getId();
        channel.addReactionById(idMessageNowPlaying, "⏯").queue();
        channel.addReactionById(idMessageNowPlaying, "⏹").queue();
        channel.addReactionById(idMessageNowPlaying, "⏭").queue();
    }

    public void updateNowPlaying() {
        AudioTrack currentTrack = musicManager.player.getPlayingTrack();
        if (currentTrack != null) {
            String title = currentTrack.getInfo().title;
            String position = getTimestamp(currentTrack.getPosition());
            String duration = getTimestamp(currentTrack.getDuration());

            String msg = String.format("**Playing:** %s\n**Time:** [%s / %s]", title, position, duration);
            channel.getMessageById(idMessageNowPlaying).complete().editMessage(msg).queue();
        } else {
            stopNowPlaying();
        }
    }

    public void stopNowPlaying() {
        if (npThread.isAlive()) {
            channel.deleteMessageById(idMessageNowPlaying).complete();
            idMessageNowPlaying = "";
            npThread.interrupt();
        }
    }

    public static String getTimestamp(long miliseconds) {
        int seconds = (int) (miliseconds / 1000) % 60;
        int minutes = (int) ((miliseconds / (1000 * 60)) % 60);
        int hours = (int) ((miliseconds / (1000 * 60 * 60)) % 24);

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof GenericMessageReactionEvent) {
            if (!((GenericMessageReactionEvent) event).getUser().equals(jda.getSelfUser())) {

            }
        }
    }

}
