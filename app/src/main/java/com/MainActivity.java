
package com;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.R;
import com.browser.BrowseChefFragment;
import com.browser.BrowseRecipeFragment;
import com.chef_order_list.ChefOrderListChefFragment;
import com.chef_order_list.ChefOrderListMemberFragment;
import com.collection.CollectionDetail;
import com.collection.CollectionGetAllByMemNOTask;
import com.collection.CollectionVO;
import com.main.Common;
import com.main.LoginDialogActivity;
import com.member.MemberGetOneByACTask;
import com.member.MemberVO;
import com.recipetype.RecipeLTypeFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String url = Common.URL + "MemberServletAndroid";               // annotation: @WebServlet("/MemberServlet") in the eclipse
    private static final int REQ_LOGIN = 1;
    private android.support.v7.widget.Toolbar toolbar;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    /////////////////Collection///////////////////////////////////////
    private List<CollectionVO> collClassList = new ArrayList<CollectionVO>();
    private List<CollectionVO> collChefList = new ArrayList<CollectionVO>();
    private List<CollectionVO> collMemberList = new ArrayList<CollectionVO>();
    private List<CollectionVO> collRecipeList = new ArrayList<CollectionVO>();
    private List<CollectionVO> collectionVOList2 = new ArrayList<CollectionVO>();
    private List<CollectionVO> collectionVOList = new ArrayList<CollectionVO>();
    private TextView tv_collection_class;
    private List collVOList;
/////////////////Collection///////////////////////////////////////

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

//        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
//        boolean login = pref.getBoolean("login", false);
//        startActivityForResult(this, REQ_LOGIN);
//        findViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);

        findViews();

        String mem_ac = pref.getString("mem_ac", "");
        String mem_pw = pref.getString("mem_pw", "");
        if(isUserValid(mem_ac, mem_pw)){
            MenuItemLogin();
        }else{
            MenuItemLogout();
        }

    }


    private void findViews() {
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        mNavigationView.refreshDrawableState();
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new MCTabFragment()).addToBackStack(null).commit();

//        mFragmentTransaction.replace(R.id.containerView, new BrowseRecipeFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                boolean login = pref.getBoolean("login", false);

