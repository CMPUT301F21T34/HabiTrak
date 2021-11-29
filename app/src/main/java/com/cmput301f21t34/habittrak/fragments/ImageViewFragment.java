package com.cmput301f21t34.habittrak.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.cmput301f21t34.habittrak.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

public class ImageViewFragment extends Fragment {

    ImageView imageDisplay;
    Uri photoUri;

    public ImageViewFragment(Uri photoUri){

        this.photoUri = photoUri;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.image_view_fragment, container, false);

        imageDisplay = view.findViewById(R.id.image_view_display);


        Picasso.get().load(photoUri).into(imageDisplay);




        return view;

    }




}
