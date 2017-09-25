/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.commands.music;

import bot.wrk.music.Music;
import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

/**
 *
 * @author renardn
 */
public class SkipCommand extends Command{
    
    private Music music;

    public SkipCommand(Music music) {
        this.music = music;
        this.name = "skip";
        this.help = "skip the current track";
        this.guildOnly = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        /* 
        0 = empty list
        1 = ok
        2 = player is not playing
        3 = player is not connected
         */
        int res = music.skipTrack(event.getTextChannel());
        if (res == 0) {
            event.replySuccess("Skipped the last track");
        }else if (res == 1){
            event.replySuccess("Skipped the track");
        }else if (res == 2){
            event.replyWarning("Player is not playing");
        }else if (res == 3){
            event.replyError(event.getMember().getAsMention() + " I'm not even connected :joy:");
        }
    }
}
