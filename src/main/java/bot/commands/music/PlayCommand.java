/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.Constant;
import bot.wrk.music.NowPlaying;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

/**
 *
 * @author renardn
 */
public class PlayCommand extends Command {

    public PlayCommand() {
        this.name = "play";
        this.help = "play a song with the specified url given in argument";
        this.arguments = "[url]";
        this.guildOnly = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String id = Constant.getTextChannelConf().getProperty(event.getGuild().getId());
        if (id != null) {
            if (!event.getChannel().getId().equals(id)) {
                return;
            }
        }
        if (!event.getArgs().isEmpty()) {
            if (event.getGuild().getAudioManager().isConnected()) {
                Constant.music.loadAndPlay(event);
                if (id != null) {
                    if (!Constant.nowPlayingList.containsKey(event.getGuild().getId())) {
                        new NowPlaying(event.getGuild(), Constant.music);
                    }
                }
            } else {
                event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
            }
        } else {
            event.replyWarning(event.getMember().getAsMention() + " You need to specify an url");
        }
    }

}
