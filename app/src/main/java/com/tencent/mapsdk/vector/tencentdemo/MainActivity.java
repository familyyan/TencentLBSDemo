package com.tencent.mapsdk.vector.tencentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private String[] names = new String[]{"测试环境Key", "正式环境Key", "测试环境底图数据", "正式环境底图数据", "测试环境样式更新", "正式环境样式更新"};
    private List mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = new ArrayList<String>();
        mRecyclerView = findViewById(R.id.rv_main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new RecyclerAdapter();
        mAdapter.setOnItemClickListener(this);
        for (int i = 0; i < names.length; i++) {
            mList.add(names[i]);
        }
        mAdapter.setList(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == 0) {
            Intent intent = new Intent(MainActivity.this, TestKeyActivity.class);
            startActivity(intent);
        } else if (position == 1) {
            Intent intent = new Intent(MainActivity.this, FormalKeyActivity.class);
            startActivity(intent);
        } else if (position == 2) {
            Intent intent = new Intent(MainActivity.this, TestMapDataActivity.class);
            startActivity(intent);
        } else if (position == 3) {
            Intent intent = new Intent(MainActivity.this, FormalMapDataActivity.class);
            startActivity(intent);
        } else if (position == 4) {
            Intent intent = new Intent(MainActivity.this, TestMapStyleActivity.class);
            startActivity(intent);
        } else if (position == 5) {
            Intent intent = new Intent(MainActivity.this, FormalMapStyleActivity.class);
            startActivity(intent);
        }
    }
}

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RvHolder> {
    private List list;
    OnItemClickListener mOnItemClickListener;

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, null);
        RvHolder rvHolder = new RvHolder(view);
        return rvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RvHolder holder, int position) {
        holder.textView.setText(list.get(position).toString());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List mList) {
        list = mList;
    }


    class RvHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public RvHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_name);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}


