package ictandroid.youtube.com.Campaign.AllChannel;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ictandroid.youtube.com.CONST;
import ictandroid.youtube.com.Campaign.CampaignActivity;
import ictandroid.youtube.com.Campaign.CampaignChanelAdapter;
import ictandroid.youtube.com.Campaign.GetKeySearchCampaign;
import ictandroid.youtube.com.Campaign.ItemChanel;
import ictandroid.youtube.com.Dialog.SLoading;
import ictandroid.youtube.com.Login.LoginActivity;
import ictandroid.youtube.com.MyApp.ItemMyChanel;
import ictandroid.youtube.com.Profile.HistoryAdapter;
import ictandroid.youtube.com.Profile.ItemHistory;
import ictandroid.youtube.com.Profile.ProfileActivity;
import ictandroid.youtube.com.R;
import ictandroid.youtube.com.Utils.GetData.DataChannelOnCampaign;
import ictandroid.youtube.com.Utils.GetData.SubcribersOnCampaign;
import ictandroid.youtube.com.Utils.GetData.Interface.SubscriberOnMyAppListener;
import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public class FragmentAllChannel extends Fragment implements GetSubFromCampaignV2Listener, GetKeySearchCampaign {
    View view;
    RecyclerView recyclerView;
    CampaignChanelAdapter campaignChanelAdapter;
    ArrayList<ItemChanel> appArrayListAllChanel = new ArrayList<>();
    ArrayList<ItemChanel> appArrayList = new ArrayList<>();
    ArrayList<ItemChanel> appArrayListtemp;
    SubcribersOnCampaign subcribersOnCampaign;
    ArrayList<String> old = new ArrayList<>();
    boolean isloaded=false;
    boolean isloaded2=false;
    boolean isend=false;
    FirebaseFirestore db ;
    DocumentSnapshot lastVisible;
    int scroll=0;
    SLoading loadmore;
    GridLayoutManager gridLayoutManager;
    CollectionReference docRef;
    FirebaseAuth auth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=FirebaseFirestore.getInstance();
        loadmore = new SLoading(getContext());
        gridLayoutManager = new GridLayoutManager(getContext(),1);
        auth=FirebaseAuth.getInstance();
        isloaded=false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_channel, container, false);
        recyclerView = view.findViewById(R.id.rv_listCampaign);
        campaignChanelAdapter = new CampaignChanelAdapter(getContext(), appArrayList);
        if(CONST.tagFragmentMyChannel==null)
        {
            String getIDFragment = this.getTag();
            String[] output = getIDFragment.split(":", 4);
            CONST.tagFragmentMyChannel = output[2];
            CONST.IDFragment = output[2];
        }
        initScrollListener();
        loadApp();
        loadmore();


        return view;
    }

    private void loadmore2() {
        Log.d("testqueradsy", "onFailure: "  );
        if(!isend) {
            isloaded=true;
            Log.d("testqueradsy", "onFailure: "  );
            loadmore.dismiss();
            loadmore.show();
            Query next = db.collection("LIST")
                    .orderBy("douutien")
                    .orderBy("time")
                    .orderBy("points").startAt("1")
                    .startAfter(lastVisible)
                    .limit(7);
            next.get()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("testquery", "onFailure: " + e);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {

                            // ...

                            // Get the last visible document
                            if(!isConnectingToInternet(getContext())){
                                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                                loadmore.dismiss();
                            }
                            DocumentReference docRef = db.collection("USERSUB").document(auth.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Boolean haves = false;
                                            for (Map.Entry<String, Object> entry : task.getResult().getData().entrySet()) {
                                                if (String.valueOf(entry.getValue()).equals("finished")) {
                                                    old.add(String.valueOf(entry.getKey()));
                                                }
                                            }
                                        }
                                        appArrayListAllChanel.clear();

                                        for (int i = 0; i < documentSnapshots.size(); i++) {
                                            ItemChanel itemApp = new ItemChanel();
                                            Log.d("testquery", documentSnapshots.getDocuments().get(i).get("tenchannel") + "");
                                            itemApp.setDiem((String) documentSnapshots.getDocuments().get(i).getData().get("points"));
                                            itemApp.setDoUuTien((String) documentSnapshots.getDocuments().get(i).getData().get("douutien"));
                                            itemApp.setLinkIcon((String) documentSnapshots.getDocuments().get(i).getData().get("linkanh"));
                                            itemApp.setTime((String) documentSnapshots.getDocuments().get(i).getData().get("time"));
                                            itemApp.setUserId((String) documentSnapshots.getDocuments().get(i).getData().get("userid"));
                                            itemApp.setNameChanel((String) documentSnapshots.getDocuments().get(i).getData().get("tenchannel"));
                                            itemApp.setChanelId(documentSnapshots.getDocuments().get(i).getId());
                                            if(!old.contains(itemApp.getChanelId())&&Integer.parseInt(itemApp.getDiem())>0) {
                                                appArrayListAllChanel.add(itemApp);
                                            }

                                        }
                                        appArrayList.addAll(appArrayListAllChanel);
                                        Log.d("testsizesss", "onSuccess: "+documentSnapshots.size()+"/"+appArrayListAllChanel.size());
                                        try {
                                            lastVisible = documentSnapshots.getDocuments()
                                                    .get(documentSnapshots.size() - 1);
                                        }catch (Exception e){
                                            isend=true;
                                        }
                                        Log.d("testsizesss", "onSuccess: "+documentSnapshots.size()+"/"+appArrayListAllChanel.size());

                                        if(appArrayList.size()==0){
                                            Log.d("testquerys", lastVisible+"");
                                            isloaded=false;
                                            loadmore2();
                                        }

                                        List<String> listIdChannel = new ArrayList<>();
                                        for (int i = 0; i < appArrayListAllChanel.size(); i++) {
                                            listIdChannel.add(appArrayListAllChanel.get(i).getChanelId());
                                        }

                                        subcribersOnCampaign = new SubcribersOnCampaign();
                                        subcribersOnCampaign.getListSubscripbers(getContext(), CONST.KEY, listIdChannel);

//                            campaignChanelAdapter.notifyDataSetChanged();
//                            isloaded=false;
                                    }
                                    loadmore.dismiss();


                                }});

                        }
                    });
        }
    }
    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                int visibleItemCount = gridLayoutManager.getChildCount();
