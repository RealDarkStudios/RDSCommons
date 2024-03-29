package net.realdarkstudios.commons.misc;

import org.bukkit.configuration.file.YamlConfiguration;

public interface IYamlSerializable {
    void toYaml(YamlConfiguration yaml, String key);
}
