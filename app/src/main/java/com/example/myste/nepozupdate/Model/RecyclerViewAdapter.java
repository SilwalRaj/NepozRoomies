package com.example.myste.nepozupdate.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.myste.nepozupdate.AdvDetailActivity;
import com.example.myste.nepozupdate.HomeActivity;
import com.example.myste.nepozupdate.R;
import com.squareup.picasso.Picasso;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder>{

    List<Advertise> listdata = new ArrayList<>();
    Context context;

    public RecyclerViewAdapter(List<Advertise> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_layout,parent,false);

        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }


    public void onBindViewHolder(MyHolder holder, int position) {
        Advertise data = listdata.get(position);
        holder.tvAdvertise.setText(data.getSuburb());
        holder.tvAdplus.setText((data.getUnitNo()==0?"":data.getUnitNo()+ "/") +data.getHouseNo() + " " +data.getStrtName());
        holder.tvAdDesc.setText(data.getDesc());
        if(!data.getPics().isEmpty()) {

            String[] imageUris = data.getPics().split(":::");
            if (imageUris.length != 0)
                Picasso.get().load(imageUris[0]).into(holder.ivAdlist);
            else
                Picasso.get().load(data.getPics()).into(holder.ivAdlist);
        }
        else{
            holder.ivAdlist.setImageResource(R.drawable.noimage);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick) {
                    //Toast.makeText(context, "Long Click:" + listdata.get(position).getAdvID(), Toast.LENGTH_SHORT).show();
                    //Activity activity = new Activity();
                    Intent intent = new Intent(context, AdvDetailActivity.class);
                    intent.putExtra("adObject", listdata.get(position));
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }

                //else
                    //Toast.makeText(context, listdata.get(position).getAdvID(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView tvAdvertise , tvAdplus, tvAdDesc;
        ImageView ivAdlist;
        ItemClickListener itemClickListener;

        public MyHolder(View itemView) {
            super(itemView);
            tvAdvertise = (TextView) itemView.findViewById(R.id.tvAdvertise);
            tvAdplus = (TextView) itemView.findViewById(R.id.tvAdvplus);
            tvAdDesc = (TextView) itemView.findViewById(R.id.tvAdvdesc);
            ivAdlist = (ImageView) itemView.findViewById(R.id.ivAdList);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {

            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }


}