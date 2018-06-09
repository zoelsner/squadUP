package zalone.squadup.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import zalone.squadup.LauncherActivity;
import zalone.squadup.R;
import zalone.squadup.views.home.ProfileFragment;
import zalone.squadup.views.home.reddit.RedditPostFragment;
import zalone.squadup.views.home.status.StatusFragment;

public class HomeActivity extends AppCompatActivity {

    private HomePagerAdapter homePagerAdapter;
    private ViewPager viewPager;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // todo: make all of this readable first of all


        //
        // TODO: verify that this is really being called and passes all checks
        //


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(homePagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // FirebaseAuth.getInstance().signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        updateUI(null);
                    }
                });

        Intent startLoginIntent = new Intent(this, LauncherActivity.class);
        startActivity(startLoginIntent);

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class HomePagerAdapter extends FragmentPagerAdapter {

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                // Feed
                case 0:
                    return StatusFragment.newInstance();

                // Search
                case 1:
                    return RedditPostFragment.newInstance();

                // News
                case 2:
                    return ProfileFragment.newInstance(mAuth.getCurrentUser());

                // Profile
                case 3:
                    return ProfileFragment.newInstance(mAuth.getCurrentUser());

                // Shouldn't be called
                default:
                    return ProfileFragment.newInstance(mAuth.getCurrentUser());
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 3;
        }
    }
}
