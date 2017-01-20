package com.chef_order_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.chef.ChefGetOneByMemNoTask;
import com.chef.ChefVO;
import com.main.Common;
import com.member.MemberGetOneTask;
import com.member.MemberVO;
import com.util.GetImageTask;

import java.util.List;

/**
 * Created by Ratan on 7/29/2015.
 */
public class ChefOrderListChefFragment extends Fragment {
    private List<ChefVO> chefVOList;
    private static final String TAG = "私廚訂單-私廚端Fragment";
    private RecyclerView rvChef_order_listVO;
    private MemberVO memberVO;
    private ChefVO chefVO;
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


//        memberVO = (MemberVO) getArguments().getSerializable("memberVO");
//        mem_no = (String)getArguments().getSerializable("mem_no");
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(new ChefRecyclerViewAdapter(inflater));
        mem_no = (String) getArguments().getSerializable("mem_no");
        String url = Common.URL + "ChefServletAndroid.do";
        try {
            chefVO = new ChefGetOneByMemNoTask().execute(url, mem_no).get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        rvChef_order_listVO = (RecyclerView) view.findViewById(R.id.recyclerView);
        rvChef_order_listVO.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        showChef_order_listVOItem();
    }

    private void showChef_order_listVOItem() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "Chef_order_listServletAndroid";
            List<Chef_order_listVO> chef_order_listVOList = null;
            try {
                chef_order_listVOList = new ChefOrderListGetAllByChefNOTask().execute(url, chefVO.getChef_no()).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (chef_order_listVOList == null || chef_order_listVOList.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoChefOrderListFound);
            } else {
                rvChef_order_listVO.setAdapter(new ChefOrderListChefFragment.OrderListRecyclerViewAdapter(getActivity(), chef_order_listVOList));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }


    private class OrderListRecyclerViewAdapter extends RecyclerView.Adapter<ChefOrderListChefFragment.OrderListRecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Chef_order_listVO> chef_order_listVOList;

        public OrderListRecyclerViewAdapter(Context context, List<Chef_order_listVO> chef_order_listVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.chef_order_listVOList = chef_order_listVOList;
        }

        @Override
        public int getItemCount() {
            return chef_order_listVOList.size();
        }

