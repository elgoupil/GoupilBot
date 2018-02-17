/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class HelloCommand extends Command {

    private final EventWaiter waiter;

    public HelloCommand(EventWaiter waiter) {
        this.waiter = waiter;
        this.name = "hello";
        this.aliases = new String[]{"hi"};
        this.help = "says hello and waits for a response";
    }

    @Override
    protected void execute(CommandEvent event) {
        // ask what the user's name is
        event.reply("Hello. What is your name?");
        // wait for a response
        waiter.waitForEvent(MessageReceivedEvent.class,
                // make sure it's by the same user, and in the same channel
                e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()),
                // respond, inserting the name they listed into the response
                e -> e.getChannel().sendMessage("Hello, `" + e.getMessage().getContentDisplay() + "`! I'm `" + e.getJDA().getSelfUser().getName() + "`!").queue(),
                // if the user takes more than a minute, time out
                1, TimeUnit.MINUTES, () -> event.reply("Sorry, you took too long."));
    }

}
