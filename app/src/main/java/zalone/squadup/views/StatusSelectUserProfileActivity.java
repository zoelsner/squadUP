package zalone.squadup.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zalone.squadup.R;
import zalone.squadup.data.Database;
import zalone.squadup.data.User;

public class StatusSelectUserProfileActivity extends Activity {

    public static final String ARG_USER = "user";

    User user;

    @BindView(R.id.epicName)
    TextView epicName;

    @BindView(R.id.userRealName)
    TextView userName;

    @BindView(R.id.squaddingUpAgo)
    TextView squaddingUpAgo;

    @BindView(R.id.locationContainer)
    View locationContainer;

    @BindView(R.id.locationContent)
    TextView locationContent;

    @BindView(R.id.landContainer)
    View landContainer;

    @BindView(R.id.landContent)
    TextView landContent;

    @BindView(R.id.gamemodeContainer)
    View gamemodeContainer;

    @BindView(R.id.gamemodeContent)
    TextView gamemodeContent;

    @BindView(R.id.snapchatContainer)
    View snapchatContainer;

    @BindView(R.id.snapchatContent)
    TextView snapchatContent;

    @BindView(R.id.instagramContainer)
    View instagramContainer;

    @BindView(R.id.instagramContent)
    TextView instagramContent;

    @BindView(R.id.profile)
    ImageView feedProfile;

    @BindView(R.id.followToggle)
    ToggleButton userFollowButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_user_profile);

        ButterKnife.bind(this);

        user = this.getIntent().getExtras().getParcelable(ARG_USER);

        epicName.setText(user.getEpicName());

        userName.setText(user.getName());
        squaddingUpAgo.setText("Squadded up " + user.getFormattedLastSquaddingUp() + " ago");

        if (user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
            Picasso.with(this)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.profile_blank_image)
                    .into(feedProfile);
        }

        locationContent.setText("" + user.getLon() + "N " + user.getLat() + "W");

        if (user.getFavDropSpot() == null || user.getFavDropSpot().equals("")) {
            landContainer.setVisibility(View.GONE);
        } else {
            landContent.setText(user.getFavDropSpot());
        }

        if (user.getFavGamemode() == null || user.getFavGamemode().equals("")) {
            gamemodeContainer.setVisibility(View.GONE);
        } else {
            gamemodeContent.setText(user.getFavGamemode());
        }

        if (user.getSnapchat() == null || user.getSnapchat().equals("")) {
            snapchatContainer.setVisibility(View.GONE);
        } else {
            snapchatContent.setText(user.getSnapchat());
        }

        if (user.getInstagram() == null || user.getInstagram().equals("")) {
            instagramContainer.setVisibility(View.GONE);
        } else {
            instagramContent.setText(user.getInstagram());
        }

        //Array of people I am following

        List<String> userFollowing = Database.get().getCachedUser().getFollowing();
        if (userFollowing == null || userFollowing.equals("")) {
            userFollowing = new ArrayList<>();
        }

        //Check is profile is followed by user
        if (!userFollowing.contains(user.getFirebaseId())) {
            userFollowButton.setChecked(false);
        } else {
            userFollowButton.setChecked(true);
        }
    }

    @OnClick(R.id.followToggle)
    void onFollowToggle() {
        List<String> userFollowing = Database.get().getCachedUser().getFollowing();
        Set<String> userFollowingSet = new HashSet<String>(userFollowing);
        if (userFollowing == null || userFollowing.equals("")) {
            userFollowingSet = new HashSet<String>();
        }

        if (userFollowingSet.contains(user.getFirebaseId())) {
            userFollowingSet.remove(user.getFirebaseId());

        } else {
            if (!userFollowingSet.contains(user.getFirebaseId())) {
                userFollowingSet.add(user.getFirebaseId());
            }
        }

        userFollowing = new ArrayList<>(userFollowingSet);

        if (!userFollowing.contains(user.getFirebaseId())) {
            userFollowButton.setChecked(false);
        } else {
            userFollowButton.setChecked(true);
        }


        DocumentReference userDocRef = Database.get().getUserDocumentReference();
        User userMe = Database.get().getCachedUser();
        userMe.setFollowing(userFollowing);
        userDocRef.set(userMe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }
}





