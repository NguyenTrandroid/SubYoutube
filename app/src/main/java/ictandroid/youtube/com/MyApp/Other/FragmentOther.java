package ictandroid.youtube.com.MyApp.Other;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

import ictandroid.youtube.com.MyApp.ItemMyChanel;
import ictandroid.youtube.com.MyApp.MyChanelAdapter;
import ictandroid.youtube.com.R;

public class FragmentOther extends Fragment {
    View view;
    RecyclerView recyclerView;
    MyChanelAdapter myChanelAdapter;
    ArrayList<ItemMyChanel> arrayListAllChanel = new ArrayList<>();
    ArrayList<ItemMyChanel> arrayList = new ArrayList<>();
    String uid;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_channel, container, false);
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
        loadApp();
        return view;
    }

    private void loadApp() {
        recyclerView = view.findViewById(R.id.rv_listMyApp);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("USER").document(uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                try {
                    if (e != null) {
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        for (Map.Entry<String, Object> entry : snapshot.getData().entrySet()) {
                            if ("listadd".equals(entry.getKey())) {
                                Map<String, Object> nestedData = (Map<String, Object>) entry.getValue();
                                for (Map.Entry<String, Object> entryNested : nestedData.entrySet()) {
                                    ItemMyChanel itemMyChanel = new ItemMyChanel();
                                    itemMyChanel.setChanelId(entryNested.getKey());
                                    Map<String, String> allData = (Map<String, String>) entryNested.getValue();
                                    itemMyChanel.setNameChanel(allData.get("tenchannel"));
                                    itemMyChanel.setDiem(allData.get("points"));
                                    itemMyChanel.setLinkIcon(allData.get("linkanh"));
                                    itemMyChanel.setSoLuotSub("9999");
                                    if (itemMyChanel.getDiem().equals("0"))
                                        arrayListAllChanel.add(itemMyChanel);
                                }
                            }
                        }
                        arrayList = arrayListAllChanel;
                        myChanelAdapter = new MyChanelAdapter(getContext(),arrayListAllChanel);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        myChanelAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(myChanelAdapter);
                    }
                } catch (Exception s) {

                }
            }
        });
    }
}
