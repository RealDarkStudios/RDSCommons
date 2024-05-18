package net.realdarkstudios.commons.data;

import java.time.LocalDateTime;
import java.util.List;

public class FieldType<T> {
    public static final FieldType<Integer> INTEGER = new FieldType<>(Integer.class, 0);
    public static final FieldType<Double> DOUBLE = new FieldType<>(Double.class, 0.0D);
    public static final FieldType<Long> LONG = new FieldType<>(Long.class, 0L);
    public static final FieldType<String> STRING = new FieldType<>(String.class, "");
    public static final FieldType<Boolean> BOOLEAN = new FieldType<>(Boolean.class, false);
    public static final FieldType<List> LIST = new FieldType<>(List.class, List.of());
    public static final FieldType<LocalDateTime> LOCAL_DATE_TIME = new FieldType<>(LocalDateTime.class, LocalDateTime.now());

    private final Class<T> clazz;
    private final T empty;
    private final boolean isCustom;

    FieldType(Class<T> clazz, T empty) {
        this.clazz = clazz;
        this.empty = empty;
        this.isCustom = false;
    }

    private FieldType(Class<T> clazz, T empty, boolean isCustom) {
        this.clazz = clazz;
        this.empty = empty;
        this.isCustom = isCustom;
    }

    public static <C extends BaseFieldType<?>> FieldType<C> custom(Class<C> clazz, C empty) {
        return new FieldType<>(clazz, empty, true);
    }

    public Class<T> getTypeClass() {
        return clazz;
    }

    public T getEmpty() {
        return empty;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public <O extends T> T apply(O obj) {
        return clazz.cast(obj);
    }
}
