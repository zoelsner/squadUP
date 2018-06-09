package zalone.squadup.views.home.reddit;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RedditJSONFetch {
    private final String API_BASE_URL = "http://www.reddit.com/";
    private AsyncHttpClient client;

    public RedditJSONFetch() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl() {
        return API_BASE_URL + "r/fortnitebr.json";

    }

    public void getPosts(JsonHttpResponseHandler handler) {
        String url = getApiUrl();
        client.get(url, handler);
    }
}
