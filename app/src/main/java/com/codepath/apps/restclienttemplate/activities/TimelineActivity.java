package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.ViewPagerAdapter;
import com.codepath.apps.restclienttemplate.fragments.Compose;
import com.codepath.apps.restclienttemplate.fragments.Mentions;
import com.codepath.apps.restclienttemplate.fragments.Timeline;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    Toolbar toolbar;

    TwitterClient twitterClient;
    User self = new User();

    private MenuItem composeAction, userAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        toolbar = findViewById(R.id.main_timeline_toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setIcon(R.drawable.twitter_48x48);
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timeline.recyclerView.smoothScrollToPosition(0);
            }
        });

        twitterClient = TwitterApp.getTwitterClient(this);
        getCurrentUser();

        ViewPager viewPager = findViewById(R.id.main_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.main_tabs);
        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_timeline, menu);
        composeAction = menu.findItem(R.id.action_compose);
        userAction = menu.findItem(R.id.action_user);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_compose:
                Compose compose = Compose.newInstance();
                compose.show(getSupportFragmentManager(), "ComposeFragment");
                return true;
            case R.id.action_user:
                //Intent intent = new Intent(this, ViewSelf.class);
                Intent intent = new Intent(this, ViewUser.class);
                intent.putExtra("user", Parcels.wrap(self));
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCurrentUser() {
        twitterClient.getCurrentUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                self = User.fromJSON(response);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Timeline(), "Timeline");
        adapter.addFragment(new Mentions(), "Mentions");
        viewPager.setAdapter(adapter);
    }

    private Fragment getFocusedFragment(){
        Fragment fragment = null;

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment fragment1 : fragments){
            if(fragment1.isAdded() &&
                    fragment1.isVisible() &&
                    fragment1.getUserVisibleHint()){
                fragment = fragment1;
            }
        }

        return fragment;
    }
}
