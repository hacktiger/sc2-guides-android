package com.sc2guide.sc2_guides_android.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.data.model.GuideBodyItem;

import java.util.ArrayList;
import java.util.List;

public class GuideDetailBodyItemAdapter extends RecyclerView.Adapter<GuideDetailBodyItemAdapter.GuideBodyItemHolder> {

    private List<GuideBodyItem> guideBodyItemList = new ArrayList<>();

    public GuideDetailBodyItemAdapter() {
        //
    }

    public void setGuideBodyItems(List<GuideBodyItem> itemList) {
        guideBodyItemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GuideBodyItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guide_detail_body_item, parent, false);
        return new GuideBodyItemHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull GuideBodyItemHolder guideBodyItemHolder, int i) {
        GuideBodyItem currentItem = guideBodyItemList.get(i);
        guideBodyItemHolder.txtView.setText(currentItem.getBody());

        if (currentItem.getType().equals("Note")){
            guideBodyItemHolder.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    public int getItemCount() {
        return guideBodyItemList.size();
    }

    class GuideBodyItemHolder extends RecyclerView.ViewHolder {
        private TextView txtView;

        private GuideBodyItemHolder(View itemView) {
            super(itemView);
            txtView = itemView.findViewById(R.id.guide_detail_body_item_text);
        }

        public void setBackgroundColor(int color) {
            itemView.setBackgroundColor(color);
        }
    }
}
