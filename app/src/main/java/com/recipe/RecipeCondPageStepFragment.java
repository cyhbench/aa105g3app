package com.recipe;

import android.content.Context;
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
import com.main.Common;
import com.member.MemberVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class RecipeCondPageStepFragment extends Fragment {
    //    private List<RecipeVO> recipeVOList;
    private static final String TAG = "RcpeCondPageStepFrgmnt";
    private RecyclerView rvRecipeVO;
    private MemberVO memberVO;
    private Recipe_contVO recipe_contVO;
    private RecipeVO recipeVO;
    private List<String> ingredients;// = new ArrayList<String>();
    private List<String> quantity;// = new ArrayList<String>();
    private String mem_no;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Set<Recipe_contVO> recipe_contVOSet;
    private TextView tv_recipe_cond_page_step_description, tv_recipe_cond_page_step_number;
    private ImageView iv_recipe_cond_page_step_pic;

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

//        if (recipe_contVOSet == null) {
//            recipe_contVOSet = (Set<Recipe_contVO>) getArguments().getSerializable("recipe_contVOSet");
//        }
//        if (recipeVO == null) {
            recipeVO = (RecipeVO) getArguments().getSerializable("recipeVO");
//        }

        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/RecipeServletAndroid";
        //    List<RecipeVO> RecipeVOList = null;
            recipe_contVOSet=null;
            try {
                recipe_contVOSet = new RecipeCondGetByRecipeNOTask().execute(url, recipeVO.getRecipe_no()).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (recipe_contVOSet == null) {
                Common.showToast(getActivity(), R.string.msg_NoRecipeContFound);
            } else {
//                recipe_viewpage_recipe_cond_recipe_no.setText(recipeVO.getRecipe_no());
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
//                if (Common.networkConnected(getActivity())) {
//            try {
//                recipe_contVOSet = new RecipeCondGetByRecipeNOTask().execute(url, recipeVO.getRecipe_no()).get();
//                Log.d(TAG, "recipeVO.getRecipe_no():" + recipeVO.getRecipe_no());
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (recipe_contVOSet == null) {
//                showToast(this, R.string.msg_NoRecipeContFound);
//            } else {
//                recipe_viewpage_recipe_cond_recipe_no.setText(recipeVO.getRecipe_no());
//            }
//        } else {
//            showToast(this, R.string.msg_NoNetwork);
//        }

//        mem_no = memberVO.getMem_no();
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
//        if (Common.networkConnected(getActivity())) {
//            String url = Common.URL + "RecipeServletAndroid";
//            List<RecipeVO> RecipeVOList = null;
//            try {
//                RecipeVOList = new RecipeGetAllByMemNOTask().execute(url, mem_no).get();
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (RecipeVOList == null || RecipeVOList.isEmpty()) {
//                Common.showToast(getActivity(), R.string.msg_NoRecipesFound);
//            } else {
        ArrayList<Recipe_contVO> recipe_contVOList = new ArrayList<Recipe_contVO>();
        recipe_contVOList.addAll(recipe_contVOSet);

        rvRecipeVO.setAdapter(new RecipeCondPageStepFragment.RecipeRecyclerViewAdapter(getActivity(), recipe_contVOList));
//            }
//        } else {
//            Common.showToast(getActivity(), R.string.msg_NoNetwork);
//        }
    }

    private class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeCondPageStepFragment.RecipeRecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private ArrayList<Recipe_contVO> recipe_contVOList;


        public RecipeRecyclerViewAdapter(Context context, ArrayList<Recipe_contVO> recipe_contVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.recipe_contVOList = recipe_contVOList;
        }

        @Override
        public int getItemCount() {
            return recipe_contVOList.size();
        }

        @Override
        public RecipeCondPageStepFragment.RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.recipe_cond_page_step_item, parent, false);
            return new RecipeCondPageStepFragment.RecipeRecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecipeCondPageStepFragment.RecipeRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            final Recipe_contVO recipe_contVO = recipe_contVOList.get(position);
//           action showRecipe_cont_pic
            String url = Common.URL + "recipe_cont/recipe_cont_android.do"; //Recipe_contServletAndroid
            String action = "showRecipe_cont_pic";
            String recipe_no = recipeVO.getRecipe_no();
            int step = recipe_contVO.getStep();
//            String mem_no = memberVO.getMem_no();
            int imageSize = 1024;

            new RecipeStepGetImageTask(viewHolder.iv_recipe_cond_page_step_pic).execute(url, recipe_no, step, imageSize);
            viewHolder.tv_recipe_cond_page_step_number.setText("步驟 "+Integer.toString(recipe_contVO.getStep()) +":");
            viewHolder.tv_recipe_cond_page_step_description.setText(recipe_contVO.getStep_cont());
//            Bitmap bitmap = BitmapFactory.decodeByteArray(recipeVO.getRecipe_pic(), 0, recipeVO.getRecipe_pic().length);
//            viewHolder.iv_recipe_pic.setImageBitmap(bitmap);

//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("recipeVO", recipeVO);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//
////                    Intent intent = new Intent(this, MemberUpdateActivity.class);
////                    Bundle bundle = new Bundle();
////                    Log.d(TAG, "getMem_name(line 306):" + memberVO.getMem_name());
////                    memberVO.setMem_image(image); //因為會員圖與文字是分開抓的，所以要把圖給set()回去
////                    memberVO.setMem_no(mem_no);
////                    Log.d(TAG, "line 306:" + memberVO.getMem_image().length);
////                    bundle.putSerializable("memberVO", memberVO);
////                    intent.putExtras(bundle);
//                }
//            });
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_recipe_cond_page_step_number, tv_recipe_cond_page_step_description;
            ImageView iv_recipe_cond_page_step_pic;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_recipe_cond_page_step_number = (TextView) itemView.findViewById(R.id.tv_recipe_cond_page_step_number);
                tv_recipe_cond_page_step_description = (TextView) itemView.findViewById(R.id.tv_recipe_cond_page_step_description);
                iv_recipe_cond_page_step_pic = (ImageView) itemView.findViewById(R.id.iv_recipe_cond_page_step_pic);
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
