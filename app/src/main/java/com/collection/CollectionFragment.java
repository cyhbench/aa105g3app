package com.collection;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.main.Common;
import com.member.MemberVO;

import java.util.ArrayList;
import java.util.List;

import static com.androidbelieve.drawerwithswipetabs.R.id.recyclerView;

//會員或私廚的頁面 > 我的最愛 (分出最愛的私廚、會員、食譜)
public class CollectionFragment extends Fragment {

    private static final String TAG = "CollectionFragment";
    private RecyclerView rvCollection;
    private RecyclerView rvCollSeqment;
    private MemberVO memberVO;
    private String mem_no;
    private String mem_name;
    private String mem_own;
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
        mem_no = memberVO.getMem_no();

        rvCollection = (RecyclerView) view.findViewById(recyclerView);
        rvCollection.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (Common.networkConnected(getActivity())) {
//            String url = Common.URL + "CollectionServletAndroid";
//            List<CollectionVO> collectionVOList = null;
//            try {
//                collectionVOList = new CollectionGetAllByMemNOTask().execute(url, mem_no).get();
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (collectionVOList == null || collectionVOList.isEmpty()) {
//                Common.showToast(getActivity(), R.string.msg_NoCollectionsFound);
//            } else {
//                List<CollectionVO> collClassList = new ArrayList<CollectionVO>();
//                List<CollectionVO> collChefList = new ArrayList<CollectionVO>();
//                List<CollectionVO> collMemberList = new ArrayList<CollectionVO>();
//                List<CollectionVO> collRecipeList = new ArrayList<CollectionVO>();
//
//                int r = 0, f = 0, c = 0, m = 0, l = 0;
//                for (CollectionVO aCollection : collectionVOList) {
//                    if (aCollection.getClass_no().equals("R")) {
//                        collRecipeList.add(aCollection);
//                        if (r == 0) {
//                            collClassList.add(aCollection);
//                            r++;
//                        }
//                    }
//                    if (aCollection.getClass_no().equals("C")) {
//                        collChefList.add(aCollection);
//                        if (c == 0) {
//                            collClassList.add(aCollection);
//                            c++;
//                        }
//                    }
//                    if (aCollection.getClass_no().equals("M")) {
//                        collMemberList.add(aCollection);
//                        if (m == 0) {
//                            collClassList.add(aCollection);
//                            m++;
//                        }
//                    }
//                }
//                rvCollection.setAdapter(new CollectionRecyclerViewAdapter(getActivity(), collClassList, collRecipeList, collMemberList, collChefList));
//            }
//        } else {
//            Common.showToast(getActivity(), R.string.msg_NoNetwork);
//        }
    }

    @Override
    public void onResume(){
        super.onResume();
        showPage();
    }

    private void showPage() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "CollectionServletAndroid";
            List<CollectionVO> collectionVOList = null;
            try {
                collectionVOList = new CollectionGetAllByMemNOTask().execute(url, mem_no).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (collectionVOList == null || collectionVOList.isEmpty()) {
//                Common.showToast(getActivity(), R.string.msg_NoCollectionsFound);
            } else {
                List<CollectionVO> collClassList = new ArrayList<CollectionVO>();
                List<CollectionVO> collChefList = new ArrayList<CollectionVO>();
                List<CollectionVO> collMemberList = new ArrayList<CollectionVO>();
                List<CollectionVO> collRecipeList = new ArrayList<CollectionVO>();

                int r = 0, f = 0, c = 0, m = 0, l = 0;
                for (CollectionVO aCollection : collectionVOList) {
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
                rvCollection.setAdapter(new CollectionRecyclerViewAdapter(getActivity(), collClassList, collRecipeList, collMemberList, collChefList));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }

    private class CollectionRecyclerViewAdapter extends RecyclerView.Adapter<CollectionRecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<CollectionVO> collectionVOList;
        private List<CollectionVO> collRecipeList;
        private List<CollectionVO> collMemberList;
        private List<CollectionVO> collChefList;

        public CollectionRecyclerViewAdapter(Context context, List<CollectionVO> collectionVOList,
                                             List<CollectionVO> collRecipeList, List<CollectionVO> collMemberList, List<CollectionVO> collChefList) {
            layoutInflater = LayoutInflater.from(context);
            this.collectionVOList = collectionVOList;
            this.collChefList = collChefList;
            this.collMemberList = collMemberList;
            this.collRecipeList = collRecipeList;
        }

        @Override
        public int getItemCount() {
            return collectionVOList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.collection_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final CollectionVO collectionVO = collectionVOList.get(position);

//            viewHolder.tv_collection_all_no.setText(collectionVO.getAll_no());
            if (collectionVO.getClass_no().equals("R")) {
                viewHolder.tv_collection_class_no.setText("食譜");
                viewHolder.tv_collection_all_no_size.setText(" " +collRecipeList.size());
//                Log.d(TAG,"collChefList.size();" +collChefList.size());
            } else if (collectionVO.getClass_no().equals("C")) {
                viewHolder.tv_collection_class_no.setText("私廚");
                viewHolder.tv_collection_all_no_size.setText(" " +collChefList.size());
            } else if (collectionVO.getClass_no().equals("M")) {
                viewHolder.tv_collection_class_no.setText("會員");
                viewHolder.tv_collection_all_no_size.setText(" "+collMemberList.size());
            }

////////////////////// recyclerview in recyclerview 失敗/////////////////////////////
//            rvCollSeqment = (RecyclerView) viewHolder.itemView.findViewById(R.id.recyclerViewChildren);
//            rvCollSeqment.setLayoutManager(new LinearLayoutManager(getActivity()));
////                    rvCollSeqment.getAdapter().notifyDataSetChanged();
//            rvCollSeqment.setAdapter(new CollSeqmentRecyclerViewAdapter(getActivity(), collectionVOList));
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(v.findViewById(R.id.recyclerViewChildren).getVisibility()==View.VISIBLE){
//                        v.findViewById(R.id.recyclerViewChildren).setVisibility(View.GONE);
//                    }else {
//                        v.findViewById(R.id.recyclerViewChildren).setVisibility(View.VISIBLE);
//                        v.findViewById(R.id.recyclerViewChildren).setMinimumHeight(500);
//                    }
//
//                }
//            });
/////////////////////////recyclerview in recyclerview 失敗/////////////////////////////


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CollectionDetail.class);
                    Bundle bundle = new Bundle();

                    if (collectionVO.getClass_no().equals("C")) {
                        bundle.putSerializable("collectionVOList", (java.io.Serializable) collChefList);
                        bundle.putSerializable("mem_no", mem_no);
                    } else if (collectionVO.getClass_no().equals("M")) {
                        bundle.putSerializable("collectionVOList", (java.io.Serializable) collMemberList);
                        bundle.putSerializable("mem_no", mem_no);
                    } else if (collectionVO.getClass_no().equals("R")) {
                        bundle.putSerializable("collectionVOList", (java.io.Serializable) collRecipeList);
                        bundle.putSerializable("mem_no", mem_no);
                    }

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_collection_all_no, tv_collection_class_no, tv_collection_all_no_size;

            public ViewHolder(View itemView) {
                super(itemView);
//                tv_collection_all_no = (TextView) itemView.findViewById(R.id.tv_collection_all_no);
                tv_collection_class_no = (TextView) itemView.findViewById(R.id.tv_collection_class_no);
                tv_collection_all_no_size = (TextView) itemView.findViewById(R.id.tv_collection_all_no_size);
            }
        }
    }
}
