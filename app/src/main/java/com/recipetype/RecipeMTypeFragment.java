package com.recipetype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.collection.CollectionInsertTask;
import com.collection.CollectionVO;
import com.main.Common;
import com.main.LoginDialogActivity;
import com.member.MemberGetImageTask;
import com.member.MemberGetOneTask;
import com.member.MemberVO;
import com.recipe.RecipeGetBelowAllByRecipe_m_type_noTask;
import com.recipe.RecipeGetImageTask;
import com.recipe.RecipeMainActivity;
import com.recipe.RecipeVO;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.androidbelieve.drawerwithswipetabs.R.id.recyclerView;

/*Qoo*/
public class RecipeMTypeFragment extends Fragment {
    private List<RecipeVO> recipeVOList = new ArrayList<RecipeVO>();
    private static final String TAG = "RecipeMTypeFragment";
    private RecyclerView rvRecipeVO;
    private RecyclerView rvRecipeTypeVO;
    private MemberVO memberVO;
    private String mem_no;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int REQ_LOGIN = 1;
    private String recipe_m_type_no;
    private List<Recipe_s_typeVO> recipe_s_typeVOList;
    private TextView choosing_type;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_l_fragment, container, false);

        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutRecipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                onStart();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recipe_m_type_no = (String) getArguments().getSerializable("recipe_m_type_no");
        String recipe_m_type_name = (String) getArguments().getSerializable("recipe_m_type_name");

        choosing_type = (TextView) view.findViewById(R.id.choosing_type);
        choosing_type.setText("所有 "+ recipe_m_type_name+" 的食譜");
        choosing_type.setBackgroundColor(Color.parseColor("#dedede"));
        rvRecipeTypeVO = (RecyclerView) view.findViewById(R.id.recyclerViewType);
        rvRecipeTypeVO.setNestedScrollingEnabled(false);//////////
//        gvRecipeLTypeVO.setLayout
        rvRecipeTypeVO.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        rvRecipeVO = (RecyclerView) view.findViewById(recyclerView);
        rvRecipeVO.setNestedScrollingEnabled(false);
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
        showRecipeTypeVOItem();
        showRecipeVOItem();
    }

    private void showRecipeTypeVOItem() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/recipe_m_type/recipe_m_type_android.do";
