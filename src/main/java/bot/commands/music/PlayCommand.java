/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.wrk.music.Music;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import java.util.Properties;

/**
 *
 * @author renardn
 */
public class PlayCommand extends Command {

    private Music music;
    private Properties servers;

    public PlayCommand(Music music, Properties servers) {
        this.servers = servers;
        this.music = music;
        this.name = "play";
        this.help = "play a song with the specified url given in argument";
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
        if (!event.getArgs().isEmpty()) {
            if (event.getGuild().getAudioManager().isConnected()) {
                music.loadAndPlay(event);
            } else {
                event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
            }
        } else {
            event.replyWarning(event.getMember().getAsMention() + " You need to specify an url");
        }
    }

}
