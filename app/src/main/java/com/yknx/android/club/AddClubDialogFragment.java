package com.yknx.android.club;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yknx.android.club.data.ClubsProvider;

/**
 * Created by Yknx on 09/08/2014.
 */
public class AddClubDialogFragment extends DialogFragment
{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_create_club,null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(getString(R.string.title_dialog_create_club));
        builder.setView(dialogView)
                .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        String[] colors = getActivity().getResources().getStringArray(R.array.colors_values_array);
                        EditText nameView = (EditText) dialogView.findViewById(R.id.dialog_create_club_name_edittext);
                        Spinner colorSpinner = (Spinner) dialogView.findViewById(R.id.dialog_create_color_spinner);
                        String msg;
                        String clubName = nameView.getText().toString();
                        String color = colors[colorSpinner.getSelectedItemPosition()];
                        if (clubName != null && !clubName.isEmpty()) {
                            long mid = ClubsProvider.addClub(getActivity(), clubName, color);
                            if(mid>0)
                            msg= clubName+" added succesfully.";
                            else
                                msg= clubName+" couldn't be added.";
                        }
                        else {
                            msg="A club needs a name.";
                        }
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddClubDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

}
