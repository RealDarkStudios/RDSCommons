package net.realdarkstudios.commons.misc;

import org.bukkit.configuration.file.YamlConfiguration;

public interface IYamlSerializable<T> {
    T fromYaml();

    void toYaml(YamlConfiguration yaml, String key);
}
