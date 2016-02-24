package test.yuri.com.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by YURI on 2016-02-21.
 */


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> mExpandListGroup;
    private HashMap<String, List<String>> mExpandListChild;

    public ExpandableListAdapter(Context context, List<String> listGroup, HashMap<String, List<String>> listChild ){
        this.mContext=context;
        this.mExpandListGroup=listGroup;
        this.mExpandListChild=listChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition){
        return  this.mExpandListChild.get(this.mExpandListGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId (int groupPosition, int childPosition){
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView ==null){
            LayoutInflater mInflater = (LayoutInflater) this.mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView=mInflater.inflate(R.layout.expand_item,null,false);
        }

        TextView mTextViewChild =(TextView) convertView.findViewById(R.id.groupItem);
        mTextViewChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition){
        return this.mExpandListChild.get(this.mExpandListGroup.get(groupPosition)).size();

    }

    @Override
    public Object getGroup(int groupPosition){
        return this.mExpandListGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount(){
        return this.mExpandListGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        String headerText = (String) getGroup(groupPosition);
        if(convertView==null){
            LayoutInflater mInflater=(LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.expand_group,null,false);
        }

        /*
        if (isExpanded) {
            groupHolder.img.setImageResource(R.drawable.group_down);
        } else {
            groupHolder.img.setImageResource(R.drawable.group_up);
        } customize indicator.. or xml and using Display Metrics  */

        TextView mTextViewHead = (TextView) convertView.findViewById(R.id.groupTitle);
        mTextViewHead.setText(headerText);

        return convertView;
    }

    @Override
    public boolean hasStableIds(){
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition){
        return true;
    }

}
