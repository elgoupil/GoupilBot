/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class ShutdownCommand extends Command {

    public ShutdownCommand()
    {
        this.name = "shutdown";
        this.help = "safely shuts off the bot";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reactWarning();
        event.getJDA().shutdown();
        System.exit(0);
    }
    
}