//                 nav_item_login;
                if (login) {
                    MenuItemLogin();
                    if (menuItem.getItemId() == R.id.nav_item_logout) { //登入狀態想登出
                        pref.edit().putBoolean("login", false).apply();

                        String mem_ac = pref.getString("mem_ac", "");
                        String mem_pw = pref.getString("mem_pw", "");
                        String action = "logout";
                        try {
                            new MemberGetOneByACTask().execute(url, action, mem_ac, mem_pw).get(); //執行登出task
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        pref.edit()
                                .putBoolean("login", false)
                                .putString("mem_ac", null)
                                .putString("mem_pw", null)
                                .apply();
                        MenuItemLogout();
                        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                        xfragmentTransaction.replace(R.id.containerView, new MCTabFragment()).commit();
                    }

//                    Log.d(TAG,"pref.getString(\"mem_own\", \"\")"+pref.getString("mem_own", ""));

                    if (menuItem.getItemId() == R.id.nav_item_collection_recipe) {
//                        pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                        pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                        String mem_no = pref.getString("mem_no", "");

                        showCollectionListItem();

                        if (!collRecipeList.isEmpty()) {
                            collectionVOList.clear();
                            collectionVOList.addAll(collRecipeList);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mem_no", mem_no);
                            bundle.putSerializable("personal_mem_no", mem_no);//個人頁面所屬會員編號
                            bundle.putSerializable("collectionVOList", (java.io.Serializable) collectionVOList);
                            Intent loginIntent = new Intent(MainActivity.this, CollectionDetail.class);
                            loginIntent.putExtras(bundle);
                            startActivityForResult(loginIntent, REQ_LOGIN);
                        } else {
                            MainActivity.this.showToast(R.string.msg_CollectionNoFind);
                        }
                    }

                    if (menuItem.getItemId() == R.id.nav_item_collection_member) {
//                        pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                        pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                        String mem_no = pref.getString("mem_no", "");

                        showCollectionListItem();

                        if (!collMemberList.isEmpty()) {
                            collectionVOList.clear();
                            collectionVOList.addAll(collMemberList);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mem_no", mem_no);
                            bundle.putSerializable("collectionVOList", (java.io.Serializable) collectionVOList);
                            Intent loginIntent = new Intent(MainActivity.this, CollectionDetail.class);
                            loginIntent.putExtras(bundle);
                            startActivityForResult(loginIntent, REQ_LOGIN);
                        } else {
                            MainActivity.this.showToast(R.string.msg_CollectionNoFind);
                        }
                    }

                    if (menuItem.getItemId() == R.id.nav_item_collection_chef) {
//                        pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                        pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                        String mem_no = pref.getString("mem_no", "");

                        showCollectionListItem();

                        if (!collChefList.isEmpty()) {
                            collectionVOList.clear();
                            collectionVOList.addAll(collChefList);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mem_no", mem_no);
                            bundle.putSerializable("collectionVOList", (java.io.Serializable) collectionVOList);
                            Intent loginIntent = new Intent(MainActivity.this, CollectionDetail.class);
                            loginIntent.putExtras(bundle);
                            startActivityForResult(loginIntent, REQ_LOGIN);
                        } else {
                            MainActivity.this.showToast(R.string.msg_CollectionNoFind);
                        }
                    }


                    if (menuItem.getItemId() == R.id.nav_item_personal_page) { //登入時狀態想進入個人頁面
//                        pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                        if (pref.getString("mem_own", "").equals("0")) {  // member_own = 0  一般會員
                            Intent loginIntent = new Intent(MainActivity.this, MemberPageMainActivity.class);
                            startActivityForResult(loginIntent, REQ_LOGIN);
                            ChefOrderListDrawer(pref.getString("mem_own", ""));
                        } else if (pref.getString("mem_own", "").equals("1")) { // member_own = 1 私廚
                            Intent loginIntent = new Intent(MainActivity.this, ChefPageMainActivity.class);
                            startActivityForResult(loginIntent, REQ_LOGIN);
                            ChefOrderListDrawer(pref.getString("mem_own", ""));
                        }
                    }

                } else {
                    MenuItemLogout();
//                    findViews();
                    if (menuItem.getItemId() == R.id.nav_item_login) { //進登入畫嗎
                        Intent loginIntent = new Intent(MainActivity.this, LoginDialogActivity.class);
                        startActivityForResult(loginIntent, REQ_LOGIN);
                    }
                }

                if (menuItem.getItemId() == R.id.nav_item_main) {//首頁
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }


                if (menuItem.getItemId() == R.id.nav_item_chef_order_list_for_member) {//下訂的需求訂單
                    String mem_no = pref.getString("mem_no", "");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mem_no", mem_no);
                    bundle.putSerializable("personal_mem_no", mem_no);//個人頁面所屬會員編號

                    ChefOrderListMemberFragment chefOrderListMemberFragment = new ChefOrderListMemberFragment();
                    chefOrderListMemberFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, chefOrderListMemberFragment).addToBackStack(null).commit();
//                    fragmentTransaction.addToBackStack(chefOrderListMemberFragment.getClass().getName());
//                    fragmentTransaction.commit();
//                    fragmentTransaction.replace(R.id.containerView, chefOrderListMemberFragment).commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_chef_order_list_for_chef) {//收到的需求訂單
                    String mem_no = pref.getString("mem_no", "");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mem_no", mem_no);
                    bundle.putSerializable("personal_mem_no", mem_no);//私廚給予個人的會員編號
                    ChefOrderListChefFragment chefOrderListChefFragment = new ChefOrderListChefFragment();
                    chefOrderListChefFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, chefOrderListChefFragment).addToBackStack(null).commit();
//                    fragmentTransaction.addToBackStack(chefOrderListChefFragment.getClass().getName());
//                    fragmentTransaction.commit();
                }

