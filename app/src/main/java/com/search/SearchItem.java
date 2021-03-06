package com.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.chef.ChefGetOneByChefNoTask;
import com.chef.ChefVO;
import com.collection.CollectionDeleteTask;
import com.collection.CollectionDetailChefMainActivity;
import com.collection.CollectionDetailMemberMainActivity;
import com.collection.CollectionDetailRecipeMainActivity;
import com.collection.CollectionGetAllByMemNOTask;
import com.collection.CollectionVO;
import com.main.Common;
import com.member.MemberGetImageTask;
import com.member.MemberGetOneTask;
import com.member.MemberVO;
import com.recipe.RecipeGetImageTask;
import com.recipe.RecipeGetOneByRecipeNOTask;
import com.recipe.RecipeVO;

import java.util.ArrayList;
import java.util.List;

//我的最愛 > 私廚、食譜、會員 > 私廚 or 食譜 or 會員 > 其一的最愛詳情
public class SearchItem extends AppCompatActivity {
    private RecyclerView rvCollectionVO;
    private static final String TAG = "CollectionDetail";
    private RecipeVO recipeVO;
    private MemberVO memberVO;
    private CollectionVO collectionVO;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CollectionVOAdapter collectionVOAdapter;
    private List<CollectionVO> collClassList = new ArrayList<CollectionVO>();
    private List<CollectionVO> collChefList = new ArrayList<CollectionVO>();
    private List<CollectionVO> collMemberList = new ArrayList<CollectionVO>();
    private List<CollectionVO> collRecipeList = new ArrayList<CollectionVO>();
    private List<CollectionVO> collectionVOList2 = new ArrayList<CollectionVO>();
    private List<CollectionVO> collectionVOList = new ArrayList<CollectionVO>();
    private TextView tv_collection_class;
    private List collVOList;
    //    private CollectionVOAdapter collectionVOAdapter;
    private String mem_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_detail_activity);

//        swipeRefreshLayout =
//                (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutFrd_list);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                onStart();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });

        collVOList = (List) getIntent().getExtras().getSerializable("collectionVOList");
        mem_no = (String)getIntent().getExtras().getSerializable("mem_no");
        tv_collection_class = (TextView) findViewById(R.id.tv_collection_class);
//        showCollectionListItem();



//        if (collectionVO.getClass_no().equals("R")) {
//            tv_collection_class.setText("我的收藏-食譜");collectionVOList.addAll(collRecipeList);
//        } else if (collectionVO.getClass_no().equals("C")) {
//            tv_collection_class.setText("我的收藏-私廚"); collectionVOList.addAll(collChefList);
//        } else if (collectionVO.getClass_no().equals("M")) {
//            tv_collection_class.setText("我的收藏-會員"); collectionVOList.addAll(collMemberList);
//        }



        rvCollectionVO = (RecyclerView) findViewById(R.id.rv_collection_detail);
        rvCollectionVO.setLayoutManager(new LinearLayoutManager(this));
        showCollectionListItem();
        collectionVOAdapter = new SearchItem.CollectionVOAdapter(this, collectionVOList);
        rvCollectionVO.setAdapter(collectionVOAdapter);

