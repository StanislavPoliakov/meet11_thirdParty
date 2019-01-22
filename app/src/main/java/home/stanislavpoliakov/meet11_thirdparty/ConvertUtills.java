package home.stanislavpoliakov.meet11_thirdparty;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public final class ConvertUtills {
    private static final String TITLE = "title";
    private static final String TEXT = "entry_text";
    private static final String ID = "id";

    public static Entry convertValuesToEntry(ContentValues contentValues) {
        Entry entry = new Entry(contentValues.getAsString(TITLE),
                contentValues.getAsString(TEXT),
                contentValues.getAsInteger(ID));
        return entry;
    }

    public static ContentValues convertEntryToValues(Entry entry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, entry.getTitle());
        contentValues.put(TEXT, entry.getText());
        contentValues.put(ID, entry.getId());

        return contentValues;
    }

    public static List<Entry> convertCursorToEntryList(Cursor cursor) {
        List<Entry> entryList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String text = cursor.getString(cursor.getColumnIndex("entry_text"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));

            Entry entry = new Entry(title, text, id);
            entryList.add(entry);

            cursor.moveToNext();
        }
        cursor.close();
        //Log.d(TAG, "convertCursorToEntryList: size = " + entryList.size());
        return entryList;
    }
}
