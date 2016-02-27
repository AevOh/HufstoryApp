package co.kr.hufstory;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.kr.hufstory.menu_communication.MenusNetwork;
import co.kr.hufstory.version_update.MarketVersionChecker;

public class   MainActivity extends AppCompatActivity {
    public static enum Week {MON, TUE, WED, THU, FRI, SAT, SUN};

    private DrawerLayout mDrawerLayout;
    private View mView;
    private ActionBarDrawerToggle mDrawerToggle;

    private ExpandableListAdapter mExpListAdapter;
    private ExpandableListView mExpListView;
    private List<String> mExpListGroup;
    private HashMap<String, List<String>> mExpListChild;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    /* 2016.02.25 노형욱*/
    private FrameLayout mFrameLayout;
    private LayoutInflater mInflater;
    private View mWebView_view;
    private WebView mWebView;

    private FragmentManager mFragmentManager;

    private MenuFragment mMenuFragment;
    // private bbangFragment mBBangFragment;
    // private hubigoFragment mHubigoFragment;
    // private momoFragment mMOMOFragment;

    private Toolbar mToolbar;

    private List<View> mMainButtonList;
    private ImageView mEatMenuButton;
    private ImageView mHubigoButton;
    private ImageView mBbangButton;
    private ImageView mMomoButton;
    /*2016.02.25 00:10 yuri*/
    private ImageView mExitButton;
    private ImageView mSettingButton;
    private ImageView mLogoutButton;

    /* 2016.02.27, Aev Oh, 자동 업데이트 부분. */
    private MarketVersionChecker mMarketVersionChecker;
    private VersionCheckTread mVersioniCheckThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mView=(View) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                // getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mExpListView = (ExpandableListView) findViewById(R.id.expandListview);
        prepareData();
        mExpListAdapter = new ExpandableListAdapter(this,mExpListGroup,mExpListChild);
        mExpListView.setAdapter(mExpListAdapter);

        /*2016.02.25 00:10 yuri*/
        mExitButton = (ImageView) findViewById(R.id.exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

        /* 2016.02.25 노형욱 */
        mInflater = LayoutInflater.from(this.getBaseContext());
        mWebView_view = mInflater.inflate(R.layout.webview, null, false);

        mWebView = (WebView)mWebView_view.findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        mFrameLayout = (FrameLayout)findViewById(R.id.content_frame);

        initialWebView("http://www.hufstory.co.kr");

        mFragmentManager = getFragmentManager();

        mMenuFragment = new MenuFragment();

        mToolbar = (Toolbar)findViewById(R.id.toolBar);
        mToolbar.setTitle("HUFSTORY");

        mMainButtonList = new ArrayList<>();

        mEatMenuButton = (ImageView)findViewById(R.id.eatmenu);
        mEatMenuButton.setOnClickListener(new MainButtonClickedListener());
        mMainButtonList.add(mEatMenuButton);

        mHubigoButton = (ImageView)findViewById(R.id.hubigo);
        mHubigoButton.setOnClickListener(new MainButtonClickedListener());
        mMainButtonList.add(mHubigoButton);

        mBbangButton = (ImageView)findViewById(R.id.bbang);
        mBbangButton.setOnClickListener(new MainButtonClickedListener());
        mMainButtonList.add(mBbangButton);

        mMomoButton = (ImageView)findViewById(R.id.momo);
        mMomoButton.setOnClickListener(new MainButtonClickedListener());
        mMainButtonList.add(mMomoButton);

        /* 2016.02.27, Aev Oh, 어플 자동 업데이트 함수 호출 부분. */
        mMarketVersionChecker = new MarketVersionChecker();
        mVersioniCheckThread = new VersionCheckTread();
        mVersioniCheckThread.run();
        mMarketVersionChecker.doMarketVersionTask();
    }

