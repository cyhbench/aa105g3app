package com.recipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;


public class RecipeDetailActivity extends AppCompatActivity {
    private final static String TAG = "RecipeDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);

//        RecipeVO recipeVO = (RecipeVO) getIntent().getExtras().getSerializable("recipeVO");
//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        TextView tvName = (TextView) findViewById(R.id.tvFilmTitle);
//        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
//        Log.d(TAG, recipeVO.getMem_no());
        //       Bitmap bitmap = BitmapFactory.decodeByteArray(recipeVO.getRecipe_pic(), 0, recipeVO.getRecipe_pic().length);
//        imageView.setImageBitmap(R.id.);
//        imageView.setImageResource(recipeVO.getRecipe_pic());
//        tvName.setText(recipeVO.getRecipe_name());
//        tvTitle.setText(recipeVO.getRecipe_intro());
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Bundle bundle =  getIntent().getExtras();
        RecipeVO recipeVO = (RecipeVO) getIntent().getExtras().getSerializable("recipeVO");
        Log.d(TAG, "recipeVO:" + recipeVO.getMem_no());

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView tvName = (TextView) findViewById(R.id.tvFilmTitle);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvName.setText(recipeVO.getRecipe_name());
        tvTitle.setText(recipeVO.getRecipe_no());
    }
}
