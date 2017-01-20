package com.collection;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;

import java.util.List;


public class CollectionDetailRecipe extends AppCompatActivity {
    private RecyclerView rvCollectionVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_detail_activity);

        List collectionVOList = (List) getIntent().getExtras().getSerializable("collectionVOList");
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        TextView tvDetail = (TextView) findViewById(R.id.tvDetail);

        rvCollectionVO = (RecyclerView) findViewById(R.id.rv_collection_detail);
        rvCollectionVO.setLayoutManager(new LinearLayoutManager(this));
        rvCollectionVO.setAdapter(new CollectionVOAdapter(this, collectionVOList));
    }

    private class CollectionVOAdapter extends RecyclerView.Adapter<CollectionVOAdapter.MyViewHolder> {
        Context context;
        List<CollectionVO> CollectionVOList;

        public CollectionVOAdapter(Context context, List<CollectionVO> CollectionVOList) {
            this.context = context;
            this.CollectionVOList = CollectionVOList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.essay_message_detail_fragment, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
            final CollectionVO CollectionVO = CollectionVOList.get(position);
            myViewHolder.ivMemPic.setImageResource(R.drawable.new_logo3);
            myViewHolder.tvMem_no.setText(CollectionVO.getMem_no());
            myViewHolder.tvEsamsg_detail.setText(CollectionVO.getColl_no());
            myViewHolder.tvEsamsg_date.setText(CollectionVO.getAll_no());

        }

        @Override
        public int getItemCount(){return CollectionVOList.size();}

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivMemPic;
            TextView tvMem_no, tvEsamsg_detail, tvEsamsg_date;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivMemPic = (ImageView) itemView.findViewById(R.id.ivMemPic);
                tvMem_no = (TextView) itemView.findViewById(R.id.tvMem_no);
                tvEsamsg_detail = (TextView) itemView.findViewById(R.id.tvEsamsg_detail);
                tvEsamsg_date = (TextView) itemView.findViewById(R.id.tvEsamsg_date);
            }
        }
    }
}
