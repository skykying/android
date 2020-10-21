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
@Target({ElementType.TYPE})
public @interface TableInterface {
    String TableName() default "";

    String ImplementingClassName() default "";

    boolean ImplementingIsAbstract() default true;

    boolean ImplementingIsPublic() default true;
}
