package net.realdarkstudios.commons.misc;

import java.util.List;

public interface IYamlStorage<T extends IYamlSerializable, O> {
    void load();

    void save();

    void updateData();


    boolean delete(T object);
    boolean has(O object);

    T create(O object);

    T get(O object);

    T getOrCreate(O object);

    List<T> getAllKnown();
}
