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
public class ShuffleCommand extends Command {

    public ShuffleCommand() {
        this.name = "shuffle";
        this.help = "shuffle the queue";
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
        if (event.getGuild().getAudioManager().isConnected()) {
            if (!Constant.music.getGuildAudioPlayer(event.getGuild()).scheduler.getQueue().isEmpty()) {
                Constant.music.getGuildAudioPlayer(event.getGuild()).scheduler.shuffleQueue();
                event.reactSuccess();
            }else{
                event.reply("The queue is empty, there is nothing to shuffle");
            }
        }else{
            event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
        }
    }
}
