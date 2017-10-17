/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot.wrk.music;

import bot.Constant;
import static bot.Constant.jda;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.player.event.TrackStartEvent;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioTrack;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioTrack;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioTrack;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioTrack;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import libs.YouTubeHelper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 *
 * @author virus
 */
public final class NowPlaying implements EventListener, AudioEventListener {

    public final Guild server;
    private Music music;
    private TextChannel channel;
    private GuildMusicManager musicManager;
    private String idMessageNowPlaying;
    private AudioTrack currentTrack;
    private Boolean isPlaying;
    private ArrayList<Color> colorList;
    private Iterator<Color> itColorList;
    private String trackImgUrl;

    public NowPlaying(Guild server, Music music) {
        this.music = music;
        this.server = server;
        channel = Constant.jda.getTextChannelById(Constant.getTextChannelConf().getProperty(this.server.getId()));
        musicManager = music.getGuildAudioPlayer(server);
        idMessageNowPlaying = "";
        musicManager.player.addListener(this);
        Constant.jda.addEventListener(this);
        colorList = new ArrayList<>();
        colorList.add(Color.RED);
        colorList.add(Color.ORANGE);
        colorList.add(Color.YELLOW);
        colorList.add(Color.GREEN);
        colorList.add(Color.CYAN);
        colorList.add(Color.BLUE);
        itColorList = colorList.iterator();
        trackImgUrl = Constant.lambdaMusicIconUrl;
        run();
    }

