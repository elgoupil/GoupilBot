/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Constant;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class CatCommand extends Command {

    public CatCommand() {
        this.name = "cat";
        this.help = "shows a random cat";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String id = Constant.getServers().getProperty(event.getGuild().getId());
        if (id != null) {
            if (!event.getChannel().getId().equals(id)) {
                return;
            }
        }
        // use Unirest to poll an API
        Unirest.get("http://random.cat/meow").asJsonAsync(new Callback<JsonNode>() {

            // The API call was successful
            @Override
            public void completed(HttpResponse<JsonNode> hr) {
                event.reply(new EmbedBuilder()
                        .setColor(event.isFromType(ChannelType.TEXT) ? event.getSelfMember().getColor() : Color.GREEN)
                        .setImage(hr.getBody().getObject().getString("file"))
                        .build());
            }

            // The API call failed
            @Override
            public void failed(UnirestException ue) {
                event.reactError();
            }

            // The API call was cancelled (this should never happen)
            @Override
            public void cancelled() {
                event.reactError();
            }
        });
    }
}
