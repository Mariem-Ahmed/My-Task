package com.example.marim.githubtask;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by marim on 8/21/2017.
 */

public class MainActivity extends AppCompatActivity {

    private String debug = "DEBUG";
    private ListView lv;
    String fork;

    private SwipeRefreshLayout swipeContainer;

    ArrayList<HashMap<String, String>> repoList;

    DB_Model db_model = new DB_Model();

    DB_Helper db = new DB_Helper(this);

    String repoo = "";
    String owner = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Welcome :)");

        repoList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listview);

        new getRepositories().execute();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new getRepositories().execute();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);
                    }
                }, 2000);

            }
        });


        swipeContainer.setColorSchemeResources(
                android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_blue_light);
    }

    private class getRepositories extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            String url = "https://api.github.com/users/square/repos";
            String jsonStr = beginConnection(url);

            Log.e(debug, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray repositories = new JSONArray(jsonStr);
                    db.onUpgrade(db.getWritableDatabase(), 1, 2);
                    for (int i = 0; i < repositories.length(); i++) {
                        JSONObject c = repositories.getJSONObject(i);
                        JSONObject owner = c.getJSONObject("owner");


                        String name = c.getString("name");
                        String description = c.getString("description");
                        String username = owner.getString("login");
                        String fork = c.getString("fork");
                        String repo_url = c.getString("html_url");
                        String owner_url = owner.getString("html_url");

                        db.createRow(name, description, username, fork, repo_url, owner_url);

                        HashMap<String, String> repos = new HashMap<>();

                        repos.put("name", "    " + name + "\n");
                        repos.put("description", "    " + description + "\n");
                        repos.put("username", "    " + username);
                        repos.put("fork", fork);
                        repos.put("repo_url", repo_url);
                        repos.put("owner_url", owner_url);

                        repoList.add(repos);
                    }
                } catch (final JSONException e) {
                    Log.e(debug, "Json parsing error: " + e.getMessage());

                }

            } else {
                Log.e(debug, "Couldn't get json from server.");

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, repoList,
                    R.layout.list_item, new String[]{"name", "description", "username"},
                    new int[]{R.id.name, R.id.description, R.id.username}) {

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    try {

                        db_model = db.readRepository(position + 1);
                        fork = db_model.getFork();

                        if (fork.equals("false")) {
                            view.setBackgroundResource(R.color.colorAccent);
                        } else {
                            view.setBackgroundResource(R.color.colorWhite);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return view;
                }
            };

            lv.setAdapter(adapter);

            lv.setLongClickable(true);
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                    try {
                        db_model = db.readRepository(position + 1);
                        repoo = db_model.getRepo_url();
                        owner = db_model.getOwner_url();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog(repoo, owner);
                    return true;
                }
            });


        }
    }

    public String beginConnection(String reqUrl) {

        String response = null;

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = converter(in);

        } catch (Exception e) {
            Log.e(debug, "Exception: " + e.getMessage());
        }

        return response;
    }

    private String converter(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;

        try {

            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public void dialog(final String repo, final String owner) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you want to go to Repository URL or Owner URL ?!!");
        alert.setPositiveButton("Repository", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(repo));
                startActivity(browserIntent);
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("Owner", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(owner));
                startActivity(browserIntent);
                dialog.dismiss();
            }
        });

        alert.show();
    }

}