//                int totalItemCount = gridLayoutManager.getItemCount();
//                int pastVisibleItems = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
//                Log.d("scrollsss", "onScrollStateChanged: "+
//                        gridLayoutManager.getSpanSizeLookup());
//
//                if(pastVisibleItems+visibleItemCount >= totalItemCount){
//                    if(!isloaded) {
//                        loadmore2();
//                    }
//                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

              int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int pastVisibleItems = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                Log.d("scrollsss", "onScrollStateChanged: "+
                        gridLayoutManager.getSpanSizeLookup());

                if(pastVisibleItems+visibleItemCount >= totalItemCount){
                    if(!isloaded) {
                        loadmore2();
                    }
                }
            }
        });
}
    public void loadmore(){
        isloaded2=true;
        Query first = db.collection("LIST")
                .orderBy("douutien")
                .orderBy("time")
                .limit(7);

        first.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("testquery", "onFailure: "+e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        // ...

                        // Get the last visible document
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
                                                old.add(String.valueOf(entry.getKey()));
                                            }
                                        }
                                    }
                                }
                                appArrayListAllChanel.clear();

                                lastVisible = documentSnapshots.getDocuments()
                                        .get(documentSnapshots.size() -1);
                                for (int i = 0; i < documentSnapshots.size(); i++) {
                                    ItemChanel itemApp = new ItemChanel();
                                    itemApp.setDiem((String) documentSnapshots.getDocuments().get(i).getData().get("points"));
                                    itemApp.setDoUuTien((String) documentSnapshots.getDocuments().get(i).getData().get("douutien"));
                                    itemApp.setLinkIcon((String) documentSnapshots.getDocuments().get(i).getData().get("linkanh"));
                                    itemApp.setTime((String) documentSnapshots.getDocuments().get(i).getData().get("time"));
                                    itemApp.setUserId((String) documentSnapshots.getDocuments().get(i).getData().get("userid"));
                                    itemApp.setNameChanel((String) documentSnapshots.getDocuments().get(i).getData().get("tenchannel"));
                                    itemApp.setChanelId(documentSnapshots.getDocuments().get(i).getId());

                                    Log.d("testquery", "loadmore1");
                                    if(!old.contains(itemApp.getChanelId())&&Integer.parseInt(itemApp.getDiem())>0) {

                                        appArrayListAllChanel.add(itemApp);
                                    }



                                }
                                Log.d("testquery", appArrayListAllChanel.size()+"");

                                appArrayList.addAll(appArrayListAllChanel);
                                Log.d("nhaaaaa", appArrayList.size()+"");

                                if(appArrayList.size()<=0){
                                    loadmore2();
                                }
                                List<String> listIdChannel = new ArrayList<>();
                                for (int i = 0; i < appArrayListAllChanel.size(); i++) {
                                    listIdChannel.add(appArrayListAllChanel.get(i).getChanelId());
                                }
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(campaignChanelAdapter);
                                subcribersOnCampaign = new SubcribersOnCampaign();
                                subcribersOnCampaign.getListSubscripbers(getContext(), CONST.KEY, listIdChannel);
                            }

                        });

