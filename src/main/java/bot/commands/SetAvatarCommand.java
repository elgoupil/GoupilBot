/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.Constant;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import java.io.IOException;
import java.net.URL;
import net.dv8tion.jda.api.entities.Icon;

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
