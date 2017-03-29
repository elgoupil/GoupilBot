package bot;

import bot.wrk.JDA.WrkJDA;

public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Config cfg = new Config();
        cfg.readFromConfigFile("config.cfg");
        startBot(cfg);
    }
    public static void startBot(Config cfg){
        WrkJDA bot = new WrkJDA(cfg.getToken(),cfg.getOwnerId(),cfg.getServerId(),cfg.getTextChannelId(),cfg.getVoiceChannelId(),cfg.getPrefix());
        bot.start();
    }
    
}
