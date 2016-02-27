package co.kr.hufstory;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import co.kr.hufstory.menu_communication.MenusAPI;
import co.kr.hufstory.menu_communication.MenusNetwork;
import co.kr.hufstory.menu_communication.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

        //**2016.02.28 YURI
        mExpListView = (ExpandableListView) findViewById(R.id.expandListview);
        View footer = getLayoutInflater().inflate(R.layout.bottom_image_menu,null,false);
        mExpListView.addFooterView(footer);
        prepareData();
        mExpListAdapter = new ExpandableListAdapter(this,mExpListGroup,mExpListChild);
        mExpListView.setAdapter(mExpListAdapter);//**

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
}


