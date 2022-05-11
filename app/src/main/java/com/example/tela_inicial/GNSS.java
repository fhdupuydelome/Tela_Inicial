package com.example.tela_inicial;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.location.GnssStatus;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

    public class GNSS extends View implements View.OnClickListener {
        private GnssStatus newStatus;
        private int r;
        private int height, width;

        public GNSS(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public void onSatelliteStatusChanged(GnssStatus status) {
            newStatus = status;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // coletando informações do tamanho tela de desenho
            width = getMeasuredWidth();
            height = getMeasuredHeight();

            // definindo o raio da esfera celeste
            if (width < height)
                r = (int) (width / 2 * 0.9);
            else
                r = (int) (height / 2 * 0.9);

            // configurando o pincel para desenhar a projeção da esfera celeste
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.rgb(48, 38, 217));

            Paint painSats = new Paint();
            painSats.setStyle(Paint.Style.STROKE);
            painSats.setColor(Color.argb(255, 255, 0, 0));


            Paint dashPaint = new Paint();
            dashPaint.setARGB(150, 0, 255, 0);
            dashPaint.setStyle(Paint.Style.STROKE);
            dashPaint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));

            Paint circlePain = new Paint();
            circlePain.setStyle(Paint.Style.STROKE);
            circlePain.setColor(Color.argb(255, 255, 0, 0));

            // desenhando círculos concêntricos
            int radius = r;

            canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);
            radius = (int) (radius * Math.cos(Math.toRadians(35)));
            canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);
            radius = (int) (radius * Math.cos(Math.toRadians(45)));
            canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);
            radius = (int) (radius * Math.cos(Math.toRadians(55)));
            canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);

            //desenhando os eixos
            canvas.drawLine(computeXc(0), computeYc(-r), computeXc(0), computeYc(r), paint);
            canvas.drawLine(computeXc(-r), computeYc(0), computeXc(r), computeYc(0), paint);
            canvas.drawLine(
                    computeXc(-r * Math.cos(Math.toRadians(45))),
                    computeYc(r * Math.sin(Math.toRadians(45))),
                    computeXc(r * Math.cos(Math.toRadians(45))),
                    computeYc(-r * Math.sin(Math.toRadians(45))),
                    paint);

            canvas.drawLine(
                    computeXc(-r * Math.cos(Math.toRadians(45))),
                    computeYc(-r * Math.sin(Math.toRadians(45))),
                    computeXc(r * Math.cos(Math.toRadians(45))),
                    computeYc(r * Math.sin(Math.toRadians(45))),
                    paint);

            // configura o pincel para desenhar os satélites
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.FILL);

            // desenhando os satélites
            if (newStatus != null) {
                for (int i = 0; i < newStatus.getSatelliteCount(); i++) {
                    float az = newStatus.getAzimuthDegrees(i);
                    float el = newStatus.getElevationDegrees(i);
                    float x = (float) (r * Math.cos(Math.toRadians(el)) * Math.sin(Math.toRadians(az)));
                    float y = (float) (r * Math.cos(Math.toRadians(el)) * Math.cos(Math.toRadians(az)));
                    painSats.setTextAlign(Paint.Align.CENTER);
                    painSats.setTextSize(50);


                    circlePain.setTextAlign(Paint.Align.CENTER);
                    circlePain.setTextSize(35);
                    circlePain.getTextAlign();

                    circlePain = setCircleCollor(circlePain, newStatus.getConstellationType(i));
                    canvas.drawCircle(computeXc(x) - 1, computeYc(y) - 10, 30, circlePain);
                    String satID = newStatus.getSvid(i) + "";
                    canvas.drawText(satID, computeXc(x), computeYc(y), circlePain);
                }
            }
        }

        private int computeXc(double x) {
            return (int) (x + width / 2);
        }

        private int computeYc(double y) {
            return (int) (-y + height / 2);
        }

        private Paint setCircleCollor(Paint circle, int id) {

            switch (id) {
                case GnssStatus.CONSTELLATION_GPS:
                    circle.setColor(Color.argb(255, 255, 255, 0));
                    break;
                case GnssStatus.CONSTELLATION_BEIDOU:
                    circle.setColor(Color.argb(255, 0, 255, 255));
                    break;
                case GnssStatus.CONSTELLATION_GLONASS:
                    circle.setColor(Color.argb(255, 255, 0, 0));
                    break;
                case GnssStatus.CONSTELLATION_GALILEO:
                    circle.setColor(Color.argb(255, 255, 0, 255));
                    break;
            }
            return circle;
        }

        @Override
        public void onClick(View v) {
        }
    }


