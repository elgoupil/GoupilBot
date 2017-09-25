/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands;

import bot.wrk.music.GuildMusicManager;
import bot.wrk.music.Music;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class HelloCommand extends Command {

    private final EventWaiter waiter;
    private Music music;

    public HelloCommand(EventWaiter waiter, Music music) {
        this.waiter = waiter;
        this.music = music;
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
                e -> {
                    GuildMusicManager manager = music.getGuildAudioPlayer(event.getGuild());
                    int volume;
                    volume = manager.scheduler.getVolume();
                    music.connectToVoiceChat(event.getGuild().getAudioManager(), event.getMember());
                    if (event.getGuild().getAudioManager().isAttemptingToConnect() || event.getGuild().getAudioManager().isConnected()) {
                        manager.scheduler.getQueue().clear();
                        manager.player.stopTrack();
                        manager.scheduler.changeVolume(volume);
                        music.loadAndPlayPlaylist(event, waiter, "https://cdn.discordapp.com/attachments/294152853751332864/361932236284493824/HELLO.wav");
                    }
                    e.getChannel().sendMessage("Hello, `" + e.getMessage().getRawContent() + "`! I'm `" + e.getJDA().getSelfUser().getName() + "`!").queue();

                },
                // if the user takes more than a minute, time out
                1, TimeUnit.MINUTES, () -> event.reply("Sorry, you took too long."));
    }

}
