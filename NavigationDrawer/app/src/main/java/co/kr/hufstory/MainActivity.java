package co.kr.hufstory;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;

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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.kr.hufstory.menu_communication.MenusNetwork;
import co.kr.hufstory.version_update.MarketVersionChecker;

public class MainActivity extends AppCompatActivity {
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
    private ImageView mEventButton;
    private ImageView mLoginButton;
    private ImageView mFacebookButton;

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

        //**2016.02.28 YURI
        mExpListView = (ExpandableListView) findViewById(R.id.expandListview);
        View header = getLayoutInflater().inflate(R.layout.top_image_menu,null,false);
        View footer = getLayoutInflater().inflate(R.layout.bottom_image_menu,null,false);
        mExpListView.addHeaderView(header);
        mExpListView.addFooterView(footer);
        mExpListPrepareData();
        mExpListAdapter = new ExpandableListAdapter(this,mExpListGroup,mExpListChild);
        mExpListView.setAdapter(mExpListAdapter);
        mExpListView.setOnChildClickListener(new ExpandableListListener());

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

        mLoginButton = (ImageView)findViewById(R.id.login);
        mLoginButton.setOnClickListener(new MainButtonClickedListener());
        mMainButtonList.add(mLoginButton);

        mFacebookButton = (ImageView)findViewById(R.id.facebook);
        mFacebookButton.setOnClickListener(new MainButtonClickedListener());
        mMainButtonList.add(mFacebookButton);

        mEventButton = (ImageView)findViewById(R.id.event);
        mEventButton.setOnClickListener(new MainButtonClickedListener());
        mMainButtonList.add(mEventButton);

        mSettingButton=(ImageView)findViewById(R.id.setting);
        mSettingButton.setOnClickListener(new MainButtonClickedListener());
        mMainButtonList.add(mSettingButton);

        /* 2016.02.27, Aev Oh, 어플 자동 업데이트 함수 호출 부분. */
        onTakeDeviceVersion();
        mMarketVersionChecker = new MarketVersionChecker();
        mVersioniCheckThread = new VersionCheckTread();
        mMarketVersionChecker.doMarketVersionTask();
        mVersioniCheckThread.execute();
    }

    private void mExpListPrepareData(){
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
        school.add("Hufs Dorm");
        school.add("기숙사 게시판");
        school.add("경력개발센터");
        school.add("취업 정보 게시판");
        school.add("교내일정");

        List<String> alliance = new ArrayList<String>();
        alliance.add("훕스토리 제휴 운영");
        alliance.add("밝은성모안과");

        List<String> hotLink = new ArrayList<String>();
        hotLink.add("외대인 인증");
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

                case R.id.login:
                    break;
                case R.id.facebook:
                    initialWebView("https://www.facebook.com/storyhufs/");
                    break;
                case R.id.event:
                    Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.setting:
                    Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /* 2016.02.28 yuri*/
    public class ExpandableListListener implements ExpandableListView.OnChildClickListener {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            String childName = mExpListAdapter.getChild(groupPosition, childPosition).toString();
            switch (childName){
                case "공지사항":
                    initialWebView("http://hufstory.co.kr/Notice");
                    break;
                case "자유게시판":
                    initialWebView("http://hufstory.co.kr/Free");
                    break;
                case "외대 갤러리":
                    initialWebView("http://hufstory.co.kr/Gallery");
                    break;
                case "동아리 게시판":
                    initialWebView("http://hufstory.co.kr/Club_board");
                    break;
                case "분실물 게시판":
                    initialWebView("http://hufstory.co.kr/Missing");
                    break;
                case "공모전 및 대외활동":
                    initialWebView("http://hufstory.co.kr/International_activity");
                    break;

                case "동아리 정보":
                    initialWebView("http://hufstory.co.kr/Club_info");
                    break;
                case "맛집기행기":
                    initialWebView("http://hufstory.co.kr/Delicousor_board");
                    break;

                case "중고시장":
                    initialWebView("http://hufstory.co.kr/Market");
                    break;
                case "주거정보":
                    initialWebView("http://hufstory.co.kr/House_info");
                    break;
                case "자유홍보":
                    initialWebView("http://hufstory.co.kr/Ads_free");
                    break;

                case "Hufs Dorm":
                    initialWebView("http://builder.hufs.ac.kr/user/mhdorm2/");
                    break;
                case "기숙사 게시판":
                    initialWebView("http://hufstory.co.kr/Dorm_board");
                    break;
                case "경력개발센터":
                    initialWebView("http://job.hufs.ac.kr");
                    break;
                case "취업 정보 게시판":
                    initialWebView("http://hufstory.co.kr/Job_board");
                    break;
                case "교내일정":
                    initialWebView("http://hufstory.co.kr/Schedule");
                    break;


                case "훕스토리 제휴 운영":
                    initialWebView("http://hufstory.co.kr/Alliance_all");
                    break;
                case "밝은성모안과":
                    initialWebView("http://hufstory.co.kr/Oklasik_board");
                    break;

                case "외대인 인증":
                    initialWebView("http://hufstory.co.kr/hufs_certification_form/form.html");
                    break;
                case "Hufs":
                    initialWebView("http://www.hufs.ac.kr/");
                    break;
                case "E-Class":
                    initialWebView("http://eclass.hufs.ac.kr/");
                    break;
                case "종합정보시스템":
                    initialWebView("http://webs.hufs.ac.kr:8989/src08/jsp/index.jsp");
                    break;
                case "수강신청 장바구니":
                    initialWebView("http://www.hufs.ac.kr/user/hufs/basket_intro/basket.html");
                    break;
                case "성적열람":
                    initialWebView("http://webs.hufs.ac.kr:8989/jsp/HUFS/stu1/stu1_i0_a0_a0.jsp");
                    break;
                case "외대 도서관":
                    initialWebView("http://mlibrary.hufs.ac.kr/");
                    break;
                case "Office 365(hufs)":
                    initialWebView("http://builder.hufs.ac.kr/user/indexSub.action?codyMenuSeq=61743429&siteId=hufs&menuType=T&uId=4&sortChar=AE&linkUrl=office365.html&mainFrame=right");
                    break;

            }
            mDrawerLayout.closeDrawers();

            return false;
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
                .setMessage("Hufstory App이 업데이트 되었습니다. \n\n" +
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
                if(gap_time > 30) {   //어플 시작후 30초 뒤 업데이트 체크 종료.
                    break;
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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


