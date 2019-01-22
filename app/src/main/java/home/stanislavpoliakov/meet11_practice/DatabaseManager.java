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
    private Handler mHandler = new Handler();

    private DatabaseManager(Context context) {
        EntryDatabase database = Room.databaseBuilder(context.getApplicationContext(),
                EntryDatabase.class, "new_database")
                .fallbackToDestructiveMigration()
                .build();
        this.dao = database.getEntryDAO();
        this.pool = Executors.newSingleThreadExecutor();
        //this.uiHandler = uiHandler;
    }

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    public void readEntries() {
        CompletableFuture
                .supplyAsync(() -> dao.getEntries(), pool)
                .thenAccept(this::postResult);
    }

    public long insertEntry(Entry entry) {
        /*CompletableFuture<Void> f = CompletableFuture
                .runAsync(() -> dao.insertEntry(entry), pool)
                .thenRunAsync(this::postRepaint);*/
        try {
            CompletableFuture<Long> completableFuture = CompletableFuture
                    .supplyAsync(() -> dao.insertEntry(entry), pool);
            long result = completableFuture.get();
            completableFuture.thenRunAsync(this::postRepaint, pool);
            return result;
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int updateEntry(Entry entry) {
        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture
                    .supplyAsync(() -> dao.updateEntry(entry), pool);
            int result = completableFuture.get();
            completableFuture.thenRunAsync(this::postRepaint, pool);
            return result;
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return 0;

    }

    public void deleteEntry(Entry entry) {
        CompletableFuture
                .runAsync(() -> dao.deleteEntry(entry))
                .thenRunAsync(this::postRepaint);
    }

    public int deleteEntryById(int id) {
        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture
                    .supplyAsync(() -> dao.deleteEntryById(id), pool);
            int result = completableFuture.get();
            completableFuture.thenRunAsync(this::postRepaint, pool);
            return result;
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void postResult(List<Entry> result) {
        Message message = Message.obtain(null, DATABASE_ENTRIES, result);
        mHandler.sendMessage(message);
    }

    private void postRepaint() {
        Message message = Message.obtain(null, REPAINT_REQUEST);
        //Log.d(TAG, "postRepaint: Thread = " + Thread.currentThread());
        mHandler.sendMessage(message);
        //Log.d(TAG, "postRepaint: what = " + message.what);
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }
}
