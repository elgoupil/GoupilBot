/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.Constant;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.ChannelType;

/**
 *
 * @author renardn
 */
public class VolumeCommand extends Command {

    public VolumeCommand() {
        this.name = "volume";
        this.help = "change the volume of the player";
        this.arguments = "[1 - 150]";
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
            if (!event.getArgs().isEmpty()) {
                try {
                    boolean ok = Constant.music.getGuildAudioPlayer(event.getGuild()).scheduler.changeVolume(Integer.parseInt(event.getArgs()));
                    if (!ok) {
                        event.replyWarning(event.getMember().getAsMention() + " Usage : `volume 1 - 150`");
                    }else{
                        event.reactSuccess();
                    }
                } catch (Exception e) {
                    event.replyWarning(event.getMember().getAsMention() + " Usage : `volume 1 - 150`");
                }
            } else {
                event.reply("Volume: "+Constant.music.getGuildAudioPlayer(event.getGuild()).scheduler.getVolume());
            }
        } else {
            event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
        }

    }
}