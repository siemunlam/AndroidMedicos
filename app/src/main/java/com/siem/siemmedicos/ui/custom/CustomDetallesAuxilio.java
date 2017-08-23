package com.siem.siemmedicos.ui.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.model.app.Auxilio;

/**
 * Created by Lucas on 21/8/17.
 */

public class CustomDetallesAuxilio extends RelativeLayout {

    private Context mContext;
    private Typeface mTypefaceBold;
    private AppCompatImageView mIconAuxilio;
    private AppCompatTextView mTextviewDescriptionAuxilio;
    private AppCompatTextView mTextviewDireccion;

    public CustomDetallesAuxilio(Context context) {
        super(context);
        initialice(context, null);
    }

    public CustomDetallesAuxilio(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialice(context, attrs);
    }

    private void initialice(Context context, AttributeSet attrs) {
        inflate(context, R.layout.custom_detalles_auxilio, this);
        mContext = context;
        mIconAuxilio = (AppCompatImageView)findViewById(R.id.iconAuxilio);
        mTextviewDescriptionAuxilio = (AppCompatTextView)findViewById(R.id.textviewDescriptionAuxilio);
        mTextviewDireccion = (AppCompatTextView)findViewById(R.id.textviewDireccion);
        mTypefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/rounded_elegance.ttf");
        mTextviewDescriptionAuxilio.setTypeface(mTypefaceBold);
        mTextviewDireccion.setTypeface(mTypefaceBold);

    }

    public void setColorFilter(int color){
        mIconAuxilio.setColorFilter(color);
    }

    public void setDireccion(String direccion){
        mTextviewDireccion.setText(direccion);
    }

    public void setDescriptionColor(String color){
        mTextviewDescriptionAuxilio.setText(mContext.getString(R.string.descripcionAuxilio, color));
    }

    public void setDatos(Auxilio auxilio) {
        setDireccion(auxilio.getDireccion());
        setDescriptionColor(auxilio.getColorDescripcion());
        try{
            setColorFilter(Color.parseColor(auxilio.getColorHexadecimal()));
        }catch(Exception e){

        }
    }
}
