package ictandroid.youtube.com;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class CloudFunction {

    private FirebaseFunctions mFunctions;
    private FirebaseAuth auth;

    public CloudFunction() {
        auth = FirebaseAuth.getInstance();
        mFunctions = FirebaseFunctions.getInstance();
    }

    public Task<String> addNewUser(final ICloundFunction iCloundFunction) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        String name = auth.getCurrentUser().getDisplayName();
        String linkanh = String.valueOf(auth.getCurrentUser().getPhotoUrl());
        data.put("username", name);
        data.put("linkanh", linkanh);

        return mFunctions
                .getHttpsCallable("addNewUser")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Log.d("tesss","success");
                        iCloundFunction.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iCloundFunction.onFailed();
                    }
                }).continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return null;
                    }
                });


    }
    public Task<String> addUserSub(String channelid, final ICloundFunction iCloundFunction) {
        // Create the arguments to the callable function.
        Map<String,Object> data = new HashMap<>();
        data.put("channelid",channelid);
        data.put("time",System.currentTimeMillis());
        return mFunctions
                .getHttpsCallable("addUserSub")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Log.d("tesss","success");
                        iCloundFunction.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iCloundFunction.onFailed();
                    }
                }).continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return null;
                    }
                });
    }
    public Task<String> addPointChannel(String channelid,int Point, final ICloundFunction iCloundFunction) {
        // Create the arguments to the callable function.
        /**
         * THeem xoa point khi click vao channel
         */
        Map<String,Object> data = new HashMap<>();
        data.put("diemadd",Point);
        data.put("channelid",channelid);
        return mFunctions
                .getHttpsCallable("addPointChannel")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Log.d("tesss","success");
                        iCloundFunction.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iCloundFunction.onFailed();
                    }
                }).continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return null;
                    }
                });
    }
    public Task<String> addPointMyChannel(String channelid,int Point, final ICloundFunction iCloundFunction) {
        // Create the arguments to the callable function.
        /**
         * THeem them point trong myChannel
         */
        Map<String,Object> data = new HashMap<>();
        data.put("diemadd",Point);
        data.put("channelid",channelid);
        return mFunctions
                .getHttpsCallable("addPointMyChannel")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Log.d("tesss","success");
                        iCloundFunction.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iCloundFunction.onFailed();
                    }
                }).continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return null;
                    }
                });
    }
    public Task<String> removePointMyChannel(String channelid,int Point, final ICloundFunction iCloundFunction) {
        // Create the arguments to the callable function.
        /**
         * Remove point trong myChannel
         */
        Map<String,Object> data = new HashMap<>();
        data.put("diemadd",Point);
        data.put("channelid",channelid);
        return mFunctions
                .getHttpsCallable("removePointMyChannel")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Log.d("tesss","success");
                        iCloundFunction.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iCloundFunction.onFailed();
                    }
                }).continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return null;
                    }
                });
    }
    public void addChannel(String channelid,String point,String douutien){
        addChannelList(channelid,point,douutien);
        addChannelUser(channelid,point);
    }
    public Task<String> addChannelList(String channelid,String point,String douutien) {
        // Create the arguments to the callable function.
        Map<String,Object> data = new HashMap<>();
        data.put("channelid",channelid);
        data.put("points",point);
        data.put("douutien",douutien);
        data.put("time",System.currentTimeMillis());
        data.put("userid",auth.getCurrentUser().getUid());
        return mFunctions
                .getHttpsCallable("addChannelList")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                }).continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return null;
                    }
                });
    }
    public Task<String> addChannelUser(String channelid,String point) {
        // Create the arguments to the callable function.
        Map<String,Object> data = new HashMap<>();
        data.put("channelid",channelid);
        data.put("points",point);
        return mFunctions
                .getHttpsCallable("addChannelUser")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                }).continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return null;
                    }
                });
    }

}
