package com.yufandong.vocabflashcard.listadapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.dialog.EditWordDialogFragment;
import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.model.Word;

public class EditListAdapter extends BaseAdapter {

    private Context context;
    private VocabSet list;

    public EditListAdapter(Context context, VocabSet list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return list.getList().get(position);
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
            convertView = inflater.inflate(R.layout.list_item_edit, parent, false);

            editViewHolder = new EditViewHolder();
            editViewHolder.front = (TextView) convertView.findViewById(R.id.frontText);
            editViewHolder.front.setId(position);
            editViewHolder.back = (TextView) convertView.findViewById(R.id.backText);
            editViewHolder.back.setId(position);
            editViewHolder.editIcon = (ImageButton) convertView.findViewById(R.id.editButton);
            editViewHolder.editIcon.setId(position);

            editViewHolder.editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = v.getId();
                    Word selectedWord = list.getList().get(position);
                    EditWordDialogFragment dialog = EditWordDialogFragment.newInstance(selectedWord);
                    FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                    dialog.show(fragmentManager, "EditWordDialog");
                    Toast.makeText(context, Integer.toString(position), Toast.LENGTH_LONG).show();
                }
            });

            convertView.setTag(editViewHolder);
        }
        else {
            editViewHolder = (EditViewHolder) convertView.getTag();
        }
        editViewHolder.position = position;
        editViewHolder.front.setText(list.getList().get(position).getFront());
        editViewHolder.back.setText(list.getList().get(position).getBack());

        return convertView;
    }

    private static class EditViewHolder {
        int position;
        TextView front;
        TextView back;
        ImageButton editIcon;
    }
}
