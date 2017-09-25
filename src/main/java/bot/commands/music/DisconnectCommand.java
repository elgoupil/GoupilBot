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
public class DisconnectCommand extends Command {

    private Music music;
    private Properties servers;

    public DisconnectCommand(Music music, Properties servers) {
        this.servers = servers;
        this.music = music;
        this.name = "disconnect";
        this.help = "disconnect the bot from the voice channel";
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
        int res = music.disconnectFromVoiceChat(event.getGuild().getAudioManager());
        if (res == 0) {
            event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
        } else {
            event.replySuccess("Disconnected");
        }
    }
}
