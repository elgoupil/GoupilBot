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
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        Properties p = Constant.getTextChannelConf();
        if (p.getProperty(event.getGuild().getId()) == null) {
            p.put(event.getGuild().getId(), "");
        }
        if (event.getArgs().isEmpty()) {
            p.replace(event.getGuild().getId(), event.getChannel().getId());
        }else{
            p.remove(event.getGuild().getId());
        }
        Constant.writeTextChannelConf(p);
        event.reactSuccess();
    }
}
