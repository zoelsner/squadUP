package zalone.squadup.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import zalone.squadup.R;
import zalone.squadup.data.Database;
import zalone.squadup.data.User;

public class AskWantToSquadUpActivity extends AppCompatActivity {

    public static final String ARG_USER = "user";

    private User user;
    private boolean working = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_want_to_squad_up);
        ButterKnife.bind(this);

        user = getIntent().getExtras().getParcelable(ARG_USER);
    }

    @OnClick(R.id.yesTryingToSquadUp)
    void onYes() {
        if (working) {
            return;
        }

        working = true;

        final AskWantToSquadUpActivity context = this;
        user.setAskedResult(true);

        Database.get().setUser(user, new Database.OnResultListener<Void>() {
            @Override
            public void onResult(Void result) {
                Intent startHomeActivity = new Intent(context, HomeActivity.class);
                context.startActivity(startHomeActivity);
                context.finish();
            }
        });
    }

    @OnClick(R.id.noNotTryingToSquadUp)
    void onNo() {
        if (working) {
            return;
        }

        working = true;

        final AskWantToSquadUpActivity context = this;
        user.setAskedResult(false);

        Database.get().setUser(user, new Database.OnResultListener<Void>() {
            @Override
            public void onResult(Void result) {
                Intent startHomeActivity = new Intent(context, HomeActivity.class);
                context.startActivity(startHomeActivity);
                context.finish();
            }
        });
    }
}
