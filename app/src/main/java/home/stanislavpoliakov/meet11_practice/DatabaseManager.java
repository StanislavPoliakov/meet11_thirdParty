package home.stanislavpoliakov.meet11_practice;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Entity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.logging.LogRecord;

public class DatabaseManager {
    public static final int DATABASE_ENTRIES = 1;
    public static final int REPAINT_REQUEST = 2;
    private static final String TAG = "meet11_logs";
    private static DatabaseManager instance;
    private EntryDAO dao;
    private ExecutorService pool;
    private Handler uiHandler;

    private DatabaseManager(Context context, Handler uiHandler) {
        EntryDatabase database = Room.databaseBuilder(context.getApplicationContext(),
                EntryDatabase.class, "new_database")
                .fallbackToDestructiveMigration()
                .build();
        this.dao = database.getEntryDAO();
        this.pool = Executors.newSingleThreadExecutor();
        this.uiHandler = uiHandler;
    }

    public static DatabaseManager getInstance(Context context, Handler uiHandler) {
        if (instance == null) {
            instance = new DatabaseManager(context, uiHandler);
        }
        return instance;
    }

    public void readEntries() {
        CompletableFuture
                .supplyAsync(() -> dao.getEntries(), pool)
                .thenAccept(this::postResult);
    }

    public void insertEntry(Entry entry) {
        CompletableFuture<Void> f = CompletableFuture
                .runAsync(() -> dao.insertEntry(entry), pool)
                .thenRunAsync(this::postRepaint);
    }

    public void updateEntry(Entry entry) {
        CompletableFuture
                .runAsync(() -> dao.updateEntry(entry), pool)
                .thenRunAsync(this::postRepaint);
    }

    public void deleteEntry(Entry entry) {
        CompletableFuture
                .runAsync(() -> dao.deleteEntry(entry))
                .thenRunAsync(this::postRepaint);
    }

    private void postResult(List<Entry> result) {
        Message message = Message.obtain(null, DATABASE_ENTRIES, result);
        uiHandler.sendMessage(message);
    }

    private void postRepaint() {
        Message message = Message.obtain(null, REPAINT_REQUEST);
        Log.d(TAG, "postRepaint: Thread = " + Thread.currentThread());
        uiHandler.sendMessage(message);
    }
}
