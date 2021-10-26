package commands.music.lavaplayer;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class playCommand extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final String[] message = event.getMessage().getContentRaw().split(" ");
        // Gambiarra pra tratar o message, pensar em algo melhor depois
        String music = "";

        if(message.length != 2) {
            for (int i = 1; i < message.length; i++) {
                music += message[i] + " ";
            }
        } else music = message[1];

        final AudioManager audioManager = event.getGuild().getAudioManager();
        final TextChannel channel = event.getChannel();
        final VoiceChannel connectedChannel = event.getMember().getVoiceState().getChannel();
        final Member self = event.getMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final GuildVoiceState memberVoiceState = self.getVoiceState();

        if(event.getGuild().getSelfMember().hasPermission(channel, Permission.VOICE_CONNECT)){
            // Esse caso deveria pegar a falta de permissão do bot, porém não está funcionando tão bem assim.
            channel.sendMessage("` I don't have permissions to entry in this channel, bitch! Do your thing with the server roles`").queue();
            return;
        }

        if(connectedChannel == null){
            // Caso o usuário não esteja em um canal de voz
            channel.sendMessage("`Are you dumb? You need to be in a channel before use $play command`").queue();
            return;
        }

        if(message.length == 1 ){
            // Caso não seja passado nenhum argumento no comando
            channel.sendMessage("`The correct usage is $play <argument>. Don't make me go grab my glock`").queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            channel.sendMessage("` We need to be in the same channel. C'mon come here ( ͡° ͜ʖ ͡°) `").queue();
            return;
        }

        String link = String.join(" ", music);

        if(!isUrl(link)){
            link = "ytsearch:" + link;
        }

        audioManager.openAudioConnection(connectedChannel);
        playerManager.getINSTANCE().loadAndPlay(channel, link);
    }

    private boolean isUrl(String link){
        String urlRegex = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(link);
        if(matcher.find()) {
            return true;
        } return false;
    }
}
