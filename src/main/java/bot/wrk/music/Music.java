/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.ArrayList;
import java.util.HashMap;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 *
 * @author goupil
 */
public class Music {

    private final AudioPlayerManager playerManager;
    private final HashMap<Long, GuildMusicManager> musicManagers;
    private JDA jda;

    /**
     *
     * @param channel
     */
    public Music(TextChannel channel, JDA jda) {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        NowPlaying nowPlaying = new NowPlaying(channel, getGuildAudioPlayer(channel.getGuild()));
    }

    public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(final TextChannel channel, final String trackUrl, Member user) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (channel.getGuild().getAudioManager().isConnected()) {
                    channel.sendMessage("Adding to queue " + track.getInfo().title).queue();
                    play(channel, musicManager, track, user);
                } else {
                    channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
                }

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (channel.getGuild().getAudioManager().isConnected()) {
                    AudioTrack firstTrack = playlist.getSelectedTrack();
                    if (firstTrack == null) {
                        for (AudioTrack track : playlist.getTracks()) {
                            play(channel, musicManager, track, user);
                        }
                    }
                    channel.sendMessage("Adding to queue playlist " + playlist.getName() + " with " + playlist.getTracks().size() + " songs").queue();
                } else {
                    channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public void play(TextChannel channel, GuildMusicManager musicManager, AudioTrack track, Member user) {
        musicManager.scheduler.queue(track);
    }

    public void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped the current track.").queue();
    }

    public void changeVolume(String volume, TextChannel channel) {
        if ((channel.getGuild().getAudioManager().isConnected()) || (channel.getGuild().getAudioManager().isAttemptingToConnect())) {
            GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
            try {
                boolean isOk = musicManager.scheduler.changeVolume(Integer.parseInt(volume));
                if (!isOk) {
                    channel.sendMessage("Volume must be between 1 - 100").queue();
                } else {
                    channel.sendMessage("Volume is now " + musicManager.scheduler.getVolume()).queue();
                }
            } catch (NumberFormatException e) {
                channel.sendMessage("Volume must be in number!").queue();
            }
        } else {
            channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
        }
    }

    public void disconnectChannel(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        AudioManager audioManager = channel.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            musicManager.player.destroy();
            audioManager.closeAudioConnection();
            musicManager.scheduler.clearQueue();
            channel.sendMessage("Bye :kissing_heart:").queue();
        } else {
            channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
        }
    }

    public void clearQueue(TextChannel channel) {
        if (getGuildAudioPlayer(channel.getGuild()).scheduler.clearQueue()) {
            channel.sendMessage("Successfully cleared the queue!").queue();
        } else {
            channel.sendMessage("Queue is already empty!").queue();
        }
    }

    public void shuffleQueue(TextChannel channel) {
        if (channel.getGuild().getAudioManager().isConnected()) {
            getGuildAudioPlayer(channel.getGuild()).scheduler.shuffleQueue();
            channel.sendMessage("Successfully shuffled the track!").queue();
        } else {
            channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
        }
    }

    public void showQueue(TextChannel channel) {
        if (channel.getGuild().getAudioManager().isConnected()) {
            GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
            if (!musicManager.scheduler.getQueue().isEmpty()) {
                ArrayList<AudioTrack> queue = new ArrayList<>(musicManager.scheduler.getQueue());
                String message = "Current queue:\n";
                if (queue.size() > 5) {
                    for (int i = 0; i < 5; i++) {
                        message += "\n" + (i + 1) + ".`" + queue.get(i).getInfo().title + "`";
                    }
                    message += "\n\nAnd `" + (queue.size() - 5) + "` more...";
                } else if (queue.size() <= 5) {
                    for (int i = 0; i < queue.size(); i++) {
                        message += "\n" + (i + 1) + ".`" + queue.get(i).getInfo().title + "`";
                    }
                }
                channel.sendMessage(message).queue();
            } else {
                channel.sendMessage("Queue is empty!").queue();
            }
        } else {
            channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
        }

    }

    public static void connectToVoiceChannel(AudioManager audioManager, Member user) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            if (user.getVoiceState().inVoiceChannel()) {
                audioManager.openAudioConnection(user.getVoiceState().getChannel());
            }
        }
    }
}
