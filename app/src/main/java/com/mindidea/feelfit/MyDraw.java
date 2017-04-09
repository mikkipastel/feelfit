package com.mindidea.feelfit;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyDraw extends View{
	Canvas c;
	Paint paint;
	double[] cal = new double[5];
	int screenWidth;
	int screenHeight;
	
	public MyDraw(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		//c = new Canvas();
	}
	
	protected void allCal(double cal1, double cal2, double cal3, double cal4, double cal5)
	{
		cal[0] = cal1;
		cal[1] = cal2;
		cal[2] = cal3;
		cal[3] = cal4;
		cal[4] = cal5;
	}
	
	protected double maxCal()
	{
		double max = 0;
		int i;
		for(i = 0; i < 5; i++){
			System.out.println(cal[i]);
			if (cal[i] > max) max = cal[i];
		}
		System.out.println(max);
		return max;
	}
	
	protected int getWidth(int screenWidth){
		System.out.println(screenWidth);
		return screenWidth;
	}
	
    protected int getHeight(int screenHeight){
    	System.out.println(screenHeight);
		return screenHeight;
	}
	
	protected void onDraw(Canvas c){
		//value
		float screenWidth = getWidth();
		float screenHeight = getHeight();
		/*int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();*/
		float barWidth = 20;
		int i = 0;
		double maxCal = maxCal();
		//ondraw
		super.onDraw(c);
		c.drawPaint(paint);
		paint.setAntiAlias(true);
		
		//defind border value
		float maxY = (screenHeight*7)/10;
		float minY = (screenHeight*3)/10;
		float maxX = (screenWidth*8)/9;
		float minX = (screenWidth*2)/9;
		
		//test
		/*double Hradio;
		for(i = 0; i < 5; i++){
			Hradio = screenHeight/maxCal;
			c.drawRect(0, 0, barWidth*(i+1), (float) (Hradio*cal[i]), paint);
			//c.drawRect(0, screenHeight/5, screenWidth/5, (float) (cal[i]*(screenHeight-Hradio)), paint);
			paint.setColor(0xFF000000 + (255<<16) + (127<<8) + 0); //orange
		}*/
		
		/*c.drawRect(0, 0, 20, 80, paint);
		paint.setColor(Color.RED);
		c.drawRect(30, 0, 20, 130, paint); //border
		paint.setColor(Color.BLUE);
		c.drawRect(60, 0, 20, 200, paint); //border
		paint.setColor(Color.GREEN);
		c.drawRect(240, 0, 20, 20, paint); //border
		paint.setColor(Color.YELLOW);  
		c.drawRect(0, 360, 20, 20, paint); //border
		paint.setColor(Color.CYAN); */
		/*c.drawRect(0, 0, screenWidth/2, screenHeight/2,  paint); //border
		paint.setColor(Color.GREEN);
		c.drawRect(screenWidth/2, screenHeight/2, 0, 0, paint); //border
		paint.setColor(Color.YELLOW);        */
		
		//draw x
		c.drawRect(minX-10, maxY-2, maxX+10, maxY+2, paint);
		//draw y
		c.drawRect(minX-2, minY, maxX+2, maxY, paint);
		for(i = 0; i < 5; i++){
			c.drawRect(minX-5, minY + (screenHeight*i*4)/40-1, 
					minX + 5, minY + (screenHeight*i*4)/40+1 , paint);
		}
		//draw data bar
		for(i = 0; i < 5; i++){
			int R, G, B;
			R = 255;
			G = 127;
			B = 0;
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(0xFF000000 + (R<<16) + (G<<8) + B); //orange
			c.drawRect( ((screenWidth*(3+i))/9) - (barWidth/2) , 
					(float) (maxY - (((maxY - minY) * cal[i])/maxCal)), 
					(screenWidth*(3+i)/9) + (barWidth/2),
					maxY, paint);
			paint.setStyle(Paint.Style.STROKE);
			//paint.setColor(Color.RED);
			c.drawRect( ((screenWidth*(3+i))/9)-(barWidth/2), 
			        (float) (maxY - ((minY * cal[i]) /maxCal)),
			        ((screenWidth*(3+i))/9) + (barWidth/2),
			        maxY ,paint);
		}			
	}
}
