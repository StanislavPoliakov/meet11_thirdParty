package home.stanislavpoliakov.meet11_practice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CRUDable{
    private static final String TAG = "meet11_logs";
    private DatabaseManager dbManager;
    private UIHandler uiHandler = new UIHandler();
    private volatile List<Entry> data;
    private MyAdapter mAdapter;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    public void create(Entry entry) {
        dbManager.insertEntry(entry);
        data.add(entry);
        Log.d(TAG, "create: data = " + data);
    }

    @Override
    public void update(Bundle entryInfo) {
        Entry entry = data.get(entryInfo.getInt("item position"));
        entry.setTitle(entryInfo.getString("title"));
        entry.setText(entryInfo.getString("body"));
        dbManager.updateEntry(entry);
    }

    @Override
    public void delete(Entry entry) {
        dbManager.deleteEntry(entry);
        data.remove(entry);
    }

    private void repaintRecycler() {
        if (data != null || !data.isEmpty()) mAdapter.onNewData(data);
    }

    private class UIHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DatabaseManager.DATABASE_ENTRIES) {
                data = (List<Entry>) msg.obj;
                //Log.d(TAG, "handleMessage: data = " + data);
                initRecyclerView();
            } else if (msg.what == DatabaseManager.REPAINT_REQUEST) {
                repaintRecycler();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(new CreateFragment(), "create dialog")
                            .commitNow();
                });

        init();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if ("Edit".equals(item.getTitle())) initEditDialog(item.getItemId());
        else if ("Delete".equals(item.getTitle())) {
            Entry entry = data.get(item.getItemId());
            delete(entry);
        }
        return super.onContextItemSelected(item);
    }

    private void initEditDialog(int itemPosition) {
        Entry entry = data.get(itemPosition);

        Bundle bundle = new Bundle();
        bundle.putString("title", entry.getTitle());
        bundle.putString("body", entry.getText());
        bundle.putInt("item position", itemPosition);

        EditFragment fragment = new EditFragment();
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .add(fragment, "edit dialog")
                .commitNow();

    }

    private void init() {
        dbManager = DatabaseManager.getInstance(this, uiHandler);
        //dbManager.insertEntry(new Entry("1", "1"));
        dbManager.readEntries();
        //Log.d(TAG, "init: Main thread = " + Thread.currentThread());
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new MyAdapter(data);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
