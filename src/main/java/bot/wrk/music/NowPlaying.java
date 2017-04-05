/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.ArrayList;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 *
 * @author Goupil
 */
public class NowPlaying implements EventListener {

    private TextChannel channel;
    private GuildMusicManager musicManager;
    private JDA jda;
    private Music music;
    private String idMessageNowPlaying;
    private NowPlayingThread npThread;
    private AudioTrack currentTrack;
    private AudioTrack oldTrack;

    public NowPlaying(TextChannel channel, GuildMusicManager musicManager, JDA jda, Music music) {
        this.channel = channel;
        this.musicManager = musicManager;
        this.jda = jda;
        this.music = music;

        idMessageNowPlaying = "";
        npThread = new NowPlayingThread(this);
    }

    public void showNowPlaying() {
        currentTrack = musicManager.player.getPlayingTrack();
        if (currentTrack != null) {
            if (!npThread.npIsWorking()) {
                sendNowPlaying();
                npThread = new NowPlayingThread(this);
                npThread.start();
            }
        } else {
            channel.sendMessage("The player is not currently playing anything!").queue();
        }
    }

    public void sendNowPlaying() {
        currentTrack = musicManager.player.getPlayingTrack();

        String title = currentTrack.getInfo().title;
        String position = getTimestamp(currentTrack.getPosition());
        String duration = getTimestamp(currentTrack.getDuration());

        String msg = String.format("**Playing:** %s\n**Time:** [%s / %s]", title, position, duration);
        channel.getManager().setTopic("**Playing:** " + title).queue();
        Message theMessage = channel.sendMessage(msg).complete();
        idMessageNowPlaying = theMessage.getId();
        try {
            channel.addReactionById(idMessageNowPlaying, "⏯").complete(true);
            channel.addReactionById(idMessageNowPlaying, "⏹").complete(true);
            channel.addReactionById(idMessageNowPlaying, "⏭").submit();
        } catch (Exception e) {
        }
    }

    public void sendNowPaused() {
        channel.getManager().setTopic("").queue();
        channel.deleteMessageById(idMessageNowPlaying).complete();
        
        idMessageNowPlaying = "";

        currentTrack = musicManager.player.getPlayingTrack();

        String title = currentTrack.getInfo().title;
        String position = getTimestamp(currentTrack.getPosition());
        String duration = getTimestamp(currentTrack.getDuration());

        String msg = String.format("**Paused:** %s\n**Time:** [%s / %s]", title, position, duration);
        channel.getManager().setTopic("**Paused:** " + title).queue();
        Message theMessage = channel.sendMessage(msg).complete();
        idMessageNowPlaying = theMessage.getId();
        try {
            channel.addReactionById(idMessageNowPlaying, "⏯").complete(true);
            channel.addReactionById(idMessageNowPlaying, "⏹").complete(true);
            channel.addReactionById(idMessageNowPlaying, "⏭").submit();
        } catch (Exception e) {
        }
    }

    public void updateNowPlaying() {
        if (!musicManager.player.isPaused()) {
            currentTrack = musicManager.player.getPlayingTrack();
            if (currentTrack != null) {
                try {
                    if (!oldTrack.getIdentifier().equals(currentTrack.getIdentifier())) {
                        channel.getManager().setTopic("").queue();
                        channel.deleteMessageById(idMessageNowPlaying).complete();
                        idMessageNowPlaying = "";
                        sendNowPlaying();
                        oldTrack = currentTrack;
                    } else {
                        oldTrack = currentTrack;
                        String title = currentTrack.getInfo().title;
                        String position = getTimestamp(currentTrack.getPosition());
                        String duration = getTimestamp(currentTrack.getDuration());
                        String msg = String.format("**Playing:** %s\n**Time:** [%s / %s]", title, position, duration);
                        channel.getMessageById(idMessageNowPlaying).complete().editMessage(msg).queue();
                    }
                } catch (Exception e) {
                    oldTrack = currentTrack;
                }
            } else {
                stopNowPlaying();
            }
        }
    }

    public void stopNowPlaying() {
        if (npThread.isAlive()) {
            if (!idMessageNowPlaying.isEmpty()) {
                channel.getManager().setTopic("").queue();
                channel.deleteMessageById(idMessageNowPlaying).complete();
                idMessageNowPlaying = "";
            }
            if (npThread.isAlive()) {
                npThread.npStop();
            }
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
        ArrayList<String> reactions = new ArrayList<>();
        reactions.add("⏯");
        reactions.add("⏹");
        reactions.add("⏭");
        if (event instanceof MessageReactionAddEvent) {
            if (!((MessageReactionAddEvent) event).getUser().equals(jda.getSelfUser())) {
                if (!idMessageNowPlaying.isEmpty() && ((MessageReactionAddEvent) event).getMessageId().equals(idMessageNowPlaying)) {
                    if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(0))
                            || ((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(1))
                            || ((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(2))) {
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(0))) {
                            if (musicManager.player.isPaused()) {
                                music.pauseMusic(false);
                                channel.getManager().setTopic("").queue();
                                channel.deleteMessageById(idMessageNowPlaying).complete();
                                idMessageNowPlaying = "";
                                sendNowPlaying();
                            } else {
                                sendNowPaused();
                                music.pauseMusic(false);
                            }
                        }
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(1))) {
                            music.stopMusic(false);
                        }
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(2))) {
                            music.skipTrack(false);
                            
                        }
                    } else {
                    }
                }
            }
        }
    }
}