    public void run() {
        Constant.nowPlayingList.put(server.getId(), this);
        new Thread(() -> {
            while (music.getGuildAudioPlayer(server).player.getPlayingTrack() == null) {
            }
            sendNowPlaying();
            while ((music.getGuildAudioPlayer(server).player.getPlayingTrack() != null) || (!music.getGuildAudioPlayer(server).scheduler.getQueue().isEmpty())) {

                if (!music.getGuildAudioPlayer(server).player.isPaused()) {
                    if (isPlaying) {
                        updateNowPlaying();
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(NowPlaying.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            channel.getManager().setTopic("").queue();
            channel.deleteMessageById(idMessageNowPlaying).complete();
            idMessageNowPlaying = "";
            Constant.nowPlayingList.remove(server.getId());
        }).start();
    }

    public static String getTimestamp(long miliseconds) {
        int seconds = (int) (miliseconds / 1000) % 60;
        int minutes = (int) ((miliseconds / (1000 * 60)) % 60);
        int hours = (int) ((miliseconds / (1000 * 60 * 60)) % 24);

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public void resendNowPlaying() {
        if (isPlaying) {
            channel.deleteMessageById(idMessageNowPlaying).complete();
            idMessageNowPlaying = "";
            sendNowPlaying();
        } else {
            sendNowPaused();
        }
    }

    public void sendNowPlaying() {
        isPlaying = true;
        currentTrack = musicManager.player.getPlayingTrack();

        String title = currentTrack.getInfo().title;
        String position = getTimestamp(currentTrack.getPosition());
        String duration = getTimestamp(currentTrack.getDuration());

        String msg = String.format("[%s](%s)\n\n**Time:** \n[%s / %s]", title, currentTrack.getInfo().uri, position, duration);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(server.getSelfMember().getColor());
        builder.setTitle("Playing:");
        builder.setDescription(msg);
        if (currentTrack instanceof YoutubeAudioTrack) {
            String id = YouTubeHelper.extractVideoIdFromUrl(currentTrack.getInfo().uri);
            trackImgUrl = String.format(Constant.youtubeThumbnailUrl, id);
        } else if (currentTrack instanceof VimeoAudioTrack) {
            trackImgUrl = Constant.vimeoIconUrl;
        } else if (currentTrack instanceof TwitchStreamAudioTrack) {
            trackImgUrl = Constant.twitchIconUrl;
        } else if (currentTrack instanceof SoundCloudAudioTrack) {
            trackImgUrl = Constant.soundcloudIconUrl;
        } else if (currentTrack instanceof BandcampAudioTrack) {
            trackImgUrl = Constant.bandcampIconUrl;
        } else {
            trackImgUrl = Constant.lambdaMusicIconUrl;
        }
        builder.setThumbnail(trackImgUrl);
        builder.setFooter(Constant.jda.getSelfUser().getName(), server.getIconUrl());
        channel.getManager().setTopic("**Playing:** " + title).queue();
        Message theMessage = channel.sendMessage(builder.build()).complete();
        idMessageNowPlaying = theMessage.getId();
        try {
            channel.addReactionById(idMessageNowPlaying, "⏹").complete(true);
            channel.addReactionById(idMessageNowPlaying, "⏯").complete(true);
            channel.addReactionById(idMessageNowPlaying, "⏭").submit();
        } catch (Exception e) {
        }
    }

    public void sendNowPaused() {
        isPlaying = false;
        channel.getManager().setTopic("").queue();
        channel.deleteMessageById(idMessageNowPlaying).complete();

        idMessageNowPlaying = "";

        currentTrack = musicManager.player.getPlayingTrack();

        String title = currentTrack.getInfo().title;
        String position = getTimestamp(currentTrack.getPosition());
        String duration = getTimestamp(currentTrack.getDuration());

        String msg = String.format("[%s](%s)\n\n**Time:** \n[%s / %s]", title, currentTrack.getInfo().uri, position, duration);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(server.getSelfMember().getColor());
        builder.setTitle("Paused:");
        builder.setDescription(msg);
        builder.setThumbnail(trackImgUrl);
        builder.setFooter(Constant.jda.getSelfUser().getName(), server.getIconUrl());
        channel.getManager().setTopic("**Paused:** " + title).queue();
        Message theMessage = channel.sendMessage(builder.build()).complete();
        idMessageNowPlaying = theMessage.getId();
        try {
            channel.addReactionById(idMessageNowPlaying, "⏹").complete(true);
            channel.addReactionById(idMessageNowPlaying, "⏯").complete(true);
            channel.addReactionById(idMessageNowPlaying, "⏭").submit();
        } catch (Exception e) {
        }
    }

    public void updateNowPlaying() {
        if (!musicManager.player.isPaused()) {
            currentTrack = musicManager.player.getPlayingTrack();
            try {
                String title = currentTrack.getInfo().title;
                String position = getTimestamp(currentTrack.getPosition());
                String duration = getTimestamp(currentTrack.getDuration());
                String msg = String.format("[%s](%s)\n\n**Time:** \n[%s / %s]", title, currentTrack.getInfo().uri, position, duration);
                EmbedBuilder builder = new EmbedBuilder();
                if (itColorList.hasNext()) {
                    builder.setColor(itColorList.next());
                } else {
                    itColorList = colorList.iterator();
                    builder.setColor(itColorList.next());
                }
                builder.setTitle("Playing:");
                builder.setDescription(msg);
                builder.setThumbnail(trackImgUrl);
                builder.setFooter(Constant.jda.getSelfUser().getName(), server.getIconUrl());

                channel.getMessageById(idMessageNowPlaying).complete().editMessage(builder.build()).queue();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        ArrayList<String> reactions = new ArrayList<>();
        reactions.add("⏯");
        reactions.add("⏹");
        reactions.add("⏭");
        if (event instanceof MessageReactionAddEvent) {
            if (!((MessageReactionAddEvent) event).getUser().equals(jda.getSelfUser())) {
                if (!idMessageNowPlaying.isEmpty() && ((MessageReactionAddEvent) event).getMessageId().equals(idMessageNowPlaying)) {
                    if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(0))
                            || ((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(1))
                            || ((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(2))) {
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(0))) {
                            if (musicManager.player.isPaused()) {
                                musicManager.player.setPaused(false);
                                channel.getManager().setTopic("").queue();
                                channel.deleteMessageById(idMessageNowPlaying).complete();
                                idMessageNowPlaying = "";
                                sendNowPlaying();
                            } else {
                                sendNowPaused();
                                musicManager.player.setPaused(true);
                            }
                        }
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(1))) {
                            musicManager.scheduler.clearQueue();
                            musicManager.player.stopTrack();
                        }
                        if (((MessageReactionAddEvent) event).getReaction().getEmote().getName().equals(reactions.get(2))) {
                            musicManager.scheduler.nextTrack();
                            musicManager.player.setPaused(false);
                        }
                    } else {
                    }
                }
            }
        }
    }

    @Override
    public void onEvent(AudioEvent event) {
        if (event instanceof TrackStartEvent) {
            channel.getManager().setTopic("").queue();
            channel.deleteMessageById(idMessageNowPlaying).complete();
            sendNowPlaying();
        }
    }
}
