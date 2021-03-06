package com.corbel.pierre.jb.lib;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.corbel.pierre.jb.R;
import com.corbel.pierre.jb.app.Jaboog;
import com.google.android.gms.games.Games;

public class LeaderBoardHelper {

    public static void incrementBestScore(Activity activity, int score) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        int bestScore = preferences.getInt("BEST_SCORE", 0);
        editor.putInt("BEST_SCORE", score > bestScore ? score : bestScore);
        editor.apply();

        if (preferences.getBoolean("IS_GOOGLE_CONN", true)) {
            if (Jaboog.getGoogleApiHelper().mGoogleApiClient.isConnected()) {
                Games.Leaderboards.submitScore(Jaboog.getGoogleApiHelper().mGoogleApiClient, activity.getString(R.string.leaderboard_meilleur_score), preferences.getInt("BEST_SCORE", 0));
            }
        }
    }

    public static void incrementPushedPlayed(Activity activity) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        int playedGames = preferences.getInt("PLAYED_GAMES", 0);
        editor.putInt("PLAYED_GAMES", ++playedGames);
        editor.apply();

        if (preferences.getBoolean("IS_GOOGLE_CONN", true)) {
            if (Jaboog.getGoogleApiHelper().mGoogleApiClient.isConnected()) {
                Games.Leaderboards.submitScore(Jaboog.getGoogleApiHelper().mGoogleApiClient, activity.getString(R.string.leaderboard_parties_joues), preferences.getInt("PLAYED_GAMES", 0));
            }
        }
    }

    public static void incrementFinishedGames(Activity activity) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        int finishedGames = preferences.getInt("FINISHED_GAMES", 0);
        editor.putInt("FINISHED_GAMES", ++finishedGames);
        editor.apply();

        if (preferences.getBoolean("IS_GOOGLE_CONN", true)) {
            if (Jaboog.getGoogleApiHelper().mGoogleApiClient.isConnected()) {
                Games.Leaderboards.submitScore(Jaboog.getGoogleApiHelper().mGoogleApiClient, activity.getString(R.string.leaderboard_parties_termines), preferences.getInt("FINISHED_GAMES", 0));
            }
        }
    }

    public static void incrementAnsweredQuestions(Activity activity, int questions) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        int answeredQuestions = preferences.getInt("ANSWERED_QUESTIONS", 0);
        editor.putInt("ANSWERED_QUESTIONS", answeredQuestions + questions);
        editor.apply();

        if (preferences.getBoolean("IS_GOOGLE_CONN", true)) {
            if (Jaboog.getGoogleApiHelper().mGoogleApiClient.isConnected()) {
                Games.Leaderboards.submitScore(Jaboog.getGoogleApiHelper().mGoogleApiClient, activity.getString(R.string.leaderboard_questions_rpondues), preferences.getInt("ANSWERED_QUESTIONS", 0));
            }
        }
    }

    public static void incrementUsedJokers(Activity activity) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        int usedJokers = preferences.getInt("USED_JOKERS", 0);
        editor.putInt("USED_JOKERS", ++usedJokers);
        editor.apply();

        if (preferences.getBoolean("IS_GOOGLE_CONN", true)) {
            if (Jaboog.getGoogleApiHelper().mGoogleApiClient.isConnected()) {
                Games.Leaderboards.submitScore(Jaboog.getGoogleApiHelper().mGoogleApiClient, activity.getString(R.string.leaderboard_jokers_utiliss), preferences.getInt("USED_JOKERS", 0));
            }
        }
    }
}