package ictandroid.youtube.com;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public Task<String> addUserSub(String channelid,String time, final ICloundFunction iCloundFunction) {
        // Create the arguments to the callable function.
        Map<String,Object> data = new HashMap<>();
        data.put("channelid",channelid);
        data.put("time",time);
        return mFunctions
                .getHttpsCallable("addUserSub")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Log.d("tesss","success");
                        if(iCloundFunction!=null){
                            iCloundFunction.onSuccess();}
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if(iCloundFunction!=null){
                            iCloundFunction.onFailed();}
                    }
                }).continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return null;
                    }
                });
    }
    public Task<String> addPointUser(int Point) {
        Map<String,Object> data = new HashMap<>();
        data.put("points",Point);
        return mFunctions
                .getHttpsCallable("addPoint")
                .call(data)
                .addOnSuccessListener(httpsCallableResult -> {
                })
                .addOnFailureListener(e -> {
                }).continueWith(task -> null);
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
                .addOnSuccessListener(httpsCallableResult -> {
                    Log.d("tesss","success");
                    iCloundFunction.onSuccess();
                })
                .addOnFailureListener(e -> iCloundFunction.onFailed())
                .continueWith(task -> null);
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
    public Task<String> addHistory(String channelid) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("channelid", channelid);
        return mFunctions
                .getHttpsCallable("addHistory")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        Log.d("teststring", result);
                        return result;
                    }
                });
    }
    public Task<String> clearHistory(ICloundFunction iCloundFunction) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        return mFunctions
                .getHttpsCallable("clearHistory")
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
    public Task<String> removeMyChannel(String channelid, final ICloundFunction iCloundFunction) {
        // Create the arguments to the callable function.
        /**
         * Remove point trong myChannel
         */
        Map<String,Object> data = new HashMap<>();
        data.put("channelid",channelid);
        return mFunctions
                .getHttpsCallable("removeMyChannel")
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
    public void addChannel(String channelid,String linkanh,String tenchannel,String point,String douutien){
        addChannelList(channelid,linkanh,tenchannel,point,douutien);
        addChannelUser(channelid,linkanh,tenchannel,point);
    }
    public Task<String> addChannelList(String channelid,String linkanh,String tenchannel,String point,String douutien) {
        // Create the arguments to the callable function.
        Map<String,Object> data = new HashMap<>();
        data.put("channelid",channelid);
        data.put("linkanh",linkanh);
        data.put("tenchannel",tenchannel);
        data.put("points",point);
        data.put("douutien",douutien);
        data.put("time",System.currentTimeMillis()+"");
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
    public Task<String> addChannelUser(String channelid,String linkanh,String tenchannel,String point) {
        // Create the arguments to the callable function.
        Map<String,Object> data = new HashMap<>();
        data.put("channelid",channelid);
        data.put("linkanh",linkanh);
        data.put("tenchannel",tenchannel);
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
