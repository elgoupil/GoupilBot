/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.Constant;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.ChannelType;

/**
 *
 * @author renardn
 */
public class DisconnectCommand extends Command {

    public DisconnectCommand() {
        this.name = "disconnect";
        this.help = "disconnect the bot from the voice channel";
        this.guildOnly = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            String id = Constant.getTextChannelConf().getProperty(event.getGuild().getId());
            if (id != null) {
                if (!event.getChannel().getId().equals(id)) {
                    return;
                }
            }
        }
        int res = Constant.music.disconnectFromVoiceChat(event.getGuild().getAudioManager());
        if (res == 0) {
            event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
        } else {
            event.replySuccess("Disconnected");
        }
    }
}