//                if (menuItem.getItemId() == R.id.nav_item_rl0006) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("recipe_l_type_no", "RL0006");
//                    bundle.putSerializable("recipe_l_type_name", "異國料理");
//                    RecipeLTypeFragment recipeLTypeFragment = new RecipeLTypeFragment();
//                    recipeLTypeFragment.setArguments(bundle);
//                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
//                    xfragmentTransaction.replace(R.id.containerView, recipeLTypeFragment).addToBackStack(null).commit();
////                    xfragmentTransaction.addToBackStack(recipeLTypeFragment.getClass().getName());
////                    xfragmentTransaction.commit();
//                }
                if (menuItem.getItemId() == R.id.nav_item_rl0001) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipe_l_type_no", "RL0001");
                    bundle.putSerializable("recipe_l_type_name", "家常食材");
                    RecipeLTypeFragment recipeLTypeFragment = new RecipeLTypeFragment();
                    recipeLTypeFragment.setArguments(bundle);
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, recipeLTypeFragment).addToBackStack(null).commit();
//                    xfragmentTransaction.addToBackStack(recipeLTypeFragment.getClass().getName());
//                    xfragmentTransaction.commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_rl0002) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipe_l_type_no", "RL0002");
                    bundle.putSerializable("recipe_l_type_name", "家常菜");
                    RecipeLTypeFragment recipeLTypeFragment = new RecipeLTypeFragment();
                    recipeLTypeFragment.setArguments(bundle);
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, recipeLTypeFragment).addToBackStack(null).commit();
//                    xfragmentTransaction.addToBackStack(recipeLTypeFragment.getClass().getName());
//                    xfragmentTransaction.commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_rl0003) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipe_l_type_no", "RL0003");
                    bundle.putSerializable("recipe_l_type_name", "點心烘焙");
                    RecipeLTypeFragment recipeLTypeFragment = new RecipeLTypeFragment();
                    recipeLTypeFragment.setArguments(bundle);
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, recipeLTypeFragment).addToBackStack(null).commit();
//                    xfragmentTransaction.addToBackStack(recipeLTypeFragment.getClass().getName());
//                    xfragmentTransaction.commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_rl0004) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipe_l_type_no", "RL0004");
                    bundle.putSerializable("recipe_l_type_name", "季節節慶");
                    RecipeLTypeFragment recipeLTypeFragment = new RecipeLTypeFragment();
                    recipeLTypeFragment.setArguments(bundle);
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, recipeLTypeFragment).addToBackStack(null).commit();
//                    xfragmentTransaction.addToBackStack(recipeLTypeFragment.getClass().getName());
//                    xfragmentTransaction.commit();
                }

//                if (menuItem.getItemId() == R.id.nav_item_rl0005) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("recipe_l_type_no", "RL0005");
//                    bundle.putSerializable("recipe_l_type_name", "飲料冰品");
//                    RecipeLTypeFragment recipeLTypeFragment = new RecipeLTypeFragment();
//                    recipeLTypeFragment.setArguments(bundle);
//                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
//                    xfragmentTransaction.replace(R.id.containerView, recipeLTypeFragment).addToBackStack(null).commit();
////                    xfragmentTransaction.addToBackStack(recipeLTypeFragment.getClass().getName());
////                    xfragmentTransaction.commit();
//                }

                mNavigationView.refreshDrawableState();
                return false;
            }


        });


        /**
         * Setup Drawer Toggle of the Toolbar
         */

//        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(toolbar);
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
//
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//        mDrawerToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mNavigationView.getMenu().findItem(R.id.nav_item_login).setVisible(false);

            mNavigationView.getMenu().findItem(R.id.nav_item_collection_recipe).setVisible(true); //
            mNavigationView.getMenu().findItem(R.id.nav_item_collection_member).setVisible(true); //
            mNavigationView.getMenu().findItem(R.id.nav_item_collection_chef).setVisible(true); //

