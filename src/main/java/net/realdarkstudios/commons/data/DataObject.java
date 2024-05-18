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
        yaml.set(plugin.getName() + "." + path, fieldType.apply(def));
    }

    public <T> T getField(Plugin plugin, String path, FieldType<T> fieldType) {
        return yaml.getObject(plugin.getName() + "." + path, fieldType.getTypeClass());
    }

    public int getInt(Plugin plugin, String path) {
        return yaml.getInt(plugin.getName() + "." + path);
    }

    public double getDouble(Plugin plugin, String path) {
        return yaml.getDouble(plugin.getName() + "." + path);
    }

    public long getLong(Plugin plugin, String path) {
        return yaml.getLong(plugin.getName() + "." + path);
    }

    public String getString(Plugin plugin, String path) {
        return yaml.getString(plugin.getName() + "." + path);
    }

    public boolean getBoolean(Plugin plugin, String path) {
        return yaml.getBoolean(plugin.getName() + "." + path);
    }

    public List<?> getList(Plugin plugin, String path) {
        return yaml.getList(plugin.getName() + "." + path);
    }

    public <T> List<T> getList(Plugin plugin, String path, Class<T> listType) {
        return (List<T>) yaml.getList(plugin.getName() + "." + path);
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
