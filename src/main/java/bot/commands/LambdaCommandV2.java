/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Constant;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 *
 * @author renardn
 */
public class LambdaCommandV2 extends Command {

    public LambdaCommandV2() {
        this.name = "test2";
        this.help = "summon the bot in the voice channel";
        this.guildOnly = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String id = Constant.getTextChannelConf().getProperty(event.getGuild().getId());
        if (id != null) {
            if (!event.getChannel().getId().equals(id)) {
                return;
            }
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Oui");
        builder.setDescription("Bonjour les tests c'est bien");
        builder.setColor(Color.black);
        Message msg = event.getChannel().sendMessage(builder.build()).complete();
    }
}
