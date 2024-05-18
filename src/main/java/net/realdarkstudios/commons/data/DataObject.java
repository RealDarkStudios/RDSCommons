package net.realdarkstudios.commons.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.time.LocalDateTime;
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

    public <T> void addField(Plugin plugin, String path, FieldType<T> fieldType) {
        yaml.set(plugin.getName().toLowerCase() + "." + path, fieldType.getDefault());
    }

    public <T, O extends T> void addField(Plugin plugin, String path, FieldType<T> fieldType, O def) {
        yaml.set(plugin.getName().toLowerCase() + "." + path, def);
    }

    public <T, O extends T> void set(Plugin plugin, String path, O toSave) {
        yaml.set(plugin.getName().toLowerCase() + "." + path, toSave);
    }

    public <C extends BaseFieldType<?>, O extends C> void set(Plugin plugin, String path, Class<C> fieldType, O toSave) {
        fieldType.cast(toSave).toYaml(yaml, plugin.getName().toLowerCase() + "." + path);
    }

    public <C extends BaseFieldType<?>, O extends C> void set(Plugin plugin, String path, FieldType<C> fieldType, O toSave) {
        fieldType.apply(toSave).toYaml(yaml, plugin.getName().toLowerCase() + "." + path);
    }

    public <T, C extends BaseFieldType<T>> T get(Plugin plugin, String path, FieldType<C> fieldType) {
        return fieldType.getDefault().fromYaml(yaml, plugin.getName().toLowerCase() + "." + path);
    }

    public <C extends BaseFieldType<?>> C get(Plugin plugin, String path, Class<C> clazz) {
        return yaml.getObject(plugin.getName().toLowerCase() + "." + path, clazz);
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

    public LocalDateTime getLocalDateTime(Plugin plugin, String path) {
        return LocalDateTimeFieldType.NOW.fromYaml(yaml, plugin.getName() + "." + path);
    }

    public boolean contains(Plugin plugin, String path) {
        return yaml.contains(plugin.getName().toLowerCase() + "." + path);
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
