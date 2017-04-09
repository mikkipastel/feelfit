package com.mindidea.feelfit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;

public class timeLabel extends View{
	private String[] timeResult;
	private Paint paint;

	public timeLabel(Context context, String timeResult[]) {
		super(context);
		if (timeResult == null) this.timeResult = new String[0];
		else this.timeResult = timeResult;
		
		paint = new Paint();
	}
	
	protected void onDraw(Canvas canvas){
		float border = 20;
		float horstart = border * 2 + 12;
		float height = getHeight();
		float width = getWidth() - 15;
		float colwidth = (width - (2 * border)) / timeResult.length;
		
		double width_d = getWidth();
		if (width_d < 460){ //from y
			paint.setTextSize(12.0f); //8
		} else if (width_d >= 460 && width_d <= 480){ //from s duo 2 (4 inch) 480 -> 464
			paint.setTextSize(14.0f); //12
		} else if (width_d > 480 && width_d <= 710){ //from oppo 720 -> 704
			paint.setTextSize(16.0f); //13
		} else if (width_d > 710 && width_d <= 800){ //from oppo 720 -> 704
			paint.setTextSize(18.0f); //14
		} else {
			paint.setTextSize(20.0f); //16
		}
		
		for (int i = 0; i < timeResult.length; i++){
			paint.setColor(Color.DKGRAY);
			paint.setTextAlign(Align.CENTER);
			paint.setFakeBoldText(true);
		    //canvas.rotate(45.0f);
			//canvas.drawText(timeResult[i], i * colwidth + horstart + colwidth/2 - 0.5f, height - border, paint);
		    canvas.drawText(timeResult[i], i * colwidth + horstart + colwidth/2 - 0.5f, height/2, paint);
		}
		
		
	}

}
