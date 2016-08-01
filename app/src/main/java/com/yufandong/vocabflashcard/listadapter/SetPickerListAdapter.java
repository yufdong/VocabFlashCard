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
import com.yufandong.vocabflashcard.activity.SetEditActivity;
import com.yufandong.vocabflashcard.model.VocabSet;

import java.util.List;

/**
 * Created by YuFan on 7/24/16.
 */
public class SetPickerListAdapter extends BaseAdapter {

    private Context context;
    private List<VocabSet> vocabSet;

    public SetPickerListAdapter(Context context, List<VocabSet> vocabSet) {
        this.context = context;
        this.vocabSet = vocabSet;
    }

    @Override
    public int getCount() {
        return vocabSet.size();
    }

    @Override
    public Object getItem(int position) {
        return vocabSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EditViewHolder editViewHolder;

        if (convertView == null || convertView.getTag() == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_set_picker, parent, false);

            editViewHolder = new EditViewHolder();
            editViewHolder.text = (TextView) convertView.findViewById(R.id.setName);
            editViewHolder.text.setId(position);
            editViewHolder.editIcon = (ImageView) convertView.findViewById(R.id.editButton);
            editViewHolder.editIcon.setId(position);

            editViewHolder.editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = v.getId();
                    Intent intent = new Intent(context, SetEditActivity.class);
                    intent.putExtra(SetEditActivity.SET_ID_KEY, vocabSet.get(position).getId());
                    context.startActivity(intent);
                }
            });

            convertView.setTag(editViewHolder);
        }
        else {
            editViewHolder = (EditViewHolder) convertView.getTag();
        }
        editViewHolder.position = position;
        editViewHolder.text.setText(vocabSet.get(position).getName());

        return convertView;
    }

    private static class EditViewHolder {
        int position;
        TextView text;
        ImageView editIcon;
    }
}
