package GeneticAlgorithms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class BatteriaPruebas {

	Problem  problemCurrent;
	double _solucion;
	int _poblacion = 0, _generaciones=0;
	long TimeConsumed;
	int  generationConsumed =0;


	public BatteriaPruebas (Problem problem ,double solucion){
		problemCurrent = problem;
		_solucion = solucion;
	}

	public void setParamGenetico (int poblacion, int generaciones){
		_poblacion 		= poblacion;
		_generaciones	=generaciones;
	}

	public void run(boolean print_problema) throws IOException {

		problemCurrent.setFitnesSolution(_solucion);

		Genetico genProblemBasic= new Genetico (_poblacion,_generaciones,problemCurrent);
		//genProblemBasic.debug	=true;

		genProblemBasic.run();
		Individual sol			 		= genProblemBasic.getSolution();
		long timeConsumed 		 		= genProblemBasic.getTimeConsumed();
		List <Double>  fitnessHistory 	= genProblemBasic.getHistoryFitness();
		int generationCountSol	 		= genProblemBasic.getGenerationCount();

		problemCurrent.setGeneticParams(_poblacion, _generaciones);
		problemCurrent.setFindSolution(sol);
		problemCurrent.setTimeConsumed(timeConsumed);
		problemCurrent.setHisttoryFitness(fitnessHistory);
		problemCurrent.setGenerationCount(generationCountSol);
		problemCurrent.setHisttoryFitnessEliStd(genProblemBasic.getHistorySTDFitness());

		if (print_problema) problemCurrent.printSolution(print_problema);


		TimeConsumed			 = problemCurrent.getTimeConsumed();
		generationConsumed   = problemCurrent.getGenerationConsumed();

		genProblemBasic.clear();
	}

	public long getTimeConsumed() {
		return TimeConsumed;
	}


	public int getGenerationConsumed() {
		return generationConsumed;
	}

	public double Avg (long []timesConsumed) {
		int sumatoria = 0;
		for (int x = 0; x < timesConsumed.length; x++) {
			sumatoria += timesConsumed[x];
		}
		double avg = sumatoria / timesConsumed.length;

		return avg;
	}

	public  void printTime (long []timesConsumed) {

		System.out.println("\n\n Time:  ");

		for (int x = 0; x < timesConsumed.length; x++) {
			System.out.print(timesConsumed[x]+ " ");
		}
		System.out.println(" ");
	}

	public  double best (long []timesConsumed) {
		Arrays.sort(timesConsumed);
		return timesConsumed[0];
	}

	public  double worst (long []timesConsumed) {
		return timesConsumed[timesConsumed.length-1];
	}

}