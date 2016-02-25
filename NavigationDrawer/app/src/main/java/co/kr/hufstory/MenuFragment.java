package co.kr.hufstory;

import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends android.app.Fragment {
    /**
     * 노형욱, 2016.02.20
     */
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     * show current selected button
     * value : 0 = welfare, 1 = literary, 2 = dorm
     */
    public static int s_selected_cafeteria = 0;
    public static HCANetworkModule s_HCANetworkModule;

    private Button mWelfareButton;
    private Button mLiteraryButton;
    private Button mDormButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        FragmentActivity fragmentActivity = (FragmentActivity) super.getActivity();
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) inflater.inflate(R.layout.activity_menu, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentActivity.getSupportFragmentManager());

        s_HCANetworkModule = new HCANetworkModule();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)coordinatorLayout.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout)coordinatorLayout.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mWelfareButton = (Button)coordinatorLayout.findViewById(R.id.wellfare);
        mLiteraryButton = (Button)coordinatorLayout.findViewById(R.id.literary);
        mDormButton = (Button)coordinatorLayout.findViewById(R.id.dorm);

        mWelfareButton.setSelected(true);

        mWelfareButton.setOnClickListener(new buttonClickListener());
        mLiteraryButton.setOnClickListener(new buttonClickListener());
        mDormButton.setOnClickListener(new buttonClickListener());

        return coordinatorLayout;
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        s_HCANetworkModule = new HCANetworkModule();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mWelfareButton = (Button)findViewById(R.id.wellfare);
        mLiteraryButton = (Button)findViewById(R.id.literary);
        mDormButton = (Button)findViewById(R.id.dorm);

        mWelfareButton.setSelected(true);

        mWelfareButton.setOnClickListener(new buttonClickListener());
        mLiteraryButton.setOnClickListener(new buttonClickListener());
        mDormButton.setOnClickListener(new buttonClickListener());
    }*/


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment {
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLinearLayoutManager;
        private RCAdapter mRCAdapter;
        private TextView mDateTextView;
        private List<CardInfo> mCardInfoList;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
            mCardInfoList = new ArrayList<>();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            //2016.02.25, Aev Oh, getMenus: 서버로 부터 데이터를 받아옴.
            s_HCANetworkModule.getMenus(fragment, s_selected_cafeteria, sectionNumber - 1);
            //s_HCANetworkModule.getData(fragment, s_selected_cafeteria, sectionNumber - 1);

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

            mDateTextView = (TextView)rootView.findViewById(R.id.date);
            mDateTextView.setText("11/" + String.valueOf(getArguments().getInt(ARG_SECTION_NUMBER)));

            mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mRCAdapter = new RCAdapter(mCardInfoList);

            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.cardList);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mRCAdapter);

            return rootView;
        }

        public void addCard(String title, String time, String body){
            CardInfo cardInfo = new CardInfo(title, time, body);
            mCardInfoList.add(cardInfo);
        }

        public void clearData(){
            mCardInfoList.clear();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<PlaceholderFragment> fragmentList;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            android.support.v4.app.Fragment fragment = PlaceholderFragment.newInstance(position + 1);
            fragmentList.add((PlaceholderFragment) fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "월요일";
                case 1:
                    return "화요일";
                case 2:
                    return "수요일";
                case 3:
                    return "목요일";
                case 4:
                    return "금요일";
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        public void refresh(){
            for(int i = 0; i < fragmentList.size(); i++) {
                PlaceholderFragment fragment = fragmentList.get(i);
                fragment.clearData();
                s_HCANetworkModule.getData(fragment, s_selected_cafeteria, i);
            }
            super.notifyDataSetChanged();
        }
    }
    /**
     * Adapter recyclerView connecting cardView
     */
    public static class CardInfo{
        protected String title;
        protected String time;
        protected String body;

        public CardInfo(String title, String time, String body){
            this.title = title;
            this.time = time;
            this.body = body;
        }
    }

    public static class RCAdapter extends RecyclerView.Adapter<RCAdapter.CarteCardViewHolder>{
        private List<CardInfo> mDataList;

        public RCAdapter(List<CardInfo> dataList){
            mDataList = dataList;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public CarteCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.card_layout, parent, false);

            return new CarteCardViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CarteCardViewHolder holder, int position) {
            CardInfo cardInfo = mDataList.get(position);
            holder.mTitle.setText(cardInfo.title);
            holder.mTime.setText(cardInfo.time);
            holder.mBody.setText(cardInfo.body);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public class CarteCardViewHolder extends RecyclerView.ViewHolder{
            protected TextView mTitle;
            protected TextView mTime;
            protected TextView mBody;

            public CarteCardViewHolder(View v){
                super(v);
                mTitle = (TextView) v.findViewById(R.id.title);
                mTime = (TextView) v.findViewById(R.id.time);
                mBody = (TextView) v.findViewById(R.id.body);
            }

        }
    }

    public class buttonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.isSelected())
                return;

            v.setSelected(true);

            switch(v.getId()){
                case R.id.wellfare:
                    s_selected_cafeteria = 0;
                    mLiteraryButton.setSelected(false);
                    mDormButton.setSelected(false);
                    break;
                case R.id.literary:
                    s_selected_cafeteria = 1;
                    mWelfareButton.setSelected(false);
                    mDormButton.setSelected(false);
                    break;
                case R.id.dorm:
                    s_selected_cafeteria = 2;
                    mWelfareButton.setSelected(false);
                    mLiteraryButton.setSelected(false);
                    break;
            }

            mSectionsPagerAdapter.refresh();
        }
    }
}
