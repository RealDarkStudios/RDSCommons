package net.realdarkstudios.commons.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class BaseEvent extends Event {
    /**
     * An extension of {@link Event} than adds a default getHandlers() and getHandlerList() implementation.
     *
     * @see Event
     * @since 0.2.0.4
     */
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
