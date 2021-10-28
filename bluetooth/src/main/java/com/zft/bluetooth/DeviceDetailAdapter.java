package com.zft.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeviceDetailAdapter extends RecyclerView.Adapter<DeviceDetailAdapter.ViewHolder> {
    private Context mContext;

    private List<DetailItem> mDataList = new ArrayList<>();

    public DeviceDetailAdapter(Context mContext, List<DetailItem> mDataList) {
        this.mContext = mContext;
//        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.device_detail_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        final DetailItem result = (DetailItem) mDataList.get(position);

        if (result.type == DetailItem.TYPE_SERVICE) {
            holder.root.setBackgroundColor(mContext.getResources().getColor(R.color.black_cccccc));
            holder.uuid.getPaint().setFakeBoldText(true);
            holder.uuid.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f);
            holder.uuid.setText(String.format("Service: %s", result.uuid.toString().toUpperCase()));

//            holder.root.setOnClickListener(null);
        } else {
            holder.root.setBackgroundColor(mContext.getResources().getColor(R.color.white_FFFFFF));
            holder.uuid.getPaint().setFakeBoldText(false);
            holder.uuid.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f);
            holder.uuid.setText(String.format("Characteristic: %s", result.uuid.toString().toUpperCase()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClicked(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public Object getItem(int position) {
        return mDataList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        TextView uuid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            uuid = (TextView) itemView.findViewById(R.id.uuid);
        }
    }


    public void setGattProfile(BleGattProfile profile) {
        List<DetailItem> items = new ArrayList<DetailItem>();

        List<BleGattService> services = profile.getServices();

        for (BleGattService service : services) {
            items.add(new DetailItem(DetailItem.TYPE_SERVICE, service.getUUID(), null));
            List<BleGattCharacter> characters = service.getCharacters();
            for (BleGattCharacter character : characters) {
                items.add(new DetailItem(DetailItem.TYPE_CHARACTER, character.getUuid(), service.getUUID()));
            }
        }

        setDataList(items);
    }

    private void setDataList(List<DetailItem> datas) {
        mDataList.clear();
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }


    OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public interface OnItemClickedListener {
        void onItemClicked(View view, int position);
    }


}
