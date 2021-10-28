package com.zft.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.search.SearchResult;
import com.zft.bluetooth.gaomu.GaoMUActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context context;
    List<SearchResult> itemList = new ArrayList<>();

    Map<String, String> DeviceNameMap = new HashMap<>();

    public MainAdapter(Context context, List<SearchResult> itemList) {
        this.context = context;
        this.itemList = itemList;
        DeviceNameMap.put("BF4030", "额温枪");
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<SearchResult> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchResult> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.device_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SearchResult result = (SearchResult) itemList.get(position);

        holder.name.setText(result.getName() + getDeviceName(result.getName()));
        holder.mac.setText(result.getAddress());
        holder.rssi.setText(String.format("Rssi: %d", result.rssi));

        Beacon beacon = new Beacon(result.scanRecord);
        holder.adv.setText(beacon.toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result.getName().equals("BF4030")) {
                    Intent intent = new Intent(context, GaoMUActivity.class);
                    intent.putExtra("mac", result.getAddress());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, DeviceDetailActivity.class);
                    intent.putExtra("mac", result.getAddress());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView mac;
        TextView rssi;
        TextView adv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            mac = (TextView) itemView.findViewById(R.id.mac);
            rssi = (TextView) itemView.findViewById(R.id.rssi);
            adv = (TextView) itemView.findViewById(R.id.adv);
        }
    }

    public String getDeviceName(String bluetoothName) {
        String s = DeviceNameMap.get(bluetoothName);
        if (s != null) {
            return String.format("(%s)", s);
        } else {
            return "";
        }
    }
}
