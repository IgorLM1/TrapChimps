package commands.general;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class help extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        // TODO adicionar embed aqui e os auxilios dos comandos
        event.getChannel().sendMessage("I'm the C, the fucking best player on the scene, bitch!").queue();
    }
}
