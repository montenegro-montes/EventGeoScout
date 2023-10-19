package ClientFerature;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;


public class Plot extends JDialog {

	List <XYSeries> seriesList = new ArrayList<XYSeries>(); 
	String m_title;
	
    public Plot(final String title) {
        
    	m_title=title;
    	setLocation(900, 100);
        
    }
    
    public void paint(){
    
    	
    	final XYSeriesCollection data = new XYSeriesCollection();
        
    	for (int i=0;i<seriesList.size();i++){
    		data.addSeries(seriesList.get(i));
    	}
    	
        final JFreeChart chart = ChartFactory.createXYLineChart(
            m_title+" record",
            "Generations", 
            m_title, 
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 270));
        add(chartPanel);
        
        
        
    }
        
        
    
    public void addData(double [][]valores,String funcionLabel) {
    	int i=valores.length;
     	int j=valores[0].length;
         
     	 XYSeries series = new XYSeries(funcionLabel);

         for (int indexi=0;indexi<i;indexi++){
     		for (int indexj=0;indexj<j;indexj++){
     			
     			series.add(indexj,valores[indexi][indexj]);
     		}
     	}
        
         seriesList.add(series);
   
    }
   
    public void addData(double []valores,String funcionLabel) {
    	int i=valores.length;
    
     	 XYSeries series = new XYSeries(funcionLabel);

         for (int indexi=0;indexi<i;indexi++){
     			series.add(indexi,valores[indexi]);
     	}
        
         seriesList.add(series);
   
    }
    
    
    public void addData(List <Double> valores,String funcionLabel) {
     	int i=valores.size();
     
      	 XYSeries series = new XYSeries(funcionLabel);

          for (int indexi=0;indexi<i;indexi++){
      			series.add(indexi,valores.get(indexi));
      	}
         
          seriesList.add(series);
          this.pack();
          this.repaint();
     }
   
    
    public void closing(){
    	this.dispose();
    }
  /*  public static void main(final String[] args) {

    	int i=10,j=1;
    	double [][] valores = new double[i][j];
    	
    	for (int indexj=0;indexj<j;indexj++){
    		for (int indexi=0;indexi<i;indexi++){
    			
    			valores[indexi][indexj]=i*indexi;
    		}
    	}
    	
        final Plot demo = new Plot("Fitness");
        demo.addData(valores,"Sin Recodif");
        demo.paint();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }*/

}