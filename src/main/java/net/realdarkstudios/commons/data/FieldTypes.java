package net.realdarkstudios.commons.data;

import java.util.List;

public class FieldTypes {
    public static final FieldType<Integer> INTEGER = new FieldType<>(Integer.class, 0);
    public static final FieldType<Double> DOUBLE = new FieldType<>(Double.class, 0.0D);
    public static final FieldType<Long> LONG = new FieldType<>(Long.class, 0L);
    public static final FieldType<String> STRING = new FieldType<>(String.class, "");
    public static final FieldType<Boolean> BOOLEAN = new FieldType<>(Boolean.class, false);
    public static final FieldType<List> LIST = new FieldType<>(List.class, List.of());
    public static final FieldType<LocalDateTimeFT> LOCAL_DATE_TIME = FieldType.custom(LocalDateTimeFT.class, LocalDateTimeFT.now());
}
