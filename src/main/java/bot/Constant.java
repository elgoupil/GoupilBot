/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import bot.wrk.WrkBot;
import bot.wrk.music.Music;
import bot.wrk.music.NowPlaying;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 *
 * @author virus
 */
public class Constant {

    public static Map<String, NowPlaying> nowPlayingList = Collections.synchronizedMap(new HashMap<>());

    public static Map<String, List<VoiceChannel>> gameList = Collections.synchronizedMap(new HashMap<>());

    public static WrkBot bot;

    public static JDA jda;

    public static Music music;

    public static EventWaiter waiter;

    public static String lambdaMusicIconUrl = "https://cdn0.iconfinder.com/data/icons/square-logo-buttons/512/apple-music-2-128.png";

    public static String youtubeThumbnailUrl = "http://img.youtube.com/vi/%s/mqdefault.jpg";

    public static String bandcampIconUrl = "https://cdn0.iconfinder.com/data/icons/square-logo-buttons/512/bandcamp-1-128.png";

    public static String vimeoIconUrl = "https://cdn2.iconfinder.com/data/icons/micon-social-pack/512/vimeo-128.png";

    public static String twitchIconUrl = "https://cdn1.iconfinder.com/data/icons/micon-social-pack/512/twitch-128.png";

    public static String soundcloudIconUrl = "https://cdn2.iconfinder.com/data/icons/micon-social-pack/512/soundcloud-128.png";

    public static String prefix = getConf().getProperty("prefix");

    public static String ownerId = getConf().getProperty("ownerId");
        
    public static String adminRole = getConf().getProperty("adminRole");
    
    public static String gameBotExampleUrl = "https://thumbs.gfycat.com/EasygoingImpressiveHake-size_restricted.gif";

    public static void writeTextChannelConf(Properties p) {
        Conf.writeConf(p, "textChannels.properties");
    }

    public static void writeConf(Properties p) {
        Conf.writeConf(p, "conf.properties");
    }

    public static void writeVoiceChannelConf(Properties p) {
        Conf.writeConf(p, "voiceChannels.properties");
    }

    public static Properties getTextChannelConf() {
        return Conf.readConf("textChannels.properties");
    }

    public static Properties getConf() {
        return Conf.readConf("conf.properties");
    }

    public static Properties getVoiceChannelConf() {
        return Conf.readConf("voiceChannels.properties");
    }

}
