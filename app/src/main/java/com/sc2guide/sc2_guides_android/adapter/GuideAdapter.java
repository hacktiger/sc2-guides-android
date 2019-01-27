package com.sc2guide.sc2_guides_android.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.data.model.Guide;

import java.util.ArrayList;
import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideHolder> {
    private OnItemLongClickListener longListener;
    private OnItemClickListener listener;

    private List<Guide> guideList = new ArrayList<>();

    public int getItemPosition(Guide guide) {
        for(int i = 0 ; i < guideList.size(); i ++) {
            if (guideList.get(i).getId() == guide.getId()) {
                return i;
            }
        }
        return -1;
    }

    public GuideAdapter(OnItemClickListener listener,
                        OnItemLongClickListener longListener
                        ){
        this.listener = listener;
        this.longListener = longListener;
    }


    public interface OnItemClickListener {
        void onItemClick(Guide guide);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(Guide guide);
    }

    public interface OnScrollListener {
        void OnScrollListener();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public GuideHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);


        return new GuideHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideHolder guideHolder, int i) {
        Guide currentGuide = guideList.get(i);
        guideHolder.bind(currentGuide, listener, longListener);
        String fullTitle = currentGuide.getTitle();
        if (fullTitle.length() > 20) {
            guideHolder.txtViewTitle.setText(fullTitle.substring(0,19) + "...");
        } else {
            guideHolder.txtViewTitle.setText(fullTitle);
        }
        guideHolder.txtViewSubtitle.setText(currentGuide.getAuthorName());
        //
        switch (currentGuide.getMyRace()) {
            case "Zerg":
                guideHolder.raceColorLayout.setBackgroundColor(Color.rgb(128,0,128));
                break;
            case "Terran":
                guideHolder.raceColorLayout.setBackgroundColor(Color.rgb(255,26,26));
                break;
            case "Protoss":
                guideHolder.raceColorLayout.setBackgroundColor(Color.rgb(0,128,128));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }

    public void setGuides(List<Guide> guides) {
        this.guideList = guides;
        notifyDataSetChanged();
    }

    public void deleteGuide(Guide guide) {
        int pos = getItemPosition(guide);
        this.guideList.remove(pos);
        notifyItemRemoved(pos);
    }

    class GuideHolder extends  RecyclerView.ViewHolder {
        private TextView txtViewTitle;
        private TextView txtViewSubtitle;
        private LinearLayout raceColorLayout;

        private GuideHolder(View itemView) {
            super(itemView);
            txtViewTitle = itemView.findViewById(R.id.text_view_title);
            txtViewSubtitle = itemView.findViewById(R.id.text_view_subtitle);
            raceColorLayout = itemView.findViewById(R.id.race_color);
        }


        private void bind(final Guide guide,
                         final OnItemClickListener listener,
                         final OnItemLongClickListener longListener) {
            itemView.setOnClickListener(v -> {
                listener.onItemClick(guide);
            });
            itemView.setOnLongClickListener(v -> {
                longListener.OnItemLongClick(guide);
                return true;
            });
        }
    }
}
