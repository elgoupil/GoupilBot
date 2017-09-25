/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.wrk.music.GuildMusicManager;
import bot.wrk.music.Music;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import java.util.Properties;

/**
 *
 * @author renardn
 */
public class StopCommand extends Command{
    
    private Music music;
    private Properties servers;

    public StopCommand(Music music, Properties servers) {
        this.servers = servers;
        this.music = music;
        this.name = "stop";
        this.help = "stop the player and clear the queue";
        this.guildOnly = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String id = servers.getProperty(event.getGuild().getId());
        if (id != null) {
            if (!event.getChannel().getId().equals(id)) {
                return;
            }
        }
        if (event.getGuild().getAudioManager().isConnected()) {
            GuildMusicManager manager = music.getGuildAudioPlayer(event.getGuild());
            if (manager.player.getPlayingTrack() != null) {
                manager.scheduler.getQueue().clear();
                manager.player.stopTrack();
                event.replySuccess("Stopped");
            }else{
                event.replyWarning(event.getMember().getAsMention()+" I'm not playing tho");
            }
        }else{
            event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
        }
    }
}
