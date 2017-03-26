/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

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
        
    }

}
