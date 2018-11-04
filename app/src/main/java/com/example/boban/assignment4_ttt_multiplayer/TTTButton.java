package com.example.boban.assignment4_ttt_multiplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


public class TTTButton extends AppCompatButton implements Observer{
	int index;
	Context context;

	public TTTButton(int i, Context c){
        super(c);
        index = i;
        context = c;
	}

    public TTTButton(Context C, AttributeSet attributeSet){
	    super(C, attributeSet);
    }

	public void update(String s) {
        this.setText(s);
        if (s != null) {
            if (!s.isEmpty()) {
                int resID = getResources().getIdentifier(s, "drawable", context.getPackageName());
                Drawable drawablePic = getResources().getDrawable(resID);
                this.setBackground(drawablePic);
            } else {
                this.setBackgroundResource(android.R.drawable.btn_default);
            }
        }

    }

    public int getIndex() {
		return index;
	}

    public void setIndex(int index) {
        this.index = index;
    }
    public void setContext(Context context){
	    this.context = context;
    }
}
