//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.antlersoft.android.dbimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Collection;

public abstract class ImplementationBase {
    public ImplementationBase() {
    }

    public abstract void Gen_populate(Cursor var1, int[] var2);

    public abstract void Gen_populate(ContentValues var1);

    public abstract ContentValues Gen_getValues();

    public abstract String Gen_tableName();

    public abstract int[] Gen_columnIndices(Cursor var1);

    public static <E extends ImplementationBase> void Gen_populateFromCursor(Cursor var0, Collection<E> var1, NewInstance<E> var2) {
        if (var0.moveToFirst()) {
            ImplementationBase var3 = var2.get();
            int[] var4 = var3.Gen_columnIndices(var0);

            do {
                if (var3 == null) {
                    var3 = var2.get();
                }

                var3.Gen_populate(var0, var4);
                var1.add((E) var3);
                var3 = null;
            } while(var0.moveToNext());
        }

    }

    public static <E extends ImplementationBase> void getAll(SQLiteDatabase var0, String var1, Collection<E> var2, NewInstance<E> var3) {
        Cursor var4 = var0.query(var1, (String[])null, (String)null, (String[])null, (String)null, (String)null, (String)null);

        try {
            Gen_populateFromCursor(var4, var2, var3);
        } finally {
            var4.close();
        }

    }
}
