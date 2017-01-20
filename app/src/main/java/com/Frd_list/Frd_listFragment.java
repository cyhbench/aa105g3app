package com.Frd_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.R;
import com.browser.BrowseOneMemberMainActivity;
import com.collection.CollectionDetailMemberMainActivity;
import com.main.Common;
import com.member.MemberGetImageTask;
import com.member.MemberGetOneTask;
import com.member.MemberVO;
import com.talk.TalkMainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.main.Common.showToast;


public class Frd_listFragment extends Fragment {

    private static final String TAG = "Frd_listFragment";
    private RecyclerView rvFrd_list;
    private MemberVO memberVO;
    private String mem_no;
    private String mem_name;
    private String mem_own;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HashMap frd_listMemberHashMap;
    private List<HashMap> frd_listMemberVOList;
    private String action;
    private List<Frd_listVO> frd_listVOList;
    private Frd_listRecyclerViewAdapter frd_listRecyclerViewAdapter;

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
        mem_no = memberVO.getMem_no();
//        Lod.d(TAG,"");
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(new Frd_listRecyclerViewAdapter(inflater));

        rvFrd_list = (RecyclerView) view.findViewById(R.id.recyclerView);
        rvFrd_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        showFrd_listItem();
        frd_listRecyclerViewAdapter = new Frd_listRecyclerViewAdapter(getActivity(), frd_listMemberVOList);
        rvFrd_list.setAdapter(frd_listRecyclerViewAdapter);

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
//        showFrd_listItem();
    }

    @Override
    public void onResume() {
        super.onResume();
        showPage();
    }

    private void showPage() {
        showFrd_listItem();

    }

    private void showFrd_listItem() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "Frd_listServletAndroid";
