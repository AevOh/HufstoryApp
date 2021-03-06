package co.kr.hufstory.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.kr.hufstory.R;
import co.kr.hufstory.Util.WebChromeFileLoadClient;
import co.kr.hufstory.hubigo_fragment.HubigoFragment;
import co.kr.hufstory.menu_fragment.MenuFragment;
import co.kr.hufstory.momo_fragment.MomoMainFragment;
import co.kr.hufstory.version_update.MarketVersionChecker;

public class MainActivity extends AppCompatActivity {
    public static final int S_RC_FILE_CHOOSE = 2833;
    public static final int FRAGMENT_LAYOUT = R.id.content_frame;
    public static final String COOKIE_SESSION = "PHPSESSID";

    private DrawerLayout mDrawerLayout;
    private View mView;
    private ActionBarDrawerToggle mDrawerToggle;

    private ExpandableListAdapter mExpListAdapter;
    private ExpandableListView mExpListView;
    private List<String> mExpListGroup;
    private HashMap<String, List<String>> mExpListChild;
    private HashMap<String, String> mExpListUrlHash;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    /* 2016.02.25 노형욱*/
    private WebViewManager mWebViewManager;
    private FrameLayout mFrameLayout;

    private FragmentManager mFragmentManager;

    private MenuFragment mMenuFragment;
    // private bbangFragment mBBangFragment;
    private HubigoFragment mHubigoFragment;
    private MomoMainFragment mMomoFragment;

    private HufstoryFragment mCurrentFragment;

    private Toolbar mToolbar;
    private CoordinatorLayout mToolbarLayout;

    private List<View> mMainButtonList;

    private TextView mNickName;
    private TextView mID;
    private TextView mLoginText;

    private boolean onFragment;

    private WebChromeFileLoadClient mWebChromeFileLoadClient;

    /* 2016.02.27, Aev Oh, 자동 업데이트 부분. */
    private MarketVersionChecker mMarketVersionChecker;
    private VersionCheckTread mVersioniCheckThread;

    private MainController mController;

