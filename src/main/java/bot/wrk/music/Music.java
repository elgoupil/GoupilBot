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
import java.util.List;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 *
 * @author goupil
 */
public class Music implements EventListener {

    private final AudioPlayerManager playerManager;
    private final HashMap<Long, GuildMusicManager> musicManagers;
    private NowPlaying nowPlaying;
    private JDA jda;
    private TextChannel channel;
    private String idMessageNowPlaying;
    private List<AudioTrack> tracks;

    /**
     *
     * @param channel
     * @param jda
     */
    public Music(TextChannel channel, JDA jda) {
        this.musicManagers = new HashMap<>();
        this.channel = channel;
        this.playerManager = new DefaultAudioPlayerManager();
        this.jda = jda;
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        nowPlaying = new NowPlaying(channel, getGuildAudioPlayer(channel.getGuild()), jda, this);
        idMessageNowPlaying = "";
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

    public NowPlaying getNowPlaying() {
        return nowPlaying;
    }

    public void loadAndPlay(final String trackUrl, Member user) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (channel.getGuild().getAudioManager().isConnected()) {
                    channel.sendMessage("Adding to queue " + track.getInfo().title).queue();
                    play(musicManager, track);
                } else {
                    channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
                }

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (channel.getGuild().getAudioManager().isConnected()) {
                    AudioTrack firstTrack = playlist.getSelectedTrack();
                    if (firstTrack == null) {
                        if (trackUrl.contains("ytsearch:")) {
                            if (playlist.getTracks().size() > 4) {
                                if (idMessageNowPlaying == "") {
                                    tracks = playlist.getTracks();
                                    String msg = "**Result of search**: " + trackUrl.substring(9) + "\n **:one:**: " + tracks.get(0).getInfo().title
                                            + "\n **:two:**: " + tracks.get(1).getInfo().title + "\n **:three:**: " + tracks.get(2).getInfo().title
                                            + "\n **:four:**: " + tracks.get(3).getInfo().title + "\n **and more...**";
                                    Message theMessage = channel.sendMessage(msg).complete();
                                    idMessageNowPlaying = theMessage.getId();
                                    try {
                                        channel.addReactionById(idMessageNowPlaying, "❌").queue();
                                        channel.addReactionById(idMessageNowPlaying, "1⃣").queue();
                                        channel.addReactionById(idMessageNowPlaying, "2⃣").queue();
                                        channel.addReactionById(idMessageNowPlaying, "3⃣").queue();
                                        channel.addReactionById(idMessageNowPlaying, "4⃣").queue();
                                        channel.addReactionById(idMessageNowPlaying, "✅").queue();
                                    } catch (Exception e) {
                                    }
                                } else {
                                    channel.deleteMessageById(idMessageNowPlaying).complete();
                                    idMessageNowPlaying = "";
                                    playlistLoaded(playlist);
                                }
                            } else {
                                channel.sendMessage("No result").queue();
                            }

//                            AudioTrack track = playlist.getTracks().get(0);
//                            play(musicManager, track);
//                            channel.sendMessage("YT search not implemented yet ;)").queue();
//                            channel.sendMessage("Adding to queue " + track.getInfo().title).queue();
                        } else {
                            for (AudioTrack track : playlist.getTracks()) {
                                play(musicManager, track);
                            }
                            channel.sendMessage("Adding to queue playlist " + playlist.getName() + " with " + playlist.getTracks().size() + " songs").queue();
                        }
                    }
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

    public void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
        showNowPlaying();
    }

    public void skipTrack(boolean withMsg) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();
        if (withMsg) {
            channel.sendMessage("Skipped the current track.").queue();
        }
    }

    public void changeVolume(String volume, boolean withMsg) {
        if ((channel.getGuild().getAudioManager().isConnected()) || (channel.getGuild().getAudioManager().isAttemptingToConnect())) {
            GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
            try {
                if (withMsg) {
                    boolean isOk = musicManager.scheduler.changeVolume(Integer.parseInt(volume));
                    if (!isOk) {
                        channel.sendMessage("Volume must be between 1 - 150").queue();
                    } else {
                        channel.sendMessage("Volume is now " + musicManager.scheduler.getVolume()).queue();
                    }
                } else {
                    musicManager.scheduler.changeVolume(Integer.parseInt(volume));
                }
            } catch (NumberFormatException e) {
                channel.sendMessage("Volume must be in number!").queue();
            }
        } else {
            channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
        }
    }

