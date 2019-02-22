package ictandroid.youtube.com.Campaign.AllChannel;

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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ictandroid.youtube.com.Campaign.AdapterChanel;
import ictandroid.youtube.com.Campaign.ItemChanel;
import ictandroid.youtube.com.R;

public class FragmentAllChannel extends Fragment {
    View view;
    RecyclerView recyclerView;
    AdapterChanel adapterChanel;
    ArrayList<ItemChanel> appArrayListAllChanel = new ArrayList<>();
    ArrayList<ItemChanel> appArrayList = new ArrayList<>();
    ArrayList<String> myapp = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_channel, container, false);
        loadApp();
        return view;
    }
    private void loadApp() {
        recyclerView = view.findViewById(R.id.rv_listCampaign);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference docRef = db.collection("LIST");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                try {
                    if (e != null) {
                        Log.d("DATAAA", "ERROR");
                        return;
                    }
                    if (queryDocumentSnapshots != null) {
                        appArrayListAllChanel.clear();
                        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                        for (int i = 0; i < documentSnapshots.size(); i++) {
                            ItemChanel itemApp = new ItemChanel();
                            itemApp.setDiem((String) documentSnapshots.get(i).getData().get("points"));
                            itemApp.setDoUuTien((String) documentSnapshots.get(i).getData().get("douutien"));
                            itemApp.setLinkIcon((String) documentSnapshots.get(i).getData().get("linkanh"));
                            itemApp.setTime((String) documentSnapshots.get(i).getData().get("time"));
                            itemApp.setIdChanel((String) documentSnapshots.get(i).getData().get("userid"));
                            itemApp.setNameChanel((String) documentSnapshots.get(i).getData().get("tenchannel"));
                            if (Integer.parseInt(itemApp.getDiem()) > 0) {
                                appArrayListAllChanel.add(itemApp);
                            }
                        }
                        appArrayList = appArrayListAllChanel;
                        Collections.sort(appArrayListAllChanel, new Comparator<ItemChanel>() {
                            @Override
                            public int compare(ItemChanel itemApp, ItemChanel t1) {
                                Integer b = Integer.parseInt(itemApp.getDoUuTien());
                                Integer a = Integer.parseInt(t1.getDoUuTien());
                                int sizeCmp = a.compareTo(b);
                                if (sizeCmp != 0) {
                                    return sizeCmp;

                                }
                                Long c = Long.parseLong(itemApp.getTime());
                                Long d = Long.parseLong(t1.getTime());
                                int nrOfToppingsCmp = c.compareTo(d);
                                if (nrOfToppingsCmp != 0) {
                                    return nrOfToppingsCmp;
                                }
                                return itemApp.getNameChanel().compareTo(t1.getNameChanel());
                            }
                        });
                        for (int i = 0; i <appArrayListAllChanel.size() ; i++) {
                            Log.d("AAA", "AdapterChanel: "+appArrayListAllChanel.get(i).getNameChanel());
                        }
                        adapterChanel = new AdapterChanel(getContext(),appArrayListAllChanel);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        adapterChanel.notifyDataSetChanged();
                        recyclerView.setAdapter(adapterChanel);
                    }
                } catch (Exception s) {

                }
            }
        });
    }
}
