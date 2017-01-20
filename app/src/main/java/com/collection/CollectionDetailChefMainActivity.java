package com.collection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Page;
import com.androidbelieve.drawerwithswipetabs.R;
import com.chef.ChefInfoFragment;
import com.chef.ChefVO;
import com.chef_order_list.ChefOrderListInsertActivity;
import com.main.Common;
import com.main.LoginDialogActivity;
import com.member.MemberGetImageTask;
import com.member.MemberGetOneTask;
import com.member.MemberVO;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.main.Common.showToast;

//個人頁面的我的收藏的私廚的私廚詳細頁面

public class CollectionDetailChefMainActivity extends AppCompatActivity {
    private static final String TAG = "CollDtlChefMainAty";
    private String url = Common.URL + "MemberServletAndroid";
    private byte[] image;
    private String mem_no;
    int imageSize = 400;
    Bitmap bitmap = null;
    private MemberVO memberVO;//= new MemberVO();
    private ChefVO chefVO;//= new ChefVO();
    private Bundle bundle = new Bundle();
    private TextView tv_chef_order_list_insert;
    private String personal_mem_no;
    private static final int REQ_LOGIN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.collection_detail_member_for_chef_main_activity);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.collection_detail_member_main_activity_toolbar);
        if (getIntent().getExtras().getSerializable("browseTitle") == null) {
            toolbar.setTitle("我的收藏-私廚");
        } else {
            toolbar.setTitle("瀏覽私廚");
        }
        setSupportActionBar(toolbar);

        tv_chef_order_list_insert = (TextView) findViewById(R.id.tv_chef_order_list_insert);

//        if (memberVO == null) {
        memberVO = (MemberVO) getIntent().getExtras().getSerializable("memberVO");
        chefVO = (ChefVO) getIntent().getExtras().getSerializable("chefVO");
        personal_mem_no = (String) getIntent().getExtras().getSerializable("personal_mem_no");
//        }
        mem_no = memberVO.getMem_no();

        ViewPager viewPager = (ViewPager) findViewById(R.id.collection_detail_member_viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.collection_detail_member_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tv_chef_order_list_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                boolean login = pref.getBoolean("login", false);
                if (login) {
                    personal_mem_no = pref.getString("mem_no", "");
                    Intent intent = new Intent(CollectionDetailChefMainActivity.this, ChefOrderListInsertActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mem_no", memberVO.getMem_no());
                    bundle.putSerializable("chef_no", chefVO.getChef_no());
                    bundle.putSerializable("personal_mem_no", personal_mem_no);

                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent loginIntent = new Intent(CollectionDetailChefMainActivity.this, LoginDialogActivity.class);
                    startActivityForResult(loginIntent, REQ_LOGIN);
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        TextView tv_collection_detail_member_mem_name = (TextView) findViewById(R.id.tv_collection_detail_member_mem_name);
        ImageView iv_collection_detail_member_mem_image = (ImageView) findViewById(R.id.iv_collection_detail_member_mem_image); //會員照片

        if (Common.networkConnected(this)) {
//            MemberVO memberVO = null;

            try {
                memberVO = new MemberGetOneTask().execute(url, mem_no).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (memberVO == null) {
                showToast(this, R.string.msg_NoMembersFound);
            } else {
                tv_collection_detail_member_mem_name.setText(memberVO.getMem_name());
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }

        try {

            bitmap = new MemberGetImageTask(null).execute(url, mem_no, imageSize).get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            iv_collection_detail_member_mem_image.setImageBitmap(getRoundedCornerBitmap(bitmap, 500.0f));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image = out.toByteArray();
        } else {
            iv_collection_detail_member_mem_image.setImageResource(R.drawable.default_image);
        }
        //將圖片及圓角數值帶入getRoundedCornerBitmap()並放入iv_member_page_mem_image內
    }


    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * 內容固定無變化:繼承FragmentPagerAdapter：記憶體保留頁面資訊。頁面數量大及變化大，繼承FragmentStatePagerAdapter：換頁時，將被換掉無顯示的頁面從記憶體清除
         */
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            memberVO.setMem_no(mem_no);
            bundle.putSerializable("memberVO", memberVO);

            CollectionDetailBrowserMemberRecipeFragment recipeFragment = new CollectionDetailBrowserMemberRecipeFragment();
            recipeFragment.setArguments(bundle);

            ChefInfoFragment chefInfoFragment = new ChefInfoFragment();
            bundle.putSerializable("chefVO", chefVO);
            chefInfoFragment.setArguments(bundle);

            String collChefInfo = chefVO.getChef_name() + " 的簡介";
            String collChefRecipe = "食譜";
            pageList = new ArrayList<>();
            pageList.add(new Page(chefInfoFragment, collChefInfo));
            pageList.add(new Page(recipeFragment, collChefRecipe));
        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
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
