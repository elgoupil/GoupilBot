/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.waiter.EventWaiter;
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
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 *
 * @author goupil
 */
public class Music {

    private final AudioPlayerManager playerManager;
    private final HashMap<Long, GuildMusicManager> musicManagers;

    public Music() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
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

    public void loadAndPlay(final CommandEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());

        playerManager.loadItemOrdered(musicManager, event.getArgs(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                event.replySuccess("Adding to queue " + track.getInfo().title);

                play(event.getGuild(), track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    if (event.getArgs().contains("ytsearch:")) {
                        if (playlist.getTracks().size() > 4) {
                            List<AudioTrack> tracks = playlist.getTracks();
                            String msg = "**Result of search**: " + event.getArgs().substring(9) + "\n **:one:**: " + tracks.get(0).getInfo().title
                                    + "\n **:two:**: " + tracks.get(1).getInfo().title + "\n **:three:**: " + tracks.get(2).getInfo().title
                                    + "\n **:four:**: " + tracks.get(3).getInfo().title + "\n **and more...**";
                            Message theMessage = event.getChannel().sendMessage(msg).complete();
                            String idMessageNowPlaying = theMessage.getId();
                            try {
                                event.getChannel().addReactionById(idMessageNowPlaying, "❌").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "1⃣").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "2⃣").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "3⃣").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "4⃣").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "✅").queue();
                            } catch (Exception e) {
                            }
                        } else {
                            event.replyError("No result");
                        }
                    } else {
                        for (AudioTrack track : playlist.getTracks()) {
                            play(event.getGuild(), track);
                        }
                        event.replySuccess("Adding to queue playlist " + playlist.getName() + " with " + playlist.getTracks().size() + " songs");
                    }
                }
            }

            @Override
            public void noMatches() {
                event.replyWarning("Nothing found by " + event.getArgs());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.replyWarning("Could not play: " + exception.getMessage());
            }
        });
    }

    public void loadAndPlayPlaylist(final CommandEvent event, EventWaiter waiter, String trackName) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());

        playerManager.loadItemOrdered(musicManager, trackName, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                event.replySuccess("Adding to queue " + track.getInfo().title);

                play(event.getGuild(), track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    if (trackName.contains("ytsearch:")) {
                        if (playlist.getTracks().size() > 4) {
                            List<AudioTrack> tracks = playlist.getTracks();
                            String msg = "**Result of search**: " + trackName.substring(9) + "\n **:one:**: " + tracks.get(0).getInfo().title
                                    + "\n **:two:**: " + tracks.get(1).getInfo().title + "\n **:three:**: " + tracks.get(2).getInfo().title
                                    + "\n **:four:**: " + tracks.get(3).getInfo().title + "\n **and more...**";
                            Message theMessage = event.getChannel().sendMessage(msg).complete();
                            String idMessageNowPlaying = theMessage.getId();
                            try {
                                event.getChannel().addReactionById(idMessageNowPlaying, "❌").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "1⃣").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "2⃣").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "3⃣").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "4⃣").queue();
                                event.getChannel().addReactionById(idMessageNowPlaying, "✅").queue();
                            } catch (Exception e) {
                            }
                            waiter.waitForEvent(MessageReactionAddEvent.class, e -> e.getMessageId().equals(idMessageNowPlaying) && e.getChannel().equals(event.getChannel()) && !event.getJDA().getSelfUser().getId().equals(e.getUser().getId()), e -> {
                                ArrayList<String> reactions = new ArrayList<>();
                                reactions.add("❌"); //Cancel
                                reactions.add("1⃣");
                                reactions.add("2⃣");
                                reactions.add("3⃣");
                                reactions.add("4⃣");
                                reactions.add("✅"); //All
                                if ((e).getReaction().getEmote().getName().equals(reactions.get(0))
                                        || (e).getReaction().getEmote().getName().equals(reactions.get(1))
                                        || (e).getReaction().getEmote().getName().equals(reactions.get(2))
                                        || (e).getReaction().getEmote().getName().equals(reactions.get(3))
                                        || (e).getReaction().getEmote().getName().equals(reactions.get(4))
                                        || (e).getReaction().getEmote().getName().equals(reactions.get(5))) {
                                    GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
                                    if ((e).getReaction().getEmote().getName().equals(reactions.get(0))) {
                                        event.getChannel().deleteMessageById(idMessageNowPlaying).queue();
                                        event.getChannel().sendMessage("Canceled search").queue();
                                    }
                                    if ((e).getReaction().getEmote().getName().equals(reactions.get(1))) {
                                        play(event.getGuild(), tracks.get(0));
                                        event.getChannel().sendMessage("Added to queue " + tracks.get(0).getInfo().title).queue();
                                        event.getChannel().deleteMessageById(idMessageNowPlaying).queue();
                                    }
                                    if ((e).getReaction().getEmote().getName().equals(reactions.get(2))) {
                                        play(event.getGuild(), tracks.get(1));
                                        event.getChannel().sendMessage("Added to queue " + tracks.get(1).getInfo().title).queue();
                                        event.getChannel().deleteMessageById(idMessageNowPlaying).queue();
                                    }
                                    if ((e).getReaction().getEmote().getName().equals(reactions.get(3))) {
                                        play(event.getGuild(), tracks.get(2));
                                        event.getChannel().sendMessage("Added to queue " + tracks.get(2).getInfo().title).queue();
                                        event.getChannel().deleteMessageById(idMessageNowPlaying).queue();
                                    }
                                    if ((e).getReaction().getEmote().getName().equals(reactions.get(4))) {
                                        play(event.getGuild(), tracks.get(3));
                                        event.getChannel().sendMessage("Added to queue " + tracks.get(3).getInfo().title).queue();
                                        event.getChannel().deleteMessageById(idMessageNowPlaying).queue();
                                    }
                                    if ((e).getReaction().getEmote().getName().equals(reactions.get(5))) {
                                        for (AudioTrack track : tracks) {
                                            play(event.getGuild(), track);
                                        }
                                        event.getChannel().deleteMessageById(idMessageNowPlaying).queue();
                                        event.getChannel().sendMessage("Added to queue " + tracks.size() + " tracks").queue();
                                    }
                                }
                            }, 1, TimeUnit.MINUTES, () -> {
                                event.getChannel().deleteMessageById(idMessageNowPlaying).queue();
                                event.replyError("Sorry, you took too long");
                            });
                        } else {
                            event.replyError("No result");
                        }
                    } else {
                        for (AudioTrack track : playlist.getTracks()) {
                            play(event.getGuild(), track);
                        }
                        event.replySuccess("Adding to queue playlist " + playlist.getName() + " with " + playlist.getTracks().size() + " songs");
                    }
                }
            }

            @Override
            public void noMatches() {
                event.replyWarning("Nothing found by " + event.getArgs());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.replyWarning("Could not play: " + exception.getMessage());
            }
        });
    }

    public void play(Guild guild, AudioTrack track) {
        getGuildAudioPlayer(guild).scheduler.queue(track);
    }

    public int skipTrack(TextChannel channel) {
        /* 
        0 = empty list
        1 = ok
        2 = player is not playing
        3 = player is not connected
         */
        int res = 0;
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        if (musicManager.player.getPlayingTrack() == null) {
            res = 2;
        } else if (!channel.getGuild().getAudioManager().isConnected()) {
            res = 3;
        } else if (musicManager.scheduler.getQueue().size() == 0) {
            musicManager.scheduler.nextTrack();
            res = 0;
        } else {
            musicManager.scheduler.nextTrack();
            res = 1;
        }
        return res;
    }

    public int connectToVoiceChat(AudioManager manager, Member member) {
        int res = 0;
        if (member.getVoiceState().inVoiceChannel()) {
            manager.openAudioConnection(member.getVoiceState().getChannel());
            getGuildAudioPlayer(manager.getGuild()).scheduler.changeVolume(20);
            res = 1;
        }
        return res;
    }

    public int disconnectFromVoiceChat(AudioManager manager) {
        int res = 0;
        if (manager.isConnected()) {
            manager.closeAudioConnection();
            res = 1;
        }
        return res;
    }

}
