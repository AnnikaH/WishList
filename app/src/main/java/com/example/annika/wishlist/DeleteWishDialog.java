package com.example.annika.wishlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteWishDialog extends DialogFragment {

    private DialogClickListener callback;

    public interface DialogClickListener
    {
        void onDeleteWishClick(int wishId);
        void onCancelDeleteWishClick();
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

    public static DeleteWishDialog newInstance(String message, int id)
    {
        DeleteWishDialog frag = new DeleteWishDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putInt("id", id);
        frag.setArguments(args);
        return frag;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Bundle bundle = getArguments();

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(bundle.getString("message"))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onDeleteWishClick(bundle.getInt("id"));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onCancelDeleteWishClick();
                    }
                }).create();

        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}