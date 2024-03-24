package net.realdarkstudios.commons.misc;

public interface IYamlStorage<T> {
    void load();

    void save();

    void updateData();

    T getInstance();
}
