package com.sc2guide.sc2_guides_android.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.adapter.helper.ItemTouchHelperAdapter;
import com.sc2guide.sc2_guides_android.adapter.helper.ItemTouchHelperViewHolder;
import com.sc2guide.sc2_guides_android.data.model.GuideBodyItem;

import java.util.ArrayList;
import java.util.List;

public class CreateGuideBodyItemAdapter extends RecyclerView.Adapter<CreateGuideBodyItemAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter{

    private List<GuideBodyItem> mItems = new ArrayList<>();
    private GuideBodyItem guideBodyItem;

    public CreateGuideBodyItemAdapter() {

    }

    public void addItem(String type, String body){
        guideBodyItem = new GuideBodyItem(type, body);
        mItems.add(guideBodyItem);
        notifyItemInserted(mItems.size());
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (guideBodyItem.getType().equals(parent.getContext().getResources().getString(R.string.guide_body_item_type_note))){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_guide_note, parent, false);
            return new ItemViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_guide_desc, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        String userInput = guideBodyItem.getBody();

        itemViewHolder.txtView.setText(userInput);
        itemViewHolder.editBtn.setText("EDIT");
        itemViewHolder.editBtn.setOnClickListener(v -> {
            itemViewHolder.switcher.showNext();
            itemViewHolder.editText.setText(userInput);
            itemViewHolder.editBtn.setText("CONFIRM");
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public List<GuideBodyItem> getItems() {
        return mItems;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        GuideBodyItem prev = mItems.remove(fromPosition);
        mItems.add(toPosition > fromPosition? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemChanged(position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        private TextView txtView;
        private Button editBtn;
        private EditText editText;
        private ViewSwitcher switcher;
        private Drawable x = itemView.getBackground();

        private ItemViewHolder(View itemView) {
            super(itemView);
            switcher = itemView.findViewById(R.id.create_guide_note_switcher);
            txtView = itemView.findViewById(R.id.note_text_view);
            editBtn = itemView.findViewById(R.id.note_button);
            editText = itemView.findViewById(R.id.note_edit_text);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackground(x);
        }
    }
}
