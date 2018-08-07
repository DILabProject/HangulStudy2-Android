package kr.ac.skuniv.di.hangulstudy;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.skuniv.di.hangulstudy.VO.StudyVo;
import kr.ac.skuniv.di.hangulstudy.VO.TestVO;

public class ExpandableListviewAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private ArrayList<TestVO> studyVO;
    private LayoutInflater inflater;

    public ExpandableListviewAdapter(Context mContext, ArrayList<TestVO> studyVO) {
        this.mContext = mContext;
        this.studyVO = studyVO;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return studyVO.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return studyVO.get(groupPosition).getWord().size();
    }

    //get position
    @Override
    public Object getGroup(int groupPosition) {
        return studyVO.get(groupPosition);
    }

    //this is where we get the information of player
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return studyVO.get(groupPosition).getWord().get(childPosition);
    }

    //position ID
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    //where to get player's id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //get parent row
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.parent_list, null);
        }

        //get position
        TestVO testVO = (TestVO)getGroup(groupPosition);

        //set positionName
        String LearningDay = testVO.getDay();

        TextView textView = (TextView) convertView.findViewById(R.id.study_day);
        textView.setText(LearningDay);

//        ImageView imageView = (ImageView) convertView.findViewById(R.id.indicator);
//        if(isExpanded){
//            imageView.setImageResource(R.drawable.arrowdown);
//        } else {
//            imageView.setImageResource(R.drawable.arrowup);
//        }

        convertView.setBackgroundColor(Color.LTGRAY);
        return convertView;
    }


    //get child_list.xml (View)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //inflate the layout
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, null);
        }

        String child = (String) getChild(groupPosition, childPosition);

        //set ChildItem
        TextView index = (TextView) convertView.findViewById(R.id.index);
        TextView word = (TextView) convertView.findViewById(R.id.word);
        ImageView check = (ImageView) convertView.findViewById(R.id.check);

        index.setText(child);
        index.setText(studyVO.get(groupPosition).getWord().get(childPosition));

//        //get position name
//        String positionName = (String) getGroup(groupPosition).toString();
//        if (positionName == "pitcher") {
//            if (child == "고원준") {
//                img.setImageResource(R.drawable.wonjun_ko);
//            } else if (child == "Brooks Raley") {
//                img.setImageResource(R.drawable.books_raley);
//            } else if (child == "박세웅") {
//                img.setImageResource(R.drawable.sewoong_park);
//            }
//        } else if (positionName == "infield") {
//            if (child == "문규현") {
//                img.setImageResource(R.drawable.kyuhyun_moon);
//            } else if (child == "박종윤") {
//                img.setImageResource(R.drawable.jongyun_park);
//            }
//        } else if (positionName == "catcher") {
//            if (child == "강민호") {
//                img.setImageResource(R.drawable.minho_kang);
//            } else if (child == "안중열") {
//                img.setImageResource(R.drawable.jungyul_ahn);
//            }
//        } else if (positionName == "outfield") {
//            if (child == "Jim Adduci") {
//                img.setImageResource(R.drawable.jim_adduci);
//            } else if (child == "손아섭") {
//                img.setImageResource(R.drawable.ahsup_son);
//            }
//        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
