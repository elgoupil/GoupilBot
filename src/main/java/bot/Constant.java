/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import bot.wrk.WrkBot;
import bot.wrk.music.NowPlaying;
import java.util.Hashtable;
import java.util.Properties;
import net.dv8tion.jda.core.JDA;

/**
 *
 * @author virus
 */
public class Constant {
    
    public static Hashtable<String, NowPlaying> nowPlayingList = new Hashtable<>();
    
    public static WrkBot bot;
    
    public static JDA jda;
    
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
