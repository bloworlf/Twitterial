package com.codepath.apps.restclienttemplate.activities;

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
import com.codepath.apps.restclienttemplate.adapters.ViewPagerAdapter;
import com.codepath.apps.restclienttemplate.fragments.Compose;
import com.codepath.apps.restclienttemplate.fragments.Mentions;
import com.codepath.apps.restclienttemplate.fragments.Timeline;

import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    Toolbar toolbar;

    private MenuItem composeAction;

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
        inflater.inflate(R.menu.tweet_timeline, menu);
        composeAction = menu.findItem(R.id.action_compose);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_compose:
                Compose compose = Compose.newInstance();
                compose.show(getSupportFragmentManager(), "ComposeFragment");
                return true;
        }
        return super.onOptionsItemSelected(item);
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
