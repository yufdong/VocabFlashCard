package com.yufandong.vocabflashcard.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.model.Flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * List adapter for the memorize list. Utilizes the ViewHolder pattern.
 */
public class MemorizeListAdapter extends BaseAdapter {

    private static final int NUM_TYPES = 2;
    private static final int TYPE_FLASHCARD = 0;
    private static final int TYPE_TOGGLE = 1;
    private Context context;
    private List<Flashcard> flashcardList;
    private List<Boolean> isRevealList;
    private boolean showFront;

    public MemorizeListAdapter(Context context, List<Flashcard> flashcardList) {
        this.context = context;
        this.flashcardList = flashcardList;
        isRevealList = new ArrayList<>(Collections.nCopies(flashcardList.size(), false));
        showFront = true;
    }

    public void toggleFlashcard(int position) {
        position = offsetPosition(position);
        boolean oldRevealState = isRevealList.get(position);
        isRevealList.set(position, !oldRevealState);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return flashcardList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null;
        }
        return flashcardList.get(offsetPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_TOGGLE : TYPE_FLASHCARD;
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
            case TYPE_FLASHCARD:
                convertView = getTextView(position, convertView, parent);
                break;
        }

        return convertView;
    }

    private View getToggleView(View convertView, ViewGroup parent) {
        if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof
                ToggleViewHolder)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_front_back_button_group, parent, false);
            convertView.setTag(createToggleViewHolder(convertView));
        }

        ToggleViewHolder toggleViewHolder = (ToggleViewHolder) convertView.getTag();

        if (showFront) {
            toggleViewHolder.front.setBackgroundResource(R.color.frontBackButtonPressed);
            toggleViewHolder.back.setBackgroundResource(R.color.frontBackButtonUnpressed);
        }
        else {
            toggleViewHolder.front.setBackgroundResource(R.color.frontBackButtonUnpressed);
            toggleViewHolder.back.setBackgroundResource(R.color.frontBackButtonPressed);
        }

        return convertView;
    }

    private ToggleViewHolder createToggleViewHolder(View convertView) {
        ToggleViewHolder toggleViewHolder = new ToggleViewHolder();
        toggleViewHolder.front = (Button) convertView.findViewById(R.id.frontButton);
        toggleViewHolder.back = (Button) convertView.findViewById(R.id.backButton);

        toggleViewHolder.front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showFront) {
                    showFront = true;
                    notifyDataSetChanged();
                }
            }
        });
        toggleViewHolder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showFront) {
                    showFront = false;
                    notifyDataSetChanged();
                }
            }
        });

        return toggleViewHolder;
    }

    private View getTextView(int position, View convertView, ViewGroup parent) {
        int index = offsetPosition(position);

        if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof TextViewHolder)) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_memorize, parent, false);

            convertView.setTag(createTextViewHolder(convertView));
        }
        TextViewHolder textViewHolder = (TextViewHolder) convertView.getTag();

        // ^ = XOR
        if (showFront ^ isRevealList.get(index)) {
            textViewHolder.text.setText(flashcardList.get(index).getFront());
            textViewHolder.text.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        else {
            textViewHolder.text.setText(flashcardList.get(index).getBack());
            textViewHolder.text.setBackgroundColor(context.getResources().getColor(R.color.memorizeListItemRevealed));
        }

        return convertView;
    }

    private TextViewHolder createTextViewHolder(View convertView) {
        TextViewHolder textViewHolder = new TextViewHolder();
        textViewHolder.container = convertView.findViewById(R.id.vocab_list);
        textViewHolder.text = (TextView) convertView.findViewById(R.id.memorizeListText);
        return textViewHolder;
    }

    private int offsetPosition(int position) {
        return position - 1;
    }

    private static class TextViewHolder {
        View container;
        TextView text;
    }

    private static class ToggleViewHolder {
        Button front;
        Button back;
    }
}
