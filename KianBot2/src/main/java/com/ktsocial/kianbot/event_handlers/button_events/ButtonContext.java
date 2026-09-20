package com.ktsocial.kianbot.event_handlers.button_events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public final class ButtonContext {

    private final ButtonInteractionEvent event;
    private final Guild guild;
    private final TextChannel channel;

    private ButtonContext(ButtonInteractionEvent event, Guild guild, TextChannel channel) {
        this.event = event;
        this.guild = guild;
        this.channel = channel;
    }

    public static ButtonContext from(ButtonInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null || !(event.getChannel() instanceof TextChannel channel)) {
            return null;
        }
        return new ButtonContext(event, guild, channel);
    }

    public ButtonInteractionEvent event() {
        return event;
    }

    public Guild guild() {
        return guild;
    }

    public TextChannel channel() {
        return channel;
    }
}
