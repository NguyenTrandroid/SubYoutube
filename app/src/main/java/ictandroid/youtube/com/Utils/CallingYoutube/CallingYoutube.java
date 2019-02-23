package ictandroid.youtube.com.Utils.CallingYoutube;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ictandroid.youtube.com.Dialog.SLoading;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CallingYoutube {
    private Activity activity;
    private SLoading sLoading;
    GetResultApiListener getResultApiListener;
    private String idChannel;
    //
    private GoogleAccountCredential mCredential;
    private final int REQUEST_ACCOUNT_PICKER = 1000;
    private final int REQUEST_AUTHORIZATION = 1001;
    private final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private final String PREF_ACCOUNT_NAME = "accountName";
    private final String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY};

    public CallingYoutube(Activity activity) {
        this.activity = activity;
        mCredential = GoogleAccountCredential.usingOAuth2(
                activity, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        sLoading = new SLoading(activity);
        getResultApiListener = (GetResultApiListener) activity;
    }

    public void getChannelFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            //mOutputText.setText("No network connection available.");
        } else {
            new MakeRequestTask(mCredential, 0).execute();
        }
    }

    public void checkSubscriberFromApi(String idChannel) {
        this.idChannel = idChannel;
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            //mOutputText.setText("No network connection available.");
        } else {
            new MakeRequestTask(mCredential, 1).execute();
        }
    }
    public void clearAccount()
    {
        mCredential.setSelectedAccount(null);
        SharedPreferences settings =
                activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                activity, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = activity.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
//                getChannelFromApi();
                checkSubscriberFromApi(idChannel);
            } else {
                activity.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {

            EasyPermissions.requestPermissions(
                    activity,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != activity.RESULT_OK) {
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getChannelFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == activity.RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                activity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getChannelFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == activity.RESULT_OK) {
                    getChannelFromApi();
                }
                break;
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
    private class MakeRequestTask extends AsyncTask<Void, Void, Void> {
        private com.google.api.services.youtube.YouTube mService = null;
        private Exception mLastError = null;
        private int type;

        MakeRequestTask(GoogleAccountCredential credential, int type) {
            this.type = type;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("YouTube Data API Android Quickstart")
                    .build();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (type == 0) {
                    getDataFromApi();
                } else {
                    checkSubscriber();
                }
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
            }
            return null;
        }

        private void getDataFromApi() throws IOException {
            ChannelListResponse result = mService.channels().list("snippet,contentDetails,statistics")
                    .setMine(true)
                    .execute();

            List<Channel> channels = result.getItems();
            if (channels != null) {
                getResultApiListener.onGetInfoChannel(channels.get(0));
            }
        }


        private void checkSubscriber() throws IOException {
            SubscriptionListResponse result = mService.subscriptions().list("id")
                    .setMine(true)
                    .setForChannelId(idChannel)
                    .execute();
            List<Subscription> subs = result.getItems();
            if(subs.size()==0)
            {
                getResultApiListener.onCheckSub(false);
            }
            else
            {
                getResultApiListener.onCheckSub(true);
            }
        }


        @Override
        protected void onPreExecute() {
            //mOutputText.setText("");
            sLoading.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sLoading.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            sLoading.dismiss();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    activity.startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
                }
            } else {
                //mOutputText.setText("Request cancelled.");
            }
        }
    }
}
