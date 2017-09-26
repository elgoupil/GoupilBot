package bot;

import bot.wrk.WrkBot;

public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Constant.bot = new WrkBot(Constant.getConf());
    }

}
