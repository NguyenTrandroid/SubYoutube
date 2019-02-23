package ictandroid.youtube.com.Campaign;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

import ictandroid.youtube.com.R;

public class AllChanelAdapter extends RecyclerView.Adapter<AllChanelAdapter.ViewHolder>  {
    Context context;
    ArrayList<ItemChanel> arrayList;
    String uid;
    Dialog dialogRemove;
    Dialog dialogEdit;


    public AllChanelAdapter(Context context, ArrayList<ItemChanel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
        uid="sangdeptrai";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new AllChanelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemChanel itemChanel = arrayList.get(position);
        holder.ivBellD.setVisibility(View.GONE);
        holder.ivBellV.setVisibility(View.GONE);
        holder.ivDelete.setVisibility(View.GONE);
        holder.ivEdit.setVisibility(View.GONE);
        holder.ivChienDich.setVisibility(View.GONE);
        if(itemChanel.getUserId().equals(uid)){
            holder.ivEdit.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(itemChanel.getNameChanel());
        holder.tvSoSub.setText(itemChanel.getSoLuotSub()+" subscribers");
        Glide.with(context).load(itemChanel.getLinkIcon()).into(holder.ivIcon);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRemove = new Dialog(context);
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
                         *
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
                dialogEdit = new Dialog(context);
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
                         *
                         */
                    }
                });
                ivTru.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         *
                         */
                    }
                });
                btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         *
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
