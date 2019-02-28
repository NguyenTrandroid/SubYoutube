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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ictandroid.youtube.com.CONST;
import ictandroid.youtube.com.MyApp.GetKeySearchMyChanel;
import ictandroid.youtube.com.MyApp.ItemMyChanel;
import ictandroid.youtube.com.MyApp.MyAppActivity;
import ictandroid.youtube.com.MyApp.MyChanelAdapter;
import ictandroid.youtube.com.R;
import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;
import ictandroid.youtube.com.Utils.GetData.SubscribersOnMyApp;

public class FragmentInCampaign extends Fragment implements GetSubFromActivityV2Listener, GetKeySearchMyChanel {
    //view
    View view;
    RecyclerView recyclerView;
    //variable
    MyChanelAdapter myChanelAdapter;
    ArrayList<ItemMyChanel> arrayListAllChanel;
    ArrayList<ItemMyChanel> arrayList;
    String uid;
    SubscribersOnMyApp subscribersOnMyApp;
    //api
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    DocumentReference docRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitOnCreate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_channel, container, false);
        if(CONST.tagFragmentOther==null)
        {
            String getIDFragment = this.getTag();
            String[] output = getIDFragment.split(":", 4);
            CONST.tagFragmentOther = output[2];
            CONST.IDFragment = output[2];
        }


        InitView();
        InitAction();

        return view;
    }
    private void InitOnCreate()
    {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
        subscribersOnMyApp = new SubscribersOnMyApp();
    }
    private void InitView()
    {
        recyclerView = view.findViewById(R.id.rv_listCampaign);
    }
    private void InitAction()
    {
        loadApp();
    }
    private void loadApp() {
        docRef = db.collection("USER").document(uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                try {
                    if (e != null) {
                        return;
                    }
                    arrayListAllChanel = new ArrayList<>();
                    arrayList = new ArrayList<>();
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
                            TextView txtCoin = getActivity().findViewById(R.id.tv_coin);
                            if(CONST.COIN!=-1)
                            {

                                txtCoin.setText(CONST.COIN+"");
                            }
                            else
                            {
                                if ("points".equals(entry.getKey())) {
                                    String coin = entry.getValue().toString();
                                    txtCoin.setText(coin);
                                }
                            }
                        }
                        arrayList = arrayListAllChanel;
                        myChanelAdapter = new MyChanelAdapter(getContext(), arrayList);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        myChanelAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(myChanelAdapter);

                        List<String> listIdChannel = new ArrayList<>();
                        for (int i = 0; i < arrayList.size(); i++) {
                            listIdChannel.add(arrayList.get(i).getChanelId());
                        }
                        subscribersOnMyApp = new SubscribersOnMyApp();
                        subscribersOnMyApp.getListSubscripbers(getContext(), CONST.KEY, listIdChannel);

                    }
                } catch (Exception s) {

                }
            }
        });
    }

    @Override
    public void onCompletedSubV2FromActivity(List<SubChannelItem> lisSubChannelItem) {
        for(int i=0;i<lisSubChannelItem.size();i++)
        {
            for(int j=0;j<arrayList.size();j++)
            {
                if(lisSubChannelItem.get(i).getItems().get(0).getId().equals(arrayList.get(j).getChanelId()))
                {
                    arrayList.get(j).setSoLuotSub(lisSubChannelItem.get(i)
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
        if(MyAppActivity.sEdit!=null)
        {
            MyAppActivity.sEdit.dismiss();
        }
    }

    @Override
    public void onGetKey(String keySearch) {
        Log.d("AAAAA", "onQueryTextChange: 1 "+keySearch);
        ArrayList<ItemMyChanel> listTemp = new ArrayList<>();
        listTemp = new ArrayList<>();
        listTemp.clear();
        if (arrayList != null) {
            if (keySearch.length() == 0) {
                listTemp.addAll(arrayList);
            } else {
                for (ItemMyChanel itemChanel : arrayList) {
                    try {
                        if (itemChanel.getNameChanel().toLowerCase().contains(keySearch.toLowerCase())) {
                            listTemp.add(itemChanel);
                        }
                    } catch (Exception e) {

                    }
                }
            }
            myChanelAdapter = new MyChanelAdapter(getContext(), listTemp);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            myChanelAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(myChanelAdapter);
        }
    }
}
