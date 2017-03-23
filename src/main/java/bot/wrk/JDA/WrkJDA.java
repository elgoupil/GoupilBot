/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.JDA;

import bot.wrk.event.WrkEvent;
import bot.wrk.game.WrkGame;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 *
 * @author Goupil
 */
public class WrkJDA extends Thread implements EventListener{
    
    private JDA jda;
    private final User owner;
    private Guild server;
    private TextChannel textChannel;
    private VoiceChannel voiceChannel;
    private WrkEvent wrkEvent;
    private WrkGame wrkGame;

    public WrkJDA(String token, String owner_id) {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
            jda.setAutoReconnect(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        owner = jda.getUserById(owner_id);
        jda.addEventListener(this);
        server = jda.getGuildById("215873323857477632");
        wrkEvent = new WrkEvent(jda);
        wrkGame = new WrkGame(jda, server);
        wrkGame.checkChannelCreated();
        wrkGame.checkGameChannel();
    }

    @Override
    public void run() {
        while (true) {
            try {
                wrkGame.checkGameChannel();
                WrkJDA.sleep(500);
                wrkGame.checkChannelCreated();
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        wrkEvent.eventWrk(event);
    }
    
}
