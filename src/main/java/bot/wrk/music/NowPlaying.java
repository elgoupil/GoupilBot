/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

import bot.Constant;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 *
 * @author virus
 */
public final class NowPlaying implements EventListener, AudioEventListener {

    private final Guild server;
    private Music music;
    private TextChannel channel;
    private GuildMusicManager musicManager;
    private String idMessageNowPlaying;
    private AudioTrack currentTrack;
    private AudioTrack oldTrack;
    private Boolean isPlaying;

    public NowPlaying(Guild server, Music music) {
        this.music = music;
        this.server = server;
        this.channel = null;
        run();
    }

    public void run() {
        Constant.nowPlayingList.put(server.getId(), this);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(NowPlaying.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (music.getGuildAudioPlayer(server).player.getPlayingTrack() != null) {

                if (music.getGuildAudioPlayer(server).player.isPaused()) {

                } else {

                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(NowPlaying.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Constant.nowPlayingList.remove(server.getId());
        }).start();
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
        
    }

    @Override
    public void onEvent(AudioEvent event) {
        
    }
}
