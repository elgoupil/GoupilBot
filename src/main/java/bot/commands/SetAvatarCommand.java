/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Constant;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;

/**
 *
 * @author renardn
 */
public class SetAvatarCommand extends Command {

    public SetAvatarCommand() {
        this.name = "setavatar";
        this.help = "change the avatar of the bot, giving `null` in argument will delete avatar";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!event.getArgs().isEmpty()) {
            if (event.getArgs().equals("null")) {
                Constant.jda.getSelfUser().getManager().setAvatar(null).complete();
                event.reactSuccess();
            } else {
                try {
                    Constant.jda.getSelfUser().getManager().setAvatar(Icon.from(new URL(event.getArgs()).openStream())).complete();
                    event.reactSuccess();
                } catch (IOException ex) {
                    event.replyError("Wrong url");
                } catch (Exception ex) {
                    event.replyError("You changed the avatar too fast");
                }
            }
        } else {
            event.replyWarning("Please specify an url");
        }
    }
}
