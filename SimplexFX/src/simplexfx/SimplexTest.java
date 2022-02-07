package simplexfx;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class SimplexTest {
    private final double[][] tabelSimplex;
    private final int nrRestrictii;
    private final int nrVariabile;
    private int vA;
    private final double[] fObiectiv;
    private final boolean maxOrMin;
    public boolean unbounded = false;
    List<double[][]> iteratii = new ArrayList<>();
    List<int[]> istoricBaze = new ArrayList<>();
    List<Double> solutiiOptime = new ArrayList<>();
    

    boolean first = true;
    private final int[] baza;
    
    public SimplexTest(double[][] tabelSimplex, int nrRestrictii, int nrVariabile, boolean maxOrMin, int vA, int[] baza){
        this.maxOrMin = maxOrMin;
        this.nrVariabile = nrVariabile;
        this.nrRestrictii = nrRestrictii;
        this.tabelSimplex = tabelSimplex;
        this.baza = baza;
        fObiectiv = new double[tabelSimplex[0].length];
        int[] baze = new int[baza.length-1];
        System.arraycopy(baza, 0, baze, 0, baze.length);
        istoricBaze.add(baze);
        for(int i = 0; i<tabelSimplex[0].length;i++){
            fObiectiv[i] = tabelSimplex[nrRestrictii][i];
            System.out.print(fObiectiv[i]+" ");
        }

        rezolva();
    }
    
    private void rezolva(){
        while(true){ // Runs while optimum criteria requirement is not met
            checkOptimal(); // Checks if the optimum criteria requirement is met
            double[][] it = new double[tabelSimplex.length][tabelSimplex[0].length];
            for(int i = 0; i<tabelSimplex.length;i++)
                for(int j = 0; j <tabelSimplex[0].length;j++)
                    it[i][j]=round(tabelSimplex[i][j],2);
            iteratii.add(it); // Adds the current iteration to a list in order to view all the iterations inside a TableView in the GUI later
            show(); // Prints the current iteration in console
            int q = 0;     
            if(maxOrMin){
                q = dantzig(); // For maximum -> Calculates the differences between Cj and Fj
            }
            else
                q = dantzigNegative(); // For minimum
            if(q==-1)
                break;
            int p = minRatioRule(q); // Finds the line of the vector that will leave the base(calculating the raport between the current base solution and the element from the column q and line i)
            if(p==-1){ // p == -1 if all the elements from the pivot's column are <= 0
                unbounded =true;
                throw new ArithmeticException("Problema de programare liniara este unbounded..");
            }
            System.out.println("Linia pivotului este: " + p);
            System.out.println("Coloana pivotului este: " + q);
            pivot(p,q); // Calculates the elements of the next iteration using Gauss-Jordan method
           baza[p]=q; // Vector p leaves the base, vector q enters the base
           int[] baze = new int[baza.length-1];
           System.arraycopy(baza, 0, baze, 0, baze.length);
           istoricBaze.add(baze);
           solutiiOptime.add(vfint());
        }
    }
    
    private int dantzig(){
        int q = 0;
        for(int j = 1; j< vA + nrVariabile; j++)
            if(tabelSimplex[nrRestrictii][j]>tabelSimplex[nrRestrictii][q])
                q=j; // finds the maximum between the differences
        if(tabelSimplex[nrRestrictii][q] <= 0)
            return -1; // solution met the optimum criteria
        else
            return q; // return the maximum
   }
    
    private int dantzigNegative(){
        int q = 0;
        for(int j = 1; j< vA + nrVariabile; j++)
            if(tabelSimplex[nrRestrictii][j]<tabelSimplex[nrRestrictii][q])
                q=j;
        if(tabelSimplex[nrRestrictii][q] >= 0)
            return -1; // solution is optimum
        else
            return q;
    }
       
       private int minRatioRule(int q) {
        int p = -1;
        for(int i = 0; i < nrRestrictii; i++){
            if(tabelSimplex[i][q] <= 0)
                continue;
            else if(p == -1){
                p=1;
            }
            else if(tabelSimplex[p][tabelSimplex[0].length-1]/tabelSimplex[p][q]<0){
                p=0;          
            }
            if(tabelSimplex[i][tabelSimplex[0].length-1]/tabelSimplex[i][q] < tabelSimplex[p][tabelSimplex[0].length-1]/tabelSimplex[p][q]){
                p=i;            
                if(tabelSimplex[p][tabelSimplex[0].length-1]/tabelSimplex[p][q] <= 0)
                    p = i;    
            }
            else if(tabelSimplex[p][tabelSimplex[0].length-1]/tabelSimplex[p][q]<=0)
                p=i;
        }
        return p;
    }

    private double vfint(){
    double valoare = 0;
      for(int i=0;i<nrRestrictii;i++){
        valoare += round(fObiectiv[baza[i]]*tabelSimplex[i][tabelSimplex[0].length-1],2);
      }
      return valoare;
    }
    public double[] getOptimal(){
        double[] optim = new double[nrRestrictii];
        for(int i = 0; i < nrRestrictii; i++)
            optim[i]=round(tabelSimplex[i][tabelSimplex[0].length-1],2);
        return optim;
    }
    private void pivot(int p, int q){          
        // Calculates using the rectangle method
        for(int i = 0; i < nrRestrictii; i++){
            for(int j = 0; j < tabelSimplex[0].length; j++){
                if(i!=p && j!=q)
                    tabelSimplex[i][j] -=tabelSimplex[p][j]*tabelSimplex[i][q]/tabelSimplex[p][q];
            }
            }
        
         System.out.println("Valoarea optima este: "+vfint());
                 
        // Divids the pivot's line to the pivot
       for(int j = 0; j < tabelSimplex[0].length;j++)
           if (j!=q)
                tabelSimplex[p][j] /=tabelSimplex[p][q];
       // Pivot's column is filled with 0
       for(int i = 0; i < nrRestrictii; i++)
            if( i!= p )
                tabelSimplex[i][q]=0.0;
       // Pivot equals 1
       tabelSimplex[p][q] = 1.0;
    }
    public boolean check(){
        for(int j = 1; j< tabelSimplex[0].length; j++)
            
        if(tabelSimplex[nrRestrictii][j] <= 0)
            return true; // optimum solution reached
        return false;
    }
    public void checkOptimal(){
       for(int j=0;j<tabelSimplex[0].length;j++){
        double suma = 0;
        for(int i = 0; i<nrRestrictii;i++){
            System.out.println("EXERC: " + tabelSimplex[i][j] + " * " + fObiectiv[baza[i]] + " BAZA = " + baza[i]);
            suma+=(tabelSimplex[i][j]*fObiectiv[baza[i]]);
        }
        System.out.println("REZ ESTE: " + fObiectiv[j] + " - " + suma);
        double rez = fObiectiv[j]-suma;
        tabelSimplex[nrRestrictii][j] = rez;
        }
    }
    public double value(){
        return vfint();
    }
    
    public double[] primal(){
        double[] x = new double[nrVariabile];
        for(int i = 0; i < nrRestrictii; i++){
            if(baza[i] < nrVariabile)
                x[baza[i]] = tabelSimplex[i][tabelSimplex[0].length-1];
        }
        return x;
    }
    
    public void show(){
        System.out.println("");
        System.out.println("Nr. restrictii: " + nrRestrictii);
        System.out.println("Nr. variabile: " + nrVariabile);
        for(int i = 0; i <= nrRestrictii; i++){
            for(int j = 0; j < tabelSimplex[0].length; j++){
                System.out.printf("%7.2f", tabelSimplex[i][j]);
            }
            System.out.println("()");
        }
        System.out.println("Valoare = " + value());
        for(int i = 0; i < nrRestrictii; i++)
            if(baza[i] < nrVariabile)
                System.out.println("x_"+baza[i] + "=" + tabelSimplex[i][tabelSimplex[0].length-1]);
        System.out.println("");
        System.out.println("Baza curenta este: ");
        for(int i = 0; i < nrRestrictii;i++)
            System.out.print(baza[i] + " ");
        
    }
    public void copiazaInLista(){
        double[][] it = new double[nrRestrictii][nrRestrictii+vA];
        for(int i = 0; i<nrRestrictii;i++)
            System.arraycopy(tabelSimplex[i], 0, it[i], 0, nrRestrictii+vA);
        iteratii.add(it);
    }
    public List<double[][]> TabeleSimplexIteratii(){ // returns all the iterations
        return iteratii;
    }
    public List<int[]> IstoricBaze(){ // returns the bases of each iteration
        return istoricBaze;
    }
    public List<Double> IstoricSolutiiOptime(){ // returns the value of the objective function of each iteration
        return solutiiOptime;
    }
    public static double round(double value, int places) { // Rounds to 2 decimals
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
