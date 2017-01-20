package com.collection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.main.Common;
import com.member.MemberGetOneTask;
import com.member.MemberVO;
import com.recipe.RecipeGetAllByMemNOTask;
import com.recipe.RecipeGetImageTask;
import com.recipe.RecipeVO;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/*本頁面是來瀏覽我的收藏中會員或私廚的食譜
* 因為食譜內容沒有包含編輯中的食譜
* 所以重寫一個fragment
* 來顯示已發布的食譜*/
public class CollectionDetailBrowserMemberRecipeFragment extends Fragment {
    private List<RecipeVO> recipeVOList;
    private static final String TAG = "RecipeFragment";
    private RecyclerView rvRecipeVO;
    private MemberVO memberVO;
    private String mem_no;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_fragment, container, false);

        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                onStart();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        memberVO = (MemberVO) getArguments().getSerializable("memberVO");
        mem_no = (String)getArguments().getSerializable("mem_no");
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(new RecipeRecyclerViewAdapter(inflater));

        rvRecipeVO = (RecyclerView) view.findViewById(R.id.recyclerView);
        rvRecipeVO.setLayoutManager(new LinearLayoutManager(getActivity()));

        //取值
//        SharedPreferences pref = this.getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
//        mem_no = pref.getString("mem_no", "M00000001");
//        mem_name = pref.getString("mem_name", "");
//        mem_own = pref.getString("mem_own", "");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showRecipeVOItem();
    }

    private void showRecipeVOItem() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "RecipeServletAndroid";
            List<RecipeVO> recipeVOList = null;
            try {
                recipeVOList = new RecipeGetAllByMemNOTask().execute(url, memberVO.getMem_no()).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (recipeVOList == null || recipeVOList.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoRecipesFound);
            } else {
                List<RecipeVO> list2 = new ArrayList<RecipeVO>();
                for (RecipeVO aRecipe : recipeVOList) {
                    if (aRecipe.getRecipe_edit().equals("已發布")) {
                        list2.add(aRecipe);
                    }
                }
                rvRecipeVO.setAdapter(new CollectionDetailBrowserMemberRecipeFragment.RecipeRecyclerViewAdapter(getActivity(), list2));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }


    private class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<CollectionDetailBrowserMemberRecipeFragment.RecipeRecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<RecipeVO> recipeVOList;

        public RecipeRecyclerViewAdapter(Context context, List<RecipeVO> recipeVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.recipeVOList = recipeVOList;
        }

        @Override
        public int getItemCount() {
            return recipeVOList.size();
        }

        @Override
        public CollectionDetailBrowserMemberRecipeFragment.RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.recipe_item, parent, false);
            return new CollectionDetailBrowserMemberRecipeFragment.RecipeRecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CollectionDetailBrowserMemberRecipeFragment.RecipeRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            final RecipeVO recipeVO = recipeVOList.get(position);
            String url = Common.URL + "RecipeServletAndroid";
            String recipe_no = recipeVO.getRecipe_no();
//            String mem_no = memberVO.getMem_no();
            int imageSize = 480;

//            viewHolder.iv_recipe_pic.setDrawingCacheEnabled(true); //cache !!!
//            viewHolder.iv_recipe_pic.destroyDrawingCache();

            new RecipeGetImageTask(viewHolder.iv_recipe_pic).execute(url, recipe_no, imageSize);
