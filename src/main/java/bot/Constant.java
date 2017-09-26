/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import bot.wrk.WrkBot;
import bot.wrk.music.Music;
import bot.wrk.music.NowPlaying;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.dv8tion.jda.core.JDA;

/**
 *
 * @author virus
 */
public class Constant {
    
    public static Map<String, NowPlaying> nowPlayingList = Collections.synchronizedMap(new HashMap<>());
    
    public static WrkBot bot;
    
    public static JDA jda;
    
    public static Music music;
    
    public static void writeServers(Properties p){
        Conf.writeConf(p, "servers.properties");
    }
    
    public static void writeConf(Properties p){
        Conf.writeConf(p, "conf.properties");
    }
    
    public static Properties getServers(){
        return Conf.readConf("servers.properties");
    }
    
    public static Properties getConf(){
        return Conf.readConf("conf.properties");
    }
    
}
