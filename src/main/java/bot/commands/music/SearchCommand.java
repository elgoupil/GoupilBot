/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.Constant;
import bot.wrk.music.Music;
import bot.wrk.music.NowPlaying;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.waiter.EventWaiter;

/**
 *
 * @author renardn
 */
public class SearchCommand extends Command {

    private Music music;
    private EventWaiter waiter;

    public SearchCommand(Music music, EventWaiter waiter) {
        this.waiter = waiter;
        this.music = music;
        this.name = "search";
        this.help = "search a song with the specified name given in argument";
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
        if (!event.getArgs().isEmpty()) {
            if (event.getGuild().getAudioManager().isConnected()) {
                String cc = "ytsearch:"+event.getArgs();
                music.loadAndPlayPlaylist(event, waiter, cc);
                if (!Constant.nowPlayingList.contains(event.getGuild().getId())) {
                    new NowPlaying(event.getGuild(), music);
                }
            } else {
                event.replyWarning(event.getMember().getAsMention() + " I'm not even connected :joy:");
            }
        } else {
            event.replyWarning(event.getMember().getAsMention() + " You need to specify an url");
        }
    }

}
