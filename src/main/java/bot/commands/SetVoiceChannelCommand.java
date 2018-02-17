/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Constant;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import java.util.Properties;

/**
 *
 * @author renardn
 */
public class SetVoiceChannelCommand extends Command {

    public SetVoiceChannelCommand() {
        this.name = "setvoicechannel";
        this.help = "set the voice channel for the server";
        this.arguments = "[id of the voice channel]";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.requiredRole = Constant.adminRole;
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            Constant.getTextChannelConf().get(event.getGuild().getId());
            Properties p = Constant.getVoiceChannelConf();
            if (p.getProperty(event.getGuild().getId()) == null) {
                p.put(event.getGuild().getId(), "");
            }
            if (!event.getArgs().isEmpty()) {
                p.replace(event.getGuild().getId(), event.getArgs());
            } else {
                p.remove(event.getGuild().getId());
            }
            Constant.writeVoiceChannelConf(p);
            event.reactSuccess();
        } catch (Exception e) {
            event.replyError("Please set a text channel first.\nSee `"+Constant.prefix+"help setTextChannel`");
        }
    }
}
