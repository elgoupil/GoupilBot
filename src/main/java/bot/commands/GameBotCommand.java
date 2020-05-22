/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Constant;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;

/**
 *
 * @author renardn
 */
public class GameBotCommand extends Command {

    public GameBotCommand() {
        this.name = "gamebot";
        this.aliases = new String[]{"gb"};
        this.help = "type " + Constant.prefix + "gameBot help";
        this.helpBiConsumer = (CommandEvent event, Command command) -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(event.isFromType(ChannelType.TEXT) ? event.getSelfMember().getColor() : Color.GREEN);
            builder.setFooter(event.getSelfUser().getName(), event.getSelfUser().getAvatarUrl());
            builder.setTitle("The \"Game Bot\" Feature");
            builder.setDescription("The \"Game Bot\" feature is quite simple:");
            builder.addField("1.", "If you're playing a game, you just have to go in the server's game channel and react with :ok: in the bot channel", false);
            builder.addField("2.", "After that you'll be moved in a channel with the name of your game", false);
            builder.addField("3.", "If you leave the channel, it will automatically delete the channel", false);
            builder.setImage(Constant.gameBotExampleUrl);
            event.reply(builder.build());
        };
        this.guildOnly = false;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.isFromType(ChannelType.TEXT) ? event.getSelfMember().getColor() : Color.GREEN);
        builder.setFooter(event.getSelfUser().getName(), event.getSelfUser().getAvatarUrl());
        builder.setTitle("The \"Game Bot\" Feature");
        builder.setDescription("The \"Game Bot\" feature is quite simple:");
        builder.addField("1.", "If you're playing a game, you just have to go in the server's game channel and react with :ok: in the bot channel", false);
        builder.addField("2.", "After that you'll be moved in a channel with the name of your game", false);
        builder.addField("3.", "If you leave the channel, it will automatically delete the channel", false);
        builder.setImage(Constant.gameBotExampleUrl);
        event.reply(builder.build());
    }
}
