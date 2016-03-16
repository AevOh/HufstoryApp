package co.kr.hufstory.menu_fragment;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.kr.hufstory.R;

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
    public static int s_selected_cafeteria;
    public static HCANetworkModule s_HCANetworkModule;

    private Button mWelfareButton;
    private Button mLiteraryButton;
    private Button mDormButton;


    /*
    2016.02.26, Aev Oh, 메뉴 프레그먼트 생성자 호출 시, 식단표 정보를 모두 HTTP 통신으로 받아온 뒤 CoordinatorLayout 생성.
    - CoordinatorLayout 선언 이후 ~ mDormButton.setClickListener 부분까지 함수화함.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        s_selected_cafeteria = 0;
        FragmentActivity fragmentActivity = (FragmentActivity) super.getActivity();

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) inflater.inflate(R.layout.activity_menu, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentActivity.getSupportFragmentManager());

        s_HCANetworkModule = new HCANetworkModule(mSectionsPagerAdapter);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)coordinatorLayout.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout)coordinatorLayout.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mWelfareButton = (Button)coordinatorLayout.findViewById(R.id.wellfare);
        mLiteraryButton = (Button)coordinatorLayout.findViewById(R.id.literary);
        mDormButton = (Button)coordinatorLayout.findViewById(R.id.dorm);

        mWelfareButton.setSelected(true);

        mWelfareButton.setOnClickListener(new buttonClickListener());
        mLiteraryButton.setOnClickListener(new buttonClickListener());
        mDormButton.setOnClickListener(new buttonClickListener());

        return coordinatorLayout;
    }

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

    private int getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day){
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            default:
                return 0;
        }
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
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            s_HCANetworkModule.getData(fragment, s_selected_cafeteria, sectionNumber - 1);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

            mDateTextView = (TextView)rootView.findViewById(R.id.date);
            mDateTextView.setText(s_HCANetworkModule.getDate(getArguments().getInt(ARG_SECTION_NUMBER)));

            mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mRCAdapter = new RCAdapter(mCardInfoList);

            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.cardList);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mRCAdapter);

            return rootView;
        }

        public void addCard(String title, String time, String body, String cost){
            CardInfo cardInfo = new CardInfo(title, time, body, cost);
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
        protected String cost;
        protected String body;

        public CardInfo(String title, String time, String cost, String body){
            this.title = title;
            this.time = time;
            this.cost = cost;
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
            holder.mCost.setText(cardInfo.cost + "원");
            holder.mBody.setText(cardInfo.body);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public class CarteCardViewHolder extends RecyclerView.ViewHolder{
            protected TextView mTitle;
            protected TextView mTime;
            protected TextView mCost;
            protected TextView mBody;

            public CarteCardViewHolder(View v){
                super(v);
                mTitle = (TextView) v.findViewById(R.id.title);
                mTime = (TextView) v.findViewById(R.id.time);
                mCost = (TextView) v.findViewById(R.id.cost);
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
