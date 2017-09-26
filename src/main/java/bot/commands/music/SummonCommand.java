/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.Constant;
import bot.wrk.music.Music;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

/**
 *
 * @author renardn
 */
public class SummonCommand extends Command {

    private Music music;

    public SummonCommand(Music music) {
        this.music = music;
        this.name = "summon";
        this.help = "summon the bot in the voice channel";
        this.guildOnly = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String id = Constant.getServers().getProperty(event.getGuild().getId());
        if (id != null) {
            if (!event.getChannel().getId().equals(id)) {
                return;
            }
        }
        int res = music.connectToVoiceChat(event.getGuild().getAudioManager(), event.getMember());
        if (res == 0) {
            event.replyWarning(event.getMember().getAsMention() + " Please connect in a voice channel first");
        } else {
            event.replySuccess("Connected");
        }
    }
}
