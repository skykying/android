//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.antlersoft.android.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface FieldAccessor {
    String Name() default "";

    FieldType Type() default FieldType.DEFAULT;

    FieldVisibility Visibility() default FieldVisibility.PRIVATE;

    boolean Nullable() default true;

    String DefaultValue() default "";

    boolean Both() default true;
}
