package bot;

import bot.wrk.JDA.WrkJDA;

public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        startBot();
    }
    public static void startBot(){
        WrkJDA bot = new WrkJDA("Mjk0MDQxODI3MDIyMDc3OTU0.C7ST6A.a-OReMrMSAJbAyywINETDnAOi0U", "203616169519742977");
        bot.start();
        
    }
    
}
