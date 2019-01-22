package home.stanislavpoliakov.meet11_practice;

import android.content.ContentValues;

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

}
