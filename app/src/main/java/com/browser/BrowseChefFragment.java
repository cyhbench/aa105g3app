package com.browser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.chef.ChefGetAllTask;
import com.chef.ChefVO;
import com.collection.CollectionDeleteByMem_noAndAll_noTask;
import com.collection.CollectionDeleteTask;
import com.collection.CollectionDetail;
import com.collection.CollectionDetailChefMainActivity;
import com.collection.CollectionGetOneByMemNOAllNOTask;
import com.collection.CollectionInsertTask;
import com.collection.CollectionVO;
import com.main.Common;
import com.main.LoginDialogActivity;
import com.member.MemberGetOneTask;
import com.member.MemberVO;
import com.util.GetImageTask;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/*本頁面是首頁來瀏覽私廚
*/
public class BrowseChefFragment extends Fragment {
    private List<ChefVO> chefVOList;
    private static final String TAG = "BrowseChefFragment";
    private RecyclerView rvChefVO;
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

        rvChefVO = (RecyclerView) view.findViewById(R.id.recyclerView);
        rvChefVO.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showChefVOItem();
    }

    private void showChefVOItem() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "ChefServletAndroid.do";
            List<ChefVO> chefVOList = null;
            try {
                chefVOList = new ChefGetAllTask().execute(url).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (chefVOList == null || chefVOList.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoChefFound);
            } else {
                rvChefVO.setAdapter(new BrowseChefFragment.ChefRecyclerViewAdapter(getActivity(), chefVOList));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }


    private class ChefRecyclerViewAdapter extends RecyclerView.Adapter<BrowseChefFragment.ChefRecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<ChefVO> chefVOList;

        public ChefRecyclerViewAdapter(Context context, List<ChefVO> chefVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.chefVOList = chefVOList;
        }

        @Override
        public int getItemCount() {
            return chefVOList.size();
        }

        @Override
        public BrowseChefFragment.ChefRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.browse_chef_item, parent, false);
            return new BrowseChefFragment.ChefRecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final BrowseChefFragment.ChefRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            final ChefVO chefVO = chefVOList.get(position);
            final ViewHolder listener_v = viewHolder;
            final int apos = position;
            String url = Common.URL + "ChefServletAndroid.do";
            String chef_no = chefVO.getChef_no();
            int imageSize = 800;

            Resources res = getResources();
            viewHolder.iv_browse_chef_chef_image.setImageDrawable(res.getDrawable(R.drawable.default_image));
            new GetImageTask(viewHolder.iv_browse_chef_chef_image).execute(url, chefVOList.get(position).getChef_no(), imageSize, "chef_image");
            viewHolder.tv_browse_chef_chef_name.setText("姓名:" + (CharSequence) chefVOList.get(position).getChef_name());
            viewHolder.tv_browse_chef_chef_area.setText("服務地點:" + chefVOList.get(position).getChef_area());
            viewHolder.tv_browse_chef_chef_menu.setText("拿手菜單:" + chefVOList.get(position).getChef_menu());
            viewHolder.tv_browse_chef_chef_intr.setText("簡介:" + chefVOList.get(position).getChef_intr());

            //判斷會員有無登入給予 紅心黑心
            url = Common.URL + "CollectionServletAndroid";
            CollectionVO collectionVO = null;
            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            boolean login = pref.getBoolean("login", false);
            try {
                if (login) {
                    mem_no = pref.getString("mem_no", "");

                    String mem_ac = pref.getString("mem_ac", "");
                    String mem_pw = pref.getString("mem_pw", "");
                    if (new CollectionGetOneByMemNOAllNOTask().execute(url, mem_no, chefVOList.get(apos).getChef_no()).get()) {
                        viewHolder.browser_chef_add_collection.setVisibility(View.VISIBLE);
                        viewHolder.browser_chef_remove_from_collection.setVisibility(View.GONE);
                    } else {
                        viewHolder.browser_chef_add_collection.setVisibility(View.GONE);
                        viewHolder.browser_chef_remove_from_collection.setVisibility(View.VISIBLE);
                    }

                } else {
                    viewHolder.browser_chef_add_collection.setVisibility(View.GONE);
                    viewHolder.browser_chef_remove_from_collection.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
//                                            count =1;
            }


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = Common.URL + "MemberServletAndroid";
                    try {
                        memberVO = new MemberGetOneTask().execute(url, chefVOList.get(apos).getMem_no()).get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    Intent intent = new Intent(getActivity(), CollectionDetailChefMainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("memberVO", memberVO);
                    bundle.putSerializable("chefVO", chefVOList.get(apos));
                    bundle.putSerializable("browseTitle","瀏覽私廚");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });


            viewHolder.browser_chef_add_collection.setOnClickListener(new View.OnClickListener() { //移除追隨
                @Override
                public void onClick(View v) {
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
                                new CollectionDeleteByMem_noAndAll_noTask().execute(url, mem_no, chefVOList.get(apos).getChef_no()).get();
                                Common.showToast(getActivity(), R.string.msg_StopFellowCollectionSuccess);
                                viewHolder.browser_chef_add_collection.setVisibility(View.GONE);
                                viewHolder.browser_chef_remove_from_collection.setVisibility(View.VISIBLE);
                            } else {
                                Intent loginIntent = new Intent(getActivity(), LoginDialogActivity.class);
                                startActivityForResult(loginIntent, REQ_LOGIN);
                            }

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
//                                            count =1;
                        }

                    } else {
                        Common.showToast(getActivity(), R.string.msg_NoNetwork);
                    }


                }
            });
            viewHolder.browser_chef_remove_from_collection.setOnClickListener(new View.OnClickListener() { //開始追隨
                @Override
                public void onClick(View vv) {

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
                                collectionVO = new CollectionInsertTask().execute(url, mem_no, chefVOList.get(apos).getChef_no()).get();
                                viewHolder.browser_chef_add_collection.setVisibility(View.VISIBLE);
                                viewHolder.browser_chef_remove_from_collection.setVisibility(View.GONE);
                            } else {
                                Intent loginIntent = new Intent(getActivity(), LoginDialogActivity.class);
                                startActivityForResult(loginIntent, REQ_LOGIN);
                            }

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
//                                            count =1;
                        }
                        if (collectionVO == null) {
//                                            Common.showToast(getActivity(), R.string.msg_AddToCollectionFail);
                        } else {
//                                            RecipeFragment.RecipeRecyclerViewAdapter.this.notifyDataSetChanged();
                            Common.showToast(getActivity(), R.string.msg_FellowCollectionSuccess);
                        }
                    } else {
                        Common.showToast(getActivity(), R.string.msg_NoNetwork);
                    }


                }
            });

        }


        //       class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_recipe_no, tv_browse_chef_chef_name, tv_browse_chef_chef_area, tv_browse_chef_chef_menu, tv_browse_chef_chef_intr;
            ImageView iv_browse_chef_chef_image;
            ImageView browser_chef_add_collection, browser_chef_remove_from_collection;

            public ViewHolder(View itemView) {
                super(itemView);
//                tv_recipe_no = (TextView) itemView.findViewById(R.id.tv_recipe_member_page_recipe_no);
                tv_browse_chef_chef_name = (TextView) itemView.findViewById(R.id.tv_browse_chef_chef_name);
                iv_browse_chef_chef_image = (ImageView) itemView.findViewById(R.id.iv_browse_chef_chef_image);
                tv_browse_chef_chef_area = (TextView) itemView.findViewById(R.id.tv_browse_chef_chef_area);
                tv_browse_chef_chef_menu = (TextView) itemView.findViewById(R.id.tv_browse_chef_chef_menu);
                tv_browse_chef_chef_intr = (TextView) itemView.findViewById(R.id.tv_browse_chef_chef_intr);
                browser_chef_add_collection = (ImageView) itemView.findViewById(R.id.browser_chef_add_collection);
                browser_chef_remove_from_collection = (ImageView) itemView.findViewById(R.id.browser_chef_remove_from_collection);
//                browser_chef_add_collection.setOnClickListener(this);
            }
        }
    }
}
