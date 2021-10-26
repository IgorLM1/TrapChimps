package commands.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageActivity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class queueCommand extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final TextChannel channel = event.getChannel();
        final guildMusicManager musicManager = playerManager.getINSTANCE().getMusicManager(event.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if(queue.isEmpty()){
            channel.sendMessage("The queue is empty!").queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        final AudioPlayer audioPlayer = musicManager.player;
        final AudioTrack track = audioPlayer.getPlayingTrack();
        final AudioTrackInfo info = track.getInfo();
        final MessageAction messageAction = channel.sendMessage("**Current Queue: **\n");
        String playingNow = String.format("Playing now: `%s` by `%s` (Link: <%s>)\n", info.title, info.author, info.uri);
        messageAction.append(playingNow);


        for(int i = 0; i < trackCount; i++){
            final AudioTrack trackqueue = trackList.get(i);
            final AudioTrackInfo infoqueue = trackqueue.getInfo();

            messageAction.append("#")
                    .append(String.valueOf(i + 1))
                    .append(" `")
                    .append(infoqueue.title)
                    .append(" by ")
                    .append(infoqueue.author)
                    .append("` [`")
                    .append(formatTime(trackqueue.getDuration()))
                    .append("`]\n");
        }

        if(trackList.size() > trackCount){
            messageAction.append(" And `")
                    .append(String.valueOf(trackList.size() - trackCount))
                    .append("` more ...");
        }

        messageAction.queue();
    }

    private String formatTime(long duration) {
        final long hours = duration / TimeUnit.HOURS.toMillis(1);
        final long minutes = duration / TimeUnit.MINUTES.toMillis(1);
        final long seconds = duration % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }
}
