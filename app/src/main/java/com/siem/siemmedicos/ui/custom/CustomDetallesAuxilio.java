package com.siem.siemmedicos.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.model.app.Auxilio;
import com.siem.siemmedicos.utils.Constants;

/**
 * Created by Lucas on 21/8/17.
 */

public class CustomDetallesAuxilio extends RelativeLayout {

    private Context mContext;
    private Typeface mTypeface;
    private RelativeLayout mContainer;
    private AppCompatImageView mIconAuxilio;
    private AppCompatTextView mTextviewDescriptionAuxilio;
    private AppCompatTextView mTextviewDireccion;
    private AppCompatTextView mTextviewReferencia;

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
        mContainer = (RelativeLayout)findViewById(R.id.container);
        mIconAuxilio = (AppCompatImageView)findViewById(R.id.iconAuxilio);
        mTextviewDescriptionAuxilio = (AppCompatTextView)findViewById(R.id.textviewDescriptionAuxilio);
        mTextviewDireccion = (AppCompatTextView)findViewById(R.id.textviewDireccion);
        mTextviewReferencia = (AppCompatTextView)findViewById(R.id.textviewReferencia);
        mTypeface = Typeface.createFromAsset(context.getAssets(), Constants.PRIMARY_FONT);
        mTextviewDescriptionAuxilio.setTypeface(mTypeface);
        mTextviewDireccion.setTypeface(mTypeface);
        mTextviewReferencia.setTypeface(mTypeface);

        setAttrs(attrs);
    }

    private void setAttrs(AttributeSet attrs) {
        if(attrs != null){
            TypedArray typed = mContext.obtainStyledAttributes(attrs, R.styleable.customComponents);
            mContainer.setClickable(typed.getBoolean(R.styleable.customComponents_android_clickable, false));
            mTextviewDireccion.setMaxLines(typed.getInt(R.styleable.customComponents_android_maxLines, Integer.MAX_VALUE));
            typed.recycle();
        }
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

    public void setReferencia(String referencia){
        mTextviewReferencia.setText(referencia);
    }

    public void setDatos(Auxilio auxilio, boolean isReferenciaVisible) {
        setDireccion(auxilio.getDireccion());
        setDescriptionColor(auxilio.getColorDescripcion());
        if(isReferenciaVisible && !auxilio.getReferencia().isEmpty()){
            setReferencia(auxilio.getReferencia());
            mTextviewReferencia.setVisibility(VISIBLE);
        }else{
            mTextviewReferencia.setVisibility(GONE);
        }

        try{
            setColorFilter(Color.parseColor(auxilio.getColorHexadecimal()));
        }catch(Exception e){}
    }

    public void setOnClickListener(View.OnClickListener listener){
        mContainer.setOnClickListener(listener);
    }
}
