//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.coboltforge.xbox.android.contentxml;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Xml;

import com.coboltforge.xbox.util.xml.IElement;
import com.coboltforge.xbox.util.xml.IHandlerStack;
import com.coboltforge.xbox.util.xml.SimpleAttributes;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

public class SqliteElement implements IElement {
    private ArrayList<String> _tableNames;
    SQLiteDatabase _db;
    private SqliteElement.ReplaceStrategy _replaceStrategy;
    private String _databaseTag;
    static final String[] TABLE_ARRAY = new String[]{"name"};
    static final String TABLE_ELEMENT = "table";
    static final String TABLE_NAME_ATTRIBUTE = "table_name";
    static final String ROW_ELEMENT = "row";

    public static void exportDbAsXmlToStream(SQLiteDatabase var0, Writer var1) throws SAXException, IOException {
        XmlSerializer var2 = Xml.newSerializer();
        var2.setOutput(var1);
        (new SqliteElement(var0, "database")).writeToXML(new SqliteElement.XmlSerializerHandler(var2));
        var2.flush();
    }

    public static void importXmlStreamToDb(SQLiteDatabase var0, Reader var1, SqliteElement.ReplaceStrategy var2) throws SAXException, IOException {
        SqliteElement var3 = new SqliteElement(var0, "database");
        var3.setReplaceStrategy(var2);
        SqliteElement.StackContentHandler var4 = new SqliteElement.StackContentHandler();
        var4.pushHandlerStack(var3.readFromXML(var4));
        Xml.parse(var1, var4);
    }

    public SqliteElement(SQLiteDatabase var1, String var2) {
        this._db = var1;
        this._tableNames = new ArrayList();
        this._databaseTag = var2;
        this._replaceStrategy = SqliteElement.ReplaceStrategy.REPLACE_EXISTING;
    }

    public SqliteElement.ReplaceStrategy getReplaceStrategy() {
        return this._replaceStrategy;
    }

    public void setReplaceStrategy(SqliteElement.ReplaceStrategy var1) {
        this._replaceStrategy = var1;
    }

    public void addTable(String var1) {
        if (!this._tableNames.contains(var1)) {
            this._tableNames.add(var1);
        }

    }

    public void removeTable(String var1) {
        this.getTableNames().remove(var1);
    }

    ArrayList<String> getTableNames() {
        if (this._tableNames.size() == 0) {
            Cursor var1 = this._db.query("sqlite_master", TABLE_ARRAY, "type = 'table'", (String[])null, (String)null, (String)null, (String)null);

            try {
                while(var1.moveToNext()) {
                    String var2 = var1.getString(0);
                    String var3 = var2.toLowerCase();
                    if (!var3.equals("android_metadata") && !var3.equals("sqlite_sequence")) {
                        this._tableNames.add(var2);
                    }
                }
            } finally {
                var1.close();
            }
        }

        return this._tableNames;
    }

    public String getElementTag() {
        return this._databaseTag;
    }

    public DefaultHandler readFromXML(IHandlerStack var1) {
        return new SqliteElement.SqliteElementHandler(var1);
    }

    public void writeToXML(ContentHandler var1) throws SAXException {
        var1.startElement("", "", this.getElementTag(), (Attributes)null);

        for(Iterator var2 = this.getTableNames().iterator(); var2.hasNext(); var1.endElement("", "", "table")) {
            String var3 = (String)var2.next();
            SimpleAttributes var4 = new SimpleAttributes();
            var4.addValue("table_name", var3);
            var1.startElement("", "", "table", var4.getAttributes());
            Cursor var5 = this._db.query(var3, (String[])null, (String)null, (String[])null, (String)null, (String)null, (String)null);

            try {
                if (var5.moveToFirst()) {
                    ContentValues var6 = new ContentValues();

                    do {
                        DatabaseUtils.cursorRowToContentValues(var5, var6);
                        (new com.coboltforge.xbox.android.contentxml.ContentValuesElement(var6, "row")).writeToXML(var1);
                    } while(var5.moveToNext());
                }
            } finally {
                var5.close();
            }
        }

        var1.endElement("", "", this.getElementTag());
    }

    static class XmlSerializerHandler extends DefaultHandler {
        XmlSerializer _serializer;
        boolean _first;

        XmlSerializerHandler(XmlSerializer var1) {
            this._serializer = var1;
            this._first = true;
        }

        public void characters(char[] var1, int var2, int var3) throws SAXException {
            try {
                this._serializer.text(var1, var2, var3);
            } catch (IOException var5) {
                throw new SAXException(var5.getMessage(), var5);
            }
        }

        public void endElement(String var1, String var2, String var3) throws SAXException {
            try {
                this._serializer.endTag(var1, var3);
            } catch (IOException var5) {
                throw new SAXException(var5.getMessage(), var5);
            }
        }

        public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
            try {
                this._serializer.ignorableWhitespace(new String(var1, var2, var3));
            } catch (IOException var5) {
                throw new SAXException(var5.getMessage(), var5);
            }
        }

