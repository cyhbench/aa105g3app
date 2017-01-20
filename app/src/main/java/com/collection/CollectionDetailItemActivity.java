package com.collection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.androidbelieve.drawerwithswipetabs.R;
import com.recipe.RecipeCondPageStepFragment;
import com.recipe.RecipeVO;


/**
 * Created by cyh on 2017/1/1.
 */

public class CollectionDetailItemActivity extends AppCompatActivity {
    private EditText et_input_mem_id;
    private RecipeVO recipeVO;
    private Bundle bundle;
    private static final String TAG = "logCollDtilItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_detail_item_activity);

        recipeVO = (RecipeVO) getIntent().getExtras().getSerializable("recipeVO");

        bundle = new Bundle();
//            bundle.putSerializable("recipe_contVOSet", (Serializable) recipe_contVOSet);
        bundle.putSerializable("recipeVO",recipeVO);
        Log.d(TAG, "recipeVO: 64" + recipeVO.getRecipe_no());

        RecipeCondPageStepFragment recipeCondPageStepFragment = new RecipeCondPageStepFragment();
        recipeCondPageStepFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.tmpFragment, recipeCondPageStepFragment);
        fragmentTransaction.commit();

//        findViews();
    }

//    private void findViews() {
//        et_input_mem_id = (EditText) findViewById(R.id.et_mem_id);
//    }
//
//    public void onSubmitClick(View view){
//        String input_mem_id = et_input_mem_id.getText().toString();
//        Fragment fragment = new MemberDetailFragment();
//        MemberVO memberVO = new MemberVO();
//        memberVO.setMem_no(input_mem_id);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("memberVO", memberVO);
//        fragment.setArguments(bundle);
//        switchFragment(fragment);
//    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tmpFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
