package com.avkapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.apache.cordova.*;
import java.util.concurrent.ExecutorService;

public class MainActivity extends ActionBarActivity
                          implements CordovaInterface {

    /**

     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.

     */

    /**

     * Used to store the last screen title. For use in {@link #restoreActionBar()}.

     */

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private String[] mTabs;

    /* Drawer Related */
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CordovaWebView cww;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mTitle = getTitle();
        mDrawerTitle = "AVK'App";
        mTabs = new String[] {"Aide Rapide", "Interactions", "Recherche de patients", "Fiche Patient"};
        cww = (CordovaWebView)findViewById(R.id.webView);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_element, mTabs));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this,
                                                  mDrawerLayout,
                                                  R.drawable.ic_drawer,
                                                  R.string.navigation_drawer_open,
                                                  R.string.navigation_drawer_close) {
            public void onDrawerClosed(View v) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View v) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            selectItem(0);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }

    public Activity getActivity() {
        return this;
    }

    public Object onMessage(String str, Object obj) {

        return null;

    }

    @Override
    public ExecutorService getThreadPool() {
        return null;
    }

    @Override
    public void setActivityResultCallback(CordovaPlugin plugin) {
        this.setActivityResultCallback(plugin);
    }

    /**

     * Launch an activity for which you would like a result when it finished. When this activity exits,

     * your onActivityResult() method is called.

     *

     * @param command           The command object

     * @param intent            The intent to start

     * @param requestCode       The request code that is passed to callback to identify the activity

     */

    public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {

        this.setActivityResultCallback(command);

        // If multitasking turned on, then disable it for activities that return results

        // Start activity

        super.startActivityForResult(intent, requestCode);

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

    }

    public void restoreActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        switch (position) {
            case 0:
                cww.loadUrl("file:///android_asset/www/index.html#/");
                break;
            case 1 :
                cww.loadUrl("file:///android_asset/www/index.html#/interactions");
                break;
            default:
                cww.loadUrl("file:///android_asset/www/index.html#/");
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mTabs[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

}
