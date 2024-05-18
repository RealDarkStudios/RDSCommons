package net.realdarkstudios.commons.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public abstract class DataObject {
    private YamlConfiguration yaml;
    private File file;

    public DataObject(YamlConfiguration yaml, File file) {
        this.yaml = yaml;
        this.file = file;
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

    public File getFile() {
        return file;
    }

    /* CUSTOM FIELDS */

    public <T, O extends T> void addField(Plugin plugin, String path, FieldType<T> fieldType, O def) {
        yaml.set(plugin.getName().toLowerCase() + "." + path, fieldType.apply(def));
    }

    public <T, O extends T> void set(Plugin plugin, String path, O toSave) {
        yaml.set(plugin.getName().toLowerCase() + "." + path, toSave);
    }

    public <T> T getField(Plugin plugin, String path, FieldType<T> fieldType) {
        return yaml.getObject(plugin.getName().toLowerCase() + "." + path, fieldType.getTypeClass());
    }

    public int getInt(Plugin plugin, String path) {
        return yaml.getInt(plugin.getName().toLowerCase() + "." + path);
    }

    public double getDouble(Plugin plugin, String path) {
        return yaml.getDouble(plugin.getName().toLowerCase() + "." + path);
    }

    public long getLong(Plugin plugin, String path) {
        return yaml.getLong(plugin.getName().toLowerCase() + "." + path);
    }

    public String getString(Plugin plugin, String path) {
        return yaml.getString(plugin.getName().toLowerCase() + "." + path);
    }

    public boolean getBoolean(Plugin plugin, String path) {
        return yaml.getBoolean(plugin.getName().toLowerCase() + "." + path);
    }

    public List<?> getList(Plugin plugin, String path) {
        return yaml.getList(plugin.getName().toLowerCase() + "." + path);
    }

    public <T> List<T> getList(Plugin plugin, String path, Class<T> listType) {
        return (List<T>) yaml.getList(plugin.getName().toLowerCase() + "." + path);
    }

    /* SAVING / LOADING */

    protected abstract void update();
    protected abstract void preload();

    public void saveData() {
        save();
        update();
    }

    public void load() {
        yaml = YamlConfiguration.loadConfiguration(file);
        yaml.options().parseComments(true);

        preload();

        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        update();
        save();
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            update();

            preload();

            saveData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
