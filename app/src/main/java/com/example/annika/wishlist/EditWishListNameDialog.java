package com.example.annika.wishlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

public class EditWishListNameDialog extends DialogFragment {

    private DialogClickListener callback;

    public interface DialogClickListener
    {
        void onSaveNameClick(String newWishListName);
        void onCancelEditNameClick();
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

    public static EditWishListNameDialog newInstance(String message, String name)
    {
        EditWishListNameDialog frag = new EditWishListNameDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("name", name);
        frag.setArguments(args);
        return frag;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Bundle bundle = getArguments();

        final EditText inputField = new EditText(getActivity());
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
        //LinearLayout.LayoutParams.MATCH_PARENT);
        //inputField.setLayoutParams(lp);
        inputField.setBackgroundResource(R.color.default_background_color);
        inputField.setText(bundle.getString("name"));

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(bundle.getString("message"))
                .setView(inputField)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onSaveNameClick(inputField.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onCancelEditNameClick();
                    }
                }).create();

        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}