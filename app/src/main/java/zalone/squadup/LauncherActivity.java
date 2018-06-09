package zalone.squadup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import zalone.squadup.data.Database;
import zalone.squadup.data.User;
import zalone.squadup.views.AskWantToSquadUpActivity;
import zalone.squadup.views.EditProfileActivity;
import zalone.squadup.views.HomeActivity;
import zalone.squadup.views.LoginActivity;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Database db = Database.get();

        // LOGGED IN ?

        if (!db.isLoggedIn()) {
            // NO - NOT LOGGED IN

            Intent startLoginIntent = new Intent(this, LoginActivity.class);
            startActivity(startLoginIntent);

            finish();
            return;
        } else {
            // YES - LOGGED IN

            // PROFILE COMPLETE ?

            final LauncherActivity context = this;

            db.getUser(new Database.OnResultListener<User>() {
                @Override
                public void onResult(User user) {
                    if (user == null) {
                        context.finish();
                        return;
                    }

                    if (user.isCompleteProfile()) {
                        // YES - PROFILE COMPLETE

                        // SHOULD ASK "SQUAD UP?" ?

                        if (user.shouldAskWantToSquadUp()) {
                            // YES - SHOULD ASK "SQUAD UP?"

                            Intent startAskWantToSquadUpIntent = new Intent(context, AskWantToSquadUpActivity.class);
                            startAskWantToSquadUpIntent.putExtra(AskWantToSquadUpActivity.ARG_USER, user);
                            startActivity(startAskWantToSquadUpIntent);

                            context.finish();
                            return;
                        } else {
                            // NO - SHOULD NOT ASK "SQUAD UP?"

                            Intent startHomeIntent = new Intent(context, HomeActivity.class);
                            startActivity(startHomeIntent);

                            context.finish();
                            return;
                        }
                    } else {
                        // NO - PROFILE NOT COMPLETE

                        Intent startEditProfileIntent = new Intent(context, EditProfileActivity.class);
                        startEditProfileIntent.putExtra(EditProfileActivity.ARG_USER, user);
                        startActivity(startEditProfileIntent);

                        context.finish();
                        return;
                    }
                }
            });
        }
    }
}
