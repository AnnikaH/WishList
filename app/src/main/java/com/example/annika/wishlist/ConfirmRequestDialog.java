package com.example.annika.wishlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmRequestDialog extends DialogFragment {

    private DialogClickListener callback;

    public interface DialogClickListener
    {
        void onConfirmClick(int sharingId, int uId, int listId);
        void onCancelConfirmClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try {
            callback = (DialogClickListener) getActivity();
        } catch (ClassCastException cce) {
            throw new ClassCastException("The calling class must implement the interface DialogClickListener!");
        }
    }

    public static ConfirmRequestDialog newInstance(String message, int sharingId, int uId, int listId)
    {
        ConfirmRequestDialog frag = new ConfirmRequestDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putInt("sharingId", sharingId);
        args.putInt("uId", uId);
        args.putInt("listId", listId);
        frag.setArguments(args);
        return frag;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Bundle bundle = getArguments();

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(bundle.getString("message"))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onConfirmClick(bundle.getInt("sharingId"), bundle.getInt("uId"), bundle.getInt("listId"));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onCancelConfirmClick();
                    }
                }).create();

        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}