package es.uma.mapas;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import simpleGa.Genetico;
import simpleGa.Individual;
import simpleGa.Problem;

public class GeneticAlgActivity extends AppCompatActivity {

    Spinner problemSpinner;
    TextView log,solutionMsg;

    String[] problemas = {"cap71", "cap72", "cap73", "cap74",
            "cap101", "cap102", "cap103", "cap104",
            "cap131", "cap132", "cap133", "cap134",
            "capa", "capab", "capac"};

    double[] soluciones = {932615.750, 977799.400, 1010641.450, 1034976.975,
            796648.438, 854704.200, 893782.113, 928941.750,
            793439.563, 851495.325, 893076.713, 928941.750,
            17156454.478, 12979071.58, 11505594.3291};

    ListView genetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genetic_alg);

        problemSpinner = (Spinner) findViewById(R.id.spinnerProblem);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, problemas);
        problemSpinner.setAdapter(adapter);

        genetic = (ListView) findViewById(R.id.listViewClient);


        log = (TextView) findViewById(R.id.editTextLog);
        log.setMovementMethod(new ScrollingMovementMethod());

        solutionMsg = (TextView) findViewById(R.id.textViewSolutionMsg);
    }


    public void onClick(View v) {


        final int problemSelected =    problemSpinner.getSelectedItemPosition();

        new GeneticTask().execute(problemSelected);


    }

    private class GeneticTask extends AsyncTask<Integer, Void, Void>{
        Problem problemCurrent = null;
        Genetico genProblemBasic;
        boolean [] solution;
        String    [] asignS;
        long timeConsumed;
        List<Double> fitnessHistory;
        int generationCountSol;

        protected Void doInBackground(Integer... problemSelected) {

            int id = getResources().getIdentifier(problemas[problemSelected[0]], "raw", getPackageName());
            InputStream file= getResources().openRawResource(id);


            try {
                problemCurrent = new Problem(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            double solucion=soluciones[problemSelected[0]];
            problemCurrent.setFitnesSolution(solucion);
            //problemCurrent.setSeed(randomSeed); //Cambia semilla y resetea el Generador de numeros aleatorios, !!! Importante


            int _poblacion = problemCurrent.getNumFacilities()*2, _generaciones = problemCurrent.getNumClients()*100;
            problemCurrent.setLog(log);

            genProblemBasic= new Genetico (_poblacion,_generaciones,problemCurrent);
            genProblemBasic.run();

            Individual sol			 		= genProblemBasic.getSolution();
            timeConsumed 		 		= genProblemBasic.getTimeConsumed();
            fitnessHistory 	= genProblemBasic.getHistoryFitness();
            generationCountSol	 		= genProblemBasic.getGenerationCount();

            problemCurrent.setGeneticParams(_poblacion, _generaciones);
            problemCurrent.setFindSolution(sol);
            problemCurrent.setTimeConsumed(timeConsumed);
            problemCurrent.setHisttoryFitness(fitnessHistory);
            problemCurrent.setHisttoryFitnessEliStd(genProblemBasic.getHistorySTDFitness());
            problemCurrent.setGenerationCount(generationCountSol);


            solution = genProblemBasic.getSolutionVector();
            int []     asign    = problemCurrent.clientAsign();
            asignS              = new String [problemCurrent.getNumClients()];

            for (int i=0;i<asign.length;i++) {
                asignS [i] = new String();
            }

            for (int i=0;i<asign.length;i++){
                String value =  asignS[asign[i]];

                if (value.length()==0) value = "C"+i;
                else				   value = value+",C"+i;

                asignS[asign[i]]=value;
            }

            return null;
        }

        protected void onProgressUpdate(Void... voids) {
        }

        protected void onPostExecute(Void voids) {
            problemCurrent.printSolution(false);
            genetic.setAdapter(new ListAdapter(getApplicationContext(),solution.length,solution,asignS));

            String msg = new String();

            if (problemCurrent.isSolutionFound()){
                msg = "Found";
            }
            else{
                msg = "Not Found";
            }
            msg =   msg.concat(", fitness value " + fitnessHistory.get(generationCountSol));
            msg =   msg.concat(" took " + timeConsumed + " milliseconds " + " and " + generationCountSol + " generations.");
            solutionMsg.setText(msg);

            }
    }


    private class ListAdapter extends BaseAdapter {
        private Context mContext;
        private String[] mDes;
        int length      =   0;
        private boolean []  _status;
        String [] _asignS;

        public ListAdapter(Context c,int nfacility,boolean []status,String [] asignS) { //Constructor
            mContext    = c;
            length      = nfacility;
            mDes        = new String[nfacility];

            for (int i=0;i<nfacility;i++){
                mDes [i] = new String("F"+i);
            }
            _status = status;
            _asignS = asignS;
        }

        public int getCount() {
            return length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup
                parent) {
            View gridElement=null;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                gridElement = inflater.inflate(R.layout.list_view_items, null);

                TextView textViewText = (TextView) gridElement.findViewById(R.id.facility_text);
                textViewText.setText(mDes[position]);

                TextView textViewStatus = (TextView) gridElement.findViewById(R.id.facility_status);
                if (_status[position]) textViewStatus.setText("Open");
                else                   textViewStatus.setText("Close");

                TextView textViewAssign = (TextView) gridElement.findViewById(R.id.client_assign);
                textViewAssign.setText(_asignS[position]);



            return gridElement;
        }


    }
}