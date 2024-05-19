package net.realdarkstudios.commons.data;

public class FieldType<T> {
    private final Class<T> clazz;
    private final T def;
    private final boolean isCustom;

    FieldType(Class<T> clazz, T def) {
        this.clazz = clazz;
        this.def = def;
        this.isCustom = false;
    }

    private FieldType(Class<T> clazz, T empty, boolean isCustom) {
        this.clazz = clazz;
        this.def = empty;
        this.isCustom = isCustom;
    }

    public static <T ,C extends BaseFieldType<T>> FieldType<C> custom(Class<C> clazz, C empty) {
        return new FieldType<>(clazz, empty, true);
    }

    public Class<T> getTypeClass() {
        return clazz;
    }

    public T getDefault() {
        return def;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public <O extends T> T apply(O obj) {
        return clazz.cast(obj);
    }
}
