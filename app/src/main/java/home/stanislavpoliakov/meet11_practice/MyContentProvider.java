package home.stanislavpoliakov.meet11_practice;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.Executors;

public class MyContentProvider extends ContentProvider {
    private static final String TAG = "meet11_logs";
    private static final String AUTHORITY = "content_provider";
    private static final String ENTRIES_TABLE = "new_database";
    private static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + ENTRIES_TABLE);

    private static final int ENTRIES = 100;
    private static final int ENTRY_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, ENTRIES_TABLE, ENTRIES);
        uriMatcher.addURI(AUTHORITY, ENTRIES_TABLE + "/#", ENTRY_ID);
    }

    private DatabaseManager mDatabase;
    //private EntryDAO dao;
    //private Handler cpHandler = new Handler();

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mDatabase = DatabaseManager.getInstance(getContext().getApplicationContext());
        return mDatabase != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Метод вставки элемента базы данных на уровне ContentProvider
     * @param uri адрес (ссылка), куда будем вставлять элемент
     * @param values данные записи (Entry), преобразованные к ContentValues через конвертер
     * @return обновленный URI, по которому теперь распологается элемент базы
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        long id;
        if (uriType == ENTRY_ID) {
            id = mDatabase.insertEntry(ConvertUtils.convertValuesToEntry(values));
            Log.d(TAG, "insert: id = " + id);
        } else throw new UnsupportedOperationException("Illegal URI(" + uri + ")");
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    /**
     * Метод обновления элемента базы данных на уровне ContentProvider
     * @param uri адрес (ссылка) изменяемого элемента
     * @param values новые данные для изменения
     * @param selection WHERE - не используется здесь (null)
     * @param selectionArgs WHERE params - не используется здесь (null)
     * @return количество заменнеых элементов
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        int rowsUpdated;
        if (uriType == ENTRY_ID) {
            rowsUpdated = mDatabase.updateEntry(ConvertUtils.convertValuesToEntry(values));
        }
        else throw new UnsupportedOperationException("Illegal URI(" + uri + ")");
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        int rowsDeleted = 0;
        if (uriType == ENTRY_ID) {
            String stringID = uri.getLastPathSegment();
            int id = Integer.parseInt(stringID);
            Log.d(TAG, "delete: ID = " + id);
            mDatabase.deleteEntryById(id);
            //Log.d(TAG, "delete: Rows Deleted = " + rowsDeleted);
        }
        else throw new UnsupportedOperationException("Illegal URI(" + uri + ")");
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static class ConvertUtils {
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

    private void insertData() {

    }
}
