package commands.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.collections4.map.HashedMap;

import java.util.List;
import java.util.Map;

public class playerManager {
    private static playerManager INSTANCE;

    private final Map<Long, guildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public playerManager() {
        this.musicManagers = new HashedMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public guildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
           final guildMusicManager guildMusicManager = new guildMusicManager(this.audioPlayerManager);
           guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
           return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl){
        final guildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                channel.sendMessage("Adding to queue: ")
                        .append(audioTrack.getInfo().title)
                        .append("` by `")
                        .append(audioTrack.getInfo().author)
                        .queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                if(audioPlaylist.isSearchResult()) {
                   musicManager.scheduler.queue(tracks.get(0));

                    channel.sendMessage("` Adding to queue: `")
                            .append(tracks.get(0).getInfo().title)
                            .append("` by `")
                            .append(tracks.get(0).getInfo().author)
                            .queue();
                } else{
                    for (final AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);
                    }

                    channel.sendMessage("Adding to queue: ")
                            .append(String.valueOf(tracks.size()))
                            .append("` tracks from playlist `")
                            .append(audioPlaylist.getName())
                            .queue();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    public static playerManager getINSTANCE(){
        if (INSTANCE == null){
            INSTANCE = new playerManager();
        }

        return INSTANCE;
    }
}
