package commands.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class guildMusicManager {
    public final AudioPlayer player;
    public final trackScheduler scheduler;
    private final audioPlayerSendHandler sendHandler;

    public guildMusicManager(AudioPlayerManager manager){
        this.player = manager.createPlayer();
        this.scheduler = new trackScheduler(this.player);
        this.player.addListener(this.scheduler);
        this.sendHandler = new audioPlayerSendHandler(this.player);
    }

    public audioPlayerSendHandler getSendHandler(){
        return sendHandler;
    }
}
