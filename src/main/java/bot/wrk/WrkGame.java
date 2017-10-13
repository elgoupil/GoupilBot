/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk;

import bot.Constant;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 *
 * @author virus
 */
public class WrkGame implements EventListener {

    public WrkGame() {
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof GuildVoiceJoinEvent) {
            if (((GuildVoiceJoinEvent) event).getChannelJoined().getId().equals(Constant.getVoiceChannelConf().getProperty(((GuildVoiceJoinEvent) event).getChannelJoined().getGuild().getId()))) {
                ((GuildVoiceJoinEvent) event).getChannelJoined().getGuild().getTextChannels().get(10).sendMessage("Hi").complete();
                if (((GuildVoiceJoinEvent) event).getMember().getGame() != null) {
                    askGame(event);
                }
            }
        } else if (event instanceof GuildVoiceMoveEvent) {
            if (((GuildVoiceMoveEvent) event).getChannelJoined().getId().equals(Constant.getVoiceChannelConf().getProperty(((GuildVoiceMoveEvent) event).getChannelJoined().getGuild().getId()))) {
                ((GuildVoiceMoveEvent) event).getChannelJoined().getGuild().getTextChannels().get(10).sendMessage("Hi").complete();
                if (((GuildVoiceMoveEvent) event).getMember().getGame() != null) {
                    askGame(event);
                }
            }
        }
    }

    private void askGame(Event event) {
        GenericGuildVoiceEvent gEvent = (GenericGuildVoiceEvent) event;
        TextChannel textChannel;
        textChannel = event.getJDA().getTextChannelById(Constant.getTextChannelConf().getProperty(gEvent.getGuild().getId()));
        Constant.waiter.waitForEvent(GuildMessageReceivedEvent.class, 
                e -> e.getMember().equals(gEvent.getMember()) && e.getChannel().equals(textChannel),
                e -> {
                    textChannel.sendMessage("Hi, wanna be moved?").queue();
                },
                1, TimeUnit.MINUTES, () -> textChannel.sendMessage(gEvent.getMember().getAsMention() + ", Sorry you took to long to answer").queue());
    }
}
