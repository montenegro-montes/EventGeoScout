package es.uma.mapas;

import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import es.uma.mapas.R;

public class GeneticPlotActivity extends AppCompatActivity {


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.genetic_plot);
        ArrayList<Double> fitnessList = (ArrayList) getIntent().getSerializableExtra("fitness");
        ArrayList<Double> STDList = (ArrayList) getIntent().getSerializableExtra("STD");
        GraphView graphFitness = (GraphView) findViewById(R.id.graphFitness);
        GraphView graphElitism = (GraphView) findViewById(R.id.graphElitism);
        DataPoint[] data = new DataPoint[fitnessList.size()];
        for (int i = 0; i < fitnessList.size(); i++) {
            data[i] = new DataPoint((double) i, fitnessList.get(i).doubleValue());
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
        graphFitness.getViewport().setXAxisBoundsManual(true);
        graphFitness.getViewport().setMinX(0.0d);
        graphFitness.getViewport().setMaxX((double) (fitnessList.size() - 1));
        graphFitness.getViewport().setScalable(true);
        graphFitness.getViewport().setScalableY(true);
        graphFitness.addSeries(series);
        DataPoint[] dataSTD = new DataPoint[STDList.size()];
        for (int i2 = 0; i2 < STDList.size(); i2++) {
            dataSTD[i2] = new DataPoint((double) i2, STDList.get(i2).doubleValue());
        }
        LineGraphSeries<DataPoint> seriesSTD = new LineGraphSeries<>(dataSTD);
        graphElitism.getViewport().setXAxisBoundsManual(true);
        graphElitism.getViewport().setMinX(0.0d);
        graphElitism.getViewport().setMaxX((double) (STDList.size() - 1));
        graphElitism.getViewport().setScalable(true);
        graphElitism.getViewport().setScalableY(true);
        graphElitism.addSeries(seriesSTD);
    }
}