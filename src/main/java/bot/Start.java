package bot;

import bot.wrk.WrkBot;
import java.util.Properties;

public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Properties prop = Conf.readConf("conf.properties");
        WrkBot bot = new WrkBot(prop);
    }

}
