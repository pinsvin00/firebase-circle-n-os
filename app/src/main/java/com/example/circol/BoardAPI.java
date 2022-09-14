package com.example.circol;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;


class BoardAPI {

    public MainActivity activity;
    public TextView notifier;
    public Context ctx;
    public GameHandler handler;
    public List<FlexboxLayout> rows;



    private Button getButton(int x, int y) {
        return (Button) this.rows.get(x).getChildAt(y);
    }


    public void markFieldAsMark(int x, int y, Mark mark) {
        Button btn = getButton(x,y);
        if(mark == Mark.CIRCLE) {
            notifier.setText("Ruch krzyzka");
            btn.setBackgroundResource(R.drawable.o);
        }
        else {
            notifier.setText("Ruch kolka");
            btn.setBackgroundResource(R.drawable.x);
        }
    }

    public void generate() {
        for(int i =  0; i < rows.size(); i++) {
            for (int j = 0 ; j < 3 ; j++) {

                Button b = new Button(this.ctx);
                b.setId( View.generateViewId() );
                b.setBackgroundResource(R.drawable.empty);

                int finalI = i;
                int finalJ = j;

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handler.handleFieldClick(finalI, finalJ);
                    }
                });

                this.rows.get(i).addView(b);
            }
        }
    }





}
