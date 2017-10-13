/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.Constant;
import bot.wrk.music.GuildMusicManager;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.ArrayList;

/**
 *
 * @author renardn
 */
public class QueueCommand extends Command {

    public QueueCommand() {
        this.name = "queue";
        this.help = "display the current queue";
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
        if (event.getGuild().getAudioManager().isConnected()) {
            GuildMusicManager musicManager = Constant.music.getGuildAudioPlayer(event.getGuild());
            if (musicManager.player.getPlayingTrack() != null) {
                ArrayList<AudioTrack> queue = new ArrayList<>(musicManager.scheduler.getQueue());
                String message = "Current queue:\n";
                if (queue.size() > 4) {
                    message += "\n 1.`" + musicManager.player.getPlayingTrack().getInfo().title + "`";
                    for (int i = 0; i < 4; i++) {
                        message += "\n" + (i + 2) + ".`" + queue.get(i).getInfo().title + "`";
                    }
                    message += "\n\nAnd `" + (queue.size() - 4) + "` more...";
                    event.reply(message);
                } else if (queue.isEmpty()) {
                    event.replyWarning("The queue is empty");
                } else if (queue.size() <= 4) {
                    message += "\n 1.`" + musicManager.player.getPlayingTrack().getInfo().title + "`";
                    for (int i = 0; i < queue.size(); i++) {
                        message += "\n" + (i + 2) + ".`" + queue.get(i).getInfo().title + "`";
                    }
                    event.reply(message);
                }
            }
        } else {
            event.replyError(event.getMember().getAsMention() + " I'm not even connected :joy:");
        }
    }
}
