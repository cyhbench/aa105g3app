package com.collection;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.Page;
import com.androidbelieve.drawerwithswipetabs.R;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.main.Common;
import com.member.MemberVO;
import com.recipe.RecipeCondPageInfoFragment;
import com.recipe.RecipeCondPageMaterFragment;
import com.recipe.RecipeCondPageStepFragment;
import com.recipe.RecipeVO;
import com.recipe.Recipe_contVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CollectionDetailRecipeMainActivity extends AppCompatActivity {
    private static final String TAG = "logRecipeMainActivity";
    private String url = Common.URL + "/RecipeServletAndroid";
    private byte[] image;
    int imageSize = 400;
    Bitmap bitmap = null;
    private static final int REQ_LOGIN = 1;
    private MemberVO memberVO;// = new MemberVO();
    private RecipeVO recipeVO;
    private Bundle bundle;// = new Bundle();
    private Set<Recipe_contVO> recipe_contVOSet;
    private Button bt_member_page_member_update;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (recipeVO == null) {
            recipeVO = (RecipeVO) getIntent().getExtras().getSerializable("recipeVO");
        }

        setContentView(R.layout.collection_detail_recipe_main_activity);
        String mTitle = (String) getIntent().getExtras().getSerializable("mTitle");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.recipe_toolbar);
        if (mTitle == null) {
            toolbar.setTitle("食譜內容");
        } else {
            toolbar.setTitle(mTitle);
        }
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.recipeViewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.recipeTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
        client.connect();


//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        if (Common.networkConnected(this)) {
//            try {
//                recipe_contVOSet = new RecipeCondGetByRecipeNOTask().execute(url, recipeVO.getRecipe_no()).get();
//                Log.d(TAG, "recipeVO.getRecipe_no():" + recipeVO.getRecipe_no());
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (recipe_contVOSet == null) {
//                showToast(this, R.string.msg_NoRecipeContFound);
//            } else {
//                recipe_viewpage_recipe_cond_recipe_no.setText(recipeVO.getRecipe_no());
//            }
//        } else {
//            showToast(this, R.string.msg_NoNetwork);
//        }
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

//圖圖圖圖圖
//        try {
//            // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()
//
//            bitmap = new MemberGetImageTask(null).execute(url, recipeVO.getRecipe_no(), imageSize).get();
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//        if (bitmap != null) {
////            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); //照相之後再把圖壓縮
////            iv_member_page_mem_image.setImageBitmap(bitmap);
//
////            iv_member_page_mem_image.setImageBitmap(bitmap);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            image = out.toByteArray();
//        } else {
////            iv_member_page_mem_image.setImageResource(R.drawable.default_image);
//        }

    }


    @Override
    public void onStop() {
        super.onStop();


    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * 內容固定無變化:繼承FragmentPagerAdapter：記憶體保留頁面資訊。頁面數量大及變化大，繼承FragmentStatePagerAdapter：換頁時，將被換掉無顯示的頁面從記憶體清除
         */
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            bundle = new Bundle();
            bundle.putSerializable("recipeVO", recipeVO);
            RecipeCondPageMaterFragment recipeCondPageMaterFragment = new RecipeCondPageMaterFragment();
            recipeCondPageMaterFragment.setArguments(bundle);

            RecipeCondPageInfoFragment recipeCondPageInfoFragment = new RecipeCondPageInfoFragment();
            recipeCondPageInfoFragment.setArguments(bundle);

            RecipeCondPageStepFragment recipeCondPageStepFragment = new RecipeCondPageStepFragment();
            recipeCondPageStepFragment.setArguments(bundle);

            pageList = new ArrayList<>();
            pageList.add(new Page(recipeCondPageInfoFragment, "簡介"));
            pageList.add(new Page(recipeCondPageMaterFragment, "食材"));
            pageList.add(new Page(recipeCondPageStepFragment, "步驟"));

        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
        }

    }

}
