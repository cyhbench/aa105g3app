package com.chef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.main.Common;
import com.member.MemberVO;
import com.recipe.RecipeVO;
import com.recipe.Recipe_contVO;
import com.util.GetImageTask;

import java.util.Set;

//私廚個人頁面的pageview中的簡介， 用在我的收藏-私廚簡介，私廚個人頁面

public class ChefInfoFragment extends Fragment {
    //    private List<RecipeVO> recipeVOList;
    private static final String TAG = "ChefInfoFragment";
    private RecyclerView rvRecipeVO;
    private MemberVO memberVO;
    private ChefVO chefVO;
    private Recipe_contVO recipe_contVO;
    private RecipeVO recipeVO;
    private String mem_no;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Set<Recipe_contVO> recipe_contVOSet;
    private TextView tv_recipe_cond_page_mater_ingredients, tv_recipe_cond_page_mater_quantity;
    private ImageView iv_chef_page_chef_info_chef_image, iv_chef_page_chef_info_chef_reci_image1, iv_chef_page_chef_info_chef_reci_image2, iv_chef_page_chef_info_chef_reci_image3,
            iv_chef_page_chef_info_chef_reci_image4, iv_chef_page_chef_info_chef_reci_image5;
    private TextView tv_chef_page_chef_info_chef_name, tv_chef_page_chef_info_skill;
    private TextView tv_chef_page_chef_info_chef_area, tv_chef_page_chef_info_chef_intr, tv_chef_page_chef_info_chef_menu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chef_info_fragment, container, false);

//        swipeRefreshLayout =
//                (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutRecipe);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                onStart();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        memberVO = (MemberVO) getArguments().getSerializable("memberVO");
        chefVO = (ChefVO) getArguments().getSerializable("chefVO");

        iv_chef_page_chef_info_chef_image = (ImageView) view.findViewById(R.id.iv_chef_page_chef_info_chef_image);
        iv_chef_page_chef_info_chef_reci_image1 = (ImageView) view.findViewById(R.id.iv_chef_page_chef_info_chef_reci_image1);
        iv_chef_page_chef_info_chef_reci_image2 = (ImageView) view.findViewById(R.id.iv_chef_page_chef_info_chef_reci_image2);
        iv_chef_page_chef_info_chef_reci_image3 = (ImageView) view.findViewById(R.id.iv_chef_page_chef_info_chef_reci_image3);
        iv_chef_page_chef_info_chef_reci_image4 = (ImageView) view.findViewById(R.id.iv_chef_page_chef_info_chef_reci_image4);
        iv_chef_page_chef_info_chef_reci_image5 = (ImageView) view.findViewById(R.id.iv_chef_page_chef_info_chef_reci_image5);

        tv_chef_page_chef_info_chef_name = (TextView) view.findViewById(R.id.tv_chef_page_chef_info_chef_name);
        tv_chef_page_chef_info_skill = (TextView) view.findViewById(R.id.tv_chef_page_chef_info_skill);
        tv_chef_page_chef_info_chef_area = (TextView) view.findViewById(R.id.tv_chef_page_chef_info_chef_area);
        tv_chef_page_chef_info_chef_intr = (TextView) view.findViewById(R.id.tv_chef_page_chef_info_chef_intr);
        tv_chef_page_chef_info_chef_menu = (TextView) view.findViewById(R.id.tv_chef_page_chef_info_chef_menu);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        String url = Common.URL + "ChefServletAndroid.do";
        int imageSize = 1020;

        new GetImageTask(iv_chef_page_chef_info_chef_image).execute(url, chefVO.getChef_no(), imageSize, "chef_image");
        new GetImageTask(iv_chef_page_chef_info_chef_reci_image1).execute(url, chefVO.getChef_no(), imageSize, "chef_reci_image1");
        new GetImageTask(iv_chef_page_chef_info_chef_reci_image2).execute(url, chefVO.getChef_no(), imageSize, "chef_reci_image2");
        new GetImageTask(iv_chef_page_chef_info_chef_reci_image3).execute(url, chefVO.getChef_no(), imageSize, "chef_reci_image3");
        new GetImageTask(iv_chef_page_chef_info_chef_reci_image4).execute(url, chefVO.getChef_no(), imageSize, "chef_reci_image4");
        new GetImageTask(iv_chef_page_chef_info_chef_reci_image5).execute(url, chefVO.getChef_no(), imageSize, "chef_reci_image5");

        tv_chef_page_chef_info_chef_name.setText("姓名:" + chefVO.getChef_name());
        tv_chef_page_chef_info_skill.setText("專長:" + chefVO.getChef_skill());
        tv_chef_page_chef_info_chef_area.setText("服務地區:" + chefVO.getChef_area());
        tv_chef_page_chef_info_chef_intr.setText("簡介:" + chefVO.getChef_intr());
        tv_chef_page_chef_info_chef_menu.setText("菜單:" + chefVO.getChef_menu());
    }
}
