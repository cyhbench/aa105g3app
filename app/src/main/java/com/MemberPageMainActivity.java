package com;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Frd_list.Frd_listFragment;
import com.androidbelieve.drawerwithswipetabs.R;
import com.collection.CollectionFragment;
import com.main.Common;
import com.main.LoginDialogActivity;
import com.member.MemberGetImageTask;
import com.member.MemberGetOneTask;
import com.member.MemberUpdateActivity;
import com.member.MemberVO;
import com.recipe.RecipeFragment;
import com.recipe.RecipeInsertActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MemberPageMainActivity extends AppCompatActivity {
    private static final String TAG = "logMainActivity";
    private String url = Common.URL + "MemberServletAndroid";
    private byte[] image;
    private String mem_no;// = "M00000001";
    private String mem_name;
    private String mem_own;
    int imageSize = 800;
    Bitmap bitmap = null;
    private static final int REQ_LOGIN = 1;
    private MemberVO memberVO = new MemberVO();
//    private MemberVO memberVO;
    private Bundle bundle = new Bundle();

    private Button bt_member_page_member_update;
    private Button bt_member_page_recipe_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent loginIntent = new Intent(this, LoginDialogActivity.class);

        startActivityForResult(loginIntent, REQ_LOGIN);
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        mem_no = pref.getString("mem_no","");

        setContentView(R.layout.member_page_main_activity);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("分享食光:個人頁面");
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        findViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView tv_member_page_member_mem_name = (TextView) findViewById(R.id.tv_member_page_member_mem_name);
        ImageView iv_member_page_mem_image = (ImageView) findViewById(R.id.iv_member_page_mem_image); //會員照片
        Button bt_member_page_member_update = (Button) findViewById(R.id.bt_member_page_member_update);
        Button bt_member_page_recipe_insert = (Button) findViewById(R.id.bt_member_page_recipe_insert);

        if (Common.networkConnected(this)) {

            try {
                memberVO = new MemberGetOneTask().execute(url, mem_no).get();
                Log.d(TAG, "getMem_name(line 146):" + memberVO.getMem_name());
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (memberVO == null) {
                Common.showToast(this, R.string.msg_NoMembersFound);
            } else {
//                tv_member_detail_mem_no.setText(memberVO.getMem_no());
                tv_member_page_member_mem_name.setText(memberVO.getMem_name());
//                tv_member_detail_mem_ac.setText(memberVO.getMem_ac());
//                tv_member_detail_mem_pw.setText(memberVO.getMem_pw());
//                tv_member_detail_mem_own.setText(memberVO.getMem_own());
//                tv_member_detail_mem_online.setText(memberVO.getMem_online());
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

        try {
            // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()

            bitmap = new MemberGetImageTask(null).execute(url, mem_no, imageSize).get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); //照相之後再把圖壓縮
//            iv_member_page_mem_image.setImageBitmap(bitmap);
            iv_member_page_mem_image.setImageBitmap(getRoundedCornerBitmap(bitmap, 500.0f));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image = out.toByteArray();
        } else {
            iv_member_page_mem_image.setImageResource(R.drawable.default_image);
        }
        //將圖片及圓角數值帶入getRoundedCornerBitmap()並放入iv_member_page_mem_image內
    }

    private void findViews() {
//        if (Common.networkConnected(this)) {
////            MemberVO memberVO = null;
//            MemberVO memberVO = new MemberVO();
//            try {
//                memberVO = new MemberGetOneTask().execute(url, mem_no).get();
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (memberVO == null) {
//                Common.showToast(this, R.string.msg_NoMembersFound);
//            } else {
////                tv_member_detail_mem_no.setText(memberVO.getMem_no());
//                tv_member_page_member_mem_name.setText(memberVO.getMem_name());
////                tv_member_detail_mem_ac.setText(memberVO.getMem_ac());
////                tv_member_detail_mem_pw.setText(memberVO.getMem_pw());
////                tv_member_detail_mem_own.setText(memberVO.getMem_own());
////                tv_member_detail_mem_online.setText(memberVO.getMem_online());
//            }
//        } else {
//            Common.showToast(this, R.string.msg_NoNetwork);
//        }
//
//        try {
//            // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()
//
//            bitmap = new MemberGetImageTask(null).execute(url, mem_no, imageSize).get();
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//        if (bitmap != null) {
//            iv_member_page_mem_image.setImageBitmap(bitmap);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            image = out.toByteArray();
//        } else {
//            iv_member_page_mem_image.setImageResource(R.drawable.default_image);
//        }
//
    }

//    MemberVO memberVO = new MemberVO();
//    memberVO.setMem_no(input_mem_id);
//    Bundle bundle = new Bundle();
//    bundle.putSerializable("memberVO", memberVO);
//    fragment.setArguments(bundle);

    private class MyPagerAdapter extends FragmentStatePagerAdapter { /**內容固定無變化:繼承FragmentPagerAdapter：記憶體保留頁面資訊。頁面數量大及變化大，繼承FragmentStatePagerAdapter：換頁時，將被換掉無顯示的頁面從記憶體清除*/
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
//            MemberVO memberVO = new MemberVO();
            memberVO.setMem_no(mem_no);
//            Bundle bundle = new Bundle();
            bundle.putSerializable("memberVO", memberVO);
            RecipeFragment recipeFragment = new RecipeFragment();
            recipeFragment.setArguments(bundle);
            Frd_listFragment frd_listFragment = new Frd_listFragment();
            frd_listFragment.setArguments(bundle);
            CollectionFragment collectionFragment = new CollectionFragment();
            collectionFragment.setArguments(bundle);


            pageList = new ArrayList<>();
            pageList.add(new Page(recipeFragment, "食譜"));
            pageList.add(new Page(frd_listFragment, "好友"));
            pageList.add(new Page(collectionFragment, "我的收藏"));
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

    public void onMemberUpdateClick(View view) {
        Intent intent = new Intent(this, MemberUpdateActivity.class);
        Bundle bundle = new Bundle();
        Log.d(TAG, "getMem_name(line 306):" + memberVO.getMem_name());
        memberVO.setMem_image(image); //因為會員圖與文字是分開抓的，所以要把圖給set()回去
        memberVO.setMem_no(mem_no);
        Log.d(TAG, "line 306:" + memberVO.getMem_image().length);
        bundle.putSerializable("memberVO", memberVO);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onRecipeInsertClick(View view){
        Intent intent = new Intent(this, RecipeInsertActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mem_no", memberVO.getMem_no());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
