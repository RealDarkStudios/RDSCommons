package net.realdarkstudios.commons.data;

import java.time.LocalDateTime;
import java.util.List;

public class FieldType<T> {
    public static final FieldType<Integer> INTEGER = new FieldType<>(Integer.class);
    public static final FieldType<Double> DOUBLE = new FieldType<>(Double.class);
    public static final FieldType<Long> LONG = new FieldType<>(Long.class);
    public static final FieldType<String> STRING = new FieldType<>(String.class);
    public static final FieldType<Boolean> BOOLEAN = new FieldType<>(Boolean.class);
    public static final FieldType<List> LIST = new FieldType<>(List.class);
    public static final FieldType<LocalDateTime> LOCAL_DATE_TIME = new FieldType<>(LocalDateTime.class);

    private final Class<T> clazz;
    FieldType(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <C extends BaseFieldType<?>> FieldType<C> custom(Class<C> clazz) {
        return new FieldType<>(clazz);
    }

    public Class<T> getTypeClass() {
        return clazz;
    }

    public <O extends T> T apply(O obj) {
        return clazz.cast(obj);
    }
}
