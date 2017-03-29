/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.event;

import bot.Start;
import bot.wrk.music.Music;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author goupil
 */
public class WrkEvent {

    public static void eventWrk(Event event, JDA jda, Music musicbot, TextChannel channel) {
        if (event instanceof MessageReceivedEvent) {
            if (((MessageReceivedEvent) event).getTextChannel().equals(channel)) {
                if (((MessageReceivedEvent) event).getAuthor() != jda.getSelfUser()) {
                    String[] command = ((MessageReceivedEvent) event).getMessage().getContent().split(" ", 2);
                    if ("play".equalsIgnoreCase(command[0])) {
                        if (command.length == 2) {
                            musicbot.loadAndPlay(((MessageReceivedEvent) event).getTextChannel(), command[1], ((MessageReceivedEvent) event).getMember());
                        } else {
                            ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `play 'url'` or `search 'name of the video on YT'`").queue();
                        }
                    }
                    if ("search".equalsIgnoreCase(command[0])) {
                        if (command.length == 2) {
                            musicbot.loadAndPlay(((MessageReceivedEvent) event).getTextChannel(), ("ytsearch:"+command[1]), ((MessageReceivedEvent) event).getMember());
                        } else {
                            ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `play 'url'` or `search 'name of the video on YT'`").queue();
                        }
                    }
                    if ("skip".equalsIgnoreCase(command[0])) {
                        musicbot.skipTrack(((MessageReceivedEvent) event).getTextChannel());
                    }
                    if ("volume".equalsIgnoreCase(command[0])) {
                        if (command.length == 2) {
                            musicbot.changeVolume(command[1], ((MessageReceivedEvent) event).getTextChannel());
                        } else {
                            ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `volume 1 - 150`").queue();
                        }
                    }
                    if ("disconnect".equalsIgnoreCase(command[0])) {
                        musicbot.disconnectChannel(((MessageReceivedEvent) event).getTextChannel());
                    }
                    if ("clear".equalsIgnoreCase(command[0])) {
                        musicbot.clearQueue(((MessageReceivedEvent) event).getTextChannel());
                    }
                    if ("shuffle".equalsIgnoreCase(command[0])) {
                        musicbot.shuffleQueue(((MessageReceivedEvent) event).getTextChannel());
                    }
                    if ("queue".equalsIgnoreCase(command[0])) {
                        musicbot.showQueue(((MessageReceivedEvent) event).getTextChannel());
                    }
                    if ("summon".equalsIgnoreCase(command[0])) {
                        musicbot.connectToVoiceChannel(((MessageReceivedEvent) event).getGuild().getAudioManager(), ((MessageReceivedEvent) event).getMember(), ((MessageReceivedEvent) event).getTextChannel());
                        musicbot.changeVolume("15", ((MessageReceivedEvent) event).getTextChannel());
                    }
                    if ("help".equalsIgnoreCase(command[0])) {
                        ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " Dat Help\n Ok :ok_hand:").queue();
                    }
                    if ("restart".equalsIgnoreCase(command[0])) {
                        ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " Restarting... :hourglass_flowing_sand::hourglass:").complete();
                        jda.shutdown(false);
//                        Start.startBot();
                    }
                    if (("shutdown".equalsIgnoreCase(command[0]) || "sd".equalsIgnoreCase(command[0]))) {
                        ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " :regional_indicator_k::o2:\t:dizzy_face::gun: ").complete();
                        jda.shutdown(true);
                        System.exit(0);
                    }
                }

            }
        }
    }
}
