package net.realdarkstudios.commons.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class LocalDateTimeFT extends BaseFieldType<LocalDateTime> {
    private final LocalDateTime ldt;

    private LocalDateTimeFT(LocalDateTime time) {
        super(LocalDateTime.class, time);
        this.ldt = time;
    }

    public static LocalDateTimeFT of(LocalDateTime ldt) {
        return new LocalDateTimeFT(ldt);
    }

    public LocalDateTime getLocalDateTime() {
        return ldt;
    }

    public static LocalDateTimeFT now() {
        return of(LocalDateTime.now());
    }

    @Override
    public void toYaml(YamlConfiguration yaml, String key) {
        yaml.set(key, ldt.toString());
    }

    @Override
    public @NotNull LocalDateTime fromYaml(YamlConfiguration yaml, String key) {
        return LocalDateTime.parse(Objects.requireNonNullElse(yaml.getString(key), LocalDateTime.now().toString()));
    }
}
