package net.realdarkstudios.commons.misc;

import java.util.List;

public interface IYamlStorage<T> {
    void load();

    void save();

    void updateData();

    boolean has(T object);

    boolean delete(T object);

    T create(Object object);

    T get(Object object);

    T getOrCreate(Object object);

    List<T> getAllKnown();
}
