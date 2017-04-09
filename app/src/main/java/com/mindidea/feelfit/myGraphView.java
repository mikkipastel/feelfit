package com.mindidea.feelfit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;

public class myGraphView extends View{
	public static boolean BAR = true;
	public static boolean LINE = false;
	
	private Paint paint;
	private float[] values;
	private String[] horlabels;
	private String[] verlabels;
	private String title;
	private boolean type;
	
	//float cal[] = {};
	//Canvas canvas_text = new Canvas();
	
	
	public myGraphView(Context context, float[] values, String title, String[] horlabels, String[] verlabels, boolean type) {
		super(context);
		if (values == null) values = new float[0];
		else this.values = values;
		
		if (title == null) title = "";
		else this.title = title;
		
		if (horlabels == null) this.horlabels = new String[0];
		else this.horlabels = horlabels;

        if (verlabels == null) this.verlabels = new String[0];
		else this.verlabels = verlabels;
		
        this.type = type;
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float border = 20;
		float horstart = border * 2 + 12;//+10
		float height = getHeight();
		float width = getWidth() - 15; //-1
		float max = getMax();
		float min = getMin();
		float diff = max - min;
		float graphheight = height - (2 * border);
		float graphwidth = width - (3 * border); //
		paint.setTextAlign(Align.LEFT);
		//paint.setTextAlign(Align.CENTER);
		paint.setFakeBoldText(true); //add
		
		//split date_time string
		/*String[] day_time = new String[5]; 
		String[] d_day = new String[5], t_time = new String[5];
		for (int i = 0; i < 5; i++){
			day_time[i] = "";
			d_day[i] = "";
			t_time[i] = "";
		}
		for (int i = 0; i < horlabels.length; i++){
			day_time = horlabels[i].split(" ");
			if (day_time.length == 2){
				d_day[i] = day_time[0];
				t_time[i] = day_time[1];
			}			
		}*/
		
		//Vertical : y
		/* int vers = verlabels.length - 1;
		   for (int i = 0; i < verlabels.length; i++) {
			paint.setColor(Color.DKGRAY);
			float y = ((graphheight / vers) * i) + border;
			canvas.drawLine(horstart, y, width, y, paint);
			paint.setColor(Color.WHITE);
			canvas.drawText(verlabels[i], 0, y, paint);
		}*/
		
		canvas.drawLine(horstart, graphheight + border, width, graphheight + border, paint);
		//canvas.drawRect(horstart, graphheight + border, horstart + 1, 0, paint);
		for (int i = 0; i <= 5; i++) {
			//paint.setColor(Color.DKGRAY);
			float y = ((graphheight / 5) * (5-i)) + border;
			canvas.drawLine(horstart - 2, y, horstart + 2, y, paint);
			paint.setColor(Color.DKGRAY);//WHITE
			//paint.setTextAlign(Align.RIGHT);
			//canvas.drawText( "" + (max * i ) / 5 , 0, y, paint);
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
			canvas.drawText( String.format("%.2f", (max * i ) / 5) , 0, y, paint);
		}

		//Horizon : x
		/*int hors = horlabels.length - 1;
		for (int i = 0; i < horlabels.length; i++) {
			paint.setColor(Color.DKGRAY);
			float x = ((graphwidth / hors) * i) + horstart;
			canvas.drawLine(x, height - border, x, border, paint);
			paint.setTextAlign(Align.CENTER);
			if (i == horlabels.length - 1) paint.setTextAlign(Align.RIGHT);
			if (i == 0) paint.setTextAlign(Align.LEFT);
			paint.setColor(Color.WHITE);
			canvas.drawText(horlabels[i], x, height - 4, paint);
		}*/
		canvas.drawLine(horstart, height - border, horstart, border, paint);
		//for (int i = 0; i < cal.length; i++) { // ##### get Cal[i]
		for (int i = 0; i < values.length; i++) { // ##### get Cal[i]
			paint.setColor(Color.DKGRAY);
			//float x = ((graphwidth / 5) * i ) + horstart;
			paint.setTextAlign(Align.CENTER);
			//canvas.drawLine(x, height - border, x, border, paint);
			//canvas.drawLine(horstart, height - border, horstart, border, paint);
			/*if (i == 0) //### first record
				paint.setTextAlign(Align.LEFT);
			//else if (i == cal.length - 1)
			else if (i == values.length - 1)
				//### last record
				paint.setTextAlign(Align.RIGHT);
			else
				paint.setTextAlign(Align.CENTER);*/
			paint.setColor(Color.DKGRAY);//WHITE
			double width_d = getWidth();
			//String width_str = Double.toString(width_d);
			//Log.d("get width layout", Double.toString(width_d));
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
			/*if (width < 460f){ //from y
				paint.setTextSize(8);
			} else if (width >= 460f && width < 750f){ //from s duo 2 (4 inch)
				paint.setTextSize(12);
			} else {
				paint.setTextSize(16);
			}*/			
			/*canvas_text.rotate(45);
			canvas_text.drawText(horlabels[i], x*1.15f, height - 4, paint);*/
			//date and time text
			//Log.d("horlabels_" + i, horlabels[i]);
			float colwidth = (width - (2 * border)) / values.length;
			paint.setTextAlign(Align.CENTER);
			/*canvas.drawText(d_day[i], i * colwidth + horstart + colwidth/2 - 0.5f, height * 1.02f - border, paint);
			canvas.drawText(t_time[i], i * colwidth + horstart + colwidth/2 - 0.5f, height * 1.04f - border, paint);*/
			//canvas.drawText(d_day[i], i * colwidth + horstart + colwidth/2 - 0.5f, height - border + 12, paint);
			//canvas.drawText(t_time[i], i * colwidth + horstart + colwidth/2 - 0.5f, height - border + 20, paint);
			canvas.drawText(horlabels[i], i * colwidth + horstart + colwidth/2 - 0.5f, height - border + 15.5f, paint);//16.5f
			
			//test
			//canvas.drawText("(0,0)", 0.f, 0.f, paint);
			//canvas.drawText("(0,0)", 10.f, 10.f, paint);
			//canvas.drawText("(x,0)", width, 0.f, paint);
			//canvas.drawText("(x,0)", width - 10.f, 10.f, paint);
			//canvas.drawText("(0,y)", 0.f, height, paint);
			//canvas.drawText("(0,y)", 10.f, height - 10.f, paint);
			//canvas.drawText("(x,y)", width, height, paint);
			//canvas.drawText("(x,y)", width - 10.f, height - 10.f, paint);
			//canvas.drawText("(x,x)", width, width, paint);
			//canvas.drawText("(y,y)", height, height, paint);
			
		}
		
		//title text
		paint.setTextAlign(Align.CENTER);
		double width_d = getWidth();
		if (width_d < 460){ //from y
			paint.setTextSize(14.0f); //8
		} else if (width_d >= 460 && width_d <= 480){ //from s duo 2 (4 inch) 480 -> 464
			paint.setTextSize(16.0f); //12
		} else if (width_d > 480 && width_d <= 710){ //from oppo 720 -> 704
			paint.setTextSize(18.0f); //13
		} else if (width_d > 710 && width_d <= 800){ //from oppo 720 -> 704
			paint.setTextSize(20.0f); //14
		} else {
			paint.setTextSize(22.0f); //16
		}
		//paint.setTextSize(16.0f);
		canvas.drawText(title, (graphwidth / 2) + horstart, border - 4, paint);
		//if (max != min) {
			paint.setColor(Color.LTGRAY);
			if (type == BAR) {
				float datalength = values.length;
				float colwidth = (width - (2 * border)) / datalength;
				for (int i = 0; i < values.length; i++) {
						float val = values[i] - min;
						float rat = val / diff;
						float h = graphheight * rat;
						
						//paint.setColor(Color.RED);
						canvas.drawRect((i * colwidth) + horstart + (colwidth/4) - 1, (border - h)
								+ graphheight, ((i * colwidth) + horstart)
								+ (colwidth - 1) - (colwidth/4) + 1, height - (border -  1), paint);
						paint.setColor(0xFF000000 + (255 << 16) + (127 << 8));
						canvas.drawRect((i * colwidth) + horstart + (colwidth/4) + 1, (border - h)
								+ graphheight, ((i * colwidth) + horstart)
								+ (colwidth - 1) - (colwidth/4) - 1, height - (border -  1), paint);

						/*canvas.drawRect((i * colwidth) + horstart, (border - h)
								+ graphheight, ((i * colwidth) + horstart)
								+ (colwidth - 1), height - (border -  1), paint);*/
				}
			} else {
				float datalength = values.length;
				float colwidth = (width - (2 * border)) / datalength;
				float halfcol = colwidth / 2;
				float lasth = 0;
				for (int i = 0; i < values.length; i++){
					float val = values[i] - min;
					float rat = val / diff;
					float h = graphheight * rat;
					if (i > 0){
						paint.setColor(0xFF000000 + (255 << 16) + (127 << 8));
						canvas.drawLine(((i - 1)* colwidth) + (horstart + 1)
							+ halfcol, (border - lasth) + graphheight,
							(i * colwidth) + (horstart + 1) + halfcol,
							(border - h) + graphheight, paint);
						lasth = h;
					}					
				}
			}
		//}
	}
	
	private float getMax() {
		float largest = Integer.MIN_VALUE;
		for (int i = 0; i < values.length; i++){
			if (values[i] > largest)
				largest = values[i];
		}
		return largest;
	}
			
	private float getMin() {
		float smallest = Integer.MAX_VALUE;
		for (int i = 0; i < values.length; i++){
			if (values[i] < smallest)
			smallest = values[i];	
		}
		return smallest;
	}
}
