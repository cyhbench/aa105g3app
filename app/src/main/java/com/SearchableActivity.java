package com;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.R;
import com.collection.CollectionDetailRecipeMainActivity;
import com.main.Common;
import com.recipe.RecipeGetAllByRecipeNameTask;
import com.recipe.RecipeGetImageTask;
import com.recipe.RecipeGetOneByRecipeNOTask;
import com.recipe.RecipeVO;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by rick.wu on 2015/8/26.
 */
public class SearchableActivity extends Activity {
    private String recipe_name;
    private List<RecipeVO> recipeVOList;
    private RecyclerView rvRecipeVO;
    private RecipeVO recipeVO;
    private RecipeVOAdapter recipeVOAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_detail_activity);
        recipe_name = (String) getIntent().getExtras().getSerializable("recipe_name");
        Toast.makeText(this,"您的搜尋是: " +  recipe_name, Toast.LENGTH_SHORT).show();
        TextView tv_collection_class;
        tv_collection_class = (TextView) findViewById(R.id.tv_collection_class);
        tv_collection_class.setText("您的搜尋如下:");

        rvRecipeVO = (RecyclerView) findViewById(R.id.rv_collection_detail);
        rvRecipeVO.setLayoutManager(new LinearLayoutManager(this));
        showSearchItem();
        recipeVOAdapter = new SearchableActivity.RecipeVOAdapter(this, recipeVOList);
        rvRecipeVO.setAdapter(recipeVOAdapter);

        handleIntent(getIntent());
    }

    private void showSearchItem() {
        if (Common.networkConnected(this)) {
            String url = Common.URL + "RecipeServletAndroid";
            List<RecipeVO> recipeTmpList = null;
            try {
                recipeVOList = new RecipeGetAllByRecipeNameTask().execute(url, recipe_name).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (recipeVOList == null || recipeVOList.isEmpty()) {
                Common.showToast(this, R.string.msg_NoCollectionsFound);
            } else {

            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }

    private class RecipeVOAdapter extends RecyclerView.Adapter<SearchableActivity.RecipeVOAdapter.MyViewHolder> {
        Context context;
//        List<CollectionVO> CollectionVOList;

        public RecipeVOAdapter(Context context, List<RecipeVO> recipeVOList) {
            this.context = context;
//            this.CollectionVOList = CollectionVOList;
        }

        @Override
        public SearchableActivity.RecipeVOAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.collection_detail_item, parent, false);
            return new SearchableActivity.RecipeVOAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SearchableActivity.RecipeVOAdapter.MyViewHolder myViewHolder, int position) {
//            final CollectionVO collectionVO = CollectionVOList.get(position);
            final int ipos = position;
            recipeVO = recipeVOList.get(position);
            myViewHolder.iv_collection_detail_item_pic.setImageResource(R.drawable.default_image);
            myViewHolder.tv_collection_detail_item_name.setText(recipeVO.getRecipe_name());

            String url = Common.URL + "RecipeServletAndroid";
            if (Common.networkConnected(SearchableActivity.this)) {
                try {
                    recipeVO = new RecipeGetOneByRecipeNOTask().execute(url, recipeVOList.get(ipos).getRecipe_no()).get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (recipeVO == null) {
                    Common.showToast(SearchableActivity.this, R.string.msg_NoRecipeFound);
                } else {
                    myViewHolder.tv_collection_detail_item_name.setText(recipeVO.getRecipe_name());
                }
            } else {
                Common.showToast(SearchableActivity.this, R.string.msg_NoNetwork);
            }

            int imageSize = 400;

            new RecipeGetImageTask(myViewHolder.iv_collection_detail_item_pic).execute(url, recipeVOList.get(ipos).getRecipe_no(), imageSize);

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = Common.URL + "RecipeServletAndroid";
                    try {
                        recipeVO = new RecipeGetOneByRecipeNOTask().execute(url, recipeVOList.get(ipos).getRecipe_no()).get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    Intent intent = new Intent(context, CollectionDetailRecipeMainActivity.class);//DetailActivity.class:物件
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipeVO", recipeVO);
                    bundle.putSerializable("mTitle", "食譜搜尋內容");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return recipeVOList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_collection_detail_item_pic;
            TextView tv_collection_detail_item_name;

            public MyViewHolder(View itemView) {
                super(itemView);
                iv_collection_detail_item_pic = (ImageView) itemView.findViewById(R.id.iv_collection_detail_item_pic);
                tv_collection_detail_item_name = (TextView) itemView.findViewById(R.id.tv_collection_detail_item_name);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private void showToast(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
