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

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.dialog.EditFlashcardDialogFragment;
import com.yufandong.vocabflashcard.model.Flashcard;
import com.yufandong.vocabflashcard.model.VocabSet;

/**
 * List adapter for the edit flashcard list view. Utilizes the ViewHolder pattern.
 */
public class EditFlashcardsListAdapter extends BaseAdapter {

    private Context context;
    private VocabSet vocabSet;

    public EditFlashcardsListAdapter(Context context, VocabSet vocabSet) {
        this.context = context;
        this.vocabSet = vocabSet;
    }

    @Override
    public int getCount() {
        return vocabSet.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return vocabSet.getList().get(position);
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
            convertView = inflater.inflate(R.layout.list_item_edit_flashcards, parent, false);
            convertView.setTag(createViewHolder(convertView, position));
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.position = position;
        viewHolder.front.setText(vocabSet.getList().get(position).getFront());
        viewHolder.back.setText(vocabSet.getList().get(position).getBack());
        return convertView;
    }

    private ViewHolder createViewHolder(View convertView, int position) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.front = (TextView) convertView.findViewById(R.id.frontText);
        viewHolder.front.setId(position);
        viewHolder.back = (TextView) convertView.findViewById(R.id.backText);
        viewHolder.back.setId(position);
        viewHolder.editIcon = (ImageButton) convertView.findViewById(R.id.editButton);
        viewHolder.editIcon.setId(position);
        viewHolder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = view.getId();
                showEditDialog(vocabSet.getList().get(position));
            }
        });
        return viewHolder;
    }

    private void showEditDialog(Flashcard selectedFlashcard) {
        EditFlashcardDialogFragment dialog = EditFlashcardDialogFragment.newInstance(selectedFlashcard);
        FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        dialog.show(fragmentManager, "EditFlashCardDialog");
    }

    private static class ViewHolder {
        int position;
        TextView front;
        TextView back;
        ImageButton editIcon;
    }
}
