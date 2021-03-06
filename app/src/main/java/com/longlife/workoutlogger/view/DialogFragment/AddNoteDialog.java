/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/27/18 9:07 PM.
 */

package com.longlife.workoutlogger.view.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.longlife.workoutlogger.AndroidUtils.DialogBase;
import com.longlife.workoutlogger.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddNoteDialog
        extends DialogBase {
    public static final String TAG = AddNoteDialog.class.getSimpleName();
    // Input constants.
    private static final String INPUT_DESCRIP = "descrip";
    private OnInputListener onInputListener;
    // Other
    private String descripText;
    private Button saveButton;
    private Button cancelButton;
    private EditText descrip;

    public static AddNoteDialog newInstance(String descrip) {
        AddNoteDialog dialog = new AddNoteDialog();

        Bundle bundle = new Bundle();
        bundle.putString(INPUT_DESCRIP, descrip);
        dialog.setArguments(bundle);

        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.dialog_add_note, container, false);

        this.descrip = mView.findViewById(R.id.et_dialog_edit_descrip);
        this.cancelButton = mView.findViewById(R.id.btn_dialog_edit_cancel);
        this.saveButton = mView.findViewById(R.id.btn_dialog_edit_save);

        // User does not want to save.
        cancelButton.setOnClickListener(view -> getDialog().dismiss());

        saveButton.setOnClickListener(view ->
        {
            String inputDescrip = this.descrip.getText().toString();

            onInputListener.sendInput(inputDescrip);
            getDialog().dismiss();
        });

        // Set default values.
        this.descrip.setText(this.descripText);

        // Set initial focus.
        this.descrip.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Open keyboard if box is in focus.
        descrip.post(() -> {
            descrip.requestFocus();

            InputMethodManager imm = (InputMethodManager) descrip.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.showSoftInput(descrip, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof OnInputListener) {
            onInputListener = (OnInputListener) getParentFragment(); // attach the input return callback to parent fragment.
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        onInputListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Note");
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Unbundle arguments.
        this.descripText = getArguments().getString(AddNoteDialog.INPUT_DESCRIP);
    }

    // Interface to callback to parent fragment the entered values. Parent must implement this to get back the value.
    public interface OnInputListener {
        void sendInput(String descrip);
    }
}