//            mNavigationView.getMenu().findItem(R.id.nav_item_chef_order_list).setVisible(true); //需求訂單

            mNavigationView.getMenu().findItem(R.id.nav_item_logout).setVisible(true);
            mNavigationView.getMenu().findItem(R.id.nav_item_personal_page).setVisible(true);

            SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            boolean login = pref.getBoolean("login", false);
            if (login) {
                pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                String mem_no = pref.getString("mem_no", "");
                ChefOrderListDrawer(pref.getString("mem_own", ""));
            }
        }
    }

    private void MenuItemLogin() {
        mNavigationView.getMenu().findItem(R.id.nav_item_login).setVisible(false);

        mNavigationView.getMenu().findItem(R.id.nav_item_collection_recipe).setVisible(true); //
        mNavigationView.getMenu().findItem(R.id.nav_item_collection_member).setVisible(true); //
        mNavigationView.getMenu().findItem(R.id.nav_item_collection_chef).setVisible(true); //

//        mNavigationView.getMenu().findItem(R.id.nav_item_chef_order_list).setVisible(true); //需求訂單

        mNavigationView.getMenu().findItem(R.id.nav_item_logout).setVisible(true);
        mNavigationView.getMenu().findItem(R.id.nav_item_personal_page).setVisible(true);

        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        if (login) {
            pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            String mem_no = pref.getString("mem_no", "");
            ChefOrderListDrawer(pref.getString("mem_own", ""));
        }
    }

    private void MenuItemLogout() {
        mNavigationView.getMenu().findItem(R.id.nav_item_login).setVisible(true);

        mNavigationView.getMenu().findItem(R.id.nav_item_collection_recipe).setVisible(false); //
        mNavigationView.getMenu().findItem(R.id.nav_item_collection_member).setVisible(false); //
        mNavigationView.getMenu().findItem(R.id.nav_item_collection_chef).setVisible(false); //

//        mNavigationView.getMenu().findItem(R.id.nav_item_chef_order_list).setVisible(false); //需求訂單
        mNavigationView.getMenu().findItem(R.id.nav_item_chef_order_list_for_chef).setVisible(false);
        mNavigationView.getMenu().findItem(R.id.nav_item_chef_order_list_for_member).setVisible(false);

        mNavigationView.getMenu().findItem(R.id.nav_item_logout).setVisible(false);
        mNavigationView.getMenu().findItem(R.id.nav_item_personal_page).setVisible(false);
    }

    private void ChefOrderListDrawer(String mem_own) {
        if (mem_own.equals("0")) {  // member_own = 0  一般會員
            mNavigationView.getMenu().findItem(R.id.nav_item_chef_order_list_for_member).setVisible(true);
            mNavigationView.getMenu().findItem(R.id.nav_item_chef_order_list_for_chef).setVisible(false);
        } else if (mem_own.equals("1")) { // member_own = 1 私廚
            mNavigationView.getMenu().findItem(R.id.nav_item_chef_order_list_for_member).setVisible(false);
            mNavigationView.getMenu().findItem(R.id.nav_item_chef_order_list_for_chef).setVisible(true);
        }
    }


//    // 按下上傳按鈕
//    public void onUploadClick(View view) {
////        if (picture == null) {
////            showToast(this, R.string.msg_NotUploadWithoutPicture);
////            return;
////        }
//        Intent loginIntent = new Intent(this, LoginDialogActivity.class);
//        startActivityForResult(loginIntent, REQ_LOGIN);
//    }

    //請求外部儲存權限
    private boolean mediaMounted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private void showToast(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private final static int REQ_PERMISSIONS = 0;


    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        String text = getString(R.string.text_ShouldGrant);
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                break;
        }
    }


//    getMenuInflater().inflate(R.menu.menu_home, menu);
//    SearchView search = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search);
//    // Associate searchable configuration with the SearchView
//    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//    search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
//    search.setQueryHint(getResources().getString(R.string.search_hint));

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem menuSearchItem = menu.findItem(R.id.my_search);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menuSearchItem.getActionView();
        SearchView searchView = (SearchView) menuSearchItem.getActionView();
        searchView.setQueryHint("請輸入食譜名稱");
        /**
         * 默?情?下是?提交搜索的按?，所以用?必?在??上按下"enter"??提交搜索.你可以同?setSubmitButtonEnabled(
         * true)?添加一?提交按?（"submit" button)
         * ?置true后，右??出?一?箭?按?。如果用??有?入，就不?触?提交（submit）事件
         */
        searchView.setSubmitButtonEnabled(true);

        /**
         * 初始是否已?是展?的??
         * ?上此句后searchView初始展?的，也就是是可以???入的??，如果不?，那么就需要??下放大?，才能展?出??入框
         */
