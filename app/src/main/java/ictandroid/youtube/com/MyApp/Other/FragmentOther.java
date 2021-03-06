package ictandroid.youtube.com.MyApp.Other;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Objects;

import ictandroid.youtube.com.CONST;
import ictandroid.youtube.com.Dialog.SLoading;
import ictandroid.youtube.com.MyApp.GetKeySearchMyChanel;
import ictandroid.youtube.com.MyApp.ItemMyChanel;
import ictandroid.youtube.com.MyApp.MyAppActivity;
import ictandroid.youtube.com.MyApp.MyChanelAdapter;
import ictandroid.youtube.com.R;
import ictandroid.youtube.com.Utils.GetData.DataChannel;
import ictandroid.youtube.com.Utils.GetData.Models.InfoSubChannel.SubChannelItem;

public class FragmentOther extends Fragment implements GetSubFomActivityListener, AddChannelOnFirebaseListener, GetKeySearchMyChanel {
    //view
    View view;
    RecyclerView recyclerView;
    ImageView imageView;
    public static SLoading sLoadingAddChannel;
    public static Dialog dialogAdd;
    //variable
    MyChanelAdapter myChanelAdapter;
    ArrayList<ItemMyChanel> arrayListAllChanel;
    ArrayList<ItemMyChanel> arrayList;
    String uid;
    DataChannel dataChannel;
    String idChannelCheckAdd;
    //api
    DocumentReference docRef;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InitOnCreate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_channel, container, false);
        if (CONST.tagFragmentOther == null) {
            String getIDFragment = this.getTag();
            String[] output = getIDFragment.split(":", 4);
            CONST.tagFragmentOther = output[2];
            CONST.IDFragment = output[2];
        }
        //
        InitView();
        InitAction();
        return view;
    }

    private void InitView() {
        imageView = view.findViewById(R.id.iv_addApp);
        recyclerView = view.findViewById(R.id.rv_listMyApp);
    }

    private void InitOnCreate() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getUid();
        dataChannel = new DataChannel();

    }

    private void InitAction() {
        loadApp();
        addApp();
        if(getActivity().getIntent().getStringExtra(CONST.SHARE_LINK)!=null)
        {
            imageView.performClick();
        }
    }

    private void addApp() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * thêm kênh
                 */
                dialogAdd = new Dialog(getContext());
                Objects.requireNonNull(dialogAdd.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAdd.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        imageView.setEnabled(false);
                    }
                });
                dialogAdd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        imageView.setEnabled(true);
                    }
                });
                dialogAdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        imageView.setEnabled(true);
                    }
                });
                dialogAdd.setContentView(R.layout.dialog_add_chanel);
                dialogAdd.setCanceledOnTouchOutside(false);
                EditText editText = dialogAdd.findViewById(R.id.ed_input);

                Button buttonOK = dialogAdd.findViewById(R.id.bt_thaydoi);
                Button buttonCancle = dialogAdd.findViewById(R.id.bt_cancel);
                dialogAdd.show();
                if(getActivity().getIntent().getStringExtra(CONST.SHARE_LINK)!=null)
                {
                    Log.d("FFFFFFFFF",getActivity().getIntent().getStringExtra(CONST.SHARE_LINK));
                    editText.setText(getActivity().getIntent().getStringExtra(CONST.SHARE_LINK));
                }
                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * add kênh
                         */
                        CONST.SHARE_LINK = null;
                        if (!editText.getText().toString().isEmpty() && editText.getText() != null && !editText.getText().equals("")) {
                            sLoadingAddChannel = new SLoading(getContext());
                            sLoadingAddChannel.show();
                            Uri uri = Uri.parse(editText.getText().toString());
                            String idChannel = uri.getLastPathSegment();
                            dataChannel.getInfo(getContext(), CONST.KEY, idChannel);
                        }
                    }
                });
                buttonCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAdd.dismiss();
                        CONST.SHARE_LINK = null;
                    }
                });

            }
        });
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
                                    if (itemMyChanel.getDiem().equals("0"))
                                        arrayListAllChanel.add(itemMyChanel);
                                }
                            }
                            TextView txtCoin = getActivity().findViewById(R.id.tv_coin);
                            if (CONST.COIN != -1) {
                                txtCoin = getActivity().findViewById(R.id.tv_coin);
                                txtCoin.setText(CONST.COIN + "");
                            } else {
                                if ("points".equals(entry.getKey())) {
                                    String coin = entry.getValue().toString();
                                    txtCoin.setText(coin);
                                }
                            }
                        }
                        arrayList = arrayListAllChanel;
                        if(arrayList.size()==0)
                        {
                            myChanelAdapter = new MyChanelAdapter(getContext(), arrayList);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            myChanelAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(myChanelAdapter);
                        }


                        List<String> listIdChannel = new ArrayList<>();
                        for (int i = 0; i < arrayList.size(); i++) {
                            listIdChannel.add(arrayList.get(i).getChanelId());
                        }
                        dataChannel = new DataChannel();
                        dataChannel.getListSubscripbers(getContext(), CONST.KEY, listIdChannel);

                    }
                } catch (Exception s) {

                }
            }
        });
    }


    @Override
    public void onCompletedSubFromActivity(List<SubChannelItem> lisSubChannelItem) {
        for (int i = 0; i < lisSubChannelItem.size(); i++) {
            for (int j = 0; j < arrayList.size(); j++) {
                if (lisSubChannelItem.get(i).getItems().get(0).getId().equals(arrayList.get(j).getChanelId())) {
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
        MyAppActivity.sLoading.dismiss();
        for (int i = 0; i < arrayList.size(); i++) {
            if (idChannelCheckAdd != null && idChannelCheckAdd.equals(arrayList.get(i).getChanelId())) {
                if (sLoadingAddChannel != null)
                    sLoadingAddChannel.dismiss();
                if (dialogAdd != null)
                    dialogAdd.dismiss();
            }
        }
    }

    @Override
    public void onCompletedAddChannel(String idChannel) {
        idChannelCheckAdd = idChannel;
    }

    @Override
    public void onGetKey(String keySearch) {
        Log.d("AAAAA", "onQueryTextChange: 2 " + keySearch);
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
