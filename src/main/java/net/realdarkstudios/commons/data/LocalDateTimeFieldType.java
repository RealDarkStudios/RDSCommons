package net.realdarkstudios.commons.data;

import net.realdarkstudios.commons.data.BaseFieldType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class LocalDateTimeFieldType extends BaseFieldType<LocalDateTime> {
    public static final LocalDateTimeFieldType NOW = new LocalDateTimeFieldType(LocalDateTime.now());
    private final LocalDateTime ldt;

    public LocalDateTimeFieldType(LocalDateTime time) {
        super(LocalDateTime.class, time);
        this.ldt = time;
    }

    @Override
    public void toYaml(YamlConfiguration yaml, String key) {
        yaml.set(key, ldt.toString());
    }

    @Override
    public @NotNull LocalDateTime fromYaml(YamlConfiguration yaml, String key) {
        return LocalDateTime.parse(yaml.getString(key));
    }
}
