/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Conf;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import java.util.Properties;

/**
 *
 * @author renardn
 */
public class SetTextChannelCommand extends Command {

    private Properties servers;

    public SetTextChannelCommand(Properties servers) {
        this.servers = servers;
        this.name = "setTextChannel";
        this.help = "set the text channel for the server";
        this.guildOnly = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (servers.getProperty(event.getGuild().getId()) == null) {
            servers.put(event.getGuild().getId(), "");
        }
        if (event.getArgs().isEmpty()) {
            servers.replace(event.getGuild().getId(), event.getChannel().getId());
        }else{
            servers.remove(event.getGuild().getId());
        }
        Conf.writeConf(servers, "servers.properties");
    }
}
