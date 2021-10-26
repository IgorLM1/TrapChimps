package commands.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class skipCommand extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final Member self = event.getMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final TextChannel channel = event.getChannel();
        final GuildVoiceState memberVoiceState = self.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("` I need to be in a voice channel. tsc `").queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            channel.sendMessage("` We need to be in the same channel. C'mon come here ( ͡° ͜ʖ ͡°) `").queue();
            return;
        }

        final guildMusicManager musicManager = playerManager.getINSTANCE().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.player;

        if(audioPlayer.getPlayingTrack() == null){
            channel.sendMessage("I can't skipping nothing").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        channel.sendMessage("Skipped the current track").queue();

    }
}
