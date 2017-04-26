package bot;

import bot.wrk.JDA.WrkJDA;

public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        startBot();
    }

    public static void startBot() {
        Config cfg = new Config();
        boolean isReaded = cfg.readFromConfigFile("config.cfg");
        if (isReaded) {
            WrkJDA bot = new WrkJDA(cfg.getToken(), cfg.getOwnerId(), cfg.getServerId(), cfg.getTextChannelId(), cfg.getVoiceChannelId(), cfg.getPrefix(), cfg.getCommanderRole());
            bot.start();
        }else{
            System.err.println("Config file was not loaded properly, try fix config file and restart");
        }
    }

}
