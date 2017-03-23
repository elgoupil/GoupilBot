/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.vent;

import bot.Start;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author goupil
 */
public class WrkEvent {

    private JDA jda;

    public WrkEvent(JDA jda) {
        this.jda = jda;
    }

    public void eventWrk(Event event) {
        if (event instanceof MessageReceivedEvent) {
            if (((MessageReceivedEvent) event).getTextChannel().getId().equals("294142499138568192")) {
                if (((MessageReceivedEvent) event).getAuthor() != jda.getSelfUser()) {
                    if (((MessageReceivedEvent) event).getMessage().getContent().equalsIgnoreCase("restart")) {

                        ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " Restarting... :hourglass_flowing_sand::hourglass:").queue();
                        jda.shutdown(false);
                        Start.startBot();
                    }
                    if (((MessageReceivedEvent) event).getMessage().getContent().equalsIgnoreCase("shutdown")) {

                        ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " :regional_indicator_k::o2:\t:dizzy_face::gun: ").complete();
                        jda.shutdown(true);
                        System.exit(0);
                    }
                    if (((MessageReceivedEvent) event).getMessage().getContent().equalsIgnoreCase("help")) {
                        ((MessageReceivedEvent) event).getTextChannel().sendMessage(((MessageReceivedEvent) event).getAuthor().getAsMention() + " Dat Help\n Ok :ok_hand:").queue();
                    }
                }
            }

        }
    }

}
