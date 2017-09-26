/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.Constant;
import bot.wrk.music.Music;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

/**
 *
 * @author renardn
 */
public class VolumeCommand extends Command {

    private Music music;

    public VolumeCommand(Music music) {
        this.music = music;
        this.name = "volume";
        this.help = "change the volume of the player";
        this.guildOnly = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String id = Constant.getServers().getProperty(event.getGuild().getId());
        if (id != null) {
            if (!event.getChannel().getId().equals(id)) {
                return;
            }
        }
        if (event.getGuild().getAudioManager().isConnected()) {
            if (!event.getArgs().isEmpty()) {
                try {
                    boolean ok = music.getGuildAudioPlayer(event.getGuild()).scheduler.changeVolume(Integer.parseInt(event.getArgs()));
                    if (!ok) {
                        event.replyWarning(event.getMember().getAsMention() + "Usage : `volume 1 - 150`");
                    }else{
                        event.reactSuccess();
                    }
                } catch (Exception e) {
                    event.replyWarning(event.getMember().getAsMention() + "Usage : `volume 1 - 150`");
                }
            } else {
                event.reply("Volume: "+music.getGuildAudioPlayer(event.getGuild()).scheduler.getVolume());
            }
        } else {
            event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
        }

    }
}