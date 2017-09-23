package com.siem.siemmedicos.ui.custom;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.CustomPagePacienteBinding;
import com.siem.siemmedicos.interfaces.PossitiveChangeVisibilityButtonListener;
import com.siem.siemmedicos.model.app.Paciente;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 4/9/17.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private Activity mActivity;
    private List<Paciente> mList;
    private CustomPagePacienteBinding mBinding;
    private WeakReference<PossitiveChangeVisibilityButtonListener> mListener;

    public CustomPagerAdapter(Activity activity, PossitiveChangeVisibilityButtonListener listener) {
        mActivity = activity;
        mList = new ArrayList<>();
        Paciente paciente = new Paciente();
        mList.add(paciente);
        mListener = new WeakReference<>(listener);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        final Paciente paciente = mList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.custom_page_paciente, collection, false);
        ViewGroup layout = (ViewGroup) mBinding.getRoot();
        collection.addView(layout);
        bindData(paciente);

        mBinding.checkboxTrasladado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                paciente.setTrasladado(isChecked);
            }
        });

        mBinding.edittextNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setNombre(editable.toString());
            }
        });

        mBinding.edittextApellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setApellido(editable.toString());
            }
        });

        mBinding.edittextDni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setDni(Integer.parseInt(editable.toString()));
            }
        });

        mBinding.edittextEdad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setEdad(Integer.parseInt(editable.toString()));
            }
        });

        mBinding.edittextDiagnostico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                paciente.setDiagnostico(editable.toString());
                if(mListener.get() != null) mListener.get().controlateSendButton();
            }
        });

        return layout;
    }

    private void bindData(Paciente paciente) {
        mBinding.checkboxTrasladado.setChecked(paciente.isTrasladado());
        mBinding.edittextDiagnostico.setText(paciente.getDiagnostico());
        mBinding.edittextNombre.setText(paciente.getNombre());
        mBinding.edittextApellido.setText(paciente.getApellido());

        if(paciente.getDni() != null)
            mBinding.edittextDni.setText(paciente.getDni());

        if(paciente.getEdad() != null)
            mBinding.edittextEdad.setText(paciente.getEdad());
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

    public void setListener(PossitiveChangeVisibilityButtonListener listener){
        mListener = new WeakReference<>(listener);
    }

    public void addPaciente(Paciente paciente){
        mList.add(paciente);
        notifyDataSetChanged();
    }

    public void removePaciente(int position){
        mList.remove(position);
        notifyDataSetChanged();
    }

    public List<Paciente> getListData(){
        return mList;
    }

    public boolean haveData(){
        for (Paciente paciente : mList) {
            if(paciente.getDiagnostico() == null ||
                    paciente.getDiagnostico().isEmpty())
                return false;
        }
        return true;
    }

}