    public void disconnectChannel() {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        AudioManager audioManager = channel.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            nowPlaying.stopNowPlaying();
            musicManager.player.destroy();
            audioManager.closeAudioConnection();
            musicManager.scheduler.clearQueue();
            channel.sendMessage("Bye :kissing_heart:").queue();
        } else {
            channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
        }
    }

    public void clearQueue() {
        if (getGuildAudioPlayer(channel.getGuild()).scheduler.clearQueue()) {
            channel.sendMessage("Successfully cleared the queue!").queue();
        } else {
            channel.sendMessage("Queue is already empty!").queue();
        }
    }

    public void shuffleQueue() {
        if (channel.getGuild().getAudioManager().isConnected()) {
            getGuildAudioPlayer(channel.getGuild()).scheduler.shuffleQueue();
            channel.sendMessage("Successfully shuffled the queue!").queue();
        } else {
            channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
        }
    }

    public void showQueue() {
        if (channel.getGuild().getAudioManager().isConnected()) {
            GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
            if (!musicManager.scheduler.getQueue().isEmpty()) {
                ArrayList<AudioTrack> queue = new ArrayList<>(musicManager.scheduler.getQueue());
                String message = "Current queue:\n";
                if (queue.size() > 4) {
                    message += "\n 1.`" + musicManager.player.getPlayingTrack().getInfo().title + "`";
                    for (int i = 0; i < 4; i++) {
                        message += "\n" + (i + 2) + ".`" + queue.get(i).getInfo().title + "`";
                    }
                    message += "\n\nAnd `" + (queue.size() - 5) + "` more...";
                } else if (queue.size() <= 4) {
                    message += "\n 1.`" + musicManager.player.getPlayingTrack().getInfo().title + "`";
                    for (int i = 0; i < queue.size(); i++) {
                        message += "\n" + (i + 2) + ".`" + queue.get(i).getInfo().title + "`";
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

    public void stopMusic(boolean withMsg) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        AudioManager audioManager = channel.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            nowPlaying.stopNowPlaying();
            musicManager.player.destroy();
            musicManager.scheduler.clearQueue();
            if (withMsg) {
                channel.sendMessage("Bye :kissing_heart:").queue();
            }
        } else if (withMsg) {
            channel.sendMessage("Bot is not connected to any channel! Use summon tu summon the bot").queue();
        }
    }

    public void pauseMusic(boolean withMsg) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        if (!musicManager.player.isPaused()) {
            musicManager.player.setPaused(true);
            if (withMsg) {
                channel.sendMessage("Paused").queue();
            }
        } else {
            musicManager.player.setPaused(false);
            if (withMsg) {
                channel.sendMessage("Playing").queue();
            }
        }
    }

    public void showNowPlaying() {
        nowPlaying.showNowPlaying();
    }

    public static void connectToVoiceChannel(AudioManager audioManager, Member user, TextChannel channel) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            if (user.getVoiceState().inVoiceChannel()) {
                audioManager.openAudioConnection(user.getVoiceState().getChannel());
            } else {
                channel.sendMessage(user.getAsMention() + " Master where are you! :scream:").queue();
            }
        } else if (user.getVoiceState().inVoiceChannel()) {
            audioManager.openAudioConnection(user.getVoiceState().getChannel());
        } else {
            channel.sendMessage(user.getAsMention() + " Master where are you! :scream:").queue();
        }
    }

    @Override
    public void onEvent(Event event) {
        ArrayList<String> reactions = new ArrayList<>();
        reactions.add("❌"); //Cancel
        reactions.add("1⃣");
        reactions.add("2⃣");
        reactions.add("3⃣");
        reactions.add("4⃣");
        reactions.add("✅"); //All
        if (event instanceof MessageReactionAddEvent) {
            if (!((MessageReactionAddEvent) event).getUser().equals(jda.getSelfUser())) {
                if (!idMessageNowPlaying.isEmpty() && ((MessageReactionAddEvent) event).getMessageId().equals(idMessageNowPlaying)) {
                    if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(0))
                            || ((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(1))
                            || ((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(2))
                            || ((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(3))
                            || ((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(4))
                            || ((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(5))) {
                        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(0))) {
                            channel.deleteMessageById(idMessageNowPlaying).queue();
                            idMessageNowPlaying = "";
                        }
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(1))) {
                            play(musicManager, tracks.get(0));
                            channel.deleteMessageById(idMessageNowPlaying).queue();
                            idMessageNowPlaying = "";
                        }
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(2))) {
                            play(musicManager, tracks.get(1));
                            channel.deleteMessageById(idMessageNowPlaying).queue();
                            idMessageNowPlaying = "";
                        }
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(3))) {
                            play(musicManager, tracks.get(2));
                            channel.deleteMessageById(idMessageNowPlaying).queue();
                            idMessageNowPlaying = "";
                        }
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(4))) {
                            play(musicManager, tracks.get(3));
                            channel.deleteMessageById(idMessageNowPlaying).queue();
                            idMessageNowPlaying = "";
                        }
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(5))) {
                            for (AudioTrack track : tracks) {
                                play(musicManager, track);
                            }
                            channel.deleteMessageById(idMessageNowPlaying).queue();
                            idMessageNowPlaying = "";
                            channel.sendMessage("Added to queue " + tracks.size() + " tracks").queue();
                        }
                    }
                }
            }
        }
    }
}
