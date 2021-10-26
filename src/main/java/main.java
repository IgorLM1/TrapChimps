import events.handler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class main {
    public static void main(String args[])throws Exception{
        JDA jda = JDABuilder.createDefault("Your bot credentials here").build();
        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.getPresence().setActivity(Activity.listening("$help"));
        jda.getCacheFlags().add(CacheFlag.VOICE_STATE);
        // TODO Desabilitar a escuta do bot

        jda.addEventListener(new handler());
    }
}
