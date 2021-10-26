package events;


import commands.general.help;
import commands.music.lavaplayer.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class handler extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        Boolean isABot = event.getMember().getUser().isBot()? true : false;
        if (!isABot){
            String[] message = event.getMessage().getContentRaw().split(" ");

            // TODO $autoplay e $lyrics o quanto antes.
            // TODO adicionar embeds as mensagens do bot.
            switch (message[0]) {
                case ("$help"):
                    help help = new help();
                    help.onGuildMessageReceived(event);
                    break;
                case ("$play"):
                    playCommand play = new playCommand();
                    play.onGuildMessageReceived(event);
                    break;
                case("$stop"):
                    stopCommand stop = new stopCommand();
                    stop.onGuildMessageReceived(event);
                    break;
                case("$skip"):
                    skipCommand skip = new skipCommand();
                    skip.onGuildMessageReceived(event);
                    break;
                case("$queue"):
                    queueCommand queue = new queueCommand();
                    queue.onGuildMessageReceived(event);
                    break;
                case("$disconnect"):
                    disconnectCommand disconnect = new disconnectCommand();
                    disconnect.onGuildMessageReceived(event);
                    break;
            }

        }


    }
}
