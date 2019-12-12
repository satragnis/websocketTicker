package com.example.bitcointicker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bitcointicker.Model.DisplayTransaction;
import com.example.bitcointicker.R;
import com.example.bitcointicker.Utils.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.DataObjectHolder>{
    View view;
    RecyclerView recyclerView;
    Context context;
    List<DisplayTransaction> displayTransactions = new ArrayList<>();


    public TransactionAdapter(List<DisplayTransaction> displayTransactions, Context context) {
        this.displayTransactions = displayTransactions;
        this.context = context;

    }




    @NonNull
    @Override
    public TransactionAdapter.DataObjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_row,viewGroup,false);
        return new TransactionAdapter.DataObjectHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.DataObjectHolder holder, int i) {
        holder.hash.setText(context.getResources().getString(R.string.block_hash_label)+"-"+ displayTransactions.get(i).getHash());
        holder.amount.setText(context.getResources().getString(R.string.amount_satoshi_label)+"-"+ displayTransactions.get(i).getAmount());
        holder.time.setText(context.getResources().getString(R.string.time)+"-"+ Helper.convertDate(displayTransactions.get(i).getTime()));
    }

    @Override
    public int getItemCount() {
        return displayTransactions.size();
    }


    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView hash,amount,time;
        public DataObjectHolder(View itemView) {
            super(itemView);
            hash     = itemView.findViewById(R.id.tv_block_hash);
            amount = itemView.findViewById(R.id.tv_amount);
            time = itemView.findViewById(R.id.tv_time);
        }
    }



}
