package zalone.squadup.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zalone.squadup.LauncherActivity;
import zalone.squadup.R;
import zalone.squadup.data.Database;
import zalone.squadup.data.User;

public class EditProfileActivity extends Activity implements LocationListener {

    public static final String ARG_USER = "user";

    @BindView(R.id.epic_name_edit_text)
    EditText epicNameEditField;

    @BindView(R.id.favDropSpotText)
    EditText favDropSpotEditField;

    @BindView(R.id.favGamemodeText)
    EditText favGamemodeEditField;

    @BindView(R.id.snapchatText)
    EditText snapchatEditField;

    @BindView(R.id.instagramText)
    EditText instagramEditField;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        user = this.getIntent().getExtras().getParcelable(ARG_USER);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    100,
                    100,
                    this
            );
        }

        if (user.getEpicName() != null) {
            epicNameEditField.setText(user.getEpicName());
        }


//        //TIME ZONE//
//        if (user.gettimeZone() != null) {
//            timezoneContainer.setText(user.gettimezoneContainer());
//        }


        if (user.getFavDropSpot() != null) {
            favDropSpotEditField.setText(user.getFavDropSpot());
        }

        if (user.getFavGamemode() != null) {
            favGamemodeEditField.setText(user.getFavGamemode());
        }

        if (user.getSnapchat() != null) {
            snapchatEditField.setText(user.getSnapchat());
        }

        if (user.getInstagram() != null) {
            instagramEditField.setText(user.getInstagram());
        }

        epicNameEditField.requestFocus();
    }

    @OnClick(R.id.submit_edit_profile_button)
    void onSubmit() {
        epicNameEditField.setEnabled(false);
        favDropSpotEditField.setEnabled(false);
        favGamemodeEditField.setEnabled(false);
        snapchatEditField.setEnabled(false);
        instagramEditField.setEnabled(false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        User updatedUser = new User(
                user.getName(),
                firebaseUser.getPhotoUrl().toString(),
                epicNameEditField.getText().toString(),
                user.getLastAsked(),
                user.getLastSquaddingUp(),
                favDropSpotEditField.getText().toString(),
                favGamemodeEditField.getText().toString(),
                snapchatEditField.getText().toString(),
                instagramEditField.getText().toString(),
                user.getFollowing(),
                user.getLon(),
                user.getLat()
        );

        DocumentReference userDocRef = db.collection("users").document(firebaseUser.getUid());

        final EditProfileActivity context = this;
        userDocRef.set(updatedUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Intent startLauncherActivityIntent = new Intent(context, LauncherActivity.class);
                        context.startActivity(startLauncherActivityIntent);
                        context.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                epicNameEditField.setEnabled(true);
                favDropSpotEditField.setEnabled(true);
                favGamemodeEditField.setEnabled(true);
                snapchatEditField.setEnabled(true);
                instagramEditField.setEnabled(true);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        double lon = ((int) (location.getLongitude() * 100)) / 100d;
        double lat = ((int) (location.getLatitude() * 100)) / 100d;

        user.setLon(lon);
        user.setLat(lat);

        DocumentReference userDocRef = Database.get().getUserDocumentReference();
        userDocRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {}
                });
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
