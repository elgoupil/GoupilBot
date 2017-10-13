/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk;

import bot.Constant;
import bot.commands.BroadcastCommand;
import bot.commands.CatCommand;
import bot.commands.HelloCommand;
import bot.commands.LambdaCommand;
import bot.commands.LambdaCommandV2;
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
import bot.wrk.music.Music;
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import com.jagrosh.jdautilities.commandclient.examples.AboutCommand;
import com.jagrosh.jdautilities.commandclient.examples.GuildlistCommand;
import com.jagrosh.jdautilities.commandclient.examples.PingCommand;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import java.awt.Color;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 *
 * @author renardn
 */
public class WrkBot {
    EventWaiter waiter;
    WrkGame wrkGame;
    CommandClientBuilder client;

    public WrkBot(Properties prop) {
        Constant.music = new Music();
        Constant.waiter = waiter = new EventWaiter();
        wrkGame = new WrkGame();
        client = new CommandClientBuilder();
        client.useDefaultGame();
        client.setOwnerId(prop.getProperty("ownerId"));
        client.setEmojis("✅", "⚠", "❌");
        client.setPrefix(prop.getProperty("prefix"));
        // adds commands
        client.addCommands(
                // command to show information about the bot
                new AboutCommand(Color.GREEN, "a music bot with a lot more",
                        new String[]{"Cool commands", "Nice cats", "Lots of dicks!", "A mean Potch"},
                        new Permission[]{Permission.MANAGE_CHANNEL, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_MANAGE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_MOVE_OTHERS, Permission.VOICE_SPEAK}),
                new GuildlistCommand(waiter),
                new PingCommand(),
                new RestartCommand(),
                new BroadcastCommand(),
                new CatCommand(),
                new LambdaCommand(),
                new LambdaCommandV2(),
                new SetTextChannelCommand(),
                new SummonCommand(),
                new DisconnectCommand(),
                new PlayCommand(),
                new QueueCommand(),
                new SearchCommand(waiter),
                new VolumeCommand(),
                new HelloCommand(waiter),
                new NowPlayingCommand(),
                new ShuffleCommand(),
                new SetVoiceChannelCommand(),
                new bot.commands.ShutdownCommand());

        try {
            Constant.jda = new JDABuilder(AccountType.BOT)
                    // set the token
                    .setToken(prop.getProperty("token"))
                    // set the game for when the bot is loading
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setGame(Game.of("loading..."))
                    // add the listeners
                    .addEventListener(waiter)
                    .addEventListener(client.build())
                    .addEventListener(wrkGame)
                    // start it up!
                    .buildAsync();
        } catch (LoginException | IllegalArgumentException | RateLimitedException ex) {
            Logger.getLogger(WrkBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
