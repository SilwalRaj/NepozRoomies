package com.example.myste.nepozupdate.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myste.nepozupdate.AdvDetailActivity;
import com.example.myste.nepozupdate.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RequestViewAdapter extends  RecyclerView.Adapter<RequestViewAdapter.MyHolder>{

    List<RequestUnit> listdata = new ArrayList<RequestUnit>();
    Context context;

    public RequestViewAdapter(List<RequestUnit> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public RequestViewAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestlistview_layout,parent,false);

        RequestViewAdapter.MyHolder myHolder = new RequestViewAdapter.MyHolder(view);
        return myHolder;
    }


    public void onBindViewHolder(RequestViewAdapter.MyHolder holder, int position) {
        RequestUnit data = listdata.get(position);
        holder.tVUserdesc.setText(data.getReqFromUserName()+ "  sends request for ");
        holder.tvFadress.setText(data.getCompleteAdress());
        //if(!data.getReqFrmImage().isEmpty()) {

          //  Picasso.get().load(data.getReqFrmImage()).into(holder.iVreqstee);
        //}
        //else{
            holder.iVreqstee.setImageResource(R.drawable.noimage);
        //}

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //if(!isLongClick) {
                    if(view.getId()==R.id.btnAccept) {
                        FirebaseDatabase.getInstance().getReference("Requests").child(listdata.get(position).getReqToUserId())
                                .child(listdata.get(position).getRequestId()).child("reqStatus")
                                .setValue(2);
                        //Toast.makeText(context, listdata.get(position).getRequestId()+ "hellya",Toast.LENGTH_SHORT).show();
                        Button btn = (Button)view;
                        btn.setText("Accepted");
                    }
                    else if(view.getId()==R.id.btnReject){
                        Toast.makeText(context, listdata.get(position).getRequestId()+ "teraffas",Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference("Requests").child(listdata.get(position).getReqToUserId())
                                .child(listdata.get(position).getRequestId()).child("reqStatus")
                                .setValue(0);
                        Button btn = (Button)view;
                        btn.setText("Rejected");
                    }

               // }

                //else
                   // Toast.makeText(context, listdata.get(position).getRequestId(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView tVUserdesc, tvFadress;
        ImageView iVreqstee;
        Button btnReject, btnAccept;
        ItemClickListener itemClickListener;

        public MyHolder(View itemView) {
            super(itemView);
            tvFadress = (TextView) itemView.findViewById(R.id.tVFaddress);
            tVUserdesc = (TextView) itemView.findViewById(R.id.tVUserDesc);
            iVreqstee = (ImageView) itemView.findViewById(R.id.iVrequest);
            btnAccept =(Button)itemView.findViewById(R.id.btnReject);
            btnReject =(Button)itemView.findViewById(R.id.btnAccept);
            itemView.setOnClickListener(this);
            btnReject.setOnClickListener(this);
            btnAccept.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            //if(v.getId()==R.id.btnAccept || v.getId()==R.id.btnReject)
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }
}
