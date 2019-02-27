package ictandroid.youtube.com.MyApp;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

import ictandroid.youtube.com.Campaign.ItemChanel;
import ictandroid.youtube.com.CloudFunction;
import ictandroid.youtube.com.R;

public class MyChanelAdapter extends RecyclerView.Adapter<MyChanelAdapter.ViewHolder>  {
    Context context;
    ArrayList<ItemMyChanel> arrayList;
    Dialog dialogRemove;
    Dialog dialogEdit;
    Dialog dialogAdd;
    MyChannelInterface myChannelInterface;
    FirebaseFirestore db;
    FirebaseAuth auth;
    long pointsUser;
    public MyChanelAdapter(Context context, ArrayList<ItemMyChanel> arrayList) {
        this.context = context;
        myChannelInterface = (MyChannelInterface) context;
        this.arrayList = arrayList;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        dialogEdit=new Dialog(context);
        dialogAdd=new Dialog(context);
        dialogRemove=new Dialog(context);
    }

    @NonNull
    @Override
    public MyChanelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new MyChanelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChanelAdapter.ViewHolder holder, int position) {
        ItemMyChanel itemChanel = arrayList.get(position);
        holder.ivBellD.setVisibility(View.GONE);
        holder.ivBellV.setVisibility(View.GONE);
        holder.ivDelete.setVisibility(View.GONE);
        holder.ivEdit.setVisibility(View.GONE);
        holder.ivChienDich.setVisibility(View.GONE);

        if(itemChanel.getDiem().equals("0")){
            holder.ivChienDich.setVisibility(View.VISIBLE);
        } else {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivEdit.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(itemChanel.getNameChanel());
        holder.tvSoSub.setText(itemChanel.getSoLuotSub()+" subscribers");
        Glide.with(context).load(itemChanel.getLinkIcon()).into(holder.ivIcon);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * gõ bỏ kênh ra khỏi chiến dịch
                 */
                Objects.requireNonNull(dialogRemove.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogRemove.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        holder.ivDelete.setEnabled(false);
                        holder.ivEdit.setEnabled(false);
                        holder.rlCarView.setEnabled(false);
                    }
                });
                dialogRemove.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        holder.ivDelete.setEnabled(true);
                        holder.ivEdit.setEnabled(true);
                        holder.rlCarView.setEnabled(true);
                    }
                });
                dialogRemove.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        holder.ivDelete.setEnabled(true);
                        holder.ivEdit.setEnabled(true);
                        holder.rlCarView.setEnabled(true);
                    }
                });
                dialogRemove.setContentView(R.layout.dialog_rm);
                dialogRemove.setCanceledOnTouchOutside(false);
                Button btOk = dialogRemove.findViewById(R.id.bt_ok);
                Button btCancle = dialogRemove.findViewById(R.id.bt_cancel);
                if(!dialogRemove.isShowing()&&!dialogEdit.isShowing()&&!dialogAdd.isShowing()){
                    dialogRemove.show();
                }
                btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         *gõ bỏ kênh
                         */
                        myChannelInterface.delete(itemChanel.getChanelId());
                        dialogRemove.dismiss();
                    }
                });
                btCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogRemove.dismiss();
                    }
                });
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * thay đổi lượt đăng kí
                 */
                Objects.requireNonNull(dialogEdit.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogEdit.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        holder.ivDelete.setEnabled(false);
                        holder.ivEdit.setEnabled(false);
                        holder.rlCarView.setEnabled(false);
                    }
                });
                dialogEdit.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        holder.ivDelete.setEnabled(true);
                        holder.ivEdit.setEnabled(true);
                        holder.rlCarView.setEnabled(true);
                    }
                });
                dialogEdit.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        holder.ivDelete.setEnabled(true);
                        holder.ivEdit.setEnabled(true);
                        holder.rlCarView.setEnabled(true);
                    }
                });
                dialogEdit.setContentView(R.layout.dialog_edit_chanel);
                dialogEdit.setCanceledOnTouchOutside(false);
                Button btOk = dialogEdit.findViewById(R.id.bt_thaydoi);
                Button btCancle = dialogEdit.findViewById(R.id.bt_cancel);
                TextView tvDiem = dialogEdit.findViewById(R.id.tv_thay_doi);
                ImageView ivCong = dialogEdit.findViewById(R.id.iv_cong);
                ImageView ivTru = dialogEdit.findViewById(R.id.iv_tru);
                tvDiem.setText(itemChanel.getDiem());
                /**
                 *
                 */

                DocumentReference documentReference = db.collection("USER").document(auth.getUid());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            pointsUser = (long) document.getData().get("points");
                            /**
                             *
                             */
                            if(!dialogRemove.isShowing()&&!dialogEdit.isShowing()&&!dialogAdd.isShowing()){
                                dialogEdit.show();
                            }
                            ivCong.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     *cộng điểm
                                     */
                                    int a = Integer.parseInt((String) tvDiem.getText());
                                    if (pointsUser > 0) {
                                        a++;
                                        pointsUser--;
                                    } else {
                                        Toast.makeText(context, "Bạn đã hết điểm để cộng", Toast.LENGTH_SHORT).show();
                                    }
                                    tvDiem.setText(a + "");
                                }
                            });
                            ivTru.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     *trừ điểm
                                     */
                                    int b = Integer.parseInt((String) tvDiem.getText());
                                    if (b > 0) {
                                        b--;
                                        pointsUser++;
                                    } else {
                                        Toast.makeText(context, "Kênh của bạn đã hết điểm để trừ", Toast.LENGTH_SHORT).show();
                                    }
                                    tvDiem.setText(b + "");
                                }
                            });
                            btOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     *cập nhật điểm
                                     */
                                    int pointsChanel = Integer.parseInt((String) tvDiem.getText());
                                    capnhatdiem(pointsChanel,itemChanel.getChanelId());
                                    Log.d("AAAAA", "onClick: user:   " + pointsUser);
                                    Log.d("AAAAA", "onClick: chanel: " + pointsChanel);
                                    dialogEdit.dismiss();
                                }
                            });
                            btCancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogEdit.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });
        holder.ivChienDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * đưa app vào chiến dịch
                 */
                Objects.requireNonNull(dialogAdd.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAdd.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        holder.ivChienDich.setEnabled(false);
                        holder.rlCarView.setEnabled(false);
                    }
                });
                dialogAdd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        holder.ivChienDich.setEnabled(true);
                        holder.rlCarView.setEnabled(true);
                    }
                });
                dialogAdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        holder.ivChienDich.setEnabled(true);
                        holder.rlCarView.setEnabled(true);
                    }
                });
                dialogAdd.setContentView(R.layout.dialog_chien_dich);
                dialogAdd.setCanceledOnTouchOutside(false);
                Button btOk = dialogAdd.findViewById(R.id.bt_thaydoi);
                Button btCancle = dialogAdd.findViewById(R.id.bt_cancel);
                TextView tvDiem = dialogAdd.findViewById(R.id.tv_thay_doi);
                ImageView ivCong = dialogAdd.findViewById(R.id.iv_cong);
                ImageView ivTru = dialogAdd.findViewById(R.id.iv_tru);
                tvDiem.setText("0");
                /**
                 *
                 */
                DocumentReference documentReference = db.collection("USER").document(auth.getUid());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d("AAAAA", "onClick: điểm: " + document.getData().get("points"));
                            pointsUser = (long) document.getData().get("points");
                            /**
                             *
                             */
                            if(!dialogRemove.isShowing()&&!dialogEdit.isShowing()&&!dialogAdd.isShowing()){
                                dialogAdd.show();
                            }
                            btOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     * đưa app vào chiến dịch
                                     */
                                    myChannelInterface.addpoint(itemChanel.getChanelId(),Integer.parseInt(tvDiem.getText().toString()));
                                    dialogAdd.dismiss();
                                }
                            });
                            btCancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogAdd.dismiss();
                                }
                            });
                            ivCong.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     *cộng điểm
                                     */
                                    int a = Integer.parseInt((String) tvDiem.getText());
                                    if (pointsUser > 0) {
                                        a++;
                                        pointsUser--;
                                    } else {
                                        Toast.makeText(context, "Bạn đã hết điểm để cộng", Toast.LENGTH_SHORT).show();
                                    }
                                    tvDiem.setText(a + "");
                                }
                            });
                            ivTru.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     *trừ điểm
                                     */
                                    int b = Integer.parseInt((String) tvDiem.getText());
                                    if (b > 0) {
                                        b--;
                                        pointsUser++;
                                    } else {
                                        Toast.makeText(context, "Kênh của bạn đã hết điểm để trừ", Toast.LENGTH_SHORT).show();
                                    }
                                    tvDiem.setText(b + "");
                                }
                            });
                        }
                    }
                });
            }
        });
    }
    private void capnhatdiem(int diem,String channeldi) {
        DocumentReference docRef = db.collection("LIST").document(channeldi);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int x=diem-Integer.parseInt(String.valueOf(document.getData().get("points")));
                        myChannelInterface.addpoint(channeldi,x);
                    }else {
                        /////////////////////

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvSoSub;
        ImageView ivBellV;
        ImageView ivBellD;
        ImageView ivDelete;
        ImageView ivEdit;
        ImageView ivChienDich;
        RelativeLayout rlCarView;
        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_avatarChanel);
            tvName = itemView.findViewById(R.id.tv_nameChanel);
            tvSoSub = itemView.findViewById(R.id.tv_subscribers);
            ivBellV = itemView.findViewById(R.id.iv_bellV);
            ivBellD = itemView.findViewById(R.id.iv_bellD);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_remove);
            ivChienDich = itemView.findViewById(R.id.iv_bellX);
            rlCarView = itemView.findViewById(R.id.rl_item);
        }
    }
    public interface MyChannelInterface{
        void delete(String channelid);
        void addpoint(String channelid,int point);
    }
}
