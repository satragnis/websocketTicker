package com.example.bitcointicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bitcointicker.Adapter.TransactionAdapter;
import com.example.bitcointicker.Application.MyApplication;
import com.example.bitcointicker.Model.Block;
import com.example.bitcointicker.Model.DisplayTransaction;
import com.example.bitcointicker.Model.MessageEvent;
import com.example.bitcointicker.Model.Transaction;
import com.example.bitcointicker.Socket.WebSocketEvents;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tv_socketStatus)
    public TextView socketStatus;

    @BindView(R.id.tv_block_hash)
    public TextView blockHash;
    @BindView(R.id.tv_block_height)
    public TextView blockHeight;
    @BindView(R.id.tv_reward)
    public TextView reward;
    @BindView(R.id.tv_total_btc_sent)
    public TextView totalBTC;
    @BindView(R.id.rv_transaction)
    public RecyclerView recyclerView;

    LinkedList<DisplayTransaction> displayTransactions = new LinkedList<>();
    int i=0;
    TransactionAdapter mAdapter;
    MyApplication myApplication;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
         myApplication = (MyApplication) Objects.requireNonNull(getApplicationContext());
        init();
    }

    private void init() {
        mAdapter =
                new TransactionAdapter(displayTransactions, MainActivity.this);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        myApplication.connectSocket();
        myApplication.subscribeTo(WebSocketEvents.BLOCK_SUB);
        myApplication.subscribeTo(WebSocketEvents.UNCONFIRMED_TSUB);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        myApplication.connectSocket();
        myApplication.unsubscribeFrom(WebSocketEvents.BLOCK_SUB);
        myApplication.unsubscribeFrom(WebSocketEvents.UNCONFIRMED_TSUB);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        switch (messageEvent.getEventType()) {

            case WebSocketEvents.BLOCK_SUB:
                Block block = messageEvent.getBlock();
                blockHash.setText(getResources().getString(R.string.block_hash_label)+" - "+block.getHash());
                blockHeight.setText(getResources().getString(R.string.block_height_label)+" - "+block.getHeight());
                totalBTC.setText(getResources().getString(R.string.total_btc_sent_label)+" - "+String.format("%. 6f",block.getTotalBTCSent()));
                reward.setText(getResources().getString(R.string.reward_label)+" - "+String.format("%. 6f",block.getReward()));
                break;
            case WebSocketEvents.UNCONFIRMED_TSUB:
                Transaction transaction = messageEvent.getTransaction();
                String hash = transaction.getX().getHash();
                Long time = transaction.getX().getTime();
                List<Transaction.Input> inputs = transaction.getX().getInputs();
                Long amount =0L;
                for (Transaction.Input input:inputs
                     ) {
                    amount = amount+input.getPrevOut().getValue();
                }
                DisplayTransaction displayTransaction = new DisplayTransaction(time,amount,hash);
                Log.d("??", "onMessageEvent: "+i);
                if(i<=5) {
                    displayTransactions.add(i,displayTransaction);
                    i++;
                    Log.d("??", "onMessageEvent: "+i);
                }else{
                    i=0;
                }
                mAdapter.notifyDataSetChanged();
                break;
            case WebSocketEvents.SOCKET_INFO:
                socketStatus.setText("Socket Status:" + messageEvent.getSocketState());
                break;

        }
    }


    public void clearTransactions(View view) {
        displayTransactions.clear();
        mAdapter.notifyDataSetChanged();
        i=0;
    }


}
