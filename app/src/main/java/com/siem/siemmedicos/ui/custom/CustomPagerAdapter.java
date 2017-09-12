package com.siem.siemmedicos.ui.custom;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.CustomPagePacienteBinding;
import com.siem.siemmedicos.model.app.Paciente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 4/9/17.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private Activity mActivity;
    private List<Paciente> mList;
    private CustomPagePacienteBinding mBinding;

    public CustomPagerAdapter(Activity activity) {
        mActivity = activity;
        mList = new ArrayList<>();
        Paciente paciente = new Paciente();
        mList.add(paciente);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        final Paciente paciente = mList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.custom_page_paciente, collection, false);
        ViewGroup layout = (ViewGroup) mBinding.getRoot();
        collection.addView(layout);
        bindData(paciente);

        /*edittextNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setNombre(editable.toString());
            }
        });

        edittextApellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setApellido(editable.toString());
            }
        });

        edittextDni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setDni(editable.toString());
            }
        });

        edittextEdad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setEdad(editable.toString());
            }
        });

        edittextDiagnostico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setDiagnostico(editable.toString());
            }
        });*/

        return layout;
    }

    private void bindData(Paciente paciente) {
        mBinding.edittextNombre.setText(paciente.getNombre());
        mBinding.edittextApellido.setText(paciente.getApellido());
        mBinding.edittextDni.setText(paciente.getDni());
        mBinding.edittextEdad.setText(paciente.getEdad());
        mBinding.edittextDiagnostico.setText(paciente.getDiagnostico());
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

    public void addPaciente(Paciente paciente){
        mList.add(paciente);
        notifyDataSetChanged();
    }

    public void removePaciente(int position){
        mList.remove(position);
        notifyDataSetChanged();
    }

    public boolean haveData(){
        //TODO: Change
        return true;
    }

}