    private void prepareData(){
        mExpListGroup = new ArrayList<String>();
        mExpListChild = new HashMap<String, List<String>>();

        // Adding child data
        mExpListGroup.add("게시판");
        mExpListGroup.add("기자단");
        mExpListGroup.add("생활정보");
        mExpListGroup.add("학교생활");
        mExpListGroup.add("제휴운영");
        mExpListGroup.add("Hot Link");

        // Adding child data
        List<String> board = new ArrayList<String>();
        board.add("공지사항");
        board.add("자유게시판");
        board.add("외대 갤러리");
        board.add("동아리 게시판");
        board.add("분실물 게시판");
        board.add("공모전 및 대외활동");

        List<String> reporter = new ArrayList<String>();
        reporter.add("동아리 정보");
        reporter.add("맛집기행기");


        List<String> life_info = new ArrayList<String>();
        life_info.add("중고시장");
        life_info.add("주거정보");
        life_info.add("자유홍보");

        List<String> school = new ArrayList<String>();
        school.add("총학 게시판");
        school.add("Hufs Dorm (기숙사)");
        school.add("경력개발센터");
        school.add("취업 정보 게시판");

        List<String> alliance = new ArrayList<String>();
        alliance.add("훕스토리 제휴 운영");
        alliance.add("밝은성모안과");

        List<String> hotLink = new ArrayList<String>();
        hotLink.add("Hufs");
        hotLink.add("E-Class");
        hotLink.add("종합정보시스템");
        hotLink.add("수강신청 장바구니");
        hotLink.add("성적열람");
        hotLink.add("외대 도서관");
        hotLink.add("Office 365(hufs)");


        mExpListChild.put(mExpListGroup.get(0), board); // Header, Child data
        mExpListChild.put(mExpListGroup.get(1), reporter);
        mExpListChild.put(mExpListGroup.get(2), life_info);
        mExpListChild.put( mExpListGroup.get(3), school);
        mExpListChild.put(mExpListGroup.get(4), alliance);
        mExpListChild.put(mExpListGroup.get(5), hotLink);
    }

    // 2016.02.26 wook - start webView with url
    private void initialWebView(String url){
        mFrameLayout.removeView(mWebView);
        mWebView.loadUrl(url);

        mFrameLayout.addView(mWebView);
    }

    // 2016.02.26 wook - refactoring
    public void sleep(int timeMs){
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void contentFragmentTransaction(int layoutID, Fragment fragment){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutID, fragment);
        fragmentTransaction.commit();
    }

    // 2016.02.25 노형욱
    public class MainButtonClickedListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            mDrawerLayout.closeDrawers();

            if(v.isSelected())
                return;

            for(int i = 0; i < mMainButtonList.size(); i++)
                mMainButtonList.get(i).setSelected(false);

            mFrameLayout.removeView(mWebView);

            v.setSelected(true);

            switch(v.getId()) {
                case R.id.eatmenu:
                    //2016.02.26, Aev Oh, 식단표 누르고 월요일에 바로 정보가 안오는 문제 해결.
                    MenusNetwork.pullMenus();
                    sleep(250);

                    contentFragmentTransaction(R.id.content_frame, mMenuFragment);
                    mToolbar.setTitle("식단표");
                    break;
                case R.id.hubigo:
                    //contentFragmentTransaction(R.id.content_frame, mHubigoFragment);
                    mToolbar.setTitle("후비고");
                    break;
                case R.id.bbang:
                    //contentFragmentTransaction(R.id.content_frame, mBbangFragment);
                    mToolbar.setTitle("빵차");
                    break;
                case R.id.momo:
                    //contentFragmentTransaction(R.id.content_frame, mMomoFragment);
                    mToolbar.setTitle("모현의모든것");
                    break;
            }
        }
    }



    /*
     * 2016.02.27, Aev Oh, 앱 자동 업데이트 알림 부분.
     * 참고 링크: http://ccdev.tistory.com/12
    */
    private void showUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

        // 여기서 부터는 알림창의 속성 설정
        builder.setTitle("Hufstory 업데이트 알림")        // 제목 설정
                .setMessage("새로운 버젼의 Hufstory App이 업데이트 되었습니다." +
                        "새로운 기능과 더욱 원활한 서비스를 이용하기 위해 업데이트를 진행해 주시길 부탁드립니다.")        // 메세지 설정
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("업데이트", new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        doUpdate();
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
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
        intent.setData(Uri.parse("market://details?id=" + getPackageName()));
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
    class VersionCheckTread extends Thread{
        public VersionCheckTread(){
            update_check = false;
        }

        @Override
        public void run(){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(update_check){
                showUpdateDialog();
                return;
            }
        }
    }
}
