package com.example.annika.wishlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

public class NewWishListDialog extends DialogFragment {
    private DialogClickListener callback;

    public interface DialogClickListener
    {
        void onSaveClick();
        void onCancelClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try {
            callback = (DialogClickListener) getActivity();
        }
        catch (ClassCastException cce)
        {
            throw new ClassCastException("The calling class must implement the interface DialogClickListener!");
        }
    }

    public static NewWishListDialog newInstance(String message)
    {
        NewWishListDialog frag = new NewWishListDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();

        EditText inputField = new EditText(getActivity());
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                //LinearLayout.LayoutParams.MATCH_PARENT);
        //inputField.setLayoutParams(lp);
        inputField.setBackgroundResource(R.color.default_background_color);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(bundle.getString("message"))
                .setView(inputField)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onSaveClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onCancelClick();
                    }
                }).create();

        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}