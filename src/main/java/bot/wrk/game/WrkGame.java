/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.game;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 *
 * @author Goupil
 */
public class WrkGame {

    private JDA jda;
    private Guild server;

    public WrkGame(JDA jda, Guild server) {
        this.jda = jda;
        this.server = server;
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

}
