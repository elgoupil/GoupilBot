/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Constant;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import java.util.Properties;

/**
 *
 * @author renardn
 */
public class SetTextChannelCommand extends Command {

    public SetTextChannelCommand() {
        this.name = "setTextChannel";
        this.help = "set the text channel for the server";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.requiredRole = Constant.adminRole;
    }

    @Override
    protected void execute(CommandEvent event) {
        Properties p = Constant.getTextChannelConf();
        if (p.getProperty(event.getGuild().getId()) == null) {
            p.put(event.getGuild().getId(), "");
        }
        if (event.getArgs().isEmpty()) {
            p.replace(event.getGuild().getId(), event.getChannel().getId());
        } else {
            try {
                Constant.getVoiceChannelConf().getProperty(event.getGuild().getId());
                p.remove(event.getGuild().getId());
            } catch (Exception e) {
                event.replyError("Please unset the voice channel first.\nSee `"+Constant.prefix+"help setVoiceChannel`");
            }
        }
        Constant.writeTextChannelConf(p);
        event.reactSuccess();
    }
}
