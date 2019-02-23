package ictandroid.youtube.com.MyApp;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

import ictandroid.youtube.com.Campaign.ItemChanel;
import ictandroid.youtube.com.R;

public class MyChanelAdapter extends RecyclerView.Adapter<MyChanelAdapter.ViewHolder>  {
    Context context;
    ArrayList<ItemMyChanel> arrayList;
    Dialog dialogRemove;
    Dialog dialogEdit;
    Dialog dialogAdd;
    public MyChanelAdapter(Context context, ArrayList<ItemMyChanel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
                         *gõ bỏ kênh
                         */
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
                dialogEdit.show();
                ivCong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         *cộng điểm
                         */
                        tvDiem.setText("+");
                    }
                });
                ivTru.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         *trừ điểm
                         */
                        tvDiem.setText("-");
                    }
                });
                btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         *cập nhật điểm
                         */
                    }
                });
                btCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogEdit.dismiss();
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
                dialogAdd = new Dialog(context);
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
                dialogAdd.show();
                btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * đưa app vào chiến dịch
                         */
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
                         * cộng điểm
                         */
                        tvDiem.setText("+");
                    }
                });
                ivTru.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * trừ điểm
                         */
                        tvDiem.setText("-");
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
}
