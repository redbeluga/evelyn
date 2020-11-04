package me.evelyn;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Evelyn {

    public Evelyn() throws LoginException {

        JDABuilder.createDefault(Config.get("token"))
                .addEventListeners(new Listener())
                .setActivity(Activity.playing("Music!"))
                .build();
    }

    public static void main(String[] args) throws LoginException {
        new Evelyn();
    }
}
