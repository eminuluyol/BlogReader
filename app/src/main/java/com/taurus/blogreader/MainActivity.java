package com.taurus.blogreader;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.taurus.blogreader.adapters.BlogReaderListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import models.Blog;

public class MainActivity extends ListActivity {

    private BlogReaderListViewAdapter mlistViewAdapter;
    private ListView mListView;
    private ArrayList<String> mAndroidNamesList;
    protected JSONObject mBlogData;
    protected CircularProgressBar mCircularProgressBar;

    protected String [] mBlogPostTitles;
    public static final int NUMBER_OF_POSTS = 15;
    public static final String TAG = MainActivity.class.getSimpleName();

    private final String KEY_TITLE = "title";
    private final String KEY_AUTHOR = "author";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircularProgressBar = (CircularProgressBar)findViewById(R.id.progress_bar);
        mCircularProgressBar.setColor(ContextCompat.getColor(this, R.color.progressBarColor));
        mCircularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundProgressBarColor));
        mCircularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
        mCircularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
        int animationDuration = 2500; // 2500ms = 2,5s
        mCircularProgressBar.setProgressWithAnimation(65, animationDuration); // Default duration = 1500ms

        if(isNetworkAvailable()) {
            mCircularProgressBar.setVisibility(View.VISIBLE);
            FetchBlogPostTask fetchBlogPostTask = new FetchBlogPostTask();
            fetchBlogPostTask.execute();
        }else{
            Toast.makeText(this,getString(R.string.network_unavailable),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        try {
            JSONArray jsonPosts = mBlogData.getJSONArray("posts");
            JSONObject jsonPost = jsonPosts.getJSONObject(position);
            String blogUrl = jsonPost.getString("url");

            Intent intent = new Intent(this,BlogWebViewActivity.class);
            intent.setData(Uri.parse(blogUrl));
            startActivity(intent);
        } catch (JSONException e) {
            logException(e);
        }
    }

    private void logException(Exception e) {
        Log.e(TAG, "Exception Caught : ", e);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =(ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        //To use that service we need a Access Network State permission in Android Manifest
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }

        return isAvailable;
    }

    private class FetchBlogPostTask extends AsyncTask<Object,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(Object... params) {
            int responseCode = -1;
            JSONObject jsonResponse = null;

            try {
                URL blogFeedUrl = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS);
                HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
                connection.connect();

                responseCode = connection.getResponseCode();
                //200 is a success response code for HTTP request
                if(responseCode == HttpURLConnection.HTTP_OK){
                    //When a successful request has been made, the data stored in an input stream obj
                    //Inside the connection object.
                    InputStream inputStream = connection.getInputStream();
                    //Input stream is simply data stored as bytes that is a readable which means that
                    //Wee need to use a reader object to read the data
                    Reader reader = new InputStreamReader(inputStream);
                    //Wee need to know how many characters to read in
                    int contentLenght = connection.getContentLength();
                    //We create the array that we want to store our data in.
                    char[] charArray = new char[contentLenght];
                    //Reads from input stream and store the data in a character array
                    reader.read(charArray);
                    //Converts from char array to string object
                    String responseData = new String(charArray);

                    //Creates a jsonObject by using String type responseData
                     jsonResponse = new JSONObject(responseData);

                }
                else{
                    Log.i(TAG,"Unsuccessful HTTP Response Code: " + responseCode);
                }
            } catch (MalformedURLException e) {
                logException(e);
            } catch (IOException e) {
                logException(e);
            }catch (Exception e){
                logException(e);
            };

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mBlogData = result;
            handleBlogResponse();

        }

    }

    private void handleBlogResponse() {
        mCircularProgressBar.setVisibility(View.INVISIBLE);
        if(mBlogData == null){
            updateDisplayForError();
        }
        else{
            try {
                JSONArray jsonPosts = mBlogData.getJSONArray("posts");
               // mBlogPostTitles = new String[jsonPosts.length()];
                ArrayList<Blog> blogPosts = new ArrayList<Blog>();
                for (int i =0; i<jsonPosts.length();i++ ){
                    //To easy access to the jsonObject
                    JSONObject post = jsonPosts.getJSONObject(i);
                    //Then we get the String value of title
                    String title = post.getString(KEY_TITLE);
                    //To convert some unreadeble characters
                    title = Html.fromHtml(title).toString();
                    //Put it in our empty array
                   // mBlogPostTitles[i] = title;

                    //Then we get the String value of title
                    String author = post.getString(KEY_AUTHOR);
                    //To convert some unreadeble characters
                    author = Html.fromHtml(author).toString();

                    Blog blogPost = new Blog(title,author);


                    blogPosts.add(blogPost);
                }

               // mAndroidNamesList =new ArrayList<String>(Arrays.asList(mBlogPostTitles));
                mListView =  (ListView) findViewById(android.R.id.list);

               mlistViewAdapter = new BlogReaderListViewAdapter(this,R.layout.list_view_item,blogPosts);
                mListView.setAdapter(mlistViewAdapter);
            }
            catch (JSONException e) {
                logException(e);
            }
        }
    }

    private void updateDisplayForError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title));
        builder.setMessage(getString(R.string.error_message));
        //If we don't wanna set anything special for a listener just set null
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView emptyTextView = (TextView) getListView().getEmptyView();
        emptyTextView.setText(getString(R.string.no_items));
    }
}
