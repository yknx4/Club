package com.yknx.android.club.fragments;

import android.content.ContentResolver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.yknx.android.club.R;
import com.yknx.android.club.data.ClubsContract;
import com.yknx.android.club.data.ClubsProvider;
import com.yknx.android.club.model.Club;

import java.util.Arrays;

/**
 * Created by Yknx on 10/08/2014.
 */
public class EditClubFragment extends Fragment {

    ContentResolver mContentResolver;

    boolean hasImage = false;
    FragmentView mFragmentView;

    private final String  LOG_TAG = EditClubFragment.class.getSimpleName();

    public EditClubFragment() {
    }


    Club mClub;

    private void loadClub(View view,long clubId){

        Club club = ClubsProvider.getClub(getActivity(),clubId);
        mClub = club;
        ImageButton icon = (ImageButton)view.findViewById(R.id.fragmen_save_create_icon_imageview);
        EditText clubName = (EditText) view.findViewById(R.id.fragmen_save_create_club_name_edittext);
        EditText terms = (EditText) view.findViewById(R.id.fragmen_save_create_terms_edittext);
        SeekBar assists = (SeekBar) view.findViewById(R.id.fragmen_save_create_assists_seekbar);
        final Spinner colorSelect = (Spinner) view.findViewById(R.id.fragment_save_create_color_spinner);
        String[] colors = getActivity().getResources().getStringArray(R.array.colors_values_array);
        final TextView spinnerHelp = (TextView) view.findViewById(R.id.fragment_save_create_spinnerhelp_textview);

        assists.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               spinnerHelp.setText(seekBar.getProgress()+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






        if(club!=null){
            if(club.icon==null || club.icon.toString().isEmpty()) {
                icon.setImageResource(R.drawable.default_icon);
                icon.setColorFilter(Color.parseColor(club.color));
            }
            else icon.setImageURI(club.icon);

                clubName.setText(club.name);
                terms.setText(club.terms+"");

                assists.setProgress(club.atLeast);
                int colorPos = Arrays.asList(colors).indexOf(club.color);
                colorSelect.setSelection(colorPos);






        }
        else {
                getActivity().finish();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentResolver = getActivity().getContentResolver();
    }

    public static EditClubFragment getClubFragment(long clubID){
        EditClubFragment frag = new EditClubFragment();
        Bundle args = new Bundle();
        args.putLong(ClubsContract.ClubEntry._ID,clubID);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_save_create__club, container, false);
        mFragmentView = new FragmentView(rootView);

        Bundle args = getArguments();
        long clubId = args.getLong(ClubsContract.ClubEntry._ID);
        loadClub(rootView,clubId);


        mFragmentView.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        mFragmentView.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mod = updateClub();
                if(mod>0) getActivity().finish();

            }
        });




        return rootView;
    }

    private int updateClub(){
        Club toUpdate = getClubFromView();
       toUpdate.id = mClub.id;
        return ClubsProvider.updateClub(getActivity(),toUpdate);
    }


    Uri imageUri;
    private Club getClubFromView(){
        String[] colors = getResources().getStringArray(R.array.colors_values_array);
        Club res = new Club();
        res.name = mFragmentView.clubName.getText().toString();
        res.terms = Integer.parseInt(mFragmentView.terms.getText().toString());
        res.atLeast = mFragmentView.assists.getProgress();
        res.color = colors[mFragmentView.colorSelect.getSelectedItemPosition()];
        res.icon = imageUri;
        return res;
    }

    private class FragmentView{
        public final ImageButton icon;
        public final EditText clubName;
        public final EditText terms;
        public final SeekBar assists;
        public final Spinner colorSelect;
        public final TextView spinnerHelp;
        public final Button cancelButton ;
        public  final Button saveButton;
        FragmentView(View view){
             icon = (ImageButton)view.findViewById(R.id.fragmen_save_create_icon_imageview);
             clubName = (EditText) view.findViewById(R.id.fragmen_save_create_club_name_edittext);
             terms = (EditText) view.findViewById(R.id.fragmen_save_create_terms_edittext);
             assists = (SeekBar) view.findViewById(R.id.fragmen_save_create_assists_seekbar);
             colorSelect = (Spinner) view.findViewById(R.id.fragment_save_create_color_spinner);
             spinnerHelp = (TextView) view.findViewById(R.id.fragment_save_create_spinnerhelp_textview);
            cancelButton = (Button) view.findViewById(R.id.fragmen_save_create_cancel_button);
             saveButton = (Button) view.findViewById(R.id.fragmen_save_create_save_button);
        }
    }
}