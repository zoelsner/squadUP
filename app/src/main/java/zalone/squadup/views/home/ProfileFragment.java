package zalone.squadup.views.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zalone.squadup.R;
import zalone.squadup.data.Database;
import zalone.squadup.data.User;
import zalone.squadup.views.EditProfileActivity;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_FIREBASE_USER = "firebaseUser";

    private FirebaseUser firebaseUser;
    private User user;

    @BindView(R.id.epicName)
    TextView epicName;

    @BindView(R.id.userRealName)
    TextView userName;

    @BindView(R.id.squaddingUpAgo)
    TextView squaddingUpAgo;

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

    public ProfileFragment() { }

    public static ProfileFragment newInstance(FirebaseUser user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_FIREBASE_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firebaseUser = getArguments().getParcelable(ARG_FIREBASE_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        Database.get().getUser(new Database.OnResultListener<User>() {
            @Override
            public void onResult(User user) {
                setUser(user);
            }
        });

        userName.setText(firebaseUser.getDisplayName());
        Picasso.with(view.getContext()).load(firebaseUser.getPhotoUrl()).into((ImageView) view.findViewById(R.id.profile));

        return view;
    }

    void setUser(User user) {
        this.user = user;

        epicName.setText(user.getEpicName());
        userName.setText(user.getName());
        squaddingUpAgo.setText("Squadded up " + user.getFormattedLastSquaddingUp() + " ago");

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
    }

    @OnClick(R.id.editButton)
    void onEditClick() {
        Intent startEditIntent = new Intent(getActivity(), EditProfileActivity.class);
        startEditIntent.putExtra(EditProfileActivity.ARG_USER, user);

        getActivity().startActivity(startEditIntent);
        getActivity().finish();
    }
}
