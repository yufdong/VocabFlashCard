package com.yufandong.vocabflashcard.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.yufandong.vocabflashcard.R;
import com.yufandong.vocabflashcard.model.Word;

public class EditWordDialogFragment extends DialogFragment {

    public static final String WORD_KEY = "savedWord";

    private Word word;
    private Activity activity;
    private EditText frontEdit;
    private EditText backEdit;

    public static EditWordDialogFragment newInstance(Word word) {

        Bundle args  = new Bundle();

        EditWordDialogFragment fragment = new EditWordDialogFragment();
        args.putSerializable(EditWordDialogFragment.WORD_KEY, word);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        super.onCreateDialog(savedInstance);
        activity = getActivity();
        Bundle bundle = getArguments();
        word = (Word) bundle.getSerializable(WORD_KEY);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_word, null);
        frontEdit = (EditText) dialogView.findViewById(R.id.frontEdit);
        backEdit = (EditText) dialogView.findViewById(R.id.backEdit);
        frontEdit.setText(word.getFront());
        backEdit.setText(word.getBack());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView)
                .setTitle("Edit Word")
                .setIcon(activity.getResources().getDrawable(R.drawable.edit_icon))
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        word.setFront(frontEdit.getText().toString());
                        word.setBack(backEdit.getText().toString());
                        ((DialogCallback) activity).updateWord(word);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });


        return builder.create();
    }

    public interface DialogCallback {
        void updateWord(Word word);
    }
}
