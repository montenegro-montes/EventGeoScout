package simpleGa;

import java.util.Random;

public class RandomSingleton {
    private static RandomSingleton instance;
    private static int _semilla	=5;

    private Random rnd;

    private RandomSingleton() {
        rnd = new Random(_semilla);
    }

    public static void setSeed(int semilla){
        _semilla = semilla;
    }

    public static RandomSingleton getInstance() {
        if(instance == null) {
            instance = new RandomSingleton();
        }
        return instance;
    }

    public boolean nextBoolean (){
        return rnd.nextBoolean();
    }

    public int nextInt (){
        return rnd.nextInt();
    }

    public double nextDouble (){
        return rnd.nextDouble();
    }

    public int getSemilla(){
        return _semilla;
    }

    public int nextInt(int min, int max){
        return rnd.nextInt(max - min) + min;
    }

    public int nextInt( int max){

        return rnd.nextInt(max);
    }

    public static void clear(){
        instance	=	null;
    }

}