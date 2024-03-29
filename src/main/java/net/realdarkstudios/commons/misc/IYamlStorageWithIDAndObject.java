package net.realdarkstudios.commons.misc;

import java.util.List;

public interface IYamlStorageWithIDAndObject<T extends IYamlSerializable, O> {
    void load();

    void save();

    void updateData();

    boolean delete(T instance);

    boolean has(String id);

    T create(String id);

    T get(String id);

    T getOrCreate(String id);

    boolean delete(O object);

    boolean has(O object);

    T create(O object);

    T get(O object);

    T getOrCreate(O object);

    List<T> getAllKnown();
}
