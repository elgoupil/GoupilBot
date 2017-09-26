/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Conf;
import bot.Constant;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

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
        if (Constant.getServers().getProperty(event.getGuild().getId()) == null) {
            Constant.getServers().put(event.getGuild().getId(), "");
        }
        if (event.getArgs().isEmpty()) {
            Constant.getServers().replace(event.getGuild().getId(), event.getChannel().getId());
        }else{
            Constant.getServers().remove(event.getGuild().getId());
        }
        Conf.writeConf(Constant.getServers(), "Constant.getServers().properties");
    }
}
