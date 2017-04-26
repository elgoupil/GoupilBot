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
public class Game {

    public static VoiceChannel createChannel(String name, Guild server) {
        server.getController().createVoiceChannel(name).setBitrate(66666).complete();
        return server.getVoiceChannels().get(server.getVoiceChannels().size() - 1);
    }

    public static void checkGameChannel(JDA jda, Guild server, VoiceChannel channel) {
        boolean isExist = true;
        for (Member member : server.getMembers()) {
            try {
                if (member.getVoiceState().getChannel().equals(channel)) {
                    String gameName = member.getGame().getName();
                    if (gameName != null) {
                        for (VoiceChannel voiceChannel : server.getVoiceChannels()) {
                            if (voiceChannel.getName().equals(gameName) && voiceChannel.getBitrate() == 66666) {
                                moveChannel(member, voiceChannel, server);
                                isExist = true;
                                break;
                            } else {
                                isExist = false;
                            }
                        }
                        if (!isExist) {
                            moveChannel(member, createChannel(gameName, server), server);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public static void moveChannel(Member member, VoiceChannel channel, Guild server) {
        server.getController().moveVoiceMember(member, channel).complete();
    }

    public static void checkChannelCreated(Guild server) {
        for (VoiceChannel voiceChannel : server.getVoiceChannels()) {
            if (voiceChannel.getBitrate() == 66666) {
                if (voiceChannel.getMembers().isEmpty()) {
                    voiceChannel.delete().queue();
                }
            }
        }
    }

}
