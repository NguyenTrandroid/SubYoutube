package ictandroid.youtube.com.Login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.youtube.model.Channel;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.functions.FirebaseFunctions;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import ictandroid.youtube.com.CONST;
import ictandroid.youtube.com.CloudFunction;
import ictandroid.youtube.com.ICloundFunction;
import ictandroid.youtube.com.MainActivity;
import ictandroid.youtube.com.MyApp.MyAppActivity;
import ictandroid.youtube.com.R;
import ictandroid.youtube.com.Utils.CallingYoutube.CallingYoutube;
import ictandroid.youtube.com.Utils.CallingYoutube.GetResultApiListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements GetResultApiListener{

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseFunctions mFunctions;
    private GoogleSignInClient mGoogleSignInClient;
    private ICloundFunction icAddNewUser;
    private ICloundFunction icAddUserSub;
    private CloudFunction cloudFunction;
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    public static int userpoint = 0;
    public static String username = "username";
    public static String useravt = "avt";
    private CallingYoutube callingYoutube;
    String idchannelchecking="";
    private final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /**
         *
         */
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mFunctions = FirebaseFunctions.getInstance();
        initView();
        cloudFunction = new CloudFunction();
        callingYoutube=new CallingYoutube(this);
        icAddNewUser = new ICloundFunction() {
            @Override
            public void onSuccess() {
                Intent intent = getIntent();
                String action = intent.getAction();
                String type = intent.getType();

                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if ("text/plain".equals(type))
                        handleSendText(intent); // Handle text being sent
                } else
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailed() {
                Toast.makeText(LoginActivity.this, "Check your internet", Toast.LENGTH_SHORT).show();
            }
        };
        icAddUserSub = new ICloundFunction() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "Thanh Cong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed() {

                Toast.makeText(LoginActivity.this, "That Bai", Toast.LENGTH_SHORT).show();
            }
        };
        /**
         *
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        signOut();


    }
    private void kiemtra() {
        DocumentReference docRef = db.collection("USERSUB").document(auth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Boolean haves=false;
                        for (Map.Entry<String, Object> entry : task.getResult().getData().entrySet()) {
                            if(String.valueOf(entry.getValue()).equals("finished")){
                            }else if (String.valueOf(entry.getValue()).equals("break")) {
                            } else {
                                String idchannel =String.valueOf(entry.getKey());
                                idchannelchecking=idchannel;
                                Log.d("xawjndqw", idchannel);
                                callingYoutube.checkSubscriberFromApi(idchannel);

                            }

                        }
                    }else {
                        /////////////////////

                    }
                }
            }
        });
    }

    private void initView() {
        imageView = findViewById(R.id.iv_login);
        relativeLayout = findViewById(R.id.rl_login);
        if (getIntent().getStringExtra("action") != null) {
            relativeLayout.setVisibility(View.VISIBLE);
            CallingYoutube.mCredential=null;
        } else {
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type))
                {
                    if (auth.getCurrentUser() != null) {
                        kiemtrakhoitao();
                    } else {
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
            else
            {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
                imageView.setAnimation(animation);
                CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        if (auth.getCurrentUser() != null) {
                            kiemtrakhoitao();
                        } else {
                            relativeLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }.start();
            }

        }
    }

    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    private void kiemtrakhoitao() {
        if(!isConnectingToInternet(LoginActivity.this)){
            Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            relativeLayout.setVisibility(View.VISIBLE);
        }
        db.collection("USER").document(auth.getUid()).get()
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cloudFunction.addNewUser(icAddNewUser);
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                     @Override
                                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                         if (task.isSuccessful()) {
                                               if (task.getResult().exists()) {
                                                 callingYoutube.getChannelFromApi();
                                                 kiemtrataikhoan();

                                             } else {
                                                 cloudFunction.addNewUser(icAddNewUser);
                                             }


                                         } else {
                                             Toast.makeText(LoginActivity.this, "check iternet", Toast.LENGTH_SHORT).show();
                                         }
                                     }


                                 }
        );
    }

    private void kiemtrataikhoan() {
        DocumentReference reference = db.collection("USER").document(auth.getUid());
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    if (Integer.parseInt(String.valueOf(documentSnapshot.get("enable"))) == 0) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("action", "account disable");
                        Toast.makeText(LoginActivity.this, "Account Disable", Toast.LENGTH_LONG).show();
//                            finishAffinity();
                        startActivity(intent);
                        finish();
                    }
                    userpoint = Integer.parseInt(String.valueOf(documentSnapshot.get("points")));
                    username = String.valueOf(documentSnapshot.get("name"));
                    useravt = String.valueOf(documentSnapshot.get("Linkavt"));

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Tsssststs", "onActivityResult: "+requestCode+"/"+resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }else {
            callingYoutube.activityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION_GET_ACCOUNTS){
            callingYoutube.chooseAccount();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // [START_EXCLUDE silent]
//        showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            kiemtrakhoitao();
//                            addNewUser();
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        auth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        updateUI(null);

                    }
                });
    }

    public void logIn(View view) {
        signIn();
//        startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }

    public void signUp(View view) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public void onGetInfoChannel(Channel infoChannel) {
        kiemtra();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type))
                handleSendText(intent); // Handle text being sent
        } else
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onCheckSub(boolean existed) {
        Log.d("xawjndqw", existed+"");
        if(existed){
            cloudFunction.addPointUser(1);
            cloudFunction.addUserSub(idchannelchecking, "finished", new ICloundFunction() {
                @Override
                public void onSuccess() {
                    addHistory(idchannelchecking);
//                    cloudFunction.addHistory(idchannelchecking);
                }

                @Override
                public void onFailed() {

                }
            });
        }else {
            cloudFunction.addUserSub(idchannelchecking,"break",null);
            cloudFunction.addPointChannel(idchannelchecking, 1, new ICloundFunction() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailed() {
                }
            });
        }
    }

    @Override
    public void onCheckSubFail() {

    }

    private void addHistory(String channeldi) {
        DocumentReference docRef = db.collection("LIST").document(channeldi);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        cloudFunction.addHistory(channeldi+"<ict>"+document.getData().get("tenchannel")+"<ict>"+document.getData().get("linkanh"));
                    }else {
                        /////////////////////

                    }
                }
            }
        });
    }
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Intent intentMyApp = new Intent(LoginActivity.this, MyAppActivity.class);
            intentMyApp.putExtra(CONST.SHARE_INTENT,CONST.SHARE_INTENT);
            CONST.SHARE_LINK = sharedText;
            startActivity(intentMyApp);
        }
    }


//    private void kiemtra() {
//        DocumentReference docRef = db.collection("HISTORY").document(auth.getUid());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        for (Map.Entry<String, Object> entry : task.getResult().getData().entrySet()) {
//                            Boolean have = false;
//                            for (int i = 0; i < applist.size(); i++) {
//                                if (
//                                        applist.get(i).getAppPackage().equals(entry.getKey())) {
//                                    have = true;
//                                }
//                            }
//                            if (!have) {
//                                if (String.valueOf(entry.getValue()).equals("finished")) {
//
//                                } else if (String.valueOf(entry.getValue()).equals("break")) {
//
//                                } else if ((System.currentTimeMillis() - Long.parseLong(String.valueOf(entry.getValue())) > 3600000)) {
//                                    xoapointapplistapp2(-1, entry.getKey());
//                                    addDevice(entry.getKey(), "break");
//                                }
//
//                            } else {
//                                if (String.valueOf(entry.getValue()).equals("finished")) {
//
//                                } else if (String.valueOf(entry.getValue()).equals("break")) {
//
//                                } else {
//                                    addDevice(entry.getKey(), "finished");
//                                    addPoint(1);
//                                    addHistory(0, entry.getKey());
//                                }
//                            }
//
//                        }
//                    } else {
////                        addDevice(packagename,"finished");
////                        addPoint(1);
////                        addHistory(0,packagename);
//                    }
//                }
//            }
//        });
//    }


}
