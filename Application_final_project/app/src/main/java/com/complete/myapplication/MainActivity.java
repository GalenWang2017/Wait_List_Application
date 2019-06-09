package com.complete.myapplication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    Intent intent = new Intent();
    ArrayList<WaitList> dataset = new ArrayList<WaitList>();
    SQLiteDatabase mDB;
    DBHelper dbHelper = new DBHelper(this);
    RecyclerView recyclerview;
    CoordinatorLayout coordinatorLayout ;
    FloatingActionButton fab;
    myAdapter myAdapter;
    private static final int MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE = 88;
    MediaPlayer mPlayer;
    //private AudioInputReader mAudioInputReader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview=(RecyclerView)findViewById(R.id.rv);
        mDB=dbHelper.getReadableDatabase();
        Cursor cursor = mDB.rawQuery("SELECT * FROM waitlist",null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                //String str = "Title:" + cursor.getString(1) + "\nContent:" + cursor.getString(2);
                WaitList wl = new WaitList(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                dataset.add(wl);
            } while (cursor.moveToNext());

            cursor.close();

        }
        myAdapter = new myAdapter(dataset, new myAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(WaitList wl) {
                intent.setClass(MainActivity.this,Check_Item_Info.class);
                Bundle b= new Bundle();
                b.putString("Title",wl.getTitle());
                b.putString("Context",wl.getContext());
                b.putString("CreateTime",wl.getCreatetime());
                b.putString("AlertTime",wl.getTimeout());
                intent.putExtras(b);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"Item Click",Toast.LENGTH_SHORT).show();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button settingbutton = (Button) findViewById(R.id.settingbutton);
        settingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(MainActivity.this,SettingActivity.class);
                startActivity(it);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        coordinatorLayout=findViewById(R.id.mainCoordinatorLayout);

        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //設定分割線
        recyclerview.setLayoutManager(linearLayoutManager); //設定 LayoutManager

        recyclerview.setAdapter(myAdapter); //設定 Adapter
        setUpAnimationDecoratorHelper();
        setUpItemTouchHelper();
        setupPermissions();
        setupSharedPreferences();



        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked();
            }
        });
        FloatingActionButton qfab =findViewById(R.id.question_fab);
        qfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent _intent=new Intent(MainActivity.this,Question.class);
                startActivity(_intent);
                /*Calendar c=Calendar.getInstance();
                c.add(Calendar.SECOND,5);
                Intent testnoitify =new Intent(getApplicationContext(),Notification_reciever.class);
                PendingIntent pendingIntent =PendingIntent.getBroadcast(getApplicationContext(),100,testnoitify,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingIntent);
                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       // mVisualizerView.setShowBass(sharedPreferences.getBoolean(getString(R.string.pref_show_bass_key),
         //       getResources().getBoolean(R.bool.pref_show_bass_default)));
        MediaPlayer mPlayer;
        /*mPlayer =MediaPlayer.create(this,R.raw.music);
            if(sharedPreferences.getBoolean("sp_key",getResources().getBoolean(R.bool.pref_play_music))){
                if(mPlayer!=null){
                    mPlayer.start();
                }

            }*/
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("sp_key")) {
            mPlayer=MediaPlayer.create(this,R.raw.music);
            if(sharedPreferences.getBoolean(key,getResources().getBoolean(R.bool.pref_play_music))){
                if(mPlayer!=null){
                    mPlayer.start();
                }

            }else{
                //mPlayer.pause();
                mPlayer.stop();
                mPlayer.release();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);
    }

    //取得播放音樂權限

    private void setupPermissions() {
        // If we don't have the record audio permission...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // And if we're on SDK M or later...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Ask again, nicely, for the permissions.
                String[] permissionsWeNeed = new String[]{ Manifest.permission.RECORD_AUDIO };
                requestPermissions(permissionsWeNeed, MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE);
            }
        } /*else {
            // Otherwise, permissions were granted and we are ready to go!
            MediaPlayer mPlay = MediaPlayer.create(getApplicationContext(),R.raw.music);
            mPlay.start();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MediaPlayer mPlayer;
                    mPlayer =MediaPlayer.create(getApplicationContext(),R.raw.music);
                    mPlayer.start();
                    // The permission was granted! Start up the visualizer!
                    //mAudioInputReader = new AudioInputReader(mVisualizerView, this);

                } else {
                    Toast.makeText(this, "Can not play music.", Toast.LENGTH_LONG).show();
                    // The permission was denied, so we can show a message why we can't run the app
                    // and then close the app.
                }
            }
            // Other permissions could go down here

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
        if (id == R.id.sbts) {
            ArrayList<WaitList> d =new ArrayList<WaitList>();
            d.addAll(dataset);
            dataset.clear();
            titlecomparator tc = new titlecomparator();
            Collections.sort(d,tc);
            dataset.addAll(d);
            myAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Sort by Title(A-Z)",Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.sbtb){
            ArrayList<WaitList> d =new ArrayList<WaitList>();
            d.addAll(dataset);
            dataset.clear();
            titlecomparatorb tcb = new titlecomparatorb();
            Collections.sort(d,tcb);
            dataset.addAll(d);
            myAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Sort by Title(Z-A)",Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.sbcts){
            ArrayList<WaitList> d =new ArrayList<WaitList>();
            d.addAll(dataset);
            dataset.clear();
            createtimecomparator cts = new createtimecomparator();
            Collections.sort(d,cts);
            dataset.addAll(d);
            myAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Sort by Create time(A-Z)",Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.sbctb){
            ArrayList<WaitList> d =new ArrayList<WaitList>();
            d.addAll(dataset);
            dataset.clear();
            createtimecomparatorb ctb = new createtimecomparatorb();
            Collections.sort(d,ctb);
            dataset.addAll(d);
            myAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Sort by Create time(Z-A)",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void clicked(){

        intent.setClass(MainActivity.this,ChildActivity.class);
        startActivity(intent);
    }

    private void setUpAnimationDecoratorHelper() {
        recyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    private void setUpItemTouchHelper() {


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;


            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_clear);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int)MainActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                myAdapter testAdapter = (myAdapter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int swipedPosition = viewHolder.getAdapterPosition();
                final myAdapter adapter = (myAdapter) recyclerview.getAdapter();
                WaitList item = adapter.getItem(swipedPosition);
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    String sql = "DELETE FROM waitlist WHERE _TITLE=\""+ item.title+"\" AND _CONTENT=\""+item.context+"\"";
                    mDB.execSQL(sql);
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    String sql = "DELETE FROM waitlist WHERE _TITLE=\""+ item.title+"\" AND _CONTENT=\""+item.context+"\"";
                    mDB.execSQL(sql);
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerview);
    }


}
class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder>
{
    private static ArrayList<WaitList> mDataSet;
    private List<WaitList> itemsPendingRemoval = new ArrayList<>();
    private OnItemClickListener mlistener;
    boolean undoOn;
    private Handler handler;
    CoordinatorLayout coordinatorLayout ;
    HashMap<WaitList,Runnable> pendingRunnables = new HashMap<>();

    private static final int PENDING_REMOVAL_TIMEOUT = 500;
    public interface OnItemClickListener{
        void OnItemClick(WaitList wl);
    }

    public myAdapter(ArrayList<WaitList> data,OnItemClickListener listener)
    {
        mDataSet = data;
        mlistener = listener;
    }


    @Override
    public myAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        final WaitList item = mDataSet.get(position);
        holder.bind(mDataSet.get(position),mlistener);
        if (itemsPendingRemoval.contains(item)) {
            // we need to show the "undo" state of the row

            holder.itemView.setBackgroundColor(Color.RED);
            holder.titleItem.setVisibility(View.GONE);
            Runnable pendingRemovalRunnable = pendingRunnables.get(item);
            pendingRunnables.remove(item);
            if (pendingRemovalRunnable != null) coordinatorLayout.removeCallbacks(pendingRemovalRunnable);
            itemsPendingRemoval.remove(item);
            notifyItemChanged(mDataSet.indexOf(item));
        } else {
            //show the "normal" state
            holder.titleItem.setText(mDataSet.get(position).getTitle());
            holder.contextItem.setText(mDataSet.get(position).getCreatetime());

        }


    }

    @Override
    public int getItemCount()
    {
        return mDataSet.size();
    }
    public WaitList getItem(int posi){
        return mDataSet.get(posi);
    }
   // public int getPosition(WaitList item){return mDataSet.indexOf(item);}


    public void remove(int position) {
        WaitList item = mDataSet.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (mDataSet.contains(item)) {
            mDataSet.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void pendingRemoval(int position) {
        final WaitList item = mDataSet.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(mDataSet.indexOf(item));
                }
            };
            coordinatorLayout.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);

        }
    }

    public boolean isPendingRemoval(int position) {
        WaitList item = mDataSet.get(position);
        return itemsPendingRemoval.contains(item);
    }
    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView titleItem,contextItem;
        public ViewHolder(View v)
        {
            super(v);
            titleItem = (TextView) v.findViewById(R.id.title_view);
            contextItem=(TextView)v.findViewById(R.id.context_view);

        }
        public void bind(final WaitList wl, final OnItemClickListener listener){
            titleItem.setText(wl.getTitle());
            contextItem.setText(wl.getCreatetime());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(wl);
                }
            });
        }
    }


}

class titlecomparator implements Comparator<WaitList>{
    @Override
    public int compare(WaitList a,WaitList b){
        int diff=a.title.compareTo(b.title);
        return diff;
    }
}
class titlecomparatorb implements Comparator<WaitList>{
    @Override
    public int compare(WaitList a,WaitList b){
        int diff=a.title.compareTo(b.title);
        return -diff;
    }
}
class createtimecomparator implements Comparator<WaitList>{
    @Override
    public int compare(WaitList a, WaitList b) {
        int diff = a.createtime.compareTo(b.createtime);
        return diff;
    }
}
class createtimecomparatorb implements Comparator<WaitList>{
    @Override
    public int compare(WaitList a, WaitList b) {
        int diff = a.createtime.compareTo(b.createtime);
        return -diff;
    }
}

