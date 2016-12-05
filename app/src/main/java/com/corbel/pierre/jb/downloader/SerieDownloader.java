package com.corbel.pierre.jb.downloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;

import com.corbel.pierre.jb.R;
import com.corbel.pierre.jb.lib.DbHelper;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.corbel.pierre.jb.lib.Helper.noInternet;

public class SerieDownloader extends AsyncTask<String, Void, Boolean> {

    private ProgressDialog mProgressDialog;
    private Activity activity;

    public SerieDownloader(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle("Mise à jour des questions");
        mProgressDialog.setMessage("Chargement...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... URL) {

        String url = URL[0];
        StringBuilder questions = new StringBuilder();
        String line;
        FileOutputStream output;
        boolean hasFailed = false;
        String version;

        // Alpha for developer
        try {
            version = String.valueOf(activity.getPackageManager().getPackageInfo(activity.getString(R.string.package_name), 0).versionName);
            if (version.contains("alpha")) {
                url = activity.getString(R.string.server_alpha);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // NO-OP
        }

        // Download questions from URL
        try {
            InputStream input = new java.net.URL(url).openStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(input));
            while ((line = r.readLine()) != null) {
                questions.append(line).append("\n");
            }
        } catch (Exception e) {
            hasFailed = true;
        }

        // Storing Questions
        try {
            output = activity.openFileOutput("serie.txt", Context.MODE_PRIVATE);
            output.write(questions.toString().getBytes());
            output.close();
        } catch (IOException e) {
            hasFailed = true;
        }

        return hasFailed;
    }

    @Override
    protected void onPostExecute(Boolean hasFailed) {

        mProgressDialog.dismiss();

        if (hasFailed) {
            noInternet(activity);
        } else {
            Snackbar.make(activity.findViewById(android.R.id.content), activity.getString(R.string.update_success), Snackbar.LENGTH_LONG)
                    .show();
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
            SharedPreferences.Editor mEditor = mPreferences.edit();
            mEditor.putBoolean("SHOULD_UPDATE", false);
            mEditor.putBoolean("IS_DB_READY", true);
            mEditor.putInt("BEST_SCORE", 0);
            mEditor.apply();

            DbHelper db = DbHelper.getInstance(activity);
            db.onUpgrade(db.getWritableDatabase(), 0, 1);
            ShortcutBadger.removeCount(activity);
        }
    }

    @Override
    protected void onCancelled() {
        mProgressDialog.dismiss();
    }
}