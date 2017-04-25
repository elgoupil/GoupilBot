/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.event;

import bot.Start;
import bot.wrk.music.Music;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author goupil
 */
public class WrkEvent {

    public static void eventWrk(Event event, JDA jda, Music musicbot, TextChannel channel, String prefix) {
        if (event instanceof MessageReceivedEvent) {
            if (!((MessageReceivedEvent) event).isFromType(ChannelType.PRIVATE)) {
                if (((MessageReceivedEvent) event).getTextChannel().equals(channel)) {
                    if (((MessageReceivedEvent) event).getAuthor() != jda.getSelfUser()) {
                        String[] command = ((MessageReceivedEvent) event).getMessage().getContent().split(" ", 2);
                        if ("play".equalsIgnoreCase(command[0])) {
                            if (command.length == 2) {
                                musicbot.loadAndPlay(command[1], ((MessageReceivedEvent) event).getMember());
                            } else {
                                ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `play 'url'` or `search 'name of the video on YT'`").queue();
                            }
                        }
                        if ("search".equalsIgnoreCase(command[0])) {
                            if (command.length == 2) {
                                musicbot.loadAndPlay(("ytsearch:" + command[1]), ((MessageReceivedEvent) event).getMember());
                            } else {
                                ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `play 'url'` or `search 'name of the video on YT'`").queue();
                            }
                        }
                        if ("skip".equalsIgnoreCase(command[0])) {
                            musicbot.skipTrack(true);
                        }
                        if ("volume".equalsIgnoreCase(command[0])) {
                            if (command.length == 2) {
                                musicbot.changeVolume(command[1], true);
                            } else {
                                ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `volume 1 - 150`").queue();
                            }
                        }
                        if ("disconnect".equalsIgnoreCase(command[0])) {
                            musicbot.disconnectChannel();
                        }
                        if ("clear".equalsIgnoreCase(command[0])) {
                            musicbot.clearQueue();
                        }
                        if ("shuffle".equalsIgnoreCase(command[0])) {
                            musicbot.shuffleQueue();
                        }
                        if ("queue".equalsIgnoreCase(command[0])) {
                            musicbot.showQueue();
                        }
                        if ("nowplaying".equalsIgnoreCase(command[0]) || "np".equalsIgnoreCase(command[0])) {
                            musicbot.showNowPlaying();
                        }
                        if ("stop".equalsIgnoreCase(command[0])) {
                            musicbot.stopMusic(true);
                        }
                        if ("pause".equalsIgnoreCase(command[0])) {
                            musicbot.pauseMusic(true);
                        }
                        if ("summon".equalsIgnoreCase(command[0])) {
                            musicbot.connectToVoiceChannel(((MessageReceivedEvent) event).getGuild().getAudioManager(), ((MessageReceivedEvent) event).getMember(), ((MessageReceivedEvent) event).getTextChannel());
                            musicbot.changeVolume("15", false);
                        }
                        if ("help".equalsIgnoreCase(command[0])) {
                            String msg = " :mailbox_with_mail:";
                            String msgError = " I can't send you private message :cry:";
                            String msgHelp = "## The Help ##\n"
                                    + "**Commands: **\n"
                                    + "\n"
                                    + " - Play: \n"
                                    + " - search:\n"
                                    + " - skip\n"
                                    + " - volume\n"
                                    + " - disconnect\n"
                                    + " - clear\n"
                                    + " - shuffle\n"
                                    + " - queue\n"
                                    + " - nowplaying\n"
                                    + " - stop\n"
                                    + " - pause\n"
                                    + " - summon\n"
                                    + " - restart";
                            if (((MessageReceivedEvent) event).getAuthor().hasPrivateChannel()) {
                                ((MessageReceivedEvent) event).getAuthor().getPrivateChannel().sendMessage(msgHelp).queue();
                                ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + msg).queue();
                            } else {
                                ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + msgError).queue();
                            }
                        }
                        if ("restart".equalsIgnoreCase(command[0])) {
                            ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " Restarting... :hourglass_flowing_sand::hourglass:").complete();
                            musicbot.getNowPlaying().stopThread();
                            jda.shutdown(false);
                            Start.startBot();
                        }
                        if (("shutdown".equalsIgnoreCase(command[0]) || "sd".equalsIgnoreCase(command[0]))) {
                            ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " :regional_indicator_k::o2:\t:dizzy_face::gun: ").complete();
                            musicbot.getNowPlaying().stopThread();
                            jda.shutdown(true);
                            System.exit(0);
                        }
                    }

                }
            }
        }
    }
}
