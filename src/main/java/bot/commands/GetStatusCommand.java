/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;

/**
 *
 * @author renardn
 */
public class GetStatusCommand extends Command {

    public GetStatusCommand() {
        this.name = "getstatus";
        this.help = "return your actual status";
        this.guildOnly = false;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String type = null;
        Member oshino = null;
        if (event.isFromType(ChannelType.PRIVATE)) {
            if (!event.getAuthor().getMutualGuilds().isEmpty()) {
                oshino = event.getAuthor().getMutualGuilds().get(0).getMemberById(event.getAuthor().getId());
            } else {
                event.replyError("You need to be in at least one server in common with me");
            }
        } else {
            oshino = event.getMember();
        }
        if (oshino != null) {
            try {
                type = oshino.getGame().getType().toString().substring(0, 1).concat(oshino.getGame().getType().toString().substring(1).toLowerCase());
                if (type.equalsIgnoreCase("default")) {
                    type = "Playing";
                }
            } catch (Exception e) {
            }
            if (type != null) {
                String msg = String.format("%s %s", type, oshino.getGame().getName());
                EmbedBuilder builder = new EmbedBuilder();
                builder.setColor(event.isFromType(ChannelType.TEXT) ? event.getSelfMember().getColor() : Color.ORANGE);
                builder.setAuthor(event.getAuthor().getName(), "https://github.com/elgoupil/GoupilBot", event.getAuthor().getEffectiveAvatarUrl());
                builder.setTitle("current status is:");
                builder.setDescription(msg);
                builder.setFooter(event.getSelfUser().getName(), event.getSelfUser().getEffectiveAvatarUrl());
                event.reply(builder.build());
            } else {
                event.reply("Your status is empty!");
            }
        }
    }
}
