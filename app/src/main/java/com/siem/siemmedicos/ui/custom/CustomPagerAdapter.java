package com.siem.siemmedicos.ui.custom;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
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

    private Activity mActivity;
    private List<Paciente> mList;

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
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.custom_page_paciente, collection, false);
        collection.addView(layout);
        AppCompatEditText edittextNombre = (AppCompatEditText)layout.findViewById(R.id.edittextNombre);
        AppCompatEditText edittextApellido = (AppCompatEditText)layout.findViewById(R.id.edittextApellido);
        AppCompatEditText edittextDni = (AppCompatEditText)layout.findViewById(R.id.edittextDni);
        AppCompatEditText edittextEdad = (AppCompatEditText)layout.findViewById(R.id.edittextEdad);
        AppCompatEditText edittextDiagnostico = (AppCompatEditText)layout.findViewById(R.id.edittextDiagnostico);

        edittextNombre.setText(paciente.getNombre());
        edittextApellido.setText(paciente.getApellido());
        edittextDni.setText(paciente.getDni());
        edittextEdad.setText(paciente.getEdad());
        edittextDiagnostico.setText(paciente.getDiagnostico());

        edittextNombre.addTextChangedListener(new TextWatcher() {
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
        });

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
