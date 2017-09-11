package com.siem.siemmedicos.ui.custom;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.model.app.Paciente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 4/9/17.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Paciente> mList;

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        Paciente paciente = new Paciente();
        mList.add(paciente);
    }

    public void addPaciente(Paciente paciente){
        mList.add(paciente);
        notifyDataSetChanged();
    }

    public void removePaciente(int position){
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
       // ModelObject modelObject = mList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.custom_page_paciente, collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
