package zalone.squadup.views.home.reddit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import zalone.squadup.R;

public class RedditPostFragment extends Fragment {

    RedditPostRecyclerViewAdapter adapter;

    public RedditPostFragment() {}

    @SuppressWarnings("unused")
    public static RedditPostFragment newInstance() {
        return new RedditPostFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redditpost_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new RedditPostRecyclerViewAdapter(new ArrayList<RedditPost>());
        recyclerView.setAdapter(adapter);

        // Fetch the posts
        fetchPosts();

        return view;
    }

    public void fetchPosts() {
        new RedditJSONFetch().getPosts(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject responseBody) {
                JSONArray items;
                try {
                    JSONObject data = responseBody.getJSONObject("data");
                    items = data.getJSONArray("children");
                    ArrayList <RedditPost> posts = RedditPost.fromList(items);

                    adapter.setPosts(posts);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });
    }
}
