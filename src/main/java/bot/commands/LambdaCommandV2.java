/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import java.util.Properties;

/**
 *
 * @author renardn
 */
public class LambdaCommandV2 extends Command{
    
    private Properties servers;

    public LambdaCommandV2(Properties servers) {
        this.servers = servers;
        this.name = "test2";
        this.help = "summon the bot in the voice channel";
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
        event.reply("Echo");
    }
}
