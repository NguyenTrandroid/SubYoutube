package ictandroid.youtube.com.Campaign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ictandroid.youtube.com.R;

public class AdapterChanel extends RecyclerView.Adapter<AdapterChanel.ViewHolder>  {
    Context context;
    ArrayList<ItemChanel> arrayList;

    public AdapterChanel(Context context, ArrayList<ItemChanel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        for (int i=0; i<arrayList.size(); ++i){
            Log.d("AAA", "AdapterChanel: "+arrayList.get(i).getNameChanel());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new AdapterChanel.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemChanel itemChanel = arrayList.get(position);
        holder.tvName.setText(itemChanel.getNameChanel());
        holder.tvSoSub.setText(itemChanel.getSoLuotSub());
        Glide.with(context).load(itemChanel.getLinkIcon()).into(holder.ivIcon);
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
        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_avatarChanel);
            tvName = itemView.findViewById(R.id.tv_nameChanel);
            tvSoSub = itemView.findViewById(R.id.tv_subscribers);
            ivBellV = itemView.findViewById(R.id.iv_bellV);
            ivBellD = itemView.findViewById(R.id.iv_bellD);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_remove);
        }
    }
}
