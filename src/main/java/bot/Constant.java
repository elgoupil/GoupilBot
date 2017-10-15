/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import bot.wrk.WrkBot;
import bot.wrk.music.Music;
import bot.wrk.music.NowPlaying;
import com.jagrosh.jdautilities.waiter.EventWaiter;
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
    
    public static void writeTextChannelConf(Properties p){
        Conf.writeConf(p, "textChannels.properties");
    }
    
    public static void writeConf(Properties p){
        Conf.writeConf(p, "conf.properties");
    }
    
    public static void writeVoiceChannelConf(Properties p){
        Conf.writeConf(p, "voiceChannels.properties");
    }
    
    public static Properties getTextChannelConf(){
        return Conf.readConf("textChannels.properties");
    }
    
    public static Properties getConf(){
        return Conf.readConf("conf.properties");
    }
    
    public static Properties getVoiceChannelConf(){
        return Conf.readConf("voiceChannels.properties");
    }
    
}