//            List<Frd_listVO> frd_listVOList = null;
//            List<MemberVO> memberVOList = null;
//            List<HashMap> frd_listMemberVOList = new ArrayList<HashMap>();
            frd_listMemberVOList = new ArrayList<HashMap>();
            try {
                frd_listMemberVOList = new Frd_listGetAllByMemNOTask().execute(url, mem_no).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (frd_listMemberVOList == null || frd_listMemberVOList.isEmpty()) {
//                Common.showToast(getActivity(), R.string.msg_NoFrd_listsFound);
            } else {
                frd_listRecyclerViewAdapter = new Frd_listRecyclerViewAdapter(getActivity(), frd_listMemberVOList);
                rvFrd_list.setAdapter(frd_listRecyclerViewAdapter);
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }


    private class Frd_listRecyclerViewAdapter extends RecyclerView.Adapter<Frd_listRecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
//        private List<HashMap> frd_listMemberVOList;

        public Frd_listRecyclerViewAdapter(Context context, List<HashMap> frd_listMemberVOList) {
            layoutInflater = LayoutInflater.from(context);
//            this.frd_listMemberVOList = frd_listMemberVOList;
//            this.frd_listVOList = frd_listVOList;
        }

        @Override
        public int getItemCount() {
            return frd_listMemberVOList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.frd_list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            frd_listMemberHashMap = frd_listMemberVOList.get(position);
            final int apos = position;

//            viewHolder.tv_frd_list_mem_no.setText((CharSequence) frd_listMemberHashMap.get("mem_no"));
            viewHolder.tv_frd_list_friend_no.setText(" " + (CharSequence) frd_listMemberHashMap.get("mem_name"));

            String urlFrd = Common.URL + "Frd_listServletAndroid";
            final Frd_listVO frd_listVO = new Frd_listVO();

            frd_listVO.setMem_no((String) frd_listMemberHashMap.get("mem_no"));
            frd_listVO.setFriend_no((String) frd_listMemberHashMap.get("friend_no"));
            frd_listVO.setFriend_chk((String) frd_listMemberHashMap.get("friend_chk"));

            viewHolder.iv_frd_list_mem_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(), "test" + frd_listMemberVOList.get(apos).get("friend_no"),Toast.LENGTH_SHORT).show();
                    //              bundle.putString("name", spot.getFilmTitle());

                    Intent intent = new Intent(getActivity(), BrowseOneMemberMainActivity.class);//DetailActivity.class:物件

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mem_no", (Serializable) frd_listMemberVOList.get(apos).get("friend_no"));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(),"QQ"+ frd_listMemberVOList.get(apos).get("friend_no"),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), TalkMainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mem_no", (java.io.Serializable) frd_listMemberVOList.get(apos).get("mem_no"));
                    bundle.putSerializable("frd_no", (java.io.Serializable) frd_listMemberVOList.get(apos).get("friend_no"));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            if (frd_listMemberHashMap.get("friend_chk").equals("2")) {
//                viewHolder.tv_frd_list_friend_chk.setText("狀態:互為好友" + (CharSequence) frd_listMemberHashMap.get("friend_chk"));
                viewHolder.tv_frd_list_friend_chk.setText("狀態:互為好友");
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END);
                        popupMenu.inflate(R.menu.popup_menu_delete);

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {

                                    case R.id.delete:
                                        if (Common.networkConnected(getActivity())) {
                                            String url = Common.URL + "Frd_listServletAndroid";
                                            String action = "deleteOne";
                                            Frd_listVO frd_listVO2 = null;
                                            int count = 0;
                                            try {
                                                frd_listVO2 = new Frd_chkTask().execute(url, action, frd_listVO).get();
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                                count = 1;
                                            }
                                            if (count == 1) {
                                                Common.showToast(getActivity(), R.string.msg_DeleteFail);
                                            } else {
                                                showFrd_listItem();
                                                frd_listRecyclerViewAdapter.notifyDataSetChanged();
                                                Common.showToast(getActivity(), R.string.msg_DeleteSuccess);
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
            } else if (frd_listMemberHashMap.get("friend_chk").equals("1")) {
//                viewHolder.tv_frd_list_friend_chk.setText("狀態:邀請中" + (CharSequence) frd_listMemberHashMap.get("friend_chk"));
                viewHolder.tv_frd_list_friend_chk.setText("狀態:邀請中");
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END);
                        popupMenu.inflate(R.menu.popup_menu_deny);

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {

//                                    case R.id.updateDeny:
//                                        if (Common.networkConnected(getActivity())) {
//                                            String url = Common.URL + "Frd_listServletAndroid";
//                                            Frd_listVO frd_listVO2 = null;
//                                            String action = "deleteOne";
//                                            int count = 0;
//                                            try {
//                                                frd_listVO2 = new Frd_chkTask().execute(url, action, frd_listVO).get();
//                                            } catch (Exception e) {
//                                                Log.e(TAG, e.toString());
//                                                count = 1;
//                                            }
//                                            showFrd_listItem();
//                                            frd_listRecyclerViewAdapter.notifyDataSetChanged();
//                                            Common.showToast(getActivity(), R.string.msg_CancelInvite);
//                                            if (count == 1) {
//                                                Common.showToast(getActivity(), R.string.msg_CancelInviteFail);
//                                            } else {
//                                                showFrd_listItem();
//                                                frd_listRecyclerViewAdapter.notifyDataSetChanged();
//                                                Common.showToast(getActivity(), R.string.msg_CancelInvite);
//                                            }
//
//                                        } else {
//                                            Common.showToast(getActivity(), R.string.msg_NoNetwork);
//                                        }
//
//                                        break;

                                    case R.id.delete:
                                        if (Common.networkConnected(getActivity())) {
                                            String url = Common.URL + "Frd_listServletAndroid";
                                            String action = "deleteOne";
                                            Frd_listVO frd_listVO2 = new Frd_listVO();
                                            int count = 0;
                                            try {
                                                frd_listVO2 = new Frd_chkTask().execute(url, action, frd_listVO).get();
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                                count = 1;
                                            }
                                            if (count == 1) {
                                                Common.showToast(getActivity(), R.string.msg_UpdateInviteCancelFail);
                                            } else {
                                                Common.showToast(getActivity(), R.string.msg_UpdateInviteCancel);
                                                showFrd_listItem();
                                                frd_listRecyclerViewAdapter.notifyDataSetChanged();
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
            } else if (frd_listMemberHashMap.get("friend_chk").equals("0")) {
//                viewHolder.tv_frd_list_friend_chk.setText("狀態:受邀中" + (CharSequence) frd_listMemberHashMap.get("friend_chk"));
                viewHolder.tv_frd_list_friend_chk.setText("狀態:受邀中");
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END);
                        popupMenu.inflate(R.menu.popup_menu_agree);

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.addToCollection:
                                        if (Common.networkConnected(getActivity())) {
                                            String url = Common.URL + "Frd_listServletAndroid";
                                            Frd_listVO frd_listVO2 = new Frd_listVO();
                                            String action = "updateAgree";

                                            try {
                                                frd_listVO2 = new Frd_chkTask().execute(url, action, frd_listVO).get();
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                            }
                                            showFrd_listItem();
                                            frd_listRecyclerViewAdapter.notifyDataSetChanged();
                                            Common.showToast(getActivity(), R.string.msg_AgreeInvite);
                                        } else {
                                            Common.showToast(getActivity(), R.string.msg_NoNetwork);
                                        }

                                        break;

                                    case R.id.delete:
                                        if (Common.networkConnected(getActivity())) {
                                            String url = Common.URL + "Frd_listServletAndroid";
                                            String action = "deleteOne";
                                            Frd_listVO frd_listVO2 = new Frd_listVO();
                                            int count = 0;
                                            try {
                                                frd_listVO2 = new Frd_chkTask().execute(url, action, frd_listVO).get();
                                                Log.d(TAG, "count(delete): " + count);
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                                count = 1;
                                            }
                                            if (count == 1) {
                                                Common.showToast(getActivity(), R.string.msg_UpdateInviteCancelFail);
                                            } else {

                                                showFrd_listItem();
                                                frd_listRecyclerViewAdapter.notifyDataSetChanged();
                                                Common.showToast(getActivity(), R.string.msg_CancelInvite);
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

            String url = Common.URL + "MemberServletAndroid";
            int imageSize = 400;
            try {
                Bitmap bitmap = new MemberGetImageTask(viewHolder.iv_frd_list_mem_image).execute(url, frd_listMemberHashMap.get("friend_no"), imageSize).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_frd_list_friend_no, tv_frd_list_friend_chk;//,tv_frd_list_mem_no;
            ImageView iv_frd_list_mem_image;
            Button bt_frd_chk;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_frd_list_mem_image = (ImageView) itemView.findViewById(R.id.iv_frd_list_mem_image);
//                tv_frd_list_mem_no = (TextView) itemView.findViewById(R.id.tv_frd_list_mem_no);
                tv_frd_list_friend_no = (TextView) itemView.findViewById(R.id.tv_frd_list_friend_no);
                tv_frd_list_friend_chk = (TextView) itemView.findViewById(R.id.tv_frd_list_friend_chk);

            }
        }
    }
}