//        searchView.onActionViewExpanded();
        // ?置search view的背景色
        searchView.setBackgroundColor(0x111111ff);

//        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, SearchItem.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("recipe_name", query);

                Intent intent = new Intent(getApplicationContext(), SearchableActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
        return true;
    }

//    android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(toolbar);
//    ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
//
//    mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//    mDrawerToggle.syncState();

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                return true;
            }
        });
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    private void showCollectionListItem() {

        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String mem_no = pref.getString("mem_no", "");

        if (Common.networkConnected(this)) {
            String url = Common.URL + "CollectionServletAndroid";
            List<CollectionVO> collTmpList = null;
            collChefList.clear();
            collRecipeList.clear();
            collMemberList.clear();
            try {
                collTmpList = new CollectionGetAllByMemNOTask().execute(url, mem_no).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (collTmpList == null || collTmpList.isEmpty()) {
                Common.showToast(this, R.string.msg_NoCollectionsFound);
            } else {
//                collClassList = new ArrayList<CollectionVO>();
//                collChefList = new ArrayList<CollectionVO>();
//                collMemberList = new ArrayList<CollectionVO>();
//                collRecipeList = new ArrayList<CollectionVO>();

                int r = 0, f = 0, c = 0, m = 0, l = 0;
                for (CollectionVO aCollection : collTmpList) {
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
            }
//            CollectionVO collectionVO = (CollectionVO) collVOList.get(0);
//            collectionVOList.clear();
//            if (collectionVO.getClass_no().equals("R")) {
//                tv_collection_class.setText("我的收藏-食譜");
//                collectionVOList.addAll(collRecipeList);
//            } else if (collectionVO.getClass_no().equals("C")) {
//                tv_collection_class.setText("我的收藏-私廚");
//                collectionVOList.addAll(collChefList);
//            } else if (collectionVO.getClass_no().equals("M")) {
//                tv_collection_class.setText("我的收藏-會員");
//                collectionVOList.addAll(collMemberList);
//            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter { /**內容固定無變化:繼承FragmentPagerAdapter：記憶體保留頁面資訊。頁面數量大及變化大，繼承FragmentStatePagerAdapter：換頁時，將被換掉無顯示的頁面從記憶體清除*/
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

            BrowseRecipeFragment browseRecipeFragment = new BrowseRecipeFragment();
            BrowseChefFragment browseChefFragment = new BrowseChefFragment();

            SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            boolean login = pref.getBoolean("login", false);
            if (login) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("personal_mem_no", pref.getString("mem_no", ""));
                browseChefFragment.setArguments(bundle);
            }

            String browserRecipe = "瀏覽食譜";
            String browserChef = "瀏覽私廚";
            pageList = new ArrayList<>();
            pageList.add(new Page(browseRecipeFragment, browserRecipe));
            pageList.add(new Page(browseChefFragment, browserChef));
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

    private boolean isUserValid(String mem_ac, String mem_pw) {
        // 應該連線至server端檢查帳號密碼是否正確

        String url = Common.URL + "MemberServletAndroid";
        String action = "login";
        Boolean loginResult = false;

        MemberVO memberVO = null;
        if (Common.networkConnected(this)) {
            try {
                memberVO = new MemberGetOneByACTask().execute(url, action, mem_ac, mem_pw).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

        if (memberVO != null && memberVO.getMem_pw().equals(mem_pw)) {
            loginResult = true;
        }
        return loginResult;
    }

//    //圓角轉換函式，帶入Bitmap圖片及圓角數值則回傳圓角圖，回傳Bitmap再置入ImageView
//    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//                bitmap.getHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
//                bitmap.getHeight());
//        final RectF rectF = new RectF(rect);
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//        return output;
//    }

}