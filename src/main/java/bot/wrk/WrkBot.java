/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk;

import bot.Constant;
import bot.commands.BroadcastCommand;
import bot.commands.CatCommand;
import bot.commands.DogCommand;
import bot.commands.GameBotCommand;
import bot.commands.HelloCommand;
import bot.commands.RestartCommand;
import bot.commands.SetTextChannelCommand;
import bot.commands.SetVoiceChannelCommand;
import bot.commands.music.DisconnectCommand;
import bot.commands.music.NowPlayingCommand;
import bot.commands.music.PlayCommand;
import bot.commands.music.QueueCommand;
import bot.commands.music.SearchCommand;
import bot.commands.music.ShuffleCommand;
import bot.commands.music.SummonCommand;
import bot.commands.music.VolumeCommand;
import bot.commands.SetAvatarCommand;
import bot.wrk.music.Music;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.AboutCommand;
import com.jagrosh.jdautilities.examples.command.GuildlistCommand;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import java.awt.Color;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;

/**
 *
 * @author renardn
 */
public class WrkBot {

    EventWaiter waiter;
    CommandClientBuilder client;

    public WrkBot(Properties prop) {
        Constant.music = new Music();
        Constant.waiter = waiter = new EventWaiter();
        client = new CommandClientBuilder();
        client.setOwnerId(Constant.ownerId);
        client.setEmojis("✅", "⚠", "❌");
        client.setPrefix(Constant.prefix);
        // adds commands
        client.addCommands(// command to show information about the bot
                new AboutCommand(Color.GREEN, "a music bot with a lot more",
                        new String[]{"Pimped music bot", "Many cats", "Many dogs", "Game bot (see the help)", "And more..."},
                        new Permission[]{Permission.MANAGE_CHANNEL, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_MOVE_OTHERS, Permission.VOICE_SPEAK}),
                new BroadcastCommand(),
                new CatCommand(),
                new DisconnectCommand(),
                new DogCommand(),
                new GameBotCommand(),
                new GuildlistCommand(waiter),
                new HelloCommand(waiter),
                new NowPlayingCommand(),
                new PingCommand(),
                new PlayCommand(),
                new QueueCommand(),
                new RestartCommand(),
                new SearchCommand(waiter),
                new SetAvatarCommand(),
                new SetTextChannelCommand(),
                new SetVoiceChannelCommand(),
                new ShuffleCommand(),
                new bot.commands.ShutdownCommand(),
                new SummonCommand(),
                new VolumeCommand());
        try {
            Constant.jda = new JDABuilder(AccountType.BOT)
                    // set the token
                    .setToken(prop.getProperty("token"))
                    // set the game for when the bot is loading
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setActivity(Activity.playing("loading..."))
                    // add the listeners
                    .addEventListeners(waiter, client.build())
                    // start it up!
                    .build();
        } catch (LoginException | IllegalArgumentException ex) {
            Logger.getLogger(WrkBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
