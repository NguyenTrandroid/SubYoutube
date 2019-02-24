package ictandroid.youtube.com.Campaign;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

import ictandroid.youtube.com.R;

public class CampaignChanelAdapter extends RecyclerView.Adapter<CampaignChanelAdapter.ViewHolder> {
    Context context;
    ArrayList<ItemChanel> arrayList;
    String uid;
    Dialog dialogRemove;
    Dialog dialogEdit;
    OnChannelClick onChannelClick;
    long pointsUser;

    public CampaignChanelAdapter(Context context, ArrayList<ItemChanel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        onChannelClick = (OnChannelClick) context;
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new CampaignChanelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemChanel itemChanel = arrayList.get(position);
        holder.ivBellD.setVisibility(View.GONE);
        holder.ivBellV.setVisibility(View.GONE);
        holder.ivDelete.setVisibility(View.GONE);
        holder.ivEdit.setVisibility(View.GONE);
        holder.ivChienDich.setVisibility(View.GONE);
        if (itemChanel.getUserId().equals(uid)) {
            holder.ivEdit.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.VISIBLE);
        }
        holder.rlCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemChanel.getUserId() != uid) {
                    onChannelClick.OnClicked(itemChanel.getChanelId());
                }
            }
        });

        holder.tvName.setText(itemChanel.getNameChanel());
        holder.tvSoSub.setText(itemChanel.getSoLuotSub() + " subscribers");
        Glide.with(context).load(itemChanel.getLinkIcon()).into(holder.ivIcon);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * gỡ bỏ kênh ra khỏi chiến dịch
                 */
                dialogRemove = new Dialog(context);
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
                dialogRemove.show();
                btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         *gỡ bỏ kênh
                         */
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
                dialogEdit = new Dialog(context);
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
                FirebaseFirestore db;
                FirebaseAuth auth;
                db = FirebaseFirestore.getInstance();
                auth = FirebaseAuth.getInstance();
                DocumentReference reference = db.collection("USER").document(auth.getUid());
                reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            pointsUser = (long) documentSnapshot.get("points");
                            /**
                             *
                             */
                            dialogEdit.show();
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

    public interface OnChannelClick {
        void OnClicked(String channelid);
    }
}
