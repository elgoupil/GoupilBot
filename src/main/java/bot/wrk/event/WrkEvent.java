/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.event;

import bot.Start;
import bot.wrk.music.Music;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author goupil
 */
public class WrkEvent {

    public static void eventWrk(Event event, JDA jda, Music musicbot, TextChannel channel, String prefix, String commanderRole, ArrayList<String> blacklist) {
        if (event instanceof MessageReceivedEvent) {
            if (!((MessageReceivedEvent) event).isFromType(ChannelType.PRIVATE)) {
                if (((MessageReceivedEvent) event).getTextChannel().equals(channel)) {
                    if (((MessageReceivedEvent) event).getAuthor() != jda.getSelfUser()) {
                        String[] command = ((MessageReceivedEvent) event).getMessage().getContent().split(" ", 2);
                        if ((prefix + "play").equalsIgnoreCase(command[0])) {
                            if (command.length == 2) {
                                musicbot.loadAndPlay(command[1], ((MessageReceivedEvent) event).getMember());
                            } else {
                                ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `" + prefix + "play 'url'` or `" + prefix + "search 'name of the video on YT'`").queue();
                            }
                        }
                        if ((prefix + "search").equalsIgnoreCase(command[0])) {
                            if (command.length == 2) {
                                musicbot.loadAndPlay(("ytsearch:" + command[1]), ((MessageReceivedEvent) event).getMember());
                            } else {
                                ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `" + prefix + "play 'url'` or `" + prefix + "search 'name of the video on YT'`").queue();
                            }
                        }
                        if ((prefix + "volume").equalsIgnoreCase(command[0])) {
                            if (command.length == 2) {
                                musicbot.changeVolume(command[1], true);
                            } else {
                                ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `" + prefix + "volume 1 - 150`").queue();
                            }
                        }
                        if ((prefix + "disconnect").equalsIgnoreCase(command[0])) {
                            musicbot.disconnectChannel();
                        }
                        if ((prefix + "clear").equalsIgnoreCase(command[0])) {
                            musicbot.clearQueue();
                        }
                        if ((prefix + "shuffle").equalsIgnoreCase(command[0])) {
                            musicbot.shuffleQueue();
                        }
                        if ((prefix + "queue").equalsIgnoreCase(command[0])) {
                            musicbot.showQueue();
                        }
                        if ((prefix + "coinflip").equalsIgnoreCase(command[0]) || (prefix + "cf").equalsIgnoreCase(command[0])) {
                            int random = (int) (Math.random() * 100);
                            if (random > 50) {
                                ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " http://i.imgur.com/bEJS8T7.png").queue();
                            } else {
                                ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " http://i.imgur.com/Ur6tcq1.png").queue();
                            }
                        }
                        if ((prefix + "summon").equalsIgnoreCase(command[0])) {
                            musicbot.connectToVoiceChannel(((MessageReceivedEvent) event).getGuild().getAudioManager(), ((MessageReceivedEvent) event).getMember(), ((MessageReceivedEvent) event).getTextChannel());
                            musicbot.changeVolume("15", false);
                        }
                        if ((prefix + "help").equalsIgnoreCase(command[0])) {
                            String msg;
                            String msgHelp = "**" + jda.getSelfUser().getName() + "** commands:\n"
                                    + "\n"
                                    + "`" + prefix + "play` -  Add a music the to queue with this syntax: `" + prefix + "play 'url'`\n"
                                    + "`" + prefix + "search` -  Search a music on YouTube with this syntax: `" + prefix + "search 'name of the video on YT'`\n"
                                    + "`" + prefix + "volume` - Change the volume of the music with this syntax: `" + prefix + "volume 1 - 150`\n"
                                    + "`" + prefix + "disconnect` -  Make the bot disconnect from vocal\n"
                                    + "`" + prefix + "clear` -  Clear the queue\n"
                                    + "`" + prefix + "shuffle` -  Shuffle the queue\n"
                                    + "`" + prefix + "queue` -  Show current the queue\n"
                                    + "`" + prefix + "summon` -  Connect/Move the bot in your vocal channel\n"
                                    + "`" + prefix + "restart` -  Restart the bot :warning: Don't use it too much :warning:\n"
                                    + "`" + prefix + "sd or shutdown` -  Die potato :exclamation:\n"
                                    + "\n"
                                    + "**There is some secrets command :smirk:**\n"
                                    + "\n"
                                    + "For additional help, contact " + jda.getUserById("203616169519742977").getAsMention();
                            if (command.length == 2) {
                                switch (command[1]) {
                                    case "play":
                                        msg = "Add a music the to queue with this syntax: `" + prefix + "play 'url'`";
                                        break;
                                    case "search":
                                        msg = "Search a music on YouTube with this syntax: `" + prefix + "search 'name of the video on YT'`";
                                        break;
                                    case "volume":
                                        msg = "Change the volume of the music with this syntax: `" + prefix + "volume 1 - 150`";
                                        break;
                                    case "disconnect":
                                        msg = "Make the bot disconnect from vocal";
                                        break;
                                    case "clear":
                                        msg = "Clear the queue";
                                        break;
                                    case "shuffle":
                                        msg = "Shuffle the queue";
                                        break;
                                    case "queue":
                                        msg = "Show current the queue";
                                        break;
                                    case "summon":
                                        msg = "Connect/Move the bot in your vocal channel";
                                        break;
                                    case "restart":
                                        msg = "Restart the bot \n :exclamation: Don't use it too much :exclamation:";
                                        break;
                                    case "shutdown":
                                        msg = "Die potato :exclamation:";
                                        break;
                                    default:
                                        msg = "I don't know that command :sweat_smile:";
                                }
                            } else if (((MessageReceivedEvent) event).getAuthor().hasPrivateChannel()) {
                                ((MessageReceivedEvent) event).getAuthor().getPrivateChannel().sendMessage(msgHelp).queue();
                                msg = ":mailbox_with_mail:";
                            } else {
                                msg = "I can't send you private message, send me a message here " + jda.getSelfUser().getAsMention() + " first :cry:";
                            }
                            ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " " + msg).queue();
                        }
                        if ((prefix + "restart").equalsIgnoreCase(command[0])) {
                            ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " Restarting... :hourglass_flowing_sand::hourglass:").complete();
                            musicbot.getNowPlaying().stopThread();
                            jda.shutdown(false);
                            Start.startBot();
                        }
                        if (((prefix + "shutdown").equalsIgnoreCase(command[0]) || (prefix + "sd").equalsIgnoreCase(command[0]))) {
                            List<Role> roles = ((MessageReceivedEvent) event).getGuild().getRolesByName(commanderRole, false);
                            if (!roles.isEmpty()) {
                                if (((MessageReceivedEvent) event).getMember().getRoles().contains(roles.get(0))) {
                                    ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " :regional_indicator_k::o2:\t:dizzy_face::gun: ").complete();
                                    musicbot.getNowPlaying().stopThread();
                                    jda.shutdown(true);
                                    System.exit(0);
                                } else {
                                    channel.sendMessage(((MessageReceivedEvent) event).getMember().getAsMention() + " You don't have the permission :smirk:").queue();
                                }
                            } else {
                                channel.sendMessage("The role given in the config file is incorect :sweat_smile:").queue();
                            }
                        }
//                        if ((prefix + "command").equalsIgnoreCase(command[0])) {
//                            List<Role> roles = ((MessageReceivedEvent) event).getGuild().getRolesByName(commanderRole, false);
//                            if (!roles.isEmpty()) {
//                                if (((MessageReceivedEvent) event).getMember().getRoles().contains(roles.get(0))) {
//                                    
//                                } else {
//                                    channel.sendMessage(((MessageReceivedEvent) event).getMember().getAsMention() + " You don't have the permission :smirk:").queue();
//                                }
//                            } else {
//                                channel.sendMessage("The role given in the config file is incorect :sweat_smile:").queue();
//                            }
//                        }
                        if ((prefix + "flushChat").equalsIgnoreCase(command[0])) {
                            List<Role> roles = ((MessageReceivedEvent) event).getGuild().getRolesByName(commanderRole, false);
                            if (!roles.isEmpty()) {
                                if (((MessageReceivedEvent) event).getMember().getRoles().contains(roles.get(0))) {
                                    if (command.length == 2) {
                                        try {
                                            if (!musicbot.getNowPlaying().getIsPlaying()) {
                                                int nbr = Integer.parseInt(command[1]);
                                                if (nbr > 0 && nbr < 101) {
                                                    List<Message> messages = channel.getHistory().retrievePast(nbr).complete();
                                                    channel.sendMessage("This can take long").queue();
                                                    for (Message message : messages) {
                                                        if (!message.isPinned()) {
                                                            channel.deleteMessageById(message.getId()).queue();
                                                        }
                                                    }
                                                } else {
                                                    throw new Exception();
                                                }
                                            } else {
                                                channel.sendMessage(((MessageReceivedEvent) event).getMember().getAsMention() + " Cannot flush when playing :smirk:").queue();
                                            }
                                        } catch (Exception e) {
                                            channel.sendMessage("Please choose a number between 1 - 100").queue();
                                        }
                                    } else {
                                        ((MessageReceivedEvent) event).getChannel().sendMessage("Usage : `" + prefix + "flushChat 1 - 100`").queue();
                                    }
                                } else {
                                    channel.sendMessage(((MessageReceivedEvent) event).getMember().getAsMention() + " You don't have the permission :smirk:").queue();
                                }
                            } else {
                                channel.sendMessage("The role given in the config file is incorect :sweat_smile:").queue();
                            }
                        }
                    }
                }
                if (blacklist.contains(((MessageReceivedEvent) event).getMessage().getContent().toLowerCase())) {
                    if ((blacklist != null) || (!blacklist.isEmpty())) {
                        ((MessageReceivedEvent) event).getChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " Jesus is watching :heart:").queue();
                        ((MessageReceivedEvent) event).getChannel().deleteMessageById(((MessageReceivedEvent) event).getMessage().getId()).queue();
                    }
                }
            }
        }
    }
}
