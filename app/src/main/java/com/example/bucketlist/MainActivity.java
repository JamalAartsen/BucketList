package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener{

    private RecyclerView recyclerView;
    private List<Item> itemList;
    private ItemAdapter itemAdapter;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ItemRoomDatabase db;
    private GestureDetector mGestureDetector;

    public static final int REQUESTCODE = 1;
    public static final String ITEM = "Item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<>();
        db = ItemRoomDatabase.getDatabase(this);

        recyclerView = findViewById(R.id.rv_bucket_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new ItemAdapter(itemList));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemAdd.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });

        //Delete item with long click on the item
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int adapterPosition = recyclerView.getChildAdapterPosition(child);
                    deleteItem(itemList.get(adapterPosition));
                }
            }
        });

        recyclerView.addOnItemTouchListener(this);

        getAllItems();
    }

    private void getAllItems() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                itemList = db.itemDao().getAllItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });
    }

    private void deleteItem(final Item item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().delete(item);
                getAllItems();
            }
        });
    }

    private void insertItem(final Item item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().insert(item);
                getAllItems();
            }
        });
    }

    private void deleteAllItems(final List<Item> items) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().delete(items);
                getAllItems();
            }
        });
    }


    private void updateUI() {
        if (itemAdapter == null) {
                itemAdapter = new ItemAdapter(itemList);
                recyclerView.setAdapter(itemAdapter);
        } else {
            itemAdapter.swapList(itemList);
        }
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
        if (id == R.id.action_delete_item) {
            deleteAllItems(itemList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                Item addItem = data.getParcelableExtra(MainActivity.ITEM);
                insertItem(addItem);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
