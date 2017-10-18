/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.Constant;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.entities.ChannelType;

/**
 *
 * @author renardn
 */
public class SearchCommand extends Command {

    private EventWaiter waiter;

    public SearchCommand(EventWaiter waiter) {
        this.waiter = waiter;
        this.name = "search";
        this.help = "search a song with the specified name given in argument";
        this.arguments = "[name of the music to search]";
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
        if (!event.getArgs().isEmpty()) {
            if (event.getGuild().getAudioManager().isConnected()) {
                String cc = "ytsearch:" + event.getArgs();
                Constant.music.loadAndPlaySearch(event, waiter, cc);
            } else {
                event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
            }
        } else {
            event.replyWarning(event.getMember().getAsMention() + " You need to specify an url");
        }
    }

}
