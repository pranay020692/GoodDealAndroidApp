package com.example.tonyhuang.gooddealapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String myString = "main";
    // Declaring Your View and Variables
    DealsTab dealsTab;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Search","Deals","History", "Wish List"};
    int Numboftabs = 4;
    Button compareBtn, barcodeBtn;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* bundle = new Bundle();
        bundle.putString("list", "noList");
// set Fragmentclass Arguments
        dealsTab = new DealsTab();
        dealsTab.setArguments(bundle);*/

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs,"main");

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);



        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        /*DealsTab dealstab = (DealsTab)
                getSupportFragmentManager().findFragmentById(R.id.searchTab);
        dealstab.;*/






        /*compareBtn = (Button) findViewById(R.id.button);
        compareBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //startResultListActivity(view);
                FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
// Replace the content of the container
                fts.replace(R.id.searchTab, new DealsTab());
// Append this transaction to the backstack
                fts.addToBackStack(null);
// Commit the changes
                fts.commit();
            }
        });*/
        //startResultListActivity();

    }

    /*ublic void startResultListActivity(){
        Intent intent = new Intent(this, SearchForResultActivity.class);
        //intent.putExtra("trip", trip);
        startActivity(intent);
    }*/

   public void startResultListActivity(View view){
        Intent intent = new Intent(this, DealsTab.class);
        //intent.putExtra("trip", trip);
        startActivity(intent);
    }

    public String getMyData() {
        return myString;
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
