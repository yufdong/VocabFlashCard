package com.yufandong.vocabflashcard.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.model.WordCard;

import java.util.List;

public class MemListAdapter extends BaseAdapter {

    private static final int NUM_TYPES = 2;
    private static final int TYPE_WORD = 0;
    private static final int TYPE_TOGGLE = 1;
    private Context context;
    private List<WordCard> list;
    private boolean showFront;

    public MemListAdapter(Context context, List<WordCard> list) {
        this.context = context;
        this.list = list;
        showFront = true;
    }

    public void setShowFront(boolean showFront) {
        this.showFront = showFront;
        notifyDataSetChanged();
    }

    public void toggleWord(int position) {
        WordCard wordCard = list.get(position - 1);
        wordCard.setReveal(!wordCard.isReveal());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null;
        }
        return list.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_TOGGLE : TYPE_WORD;
    }

    @Override
    public int getViewTypeCount() {
        return NUM_TYPES;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        switch (type) {
            case TYPE_TOGGLE:
                convertView = getToggleView(convertView, parent);
                break;
            case TYPE_WORD:
                convertView = getTextView(position, convertView, parent);
                break;
        }

        return convertView;
    }

    private View getToggleView(View convertView, ViewGroup parent) {
        ToggleViewHolder toggleViewHolder;

        // View holder pattern
        if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof
                ToggleViewHolder)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_front_back_button_group, parent, false);

            toggleViewHolder = new ToggleViewHolder();
            toggleViewHolder.front = (Button) convertView.findViewById(R.id.frontButton);
            toggleViewHolder.back = (Button) convertView.findViewById(R.id.backButton);

            toggleViewHolder.front.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFront = true;
                    notifyDataSetChanged();
                }
            });
            toggleViewHolder.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFront = false;
                    notifyDataSetChanged();
                }
            });

            convertView.setTag(toggleViewHolder);
        }
        else {
            toggleViewHolder = (ToggleViewHolder) convertView.getTag();
        }

        if (showFront) {
            toggleViewHolder.front.setBackgroundResource(R.color.buttonPressed);
            toggleViewHolder.back.setBackgroundResource(R.color.buttonUnpressed);
        }
        else {
            toggleViewHolder.front.setBackgroundResource(R.color.buttonUnpressed);
            toggleViewHolder.back.setBackgroundResource(R.color.buttonPressed);
        }

        return convertView;
    }

    private View getTextView(int position, View convertView, ViewGroup parent) {
        TextViewHolder textViewHolder;
        int index = position - 1;

        // View holder pattern
        if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof TextViewHolder)) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_memorize, parent, false);

            textViewHolder = new TextViewHolder();
            textViewHolder.container = convertView.findViewById(R.id.vocab_list);
            textViewHolder.text = (TextView) convertView.findViewById(R.id.memorizeListText);
            convertView.setTag(textViewHolder);
        }
        else {
            textViewHolder = (TextViewHolder) convertView.getTag();
        }

        // Show front if word card is facing front and not revealing or if word card is facing back and is revealed
        if (showFront ^ list.get(index).isReveal()) {
            textViewHolder.text.setText(list.get(index).getWord().getFront());
            textViewHolder.text.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        else {
            textViewHolder.text.setText(list.get(index).getWord().getBack());
            textViewHolder.text.setBackgroundColor(context.getResources().getColor(R.color.memorizeSelected));
        }

        return convertView;
    }

    static class TextViewHolder {
        View container;
        TextView text;
    }

    static class ToggleViewHolder {
        Button front;
        Button back;
    }
}
