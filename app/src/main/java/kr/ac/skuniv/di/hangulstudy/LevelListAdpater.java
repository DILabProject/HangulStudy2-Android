package kr.ac.skuniv.di.hangulstudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Level;

import kr.ac.skuniv.di.hangulstudy.VO.LevelListVO;

public class LevelListAdpater extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<LevelListVO> listViewItemList = new ArrayList<LevelListVO>() ;

    // ListViewAdapter의 생성자
    public LevelListAdpater() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴.
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴.
    @Override
    public long getItemId(int position) {
//        return position ;
        return 0;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public LevelListVO getItem(int position) {
        return listViewItemList.get(position);
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴.
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        LevelListAdpater.ViewHolder holder;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.level_listview_item, parent, false);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            TextView level = (TextView) convertView.findViewById(R.id.level) ;
            TextView achievement = (TextView) convertView.findViewById(R.id.achievement) ;

            holder = new LevelListAdpater.ViewHolder();
            holder.level = level;
            holder.achievement = achievement;

            convertView.setTag(holder);
        }
        else{
            holder = (LevelListAdpater.ViewHolder)convertView.getTag();
        }

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        LevelListVO listViewItem = listViewItemList.get(position);


        if(listViewItem != null){
            // 아이템 내 각 위젯에 데이터 반영

            LevelListAdpater.ViewHolder viewHolder = (LevelListAdpater.ViewHolder) convertView.getTag();
            viewHolder.level.setText(String.valueOf(listViewItem.getDay()));

            int word_count = listViewItem.getWord_count();
            int check_count = listViewItem.getCheck_count();

            int percent = (int)( (double)check_count / (double)word_count * 100.0);

            viewHolder.achievement.setText(String.valueOf(percent) + "%");
        }


        return convertView;
    }

    public class ViewHolder {
        public TextView level;
        public TextView achievement;

    }

    public void addItem(LevelListVO levelListVO) {

        listViewItemList.add(levelListVO);
    }
}