//        showFrd_listItem();
//        frd_listRecyclerViewAdapter = new Frd_listFragment.Frd_listRecyclerViewAdapter(getActivity(), frd_listMemberVOList);
//        rvFrd_list.setAdapter(frd_listRecyclerViewAdapter);
    }

    private void showCollectionListItem() {
        if (Common.networkConnected(this)) {
            String url = Common.URL + "CollectionServletAndroid";
            List<CollectionVO> collTmpList = null; collChefList.clear();collRecipeList.clear();collMemberList.clear();
            try {
                collTmpList = new CollectionGetAllByMemNOTask().execute(url, mem_no).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (collTmpList == null || collTmpList.isEmpty()) {
                Common.showToast(this, R.string.msg_NoCollectionsFound);
            } else {
//                collClassList = new ArrayList<CollectionVO>();
//                collChefList = new ArrayList<CollectionVO>();
//                collMemberList = new ArrayList<CollectionVO>();
//                collRecipeList = new ArrayList<CollectionVO>();

                int r = 0, f = 0, c = 0, m = 0, l = 0;
                for (CollectionVO aCollection : collTmpList) {
                    if (aCollection.getClass_no().equals("R")) {
                        collRecipeList.add(aCollection);
                        if (r == 0) {
                            collClassList.add(aCollection);
                            r++;
                        }
                    }
                    if (aCollection.getClass_no().equals("C")) {
                        collChefList.add(aCollection);
                        if (c == 0) {
                            collClassList.add(aCollection);
                            c++;
                        }
                    }
                    if (aCollection.getClass_no().equals("M")) {
                        collMemberList.add(aCollection);
                        if (m == 0) {
                            collClassList.add(aCollection);
                            m++;
                        }
                    }
                }
            }
            CollectionVO collectionVO = (CollectionVO) collVOList.get(0);
            collectionVOList.clear();
            if (collectionVO.getClass_no().equals("R")) {
                tv_collection_class.setText("我的收藏-食譜");
                collectionVOList.addAll(collRecipeList);
            } else if (collectionVO.getClass_no().equals("C")) {
                tv_collection_class.setText("我的收藏-私廚");
                collectionVOList.addAll(collChefList);
            } else if (collectionVO.getClass_no().equals("M")) {
                tv_collection_class.setText("我的收藏-會員");
                collectionVOList.addAll(collMemberList);
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }

    private class CollectionVOAdapter extends RecyclerView.Adapter<CollectionVOAdapter.MyViewHolder> {
        Context context;
//        List<CollectionVO> CollectionVOList;

        public CollectionVOAdapter(Context context, List<CollectionVO> collectionVOList) {
            this.context = context;
//            this.CollectionVOList = CollectionVOList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.collection_detail_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
//            final CollectionVO collectionVO = CollectionVOList.get(position);
            final int ipos = position;
            collectionVO = collectionVOList.get(position);
            if (collectionVO.getClass_no().equals("R")) {//食譜 getOneByRecipe_no_For_Display
                myViewHolder.iv_collection_detail_item_pic.setImageResource(R.drawable.default_image);
                myViewHolder.tv_collection_detail_item_name.setText(collectionVO.getAll_no());

                String url = Common.URL + "RecipeServletAndroid";
                if (Common.networkConnected(SearchItem.this)) {
                    try {
                        recipeVO = new RecipeGetOneByRecipeNOTask().execute(url, collectionVOList.get(ipos).getAll_no()).get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (recipeVO == null) {
                        Common.showToast(SearchItem.this, R.string.msg_NoRecipeFound);
                    } else {
                        myViewHolder.tv_collection_detail_item_name.setText(recipeVO.getRecipe_name());
                    }
                } else {
                    Common.showToast(SearchItem.this, R.string.msg_NoNetwork);
                }

                int imageSize = 400;
//                String url = Common.URL + "RecipeServletAndroid";
                new RecipeGetImageTask(myViewHolder.iv_collection_detail_item_pic).execute(url, collectionVOList.get(ipos).getAll_no(), imageSize);
//                Log.d(TAG, "recipe=================" + recipeVO.getRecipe_name());

                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = Common.URL + "RecipeServletAndroid";
                        try {
                            recipeVO = new RecipeGetOneByRecipeNOTask().execute(url, collectionVOList.get(ipos).getAll_no()).get();
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        //
                        Intent intent = new Intent(context, CollectionDetailRecipeMainActivity.class);//DetailActivity.class:物件
                        Bundle bundle = new Bundle();
                        //              bundle.putString("name", spot.getFilmTitle());
                        bundle.putSerializable("recipeVO", recipeVO);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(SearchItem.this, view, Gravity.END);
                        popupMenu.inflate(R.menu.popup_menu_delete_collection);

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {

                                    case R.id.delete_from_collection:
                                        if (Common.networkConnected(SearchItem.this)) {
                                            String url = Common.URL + "CollectionServletAndroid";

                                            int count = 0;
                                            try {
                                                new CollectionDeleteTask().execute(url, collectionVOList.get(ipos).getColl_no(), collectionVOList.get(ipos).getMem_no()).get();
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                                count = 1;
                                            }
                                            if (count == 1) {
                                                Common.showToast(SearchItem.this, R.string.msg_DeleteFromCollectionFail);
                                            } else {
//                                                showFrd_listItem();
//                                                frd_listRecyclerViewAdapter.notifyDataSetChanged();
                                                showCollectionListItem();
                                                collectionVOAdapter.notifyDataSetChanged();
                                                Common.showToast(SearchItem.this, R.string.msg_DeleteFromCollectionSuccess);
                                            }
                                        } else {
                                            Common.showToast(SearchItem.this, R.string.msg_NoNetwork);
                                        }
                                }
                                return true;
                            }
                        });
                        popupMenu.show();

                        return true;
                    }
                });

            } else if (collectionVO.getClass_no().equals("C")) {//私廚
                myViewHolder.iv_collection_detail_item_pic.setImageResource(R.drawable.default_image);
                myViewHolder.tv_collection_detail_item_name.setText(collectionVO.getAll_no());

                String url = Common.URL + "ChefServletAndroid.do";

                ChefVO chefVO = null;
                if (Common.networkConnected(SearchItem.this)) {
                    try {
                        chefVO = new ChefGetOneByChefNoTask().execute(url, collectionVOList.get(ipos).getAll_no()).get();
                        url = Common.URL + "MemberServletAndroid";
                        memberVO = new MemberGetOneTask().execute(url, chefVO.getMem_no()).get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (memberVO == null) {
                        Common.showToast(SearchItem.this, R.string.msg_NoChefsFound);
                    } else {
                        myViewHolder.tv_collection_detail_item_name.setText(chefVO.getChef_name());
                    }
                } else {
                    Common.showToast(SearchItem.this, R.string.msg_NoNetwork);
                }
                int imageSize = 400;
                try {
                    Bitmap bitmap = new MemberGetImageTask(myViewHolder.iv_collection_detail_item_pic).execute(url, chefVO.getMem_no(), imageSize).get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = Common.URL + "ChefServletAndroid.do";
                        ChefVO chefVO = null;
                        try {
                            chefVO = new ChefGetOneByChefNoTask().execute(url, collectionVOList.get(ipos).getAll_no()).get();
                            url = Common.URL + "MemberServletAndroid";
                            memberVO = new MemberGetOneTask().execute(url, chefVO.getMem_no()).get();
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        Intent intent = new Intent(context, CollectionDetailChefMainActivity.class);//DetailActivity.class:物件
                        Bundle bundle = new Bundle();
                        //              bundle.putString("name", spot.getFilmTitle());
                        bundle.putSerializable("memberVO", memberVO);
                        bundle.putSerializable("chefVO", chefVO);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(SearchItem.this, view, Gravity.END);
                        popupMenu.inflate(R.menu.popup_menu_delete_collection);

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {

                                    case R.id.delete_from_collection:
                                        if (Common.networkConnected(SearchItem.this)) {
                                            String url = Common.URL + "CollectionServletAndroid";

                                            int count = 0;
                                            try {
                                                new CollectionDeleteTask().execute(url, collectionVOList.get(ipos).getColl_no(), collectionVOList.get(ipos).getMem_no()).get();
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                                count = 1;
                                            }
                                            if (count == 1) {
                                                Common.showToast(SearchItem.this, R.string.msg_DeleteFromCollectionFail);
                                            } else {
                                                showCollectionListItem();
                                                collectionVOAdapter.notifyDataSetChanged();
                                                Common.showToast(SearchItem.this, R.string.msg_DeleteFromCollectionSuccess);
                                            }
                                        } else {
                                            Common.showToast(SearchItem.this, R.string.msg_NoNetwork);
                                        }
                                }
                                return true;
                            }
                        });
                        popupMenu.show();

                        return true;
                    }
                });


            } else if (collectionVO.getClass_no().equals("M")) {//會員
                myViewHolder.iv_collection_detail_item_pic.setImageResource(R.drawable.default_image);
                myViewHolder.tv_collection_detail_item_name.setText(collectionVO.getAll_no());

                String url = Common.URL + "MemberServletAndroid";
                if (Common.networkConnected(SearchItem.this)) {
                    try {
                        memberVO = new MemberGetOneTask().execute(url, collectionVOList.get(ipos).getAll_no()).get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (memberVO == null) {
                        Common.showToast(SearchItem.this, R.string.msg_NoMembersFound);
                    } else {
                        myViewHolder.tv_collection_detail_item_name.setText(memberVO.getMem_name());
                    }
                } else {
                    Common.showToast(SearchItem.this, R.string.msg_NoNetwork);
                }
                int imageSize = 400;
                try {
                    Bitmap bitmap = new MemberGetImageTask(myViewHolder.iv_collection_detail_item_pic).execute(url, collectionVOList.get(ipos).getAll_no(), imageSize).get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = Common.URL + "MemberServletAndroid";
                        try {
                            memberVO = new MemberGetOneTask().execute(url, collectionVOList.get(ipos).getAll_no()).get();
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        Intent intent = new Intent(context, CollectionDetailMemberMainActivity.class);//DetailActivity.class:物件
                        Bundle bundle = new Bundle();
                        //              bundle.putString("name", spot.getFilmTitle());
                        bundle.putSerializable("memberVO", memberVO);
                        bundle.putSerializable("mem_no", mem_no);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, spot.getFilmTitle(), Toast.LENGTH_SHORT).show();
//                }
                });
            }

//            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, CollectionDetail.class);//DetailActivity.class:物件
//                    Bundle bundle = new Bundle();
//                    //              bundle.putString("name", spot.getFilmTitle());
//                    bundle.putSerializable("CollectionVO", CollectionVO);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//
////                @Override
////                public void onClick(View v) {
////                    Toast.makeText(context, spot.getFilmTitle(), Toast.LENGTH_SHORT).show();
////                }
//            });

        }

        @Override
        public int getItemCount() {
            return collectionVOList.size();
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
}
