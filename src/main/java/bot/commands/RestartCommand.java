/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Conf;
import bot.Constant;
import bot.wrk.WrkBot;
import bot.wrk.music.GuildMusicManager;
import bot.wrk.music.NowPlaying;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import java.util.Properties;

/**
 *
 * @author renardn
 */
public class RestartCommand extends Command {

    public RestartCommand() {
        this.name = "restart";
        this.help = "safely restart the bot";
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
        Properties prop = Conf.readConf("conf.properties");
        new WrkBot(prop);
    }
}
