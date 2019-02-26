package ictandroid.youtube.com.MyApp.InCampaign;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ictandroid.youtube.com.Campaign.CampaignChanelAdapter;
import ictandroid.youtube.com.Campaign.ItemChanel;
import ictandroid.youtube.com.MyApp.ItemMyChanel;
import ictandroid.youtube.com.MyApp.MyChanelAdapter;
import ictandroid.youtube.com.MyApp.Other.GetDataOtherListener;
import ictandroid.youtube.com.MyApp.Other.GetSubFomActivityListener;
import ictandroid.youtube.com.R;
import ictandroid.youtube.com.Utils.GetData.DataChannel;
import ictandroid.youtube.com.Utils.GetData.Interface.GetSubListener;
import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;
import ictandroid.youtube.com.Utils.GetData.Models.ListSub.SubItem;

public class FragmentInCampaign extends Fragment implements GetDataOtherListener, GetSubFomActivityListener {
    View view;
    RecyclerView recyclerView;
    MyChanelAdapter myChanelAdapter;
    ArrayList<ItemMyChanel> arrayListAllChanel = new ArrayList<>();
    ArrayList<ItemMyChanel> arrayList = new ArrayList<>();
    String uid;
    DataChannel dataChannel;
    GetDataOtherListener getDataOtherListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_channel, container, false);
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
        dataChannel=new DataChannel();
        loadApp();
        return view;
    }
    private void loadApp() {
        recyclerView = view.findViewById(R.id.rv_listCampaign);
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
                    arrayListAllChanel.clear();
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
                                    if (!itemMyChanel.getDiem().equals("0"))
                                        arrayListAllChanel.add(itemMyChanel);
                                }
                            }
                        }
                        arrayList.clear();
                        arrayList = arrayListAllChanel;
                        List<String> listIdChannel = new ArrayList<>();
                        for(int i=0;i<arrayList.size();i++)
                        {
                            listIdChannel.add(arrayList.get(i).getChanelId());
                        }
                        getDataOtherListener = (GetDataOtherListener) getContext();
                        getDataOtherListener.onCompletedDataOther(listIdChannel);
                    }
                } catch (Exception s) {

                }
            }
        });
    }

    @Override
    public void onCompletedDataOther(List<String> lisIdChannel) {
        dataChannel.getListSubscripbers(getContext(),"AIzaSyBU_oWEIULi3-n96vWKETYCMsldYDAlz2M",lisIdChannel);
    }

    @Override
    public void onCompletedSubFromActivity(List<SubChannelItem> lisSubChannelItem) {
        for(int i=0;i<lisSubChannelItem.size();i++)
        {
            for(int j=0;j<arrayList.size();j++)
            {
                if(lisSubChannelItem.get(i).getItems().get(0).getId().equals(arrayList.get(j).getChanelId()))
                {
                    arrayList.get(j)
                            .setSoLuotSub(lisSubChannelItem.get(i)
                                    .getItems()
                                    .get(0)
                                    .getStatistics()
                                    .getSubscriberCount());

                }
            }
        }
        myChanelAdapter = new MyChanelAdapter(getContext(), arrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myChanelAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(myChanelAdapter);

    }
}
