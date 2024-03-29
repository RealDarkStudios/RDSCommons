package net.realdarkstudios.commons.misc;

import java.util.List;

public interface IYamlStorageWithID<T> {
    void load();

    void save();

    void updateData();

    boolean has(T object);

    boolean delete(T object);

    T create(String id);

    T get(String id);

    T getOrCreate(String id);

    List<T> getAllKnown();
}
