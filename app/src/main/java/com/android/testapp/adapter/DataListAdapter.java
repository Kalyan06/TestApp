package com.android.testapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.testapp.R;
import com.android.testapp.model.Data;

import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.ListViewHolder> {
    private List<Data> dataList;

    public DataListAdapter(List<Data> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public DataListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.data_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataListAdapter.ListViewHolder holder, int position) {
        holder.phone.setText(dataList.get(position).getPhone());
        holder.email.setText(dataList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView phone, email;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.data_list_phone);
            email = itemView.findViewById(R.id.data_list_email);
        }
    }
}
