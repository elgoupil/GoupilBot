/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Constant;
import bot.wrk.music.GuildMusicManager;
import bot.wrk.music.NowPlaying;
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
        this.aliases = new String[]{"sd"};
        this.help = "safely shuts off the bot";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        for (NowPlaying np : Constant.nowPlayingList.values()) {
            GuildMusicManager gm = Constant.music.getGuildAudioPlayer(np.server);
            Constant.music.disconnectFromVoiceChat(np.server.getAudioManager());
            gm.scheduler.clearQueue();
            gm.player.stopTrack();
        }
        while (!Constant.nowPlayingList.isEmpty()) {
        }
        event.reactWarning();
        event.getJDA().shutdown();
        System.exit(0);
    }
    
}