//
                    }
                });

    }
    private void loadApp() {
        docRef = db.collection("LIST");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                try {
                    if (e != null) {
                        Log.d("DATAAA", "ERROR");
                        return;
                    }
                    if (queryDocumentSnapshots != null) {
                        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                        for (int i = 0; i < documentSnapshots.size(); i++) {
                            if(documentSnapshots.get(i).getData().get("userid").equals(auth.getUid())){
                                CampaignActivity.sEdit.dismiss();
                                if(Integer.parseInt(documentSnapshots.get(i).getData().get("points")+"")==0){
                                    for (int j = 0; j < appArrayList.size(); j++) {
                                        if(appArrayList.get(j).getChanelId().equals(documentSnapshots.get(i).getId())){
                                            appArrayList.remove(j);
                                            campaignChanelAdapter.notifyItemRemoved(j);
                                        }
                                    }
                                }else {
                                    for (int j = 0; j < appArrayList.size(); j++) {
                                        if(appArrayList.get(j).getChanelId().equals(documentSnapshots.get(i).getId())){
                                            appArrayList.get(j).setDiem(documentSnapshots.get(i).get("points")+"");
                                            campaignChanelAdapter.notifyItemChanged(j);
                                        }
                                    }
                                }
                            }



                        }
                    }
                } catch (Exception s) {

                }
            }
        });
        DocumentReference doc2;
        doc2 = db.collection("USERSUB").document(auth.getUid());
        doc2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                try {

                    CampaignActivity.skiemtra.dismiss();
                    CampaignActivity.sopenyoutube.dismiss();
                    for (Map.Entry<String, Object> entry : documentSnapshot.getData().entrySet()) {
                        if (entry.getValue().equals("finished")) {
                            for (int j = 0; j < appArrayList.size(); j++) {
                                if (appArrayList.get(j).getChanelId().equals(entry.getKey())) {
                                    appArrayList.remove(j);
                                    campaignChanelAdapter.notifyItemRemoved(j);
                                }
                            }
                        }

                    }

                } catch (Exception s) {

                }
            }
        });
    }

    @Override
    public void onCompletedSubV2FromActivity(List<SubChannelItem> lisSubChannelItem) {
        for (int i = 0; i < lisSubChannelItem.size(); i++) {
            for (int j = 0; j < appArrayList.size(); j++) {
                if (lisSubChannelItem.get(i).getItems().get(0).getId().equals(appArrayList.get(j).getChanelId())) {
                    appArrayList.get(j)
                            .setSoLuotSub(lisSubChannelItem.get(i)
                                    .getItems()
                                    .get(0)
                                    .getStatistics()
                                    .getSubscriberCount());

                }
            }
        }
        campaignChanelAdapter.notifyDataSetChanged();
        if(isloaded2){
        isloaded=false;}
    }

    @Override
    public void onGetKey(String keySearch) {
        Log.d("AAAAA", "onQueryTextChange: 1 "+keySearch);
        ArrayList<ItemChanel> listTemp = new ArrayList<>();
        listTemp = new ArrayList<>();
        listTemp.clear();
        if (appArrayList != null) {
            if (keySearch.length() == 0) {
                listTemp.addAll(appArrayList);
            } else {
                for (ItemChanel itemChanel : appArrayList) {
                    try {
                        if (itemChanel.getNameChanel().toLowerCase().contains(keySearch.toLowerCase())) {
                            listTemp.add(itemChanel);
                        }
                    } catch (Exception e) {

                    }
                }
            }
            campaignChanelAdapter = new CampaignChanelAdapter(getContext(),listTemp);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            campaignChanelAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(campaignChanelAdapter);
        }
    }
}
