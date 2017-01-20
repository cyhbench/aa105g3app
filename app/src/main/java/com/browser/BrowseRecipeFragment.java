package com.browser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
import android.widget.Toast;

import com.Frd_list.Frd_listInsertTask;
import com.androidbelieve.drawerwithswipetabs.R;
import com.collection.CollectionInsertTask;
import com.collection.CollectionVO;
import com.main.Common;
import com.main.LoginDialogActivity;
import com.member.MemberGetImageTask;
import com.member.MemberGetOneTask;
import com.member.MemberVO;
import com.recipe.RecipeGetAllTask;
import com.recipe.RecipeGetImageTask;
import com.recipe.RecipeMainActivity;
import com.recipe.RecipeVO;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/*本頁面是來瀏覽食譜
* 因為食譜內容沒有包含編輯中的食譜
* 所以重寫一個fragment
* 來顯示已發布的食譜*/
public class BrowseRecipeFragment extends Fragment {
    private List<RecipeVO> recipeVOList;
    private static final String TAG = "BrowseRecipeFragment";
    private RecyclerView rvRecipeVO;
    private MemberVO memberVO;
    private String mem_no;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int REQ_LOGIN = 1;

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

        rvRecipeVO = (RecyclerView) view.findViewById(R.id.recyclerView);
        rvRecipeVO.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                recipeVOList = new RecipeGetAllTask().execute(url).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (recipeVOList == null || recipeVOList.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoRecipesFound);
            } else {
                rvRecipeVO.setAdapter(new BrowseRecipeFragment.RecipeRecyclerViewAdapter(getActivity(), recipeVOList));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }


    private class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<BrowseRecipeFragment.RecipeRecyclerViewAdapter.ViewHolder> {
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
        public BrowseRecipeFragment.RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.browse_recipe_item, parent, false);
            return new BrowseRecipeFragment.RecipeRecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BrowseRecipeFragment.RecipeRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            final RecipeVO recipeVO = recipeVOList.get(position);
            final int apos = position;
            String url = Common.URL + "RecipeServletAndroid";
            String recipe_no = recipeVO.getRecipe_no();

            int imageSize = 1024;


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


//            viewHolder.tv_recipe_no.setText((java.lang.CharSequence)recipeVO.getRecipe_no());
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

            BitmapDrawable bitmapDrawable = (BitmapDrawable) viewHolder.iv_recipe_pic.getDrawable();
//            Bitmap bitmap = bitmapDrawable.getBitmap();
//            byte[] image;

            bitmap = bitmapDrawable.getBitmap();


            if (bitmap != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                image = out.toByteArray();
                recipeVO.setRecipe_pic(image);
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

            viewHolder.iv_browse_recipe_who_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), recipeVOList.get(apos).getMem_no(), Toast.LENGTH_LONG).show();
//                  Intent intent = new Intent(getActivity(), BrowseOneMemberMainActivity.class);//DetailActivity.class:物件
                    Intent intent = new Intent(getActivity(), BrowseOneMemberMainActivity.class);
                    Bundle bundle = new Bundle();
                    //              bundle.putString("name", spot.getFilmTitle());
                    bundle.putSerializable("mem_no", (Serializable) recipeVOList.get(apos).getMem_no());
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
                                        CollectionVO collectionVO = null;
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
                                        }
                                        if (collectionVO == null) {
                                        } else {
                                            Common.showToast(getActivity(), R.string.msg_AddToCollectionSuccess);
                                        }
                                    } else {
                                        Common.showToast(getActivity(), R.string.msg_NoNetwork);
                                    }

                                case R.id.addToMember:
                                    if (Common.networkConnected(getActivity())) {
                                        String url = Common.URL + "CollectionServletAndroid";
                                        CollectionVO collectionVO = null;
                                        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                                        boolean login = pref.getBoolean("login", false);


                                        try {
                                            if (login) {
                                                mem_no = pref.getString("mem_no", "");

                                                String mem_ac = pref.getString("mem_ac", "");
                                                String mem_pw = pref.getString("mem_pw", "");
                                                collectionVO = new CollectionInsertTask().execute(url, mem_no, recipeVO.getMem_no()).get();
                                            } else {
                                                Intent loginIntent = new Intent(getActivity(), LoginDialogActivity.class);
                                                startActivityForResult(loginIntent, REQ_LOGIN);
                                            }

                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (collectionVO == null) {
                                        } else {
                                            Common.showToast(getActivity(), R.string.msg_AddToCollectionSuccess);
                                        }
                                    } else {
                                        Common.showToast(getActivity(), R.string.msg_NoNetwork);
                                    }

                                case R.id.addToFrd:
                                    if (Common.networkConnected(getActivity())) {
                                        String url = Common.URL + "Frd_listServletAndroid";
                                        CollectionVO collectionVO = null;
                                        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                                        boolean login = pref.getBoolean("login", false);


                                        try {
                                            if (login) {
                                                mem_no = pref.getString("mem_no", "");

                                                String mem_ac = pref.getString("mem_ac", "");
                                                String mem_pw = pref.getString("mem_pw", "");
                                                new Frd_listInsertTask().execute(url, "insert", mem_no, recipeVO.getMem_no()).get();
                                            } else {
                                                Intent loginIntent = new Intent(getActivity(), LoginDialogActivity.class);
                                                startActivityForResult(loginIntent, REQ_LOGIN);
                                            }

                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (collectionVO == null) {
                                        } else {
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
