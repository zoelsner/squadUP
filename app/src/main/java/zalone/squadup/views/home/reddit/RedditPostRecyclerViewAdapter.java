package zalone.squadup.views.home.reddit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zalone.squadup.R;

import java.util.ArrayList;
import java.util.List;


public class RedditPostRecyclerViewAdapter extends RecyclerView.Adapter<RedditPostRecyclerViewAdapter.ViewHolder> {

    private List <RedditPost> mPosts;

    public RedditPostRecyclerViewAdapter(List <RedditPost> posts) {
        mPosts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_redditpost, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        RedditPost post = mPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void setPosts(ArrayList <RedditPost> posts) {
        this.mPosts = posts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        RedditPost post;

        @BindView(R.id.tvTitle)
        TextView mTitleView;

        @BindView(R.id.tvUpvotes)
        TextView mUpvotes;

        @BindView(R.id.tvComments)
        TextView mComments;

        @BindView(R.id.tvDomain)
        TextView mDomain;

        @BindView(R.id.ivThumbnail)
        ImageView mThumbnail;

       // @BindView(R.id.tvBackground)
       // ImageView mBackground;

      


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            mView = view;

        }

        public void bind(RedditPost post) {
            this.post = post;

            mTitleView.setText(post.getTitle());
            mUpvotes.setText(String.valueOf(post.getUpvotes()));
            mComments.setText(String.valueOf(post.getNumComments()) + " comments");
            mDomain.setText(String.valueOf(post.getDomain()));

            //mBackground.setBackgroundResource(R.drawable.bckimage);

            //Picasso.with(mThumbnail.getContext()).load(post.getThumbnail());

            if (post.getThumbnail() != null && !post.getThumbnail().equals("self")) {

                Picasso.with(mThumbnail.getContext()).load(post.getThumbnail()).into((ImageView) mThumbnail.findViewById(R.id.ivThumbnail));
            }
        }

        @OnClick(R.id.postWrapper)
        public void onTap() {
            Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW);
            openBrowserIntent.setData(Uri.parse(this.post.getUrl()));
            this.mView.getContext().startActivity(openBrowserIntent);
        }
    }
}
