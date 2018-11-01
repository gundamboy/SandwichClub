package com.wickedword.charlesr.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wickedword.charlesr.sandwichclub.model.Sandwich;
import com.wickedword.charlesr.sandwichclub.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity fart";
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    // using Butterknife for view binding makes life easier
    @BindView(R.id.backdrop) ImageView mBackdrop;
    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.sandwich_description) TextView mDescription;
    @BindView(R.id.header_aka) TextView mHeaderAlsoKnownAs;
    @BindView(R.id.aka) TextView mAlsoKnownAs;
    @BindView(R.id.header_origin) TextView mHeaderOrigin;
    @BindView(R.id.origin) TextView mOrigin;
    @BindView(R.id.ingredients) TextView mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // get the intent
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        // get the list item position that was passed. no position, close it down.
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        // get the sandwich array
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        Sandwich sandwich = null;

        // do some json stuff
        try {
            sandwich = JsonUtils.parseSandwichJson(sandwiches[position]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // no sandwich, close it down
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        // build the ui
        populateUI(sandwich);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        // set the title of the appbar
        String title = sandwich.getMainName();
        setTitle(title);
        mTitle.setText(title);

        String url = sandwich.getImage();

        // using Picasso to load images.
        Picasso.get().load(url).error(R.drawable.ic_error).into(mBackdrop);

        // set the sandwich origin. if it doesnt have one, hides the view
        String origin = sandwich.getPlaceOfOrigin();
        if (origin.isEmpty()) {
            mHeaderOrigin.setVisibility(View.GONE);
            mOrigin.setVisibility(View.GONE);
        } else {
            mOrigin.setText(origin);
        }

        // set the also known as stuff. if it doesnt have any, hide the view
        String aka = sandwich.getAlsoKnownAs().toString().replaceAll("\\[|\\]", "");
        if (!aka.isEmpty()) {
            mAlsoKnownAs.setText(aka);
        } else {
            mHeaderAlsoKnownAs.setVisibility(View.GONE);
            mAlsoKnownAs.setVisibility(View.GONE);
        }

        // set the description
        mDescription.setText(sandwich.getDescription());
        mIngredients.setText(sandwich.getIngredients().toString().replaceAll("\\[|\\]", ""));
    }
}