        @Override
        public ChefOrderListChefFragment.OrderListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.chef_order_list_for_chef_item, parent, false);
            return new ChefOrderListChefFragment.OrderListRecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ChefOrderListChefFragment.OrderListRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            final Chef_order_listVO chef_order_listVO = chef_order_listVOList.get(position);
            final ChefOrderListChefFragment.OrderListRecyclerViewAdapter.ViewHolder listener_v = viewHolder;
            final int apos = position;
            String url = Common.URL + "Chef_order_listServletAndroid";
            String chef_no = chef_order_listVO.getChef_no();
            int imageSize = 800;


            new GetImageTask(viewHolder.iv_order_chef_image).execute(url, chef_order_listVOList.get(position).getChef_no(), imageSize, "chef_image");
            url = Common.URL + "MemberServletAndroid";
            MemberVO memberVO = null;
            try {
                memberVO = new MemberGetOneTask().execute(url, chef_order_listVOList.get(position).getMem_no()).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            viewHolder.tv_order_chef_name.setText("會員:" + memberVO.getMem_name());
            viewHolder.tv_order_cost.setText("訂單價格:" + chef_order_listVOList.get(position).getChef_ord_cost().shortValue() + " 元整");
            viewHolder.tv_order_act_date.setText("執行日期:" + String.valueOf(chef_order_listVOList.get(position).getChef_act_date()).subSequence(0, 10));
            viewHolder.tv_order_place.setText("執行地點:" + chef_order_listVOList.get(position).getChef_ord_place());
            viewHolder.tv_order_date.setText("訂單產生時間" + String.valueOf(chef_order_listVOList.get(position).getChef_ord_date()).subSequence(0, 16));

            if (chef_order_listVOList.get(position).getChef_ord_con().equals("0")) {
                viewHolder.tv_order_cost.setText("訂單價格:" +" 請定價");
                viewHolder.tv_order_cost.setTextColor(Color.RED);

                viewHolder.tv_order_con.setText("未定價");
                viewHolder.tv_order_con.setTextColor(Color.RED);
            } else if (chef_order_listVOList.get(position).getChef_ord_con().equals("1")) {

                viewHolder.tv_order_con.setText("待同意");
                viewHolder.tv_order_con.setTextColor(Color.BLUE);
            }
            if (chef_order_listVOList.get(position).getChef_ord_con().equals("2")) {
                viewHolder.tv_order_con.setText("待執行");
                viewHolder.tv_order_con.setTextColor(Color.GREEN);
            }


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    String url = Common.URL + "Chef_order_listServletAndroid";
//                    Chef_order_listVO chef_order_listVO = null;
//                    try {
//                        chef_order_listVO = (Chef_order_listVO) new ChefOrderListGetOneByNOTask().execute(url, chefVOList.get(apos).getChef_no()).get();
//                    } catch (Exception e) {
//                        Log.e(TAG, e.toString());
//                    }

                    if (chef_order_listVOList.get(apos).getChef_ord_con().equals("0")) {
                        Intent intent = new Intent(getActivity(), ChefOrderListUpdateForChefActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mem_no", mem_no);
                        bundle.putSerializable("personal_mem_no", mem_no);
//                    bundle.putSerializable("chefVO", chefVOList.get(apos));
                        bundle.putSerializable("chef_order_listVO", chef_order_listVOList.get(apos));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    if (chef_order_listVOList.get(apos).getChef_ord_con().equals("1")) {
                        Intent intent = new Intent(getActivity(), ChefOrderListUpdateForChefActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mem_no", mem_no);
                        bundle.putSerializable("personal_mem_no", mem_no);
//                    bundle.putSerializable("chefVO", chefVOList.get(apos));
                        bundle.putSerializable("chef_order_listVO", chef_order_listVOList.get(apos));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                }
            });

///////////////////////////@@@@@@@@@@@出現消失@@@@@@@@@@@@@@////////////////////////////////////
//            viewHolder.browser_chef_add_collection.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    viewHolder.browser_chef_add_collection.setVisibility(View.GONE);
//                    viewHolder.browser_chef_remove_from_collection.setVisibility(View.VISIBLE);
//                }
//            });
//            viewHolder.browser_chef_remove_from_collection.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View vv) {
//
//                    viewHolder.browser_chef_add_collection.setVisibility(View.VISIBLE);
//                    viewHolder.browser_chef_remove_from_collection.setVisibility(View.GONE);
//                }
//            });
///////////////////////////@@@@@@@@@@@出現消失@@@@@@@@@@@@@@////////////////////////////////////

        }


        //       class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_recipe_no, tv_order_chef_name, tv_order_cost, tv_order_act_date, tv_order_place, tv_order_con, tv_order_date;
            ImageView iv_order_chef_image;
            ImageView browser_chef_add_collection, browser_chef_remove_from_collection;

            public ViewHolder(View itemView) {
                super(itemView);
//                tv_recipe_no = (TextView) itemView.findViewById(R.id.tv_recipe_member_page_recipe_no);
                tv_order_chef_name = (TextView) itemView.findViewById(R.id.tv_order_chef_name);
                iv_order_chef_image = (ImageView) itemView.findViewById(R.id.iv_order_chef_image);
                tv_order_cost = (TextView) itemView.findViewById(R.id.tv_order_cost);
                tv_order_act_date = (TextView) itemView.findViewById(R.id.tv_order_act_date);
                tv_order_place = (TextView) itemView.findViewById(R.id.tv_order_place);
                tv_order_con = (TextView) itemView.findViewById(R.id.tv_order_con);
                tv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);

                browser_chef_add_collection = (ImageView) itemView.findViewById(R.id.browser_chef_add_collection);
                browser_chef_remove_from_collection = (ImageView) itemView.findViewById(R.id.browser_chef_remove_from_collection);
//                browser_chef_add_collection.setOnClickListener(this);
            }
//           @Override
//           public void onClick(View v) {
//               // do your action here with imageView
//           }
        }
    }
}

