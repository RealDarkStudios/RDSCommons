package net.realdarkstudios.commons.data;

import net.realdarkstudios.commons.misc.IYamlSerializable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class BaseFieldType<T> extends FieldType<T> implements IYamlSerializable {
    private final T empty;

    public BaseFieldType(Class<T> clazz, T empty) {
        super(clazz);
        this.empty = empty;
    }

    @NotNull
    public T fromYaml(YamlConfiguration yaml, String key) {
        return empty;
    }
}
