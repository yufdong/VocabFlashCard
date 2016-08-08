package com.yufandong.vocabflashcard.listadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.activity.EditFlashcardsActivity;
import com.yufandong.vocabflashcard.model.VocabSet;

import java.util.List;

/**
 * List adapter for the vocab set picker list view. Utilizes the ViewHolder pattern.
 */
public class VocabSetPickerListAdapter extends BaseAdapter {

    private Context context;
    private List<VocabSet> vocabSetList;

    public VocabSetPickerListAdapter(Context context, List<VocabSet> vocabSetList) {
        this.context = context;
        this.vocabSetList = vocabSetList;
    }

    @Override
    public int getCount() {
        return vocabSetList.size();
    }

    @Override
    public Object getItem(int position) {
        return vocabSetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.getTag() == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_vocab_set_picker, parent, false);
            convertView.setTag(createViewHolder(convertView, position));
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.position = position;
        viewHolder.text.setText(vocabSetList.get(position).getName());
        return convertView;
    }

    private ViewHolder createViewHolder(View convertView, int position) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.text = (TextView) convertView.findViewById(R.id.setName);
        viewHolder.text.setId(position);
        viewHolder.editIcon = (ImageView) convertView.findViewById(R.id.editButton);
        viewHolder.editIcon.setId(position);
        viewHolder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = v.getId();
                startEditFlashcardsActivity(vocabSetList.get(position));
            }
        });
        return viewHolder;
    }

    private void startEditFlashcardsActivity(VocabSet vocabSet) {
        Intent intent = new Intent(context, EditFlashcardsActivity.class);
        intent.putExtra(EditFlashcardsActivity.SET_ID_KEY, vocabSet.getId());
        context.startActivity(intent);
    }

    private static class ViewHolder {
        int position;
        TextView text;
        ImageView editIcon;
    }
}