//            List<Recipe_l_typeVO> recipe_l_typeVOList = null;
            try {
                recipe_s_typeVOList = new RecipeSTypeGetAllByMTypeNoTask().execute(url, recipe_m_type_no).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (recipe_s_typeVOList == null || recipe_s_typeVOList.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoRecipeLTypeFound);
            } else {
                rvRecipeTypeVO.setAdapter(new RecipeMTypeFragment.RecipeTypeRecyclerViewAdapter(getActivity(), recipe_s_typeVOList));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }


    }

    private void showRecipeVOItem() {
        if (recipeVOList == null || recipeVOList.isEmpty()) {

        } else {
            recipeVOList.clear();
        }
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "RecipeServletAndroid";
            List<RecipeVO> list = null;
//            list.clear();
//            for (Recipe_s_typeVO aRecipe_s_typeVO : recipe_s_typeVOList) {
                try {
                    list = new RecipeGetBelowAllByRecipe_m_type_noTask().execute(url, recipe_m_type_no).get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

                if(list != null && !list.isEmpty()){
                    for(RecipeVO aRecipe : list){
                        if(aRecipe!=null){
                            recipeVOList.add(aRecipe);
                        }
                    }
                }
//            }

            if (recipeVOList == null || recipeVOList.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoRecipesFound);
            } else {
                rvRecipeVO.setAdapter(new RecipeMTypeFragment.RecipeRecyclerViewAdapter(getActivity(), recipeVOList));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }


    private class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeMTypeFragment.RecipeRecyclerViewAdapter.ViewHolder> {
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
        public RecipeMTypeFragment.RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.browse_recipe_item, parent, false);
            return new RecipeMTypeFragment.RecipeRecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecipeMTypeFragment.RecipeRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            final RecipeVO recipeVO = recipeVOList.get(position);
            String url = Common.URL + "RecipeServletAndroid";
            String recipe_no = recipeVO.getRecipe_no();
//            String mem_no = memberVO.getMem_no();
            int imageSize = 800;

//            viewHolder.iv_recipe_pic.setDrawingCacheEnabled(true); //cache !!!
//            viewHolder.iv_recipe_pic.destroyDrawingCache();

            new RecipeGetImageTask(viewHolder.iv_recipe_pic).execute(url, recipe_no, imageSize);

            Bitmap bitmap = null;
            byte[] image;
            try {
                url = Common.URL + "MemberServletAndroid";
                imageSize = 100;
                bitmap = new MemberGetImageTask(null).execute(url, recipeVOList.get(position).getMem_no(), imageSize).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (bitmap != null) {
                viewHolder.iv_browse_recipe_who_pic.setImageBitmap(getRoundedCornerBitmap(bitmap, 500.0f));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                image = out.toByteArray();
            } else {
                viewHolder.iv_browse_recipe_who_pic.setImageResource(R.drawable.default_image);
            }

            viewHolder.tv_recipe_name.setText((CharSequence) recipeVO.getRecipe_name());
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

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RecipeMainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipeVO", recipeVO);
                    intent.putExtras(bundle);
                    startActivity(intent);
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
                                        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                                        boolean login = pref.getBoolean("login", false);


                                        try {
                                            if (login) {
                                                mem_no = pref.getString("mem_no", "");

                                                String mem_ac = pref.getString("mem_ac", "");
                                                String mem_pw = pref.getString("mem_pw", "");
                                                collectionVO = new CollectionInsertTask().execute(url, mem_no, recipeVO.getRecipe_no()).get();
                                            } else {
                                                Intent loginIntent = new Intent(getActivity(), LoginDialogActivity.class);
                                                startActivityForResult(loginIntent, REQ_LOGIN);
                                            }

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
            ImageView iv_browse_recipe_who_pic;

            public ViewHolder(View itemView) {
                super(itemView);
//                tv_recipe_no = (TextView) itemView.findViewById(R.id.tv_recipe_member_page_recipe_no);
                tv_recipe_name = (TextView) itemView.findViewById(R.id.tv_browse_recipe_recipe_name);
                iv_recipe_pic = (ImageView) itemView.findViewById(R.id.iv_browse_recipe_recipe_pic);
                tv_recipe_by_who = (TextView) itemView.findViewById(R.id.tv_browse_recipe_recipe_by_who);
                tv_recipe_time = (TextView) itemView.findViewById(R.id.tv_browse_recipe_recipe_time);
                tv_recipe_total_views = (TextView) itemView.findViewById(R.id.tv_browse_recipe_total_views);

                iv_browse_recipe_who_pic = (ImageView) itemView.findViewById(R.id.iv_browse_recipe_who_pic);
            }
        }
    }

    public class RecipeTypeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeMTypeFragment.RecipeTypeRecyclerViewAdapter.TypeViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Recipe_s_typeVO> recipe_s_typeVOList;

        public RecipeTypeRecyclerViewAdapter(Context context, List<Recipe_s_typeVO> recipe_s_typeVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.recipe_s_typeVOList = recipe_s_typeVOList;
        }

        @Override
        public int getItemCount() {
            return recipe_s_typeVOList.size();
        }

        @Override
        public RecipeMTypeFragment.RecipeTypeRecyclerViewAdapter.TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.recipe_l_type_item, parent, false);
            return new RecipeMTypeFragment.RecipeTypeRecyclerViewAdapter.TypeViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecipeMTypeFragment.RecipeTypeRecyclerViewAdapter.TypeViewHolder viewHolder, int position) {
            final Recipe_s_typeVO recipe_s_typeVO = recipe_s_typeVOList.get(position);
            final int apos = position;
            String url = Common.URL + "/recipe_m_type/recipe_m_type_android.do";
            String s_type_name = recipe_s_typeVO.getS_type_name();

//            int imageSize = 800;
//            new RecipeGetImageTask(viewHolder.iv_recipe_pic).execute(url, recipe_no, imageSize);

//            viewHolder.tv_recipe_name.setText((CharSequence) recipeVO.getRecipe_name());
            viewHolder.tv_recipe_l_type_name.setText(s_type_name);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable( "recipe_s_type_no", recipe_s_typeVOList.get(apos).getRecipe_s_type_no());
                    bundle.putSerializable( "recipe_s_type_name", recipe_s_typeVOList.get(apos).getS_type_name());
                    RecipeSTypeFragment recipeSTypeFragment = new RecipeSTypeFragment();
                    recipeSTypeFragment.setArguments(bundle);
                    mFragmentManager = getFragmentManager();
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, recipeSTypeFragment).addToBackStack(null).commit();

                }
            });
//            viewHolder.tv_recipe_total_views.setText("人氣:" + recipeVO.getRecipe_total_views().toString() + " ");

//            MemberVO recipeByWho = null;
//            url = Common.URL + "MemberServletAndroid";
//            if (Common.networkConnected(getActivity())) {
//                try {
//                    recipeByWho = new MemberGetOneTask().execute(url, recipeVO.getMem_no()).get();
//                    Log.d(TAG, "getMem_name(line 146):" + memberVO.getMem_name());
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }
//                if (recipeByWho == null) {
//                    Common.showToast(getActivity(), R.string.msg_NoMembersFound);
//                } else {
//                    viewHolder.tv_recipe_by_who.setText("作者:" + recipeByWho.getMem_name());
//                }
//            } else {
//                Common.showToast(getActivity(), R.string.msg_NoNetwork);
//            }
//
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) viewHolder.iv_recipe_pic.getDrawable();
//            Bitmap bitmap = bitmapDrawable.getBitmap();
//            byte[] image;
//
//            if (bitmap != null) {
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                image = out.toByteArray();
//                recipeVO.setRecipe_pic(image);
//            }

//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), RecipeMainActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("recipeVO", recipeVO);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
        }

        class TypeViewHolder extends RecyclerView.ViewHolder {
            TextView tv_recipe_l_type_name;

            public TypeViewHolder(View itemView) {
                super(itemView);
                tv_recipe_l_type_name = (TextView) itemView.findViewById(R.id.tv_recipe_l_type_name);
            }
        }
    }

    //圓角轉換函式，帶入Bitmap圖片及圓角數值則回傳圓角圖，回傳Bitmap再置入ImageView
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
