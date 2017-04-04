/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 *
 * @author Goupil
 */
public class NowPlaying extends Thread {

    private TextChannel channel;
    private GuildMusicManager musicManager;

    public NowPlaying(TextChannel channel, GuildMusicManager musicManager) {
        this.channel = channel;
        this.musicManager = musicManager;
    }

    @Override
    public void run() {
        while (true) {
            try {
                NowPlaying.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }
    
    public void showNowPlaying(){
        String msg;
        AudioTrack currentTrack = musicManager.player.getPlayingTrack();
        if (currentTrack != null){
            String title = currentTrack.getInfo().title;
            String position = getTimestamp(currentTrack.getPosition());
            String duration = getTimestamp(currentTrack.getDuration());

            msg = String.format("**Playing:** %s\n**Time:** [%s / %s]",
                    title, position, duration);
        }else{
            msg = "The player is not currently playing anything!";
        }
        channel.sendMessage(msg).queue();
    }
    
    public static String getTimestamp(long miliseconds){
        int seconds = (int) (miliseconds / 1000) % 60;
        int minutes = (int) ((miliseconds / (1000 * 60)) % 60);
        int hours = (int) ((miliseconds / (1000 * 60 * 60)) % 24);
        
        if(hours > 0){
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }else{
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

}
