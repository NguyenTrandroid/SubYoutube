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

}
