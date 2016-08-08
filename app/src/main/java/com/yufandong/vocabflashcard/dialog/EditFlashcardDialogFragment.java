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
import com.yufandong.vocabflashcard.model.Flashcard;

public class EditFlashcardDialogFragment extends DialogFragment {

    public static final String FLASH_ARD_KEY = "savedFlashcard";

    private static final String TITLE = "Edit Flashcard";

    private Flashcard flashcard;
    private Activity activity;
    private EditText frontEdit;
    private EditText backEdit;

    public static EditFlashcardDialogFragment newInstance(Flashcard flashcard) {
        Bundle args  = new Bundle();
        EditFlashcardDialogFragment fragment = new EditFlashcardDialogFragment();
        args.putSerializable(EditFlashcardDialogFragment.FLASH_ARD_KEY, flashcard);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        super.onCreateDialog(savedInstance);
        activity = getActivity();
        Bundle bundle = getArguments();
        flashcard = (Flashcard) bundle.getSerializable(FLASH_ARD_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(createDialogView())
                .setTitle(TITLE)
                .setIcon(activity.getResources().getDrawable(R.drawable.edit_icon))
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveFlashcard();
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

    private View createDialogView() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_flashcard, null);
        frontEdit = (EditText) dialogView.findViewById(R.id.frontEdit);
        backEdit = (EditText) dialogView.findViewById(R.id.backEdit);
        frontEdit.setText(flashcard.getFront());
        backEdit.setText(flashcard.getBack());
        return dialogView;
    }

    private void saveFlashcard() {
        flashcard.setFront(frontEdit.getText().toString());
        flashcard.setBack(backEdit.getText().toString());
        ((DialogCallback) activity).updateFlashcard(flashcard);
    }

    public interface DialogCallback {
        void updateFlashcard(Flashcard flashcard);
    }
}