    public MainActivity(){
        super();
        mController = new MainController();
        mController.attachView(this);

        mWebChromeFileLoadClient = new WebChromeFileLoadClient(this, S_RC_FILE_CHOOSE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mView=(View) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                mController.loadUserInfo(CookieManager.getInstance().getCookie(
                        getResources().getString(R.string.hufstory_login)));
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //**2016.02.28 YURI
        mExpListView = (ExpandableListView) findViewById(R.id.expandListview);
        View header = getLayoutInflater().inflate(R.layout.top_image_menu,null,false);
        View footer = getLayoutInflater().inflate(R.layout.bottom_image_menu,null,false);
        mExpListView.addHeaderView(header);
        mExpListView.addFooterView(footer);

        mController.loadMenuCategory();

        /* 2016.02.25 노형욱 */
        mToolbar = (Toolbar)findViewById(R.id.toolBar);
        mToolbarLayout = (CoordinatorLayout)findViewById(R.id.toolbarLayout);

        mFrameLayout = (FrameLayout)findViewById(R.id.content_frame);
        mWebViewManager = new WebViewManager(R.id.webView, this, mFrameLayout, mWebChromeFileLoadClient);
        mWebViewManager.startWebView(getResources().getString(R.string.main_url));

        mFragmentManager = getFragmentManager();

        mMenuFragment = new MenuFragment();
        mHubigoFragment = new HubigoFragment();
        mHubigoFragment.attachActivity(this);
        mMomoFragment = new MomoMainFragment();

        mMainButtonList = new ArrayList<>();

        mNickName = (TextView)findViewById(R.id.nick_name);
        mID = (TextView)findViewById(R.id.id);
        mLoginText = (TextView)findViewById(R.id.login_text);

        // buttons initial
        initialManagingButton(R.id.home);
        initialManagingButton(R.id.exit);
        initialMainButton(R.id.eatmenu);
        initialMainButton(R.id.hubigo);
        initialMainButton(R.id.bbang);
        initialMainButton(R.id.momo);
        initialMainButton(R.id.login);
        initialMainButton(R.id.facebook);
        initialMainButton(R.id.event);
        initialMainButton(R.id.setting);

        /* 2016.02.27, Aev Oh, 어플 자동 업데이트 함수 호출 부분. */
        onTakeDeviceVersion();
        mMarketVersionChecker = new MarketVersionChecker();
        mVersioniCheckThread = new VersionCheckTread();
        mMarketVersionChecker.doMarketVersionTask();
        mVersioniCheckThread.execute();
    }

    /* 2016.03.15, Aev Oh, WebView 세션 유지를 위해 추가. */
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void onWebViewTrigger(){
        mToolbar.setTitle("Hufstory");
    }

    public void showNavigationBar(List<String> groupName, HashMap<String,
            List<String>> childData, HashMap<String, String> urlMap){
        mExpListGroup = groupName;
        mExpListChild = childData;
        mExpListUrlHash = urlMap;

        mExpListAdapter = new ExpandableListAdapter(this,mExpListGroup,mExpListChild);
        mExpListView.setAdapter(mExpListAdapter);
        mExpListView.setOnChildClickListener(new ExpandableListListener());
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public WebViewManager getWebViewManager(){
        return mWebViewManager;
    }

    public HubigoFragment getHubigoFragment(){
        return mHubigoFragment;
    }

    private void addUrlHash(List<String> keyList, List<String> valueList){
        for(int i = 0; i < keyList.size(); i++)
            mExpListUrlHash.put(keyList.get(i), valueList.get(i));
    }

    private void initialMainButton(int id){
        View button = findViewById(id);
        button.setOnClickListener(new MainButtonClickedListener());
        mMainButtonList.add(button);
    }

    private void initialManagingButton(int id){
        View button = findViewById(id);
        button.setOnClickListener(new ManagingButtonClickedListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == S_RC_FILE_CHOOSE)
            mWebChromeFileLoadClient.getUploadedRCFile(resultCode, data);
    }

    public class ManagingButtonClickedListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.home:
                    mWebViewManager.goBackWebViewToHome();
                    mToolbarLayout.removeAllViews();
                    getSupportActionBar().show();
                    break;

                case R.id.exit:
                    break;
            }
            mDrawerLayout.closeDrawers();
        }
    }

    public void contentFragmentTransaction(int layoutID, HufstoryFragment fragment){
        contentFragmentTransaction(layoutID, fragment, R.anim.no_animation, R.anim.no_animation);
    }

    public void contentFragmentTransaction(int layoutID, HufstoryFragment fragment, int startAnimation, int endAnimation){
        mWebViewManager.endWebView();
        mCurrentFragment = fragment;

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(startAnimation, endAnimation);

        fragmentTransaction
                .replace(layoutID, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void showUserInfo(UserInfo userInfo){
        mNickName.setText(userInfo.getNick_name());
        mID.setText("(" + userInfo.getUser_id() + ")");
        mLoginText.setText(userInfo.isLogin()?
                getResources().getString(R.string.logout_text) : getResources().getString(R.string.login_text));
    }

    // 2016.02.25 노형욱
    public class MainButtonClickedListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            this.firstActionsOnClicked(v);

            switch(v.getId()) {
                case R.id.eatmenu:
                    //2016.02.26, Aev Oh, 식단표 누르고 월요일에 바로 정보가 안오는 문제 해결.
                    contentFragmentTransaction(FRAGMENT_LAYOUT, mMenuFragment);
                    break;
                case R.id.hubigo:
                    contentFragmentTransaction(FRAGMENT_LAYOUT, mHubigoFragment);
                    mHubigoFragment.cookieChange(CookieManager.getInstance().getCookie(getResources().getString(R.string.hufstory_login)));
                    mHubigoFragment.showToolbarButtons();
                    break;
                case R.id.bbang:
                    //contentFragmentTransaction(R.id.content_frame, mBbangFragment);
                    Toast.makeText(getApplicationContext(), "서버 점검중입니다.", Toast.LENGTH_SHORT).show();
                    mWebViewManager.goBackWebViewToHome();
                    break;
                case R.id.momo:
                    contentFragmentTransaction(FRAGMENT_LAYOUT, mMomoFragment);
                    mToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.momo_color));
                    //mWebViewManager.startWebView(getResources().getString(R.string.momo_url));
                    break;

                case R.id.login:
                    if(mLoginText.getText().equals(getResources().getString(R.string.login_text)))
                        mWebViewManager.startWebView(getResources().getString(R.string.hufstory_login));
                    else
                        mWebViewManager.startWebView(getResources().getString(R.string.hufstory_logout));
                    break;
                case R.id.facebook:
                    mWebViewManager.startWebView(getResources().getString(R.string.facebook_url));
                    break;
                case R.id.event:
                    Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                    mWebViewManager.goBackWebViewToHome();
                    break;
                case R.id.setting:
                    Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                    mWebViewManager.goBackWebViewToHome();
                    break;
            }

            this.lastActionsOnClikced(v);
        }

        private void firstActionsOnClicked(View v){
            for(int i = 0; i < mMainButtonList.size(); i++)
                mMainButtonList.get(i).setSelected(false);

            mFrameLayout.removeView(mWebViewManager.getWebView());
            mToolbarLayout.removeAllViews();
            mToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.hufstory_color));
            getSupportActionBar().show();

            v.setSelected(true);
        }

        private void lastActionsOnClikced(View v){
            mDrawerLayout.closeDrawers();

            if(v.getTag() != null)
                mToolbar.setTitle(v.getTag().toString());
        }
    }

    /* 2016.02.28 yuri*/
    public class ExpandableListListener implements ExpandableListView.OnChildClickListener {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            String childName = mExpListAdapter.getChild(groupPosition, childPosition).toString();
            mWebViewManager.startWebView(mExpListUrlHash.get(childName));

            mToolbarLayout.removeAllViews();
            mToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.hufstory_color));
            mDrawerLayout.closeDrawers();
            getSupportActionBar().show();

            return true;
        }
    }

    // 2016.03.01 wook back key Actions
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
            return;
        }

        if(mWebViewManager.onWebView() || mWebViewManager.onFragment() || doubleBackToExitPressedOnce){
            if(mWebViewManager.onWebView()) mWebViewManager.webViewbackAction();
            else if(mWebViewManager.onFragment()) mCurrentFragment.backKeyAction(this);
            else if(doubleBackToExitPressedOnce) super.onBackPressed();

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /*
     * 2016.02.27, Aev Oh, 앱 자동 업데이트 알림 부분.
     * 참고 링크: http://ccdev.tistory.com/12
    */
    private void showUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

        // 여기서 부터는 알림창의 속성 설정
        builder.setTitle("Hufstory 업데이트 알림")        // 제목 설정
                .setMessage("Hufstory App이 업데이트 되었습니다. \n\n" +
                        "새로운 기능과 더욱 원활한 서비스를 이용하기 위해 업데이트를 진행해 주시길 부탁드립니다.")        // 메세지 설정
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("업데이트", new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        doUpdate();
                        setUpdateCheckFalse();
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setUpdateCheckFalse();
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*
     * 2016.02.27, Aev Oh, PlayStore로 이동하는 부분.
     * 참고 링크: http://developer.android.com/intl/ko/distribute/tools/promote/linking.html
    */
    private void doUpdate(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivity(intent);
    }

    /*
     * 2016.02.27, Aev Oh
     * 사용자 디바이스의 Hufstory App 버젼 정보를 넘기는 부분.
    */
    private static String sDeviceVersion = "";
    public void onTakeDeviceVersion(){
        try {
            sDeviceVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static String getDeviceVersion(){
        return sDeviceVersion;
    }


    /*
     * 2016.02.27, Aev Oh
     * 업데이트를 할지 안 할 지 체크하는 부분.
    */
    private static boolean update_check;
    public static void setUpdateCheckTrue(){
        update_check = true;
    }
    public static void setUpdateCheckFalse(){
        update_check = false;
    }
    class VersionCheckTread extends AsyncTask<Void, Void, Void> {
        private long  start_time;
        public VersionCheckTread(){
            update_check = false;
            start_time = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... params) {

            long current_time;
            while(!update_check) {
                current_time = System.currentTimeMillis();
                long gap_time = (current_time - start_time) / 1000; //gap_time 단위: 1초
                if(gap_time > 1) {   //어플 시작후 30초 뒤 업데이트 체크 종료.
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void params){
            if(update_check)
                showUpdateDialog();
        }
    }
}


