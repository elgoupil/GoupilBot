/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 *
 * @author renardn
 */
public class BroadcastCommand extends Command{

    public BroadcastCommand() {
        this.name = "broadcast";
        this.help = "broadcast a message on all server";
        this.arguments = "[message]";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (TextChannel textChannel : guild.getTextChannels()) {
                if (textChannel.canTalk()) {
                    if (!event.getArgs().isEmpty()) {
                        textChannel.sendMessage(event.getArgs()).queue();
                    }else{
                        textChannel.sendMessage("Broadcast from "+event.getJDA().getSelfUser().getAsMention()).queue();
                    }
                    break;
                }
            }
        }
    }
}
