/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.JDA;

import bot.wrk.event.WrkEvent;
import java.util.HashMap;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
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
    HashMap<Guild, VoiceChannel> channelByGuild;
    private Guild server;
    private WrkEvent wrkEvent;

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
        checkChannelCreated();
        checkGameChannel();
    }

    public VoiceChannel createChannel(String name) {
        server.getController().createVoiceChannel(name).setBitrate(66666).complete();
        return server.getVoiceChannels().get(server.getVoiceChannels().size() - 1);
    }

    public void checkGameChannel() {
        boolean isExist = true;
        for (Member member : server.getMembers()) {
            try {
                if (member.getVoiceState().getChannel().equals(jda.getVoiceChannelById("294226631654899712"))) {
                    String gameName = member.getGame().getName();
                    if (gameName != null) {
                        for (VoiceChannel voiceChannel : server.getVoiceChannels()) {
                            if (voiceChannel.getName().equals(gameName) && voiceChannel.getBitrate() == 66666) {
                                moveChannel(member, voiceChannel);
                                isExist = true;
                                break;
                            } else {
                                isExist = false;
                            }
                        }
                        if (!isExist) {
                            moveChannel(member, createChannel(gameName));
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void moveChannel(Member member, VoiceChannel channel) {
//        while (server.getMemberById(member.getUser().getId()).getVoiceState().getChannel() != channel) {  
//            System.out.println("Boucle");
        server.getController().moveVoiceMember(member, channel).complete();
//        }
    }

    public void checkChannelCreated() {
        for (VoiceChannel voiceChannel : server.getVoiceChannels()) {
            if (voiceChannel.getBitrate() == 66666) {
                if (voiceChannel.getMembers().isEmpty()) {
                    voiceChannel.delete().queue();
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                checkGameChannel();
                WrkJDA.sleep(500);
                checkChannelCreated();
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        wrkEvent.eventWrk(event);
    }

}