//            viewHolder.tv_recipe_no.setText((java.lang.CharSequence)recipeVO.getRecipe_no());
            viewHolder.tv_recipe_name.setText((java.lang.CharSequence) recipeVO.getRecipe_name());
            viewHolder.tv_recipe_time.setText("發布:" + String.valueOf(recipeVO.getRecipe_time()).subSequence(0, 16));
            viewHolder.tv_recipe_total_views.setText("人氣:" + recipeVO.getRecipe_total_views().toString() + " ");

            MemberVO recipeByWho = null;
            url = Common.URL + "MemberServletAndroid";
            if (Common.networkConnected(getActivity())) {
                try {
                    recipeByWho = new MemberGetOneTask().execute(url, recipeVO.getMem_no()).get();
                    Log.d(TAG, "getMem_name(line 146):" + memberVO.getMem_name());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (recipeByWho == null) {
                    Common.showToast(getActivity(), R.string.msg_NoMembersFound);
                } else {
                    viewHolder.tv_recipe_by_who.setText("作者:" + recipeByWho.getMem_name());
                }
            } else {
                Common.showToast(getActivity(), R.string.msg_NoNetwork);
            }

            BitmapDrawable bitmapDrawable = (BitmapDrawable) viewHolder.iv_recipe_pic.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            byte[] image;

            if (bitmap != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                image = out.toByteArray();
                recipeVO.setRecipe_pic(image);
            }


//            BitmapDrawable drawable = (BitmapDrawable) viewHolder.iv_recipe_pic.getDrawable();
//            Bitmap bitmap = drawable.getBitmap();
//            byte[] image;
//            if (bitmap != null) {
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                image = out.toByteArray();
//                recipeVO.setRecipe_pic(image);
//            }


//            Bitmap bitmap = BitmapFactory.decodeByteArray(recipeVO.getRecipe_pic(), 0, recipeVO.getRecipe_pic().length);
//            viewHolder.iv_recipe_pic.setImageBitmap(bitmap);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CollectionDetailRecipeMainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipeVO", recipeVO);
                    intent.putExtras(bundle);
                    startActivity(intent);

//                    Intent intent = new Intent(this, MemberUpdateActivity.class);
//                    Bundle bundle = new Bundle();
//                    Log.d(TAG, "getMem_name(line 306):" + memberVO.getMem_name());
//                    memberVO.setMem_image(image); //因為會員圖與文字是分開抓的，所以要把圖給set()回去
//                    memberVO.setMem_no(mem_no);
//                    Log.d(TAG, "line 306:" + memberVO.getMem_image().length);
//                    bundle.putSerializable("memberVO", memberVO);
//                    intent.putExtras(bundle);
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu_add_coll);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.addToCollection:
                                    if (Common.networkConnected(getActivity())) {
                                        String url = Common.URL + "CollectionServletAndroid";
//                                        String action = "insert";
                                        CollectionVO collectionVO = null;
//                                        int count = 0;
                                        try {
//                                            collectionVO = new CollectionInsertTask().execute(url, recipeVO.getMem_no(), recipeVO.getRecipe_no()).get();
                                            collectionVO = new CollectionInsertTask().execute(url, mem_no, recipeVO.getRecipe_no()).get();

                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
//                                            count =1;
                                        }
                                        if (collectionVO == null) {
                                            Common.showToast(getActivity(), R.string.msg_AddToCollectionFail);
                                        } else {
//                                            RecipeFragment.RecipeRecyclerViewAdapter.this.notifyDataSetChanged();
                                            Common.showToast(getActivity(), R.string.msg_AddToCollectionSuccess);
                                        }
                                    } else {
                                        Common.showToast(getActivity(), R.string.msg_NoNetwork);
                                    }
                            }
                            return true;
                        }
                    });
                    popupMenu.show();

                    return true;
                }
            });
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_recipe_no, tv_recipe_name, tv_recipe_by_who, tv_recipe_time, tv_recipe_total_views;
            ImageView iv_recipe_pic;

            public ViewHolder(View itemView) {
                super(itemView);
//                tv_recipe_no = (TextView) itemView.findViewById(R.id.tv_recipe_member_page_recipe_no);
                tv_recipe_name = (TextView) itemView.findViewById(R.id.tv_recipe_member_page_recipe_name);
                iv_recipe_pic = (ImageView) itemView.findViewById(R.id.iv_tv_recipe_member_page_recipe_pic);
                tv_recipe_by_who = (TextView) itemView.findViewById(R.id.tv_recipe_member_page_recipe_by_who);
                tv_recipe_time = (TextView) itemView.findViewById(R.id.tv_recipe_member_page_recipe_time);
                tv_recipe_total_views = (TextView) itemView.findViewById(R.id.tv_recipe_member_page_recipe_total_views);
            }
        }
    }


//    private class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.ViewHolder> {
//        private LayoutInflater inflater;
//
//        public IntroAdapter(LayoutInflater inflater) {
//            this.inflater = inflater;
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            TextView tvName, tvTitle;
//            ImageView imageView;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                tvName = (TextView) itemView.findViewById(R.id.tvFilmTitle);
//                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
//                imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            }
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = inflater.inflate(R.layout.recipe_item, parent, false);
//            ViewHolder viewHolder = new ViewHolder(itemView);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder viewHolder, int position) {
//            final RecipeVO recipeVO = recipeVOList.get(position);
//            viewHolder.tvName.setText(recipeVO.getRecipe_name());
//            viewHolder.tvTitle.setText(recipeVO.getRecipe_intro());
//       //     viewHolder.imageView.setImageResource(recipeVO.getImageId());
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("recipeVO", recipeVO);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return recipeVOList.size();
//        }
//    }
//
}
