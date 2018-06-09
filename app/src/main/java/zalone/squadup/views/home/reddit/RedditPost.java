package zalone.squadup.views.home.reddit;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedditPost {

    private String title;
    private String domain;
    private int numComments;
    private String subreddit;
    private String thumbnail;
    private String url;
    private int upvotes;

    public String getTitle() {
        return title;
    }

    public String getDomain() {
        return domain;
    }

    public int getNumComments() {
        return numComments;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getUrl(){
        return url;
    }

    public int getUpvotes(){
        return upvotes;
    }

    @Override
    public String toString() {
        return "RedditPost{" +
                "title='" + title + '\'' +
                ", domain='" + domain + '\'' +
                ", numComments=" + numComments +
                ", subreddit='" + subreddit + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", url='" + url + '\'' +
                ", upvotes=" + upvotes +
                '}';
    }

    // Returns a RedditPost from the given JSON data.
    public static RedditPost fromJson(JSONObject jsonObject){
        RedditPost p = new RedditPost();
        try{
            p.title = jsonObject.getString("title");
            p.domain = jsonObject.getString("domain");
            p.numComments = jsonObject.getInt("num_comments");
            p.subreddit = jsonObject.getString("subreddit");
            p.url = jsonObject.getString("url");
            p.upvotes = jsonObject.getInt("score");

            // Try to get the thumbnailUrl (not all posts have thumbnails)
            try{
                p.thumbnail = jsonObject.getString("thumbnail");
                //Log.d("ZACH", p.thumbnail);

            } catch (JSONException e){
                // There is no thumbnail
                p.thumbnail = "";


            }

        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return p;

    }

    public static ArrayList<RedditPost> fromList(JSONArray jsonArray){
        ArrayList<RedditPost> posts = new ArrayList<>(jsonArray.length());
        //Log.d("howdy", "size initially is " + posts.size());

        // Convert each element in the json array to a json object, then to a Post
        for(int i=0; i<jsonArray.length(); i++){
            try{
                JSONObject postJson = jsonArray.getJSONObject(i).getJSONObject("data");
                RedditPost post = RedditPost.fromJson(postJson);
                //Log.d("howdy", post.toString());
                posts.add(post);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        //Log.d("howdy", "size after is " + posts.size());

        return posts;
    }

}