        public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
            try {
                if (this._first) {
                    this._first = false;
                } else {
                    this._serializer.ignorableWhitespace("\r\n");
                }

                this._serializer.startTag(var1, var3);
                if (var4 != null) {
                    int var5 = var4.getLength();

                    for(int var6 = 0; var6 < var5; ++var6) {
                        this._serializer.attribute(var4.getURI(var6), var4.getQName(var6), var4.getValue(var6));
                    }
                }

            } catch (IOException var7) {
                throw new SAXException(var7.getMessage(), var7);
            }
        }
    }

    class SqliteElementHandler extends DefaultHandler {
        private IHandlerStack _stack;
        private String _currentTable;
        private ContentValues _lastRow;
        private static final long INSERT_FAILED = -1L;

        public SqliteElementHandler(IHandlerStack var2) {
            this._stack = var2;
        }

        public void endElement(String var1, String var2, String var3) throws SAXException {
            this.saveLastRow();
        }

        public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
            this.saveLastRow();
            if (var2.equals("table")) {
                this._currentTable = var4.getValue("table_name");
                if (this._currentTable == null) {
                    throw new SAXException("table_name not found in table element.");
                }

                if (SqliteElement.this.getTableNames().contains(this._currentTable)) {
                    if (SqliteElement.this.getReplaceStrategy() == SqliteElement.ReplaceStrategy.REPLACE_ALL) {
                        SqliteElement.this._db.delete(this._currentTable, (String)null, (String[])null);
                    }
                } else {
                    this._currentTable = null;
                }
            } else if (var2.equals("row")) {
                this._lastRow = new ContentValues();
                com.coboltforge.xbox.android.contentxml.ContentValuesElement var5 = new ContentValuesElement(this._lastRow, "row");
                this._stack.startWithHandler(var5.readFromXML(this._stack), var1, var2, var3, var4);
            }

        }

        private void saveLastRow() {
            if (this._lastRow != null) {
                try {
                    if (this._currentTable != null) {
                        long var1 = SqliteElement.this._db.insert(this._currentTable, (String)null, this._lastRow);
                        if (var1 == -1L) {
                            switch(SqliteElement.this.getReplaceStrategy()) {
                            case REPLACE_ALL:
                                throw new SQLException("Failed to insert row in " + this._currentTable + " after emptying");
                            case REPLACE_EXISTING:
                                SqliteElement.this._db.delete(this._currentTable, "_id = ?", new String[]{this._lastRow.getAsString("_id")});
                                SqliteElement.this._db.insertOrThrow(this._currentTable, (String)null, this._lastRow);
                            }
                        }
                    }
                } finally {
                    this._lastRow = null;
                }
            }

        }
    }

    public static enum ReplaceStrategy {
        REPLACE_ALL,
        REPLACE_EXISTING,
        REPLACE_NONE;

        private ReplaceStrategy() {
        }
    }

    public static class StackContentHandler implements ContentHandler, IHandlerStack {
        private Stack<ContentHandler> _stack = new Stack();

        public StackContentHandler() {
        }

        public void popHandlerStack() {
            this._stack.pop();
        }

        public void pushHandlerStack(DefaultHandler var1) {
            this._stack.push(var1);
        }

        public void startWithHandler(DefaultHandler var1, String var2, String var3, String var4, Attributes var5) throws SAXException {
            this._stack.push(var1);
            var1.startElement(var2, var3, var4, var5);
        }

        public void characters(char[] var1, int var2, int var3) throws SAXException {
            ((ContentHandler)this._stack.peek()).characters(var1, var2, var3);
        }

        public void endDocument() throws SAXException {
            ((ContentHandler)this._stack.peek()).endDocument();
        }

        public void endElement(String var1, String var2, String var3) throws SAXException {
            ((ContentHandler)this._stack.peek()).endElement(var1, var2, var3);
        }

        public void endPrefixMapping(String var1) throws SAXException {
            ((ContentHandler)this._stack.peek()).endPrefixMapping(var1);
        }

        public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
            ((ContentHandler)this._stack.peek()).ignorableWhitespace(var1, var2, var3);
        }

        public void processingInstruction(String var1, String var2) throws SAXException {
            ((ContentHandler)this._stack.peek()).processingInstruction(var1, var2);
        }

        public void setDocumentLocator(Locator var1) {
            ((ContentHandler)this._stack.peek()).setDocumentLocator(var1);
        }

        public void skippedEntity(String var1) throws SAXException {
            ((ContentHandler)this._stack.peek()).skippedEntity(var1);
        }

        public void startDocument() throws SAXException {
            ((ContentHandler)this._stack.peek()).startDocument();
        }

        public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
            ((ContentHandler)this._stack.peek()).startElement(var1, var2, var3, var4);
        }

        public void startPrefixMapping(String var1, String var2) throws SAXException {
            ((ContentHandler)this._stack.peek()).startPrefixMapping(var1, var2);
        }
    }
}
