package zalone.squadup.views.home.status;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import zalone.squadup.R;

import zalone.squadup.data.Database;
import zalone.squadup.data.User;
import zalone.squadup.util.CircleTransform;
import zalone.squadup.views.StatusSelectUserProfileActivity;

public class StatusRecyclerViewAdapter extends RecyclerView.Adapter<StatusRecyclerViewAdapter.ViewHolder> {

    private List<User> users;
    private Context context;

    public StatusRecyclerViewAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final User user = users.get(position);

        holder.user = user;

        List<String> weAreFollowing = Database.get().getCachedUser().getFollowing();
        if (weAreFollowing != null && weAreFollowing.contains(user.getFirebaseId())) {
            holder.postWrapper.setBackgroundResource(R.color.friendBackgroundColor);
       }

        // set stuff on the holder like this
        holder.statusText.setText(user.getEpicName());
        holder.statussquadUP.setText("Squadded " +user.getFormattedLastSquaddingUp() + " ago");

        if (user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
            Picasso.with(context)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.fortnite_lizard)
                    .transform(new CircleTransform())
                    .into(holder.profileView);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open profile of this person
                Intent startProfileIntent = new Intent(context, StatusSelectUserProfileActivity.class);
                startProfileIntent.putExtra(StatusSelectUserProfileActivity.ARG_USER, user);

                context.startActivity(startProfileIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public User user;

        @BindView(R.id.status_postWrapper)
        public View postWrapper;

        @BindView(R.id.status_text)
        public TextView statusText;

        @BindView(R.id.status_squadUP)
        public TextView statussquadUP;

        @BindView(R.id.profile)
        public ImageView profileView;


        // Add more views here like the one above

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            ButterKnife.bind(this, view);
        }
    }

}