package com.browser;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.Page;
import com.androidbelieve.drawerwithswipetabs.R;
import com.collection.CollectionDetailBrowserMemberRecipeFragment;
import com.main.Common;
import com.member.MemberGetImageTask;
import com.member.MemberGetOneTask;
import com.member.MemberVO;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.main.Common.showToast;


public class BrowseOneMemberMainActivity extends AppCompatActivity {
    private static final String TAG = "瀏覽某個會員";
    private String url = Common.URL + "MemberServletAndroid";
    private String mem_no;
    private byte[] image;
    int imageSize = 400;
    Bitmap bitmap = null;
    private MemberVO memberVO = new MemberVO();
    private Bundle bundle = new Bundle();
    private TextView tv_collection_detail_member_mem_name;
    private ImageView iv_collection_detail_member_mem_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.collection_detail_member_main_activity);
        tv_collection_detail_member_mem_name = (TextView) findViewById(R.id.tv_collection_detail_member_mem_name);
        iv_collection_detail_member_mem_image = (ImageView) findViewById(R.id.iv_collection_detail_member_mem_image); //會員照片
        final Toolbar toolbar = (Toolbar) findViewById(R.id.collection_detail_member_main_activity_toolbar);
        mem_no = (String) getIntent().getExtras().getSerializable("mem_no");
        getOneMember();
        toolbar.setTitle("瀏覽好友");
        setSupportActionBar(toolbar);


    }

    private void getOneMember() {
        if (Common.networkConnected(this)) {

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
    }

    @Override
    protected void onStart() {
        super.onStart();


//        if (Common.networkConnected(this)) {
//
//            try {
//                memberVO = new MemberGetOneTask().execute(url, mem_no).get();
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (memberVO == null) {
//                showToast(this, R.string.msg_NoMembersFound);
//            } else {
//                tv_collection_detail_member_mem_name.setText(memberVO.getMem_name());
//            }
//        } else {
//            showToast(this, R.string.msg_NoNetwork);
//        }

        try {

            bitmap = new MemberGetImageTask(null).execute(url, memberVO.getMem_no(), imageSize).get();
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

        ViewPager viewPager = (ViewPager) findViewById(R.id.collection_detail_member_viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.collection_detail_member_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * 內容固定無變化:繼承FragmentPagerAdapter：記憶體保留頁面資訊。頁面數量大及變化大，繼承FragmentStatePagerAdapter：換頁時，將被換掉無顯示的頁面從記憶體清除
         */
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            bundle.putSerializable("memberVO", memberVO);
            bundle.putSerializable("mem_no", mem_no);
            CollectionDetailBrowserMemberRecipeFragment recipeFragment = new CollectionDetailBrowserMemberRecipeFragment();
            recipeFragment.setArguments(bundle);

            String collMemRecipe = memberVO.getMem_name() + " 的食譜";
            pageList = new ArrayList<>();
            pageList.add(new Page(recipeFragment, collMemRecipe));
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
