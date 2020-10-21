//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.coboltforge.xbox.android.dbimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class IdImplementationBase extends ImplementationBase {
    public IdImplementationBase() {
    }

    public abstract long get_Id();

    public abstract void set_Id(long var1);

    private static ContentValues removeId(ContentValues var0) {
        var0.remove("_id");
        return var0;
    }

    public boolean Gen_read(SQLiteDatabase var1, long var2) {
        Cursor var4 = var1.query(this.Gen_tableName(), (String[])null, "_id = ?", new String[]{Long.toString(var2)}, (String)null, (String)null, (String)null);
        boolean var5 = false;
        if (var4.moveToFirst()) {
            this.Gen_populate(var4, this.Gen_columnIndices(var4));
            var5 = true;
        }

        var4.close();
        return var5;
    }

    public boolean Gen_insert(SQLiteDatabase var1) {
        long var2 = var1.insert(this.Gen_tableName(), (String)null, removeId(this.Gen_getValues()));
        if (var2 != -1L) {
            this.set_Id(var2);
            return true;
        } else {
            return false;
        }
    }

    public int Gen_delete(SQLiteDatabase var1) {
        return var1.delete(this.Gen_tableName(), "_id = ?", new String[]{Long.toString(this.get_Id())});
    }

    public int Gen_update(SQLiteDatabase var1) {
        return var1.update(this.Gen_tableName(), removeId(this.Gen_getValues()), "_id = ?", new String[]{Long.toString(this.get_Id())});
    }
}
