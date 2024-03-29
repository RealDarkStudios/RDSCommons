package net.realdarkstudios.commons.misc;

import java.util.List;

public interface IYamlStorageWithID<T extends IYamlSerializable> {
    void load();

    void save();

    void updateData();

    boolean delete(T object);

    boolean has(String id);

    T create(String id);

    T get(String id);

    T getOrCreate(String id);

    List<T> getAllKnown();
}
