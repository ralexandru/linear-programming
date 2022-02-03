package simplexfx;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javax.swing.JOptionPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class principal extends Application {
    static int uId;
    int nrRestrictiiInt, nrVariabileInt;
    TextField vB[];
    TextField A[][];
    ComboBox<String> minOrMax = new ComboBox<>();
    TextField fObiectiv[];
    ComboBox<String> ineq[];
    ComboBox<String> maxMin = new ComboBox();
    int iteratieNr = 0;
    int nrRestrictii = 0;
    List<double[][]> iteratii = new ArrayList<>();
    List<String> setari = new ArrayList<>();
    TableView<double[]> tabelSimp = new TableView<>(); 
    TableView<double[]> tabelOpt = new TableView<>();
    List<int[]> istoricBaze = new ArrayList<>();
    List<Double> solutiiOptime = new ArrayList<>();
    HBox[] hbox;
    boolean toateIteratiile;
    String numeColoana;
    String numeFisier = "output";
    String numeSolutie;
    VBox dreapta = new VBox();
    Label baze;
    Label solutieOptima;
    String textFunctieObiectiv = "";
    String[] textRestrictii, textSistemRestrictii, textDreaptaRestrictii;
    @Override
    public void start(Stage primaryStage) throws Exception {

    }
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public AnchorPane spane;
    @FXML
    public AnchorPane apane;
    @FXML
    public AnchorPane rpane;
    @FXML
    public Button urmator;
    @FXML
    public Button anterior;
    @FXML
    public VBox vInfoBaza;
    @FXML
    TilePane sistemHolder;
    @FXML
    TextField nrRes;
    @FXML
    TextField nrVa;
    @FXML
    VBox vbox;
    @FXML
    ComboBox minMax;
    @FXML
    TilePane fOb;
    @FXML
    VBox vInfoSO;
    @FXML
    TextField numeColoane;
    @FXML
    TextField numeSolutii;
    @FXML
    TextField numeOutput;
    @FXML
    CheckBox fiecareIteratie;
    @FXML
    ListView istoricLista;
    @FXML
    public void DeschideSetari(){
            Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("setari.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        }
            Stage principal = new Stage();
            principal.setTitle("Setari");
            principal.setScene(new Scene(root));
            principal.show();
            principal.setResizable(false);
    }
    public void incarca(){
                    Database db = new Database();
            List<String> listaD = new ArrayList<>();
        try {
            db.openConnection();
            istoricLista.getItems().addAll(db.istoric(uId));
        } catch (SQLException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void genereazaSistem(){
        
        System.out.println("USER ID: " + uId);
        nrRestrictiiInt = Integer.parseInt(nrRes.getText());
        nrVariabileInt = Integer.parseInt(nrVa.getText());
        fObiectiv = new TextField[nrVariabileInt];
        HBox vbox2 = new HBox();
        vbox2.getChildren().add(maxMin);
        for(int i = 0; i < nrVariabileInt; i++){
            fObiectiv[i] = new TextField();
            fObiectiv[i].prefColumnCountProperty().bind(fObiectiv[i].textProperty().length());
            if(i<nrVariabileInt-1){
                vbox2.getChildren().addAll(fObiectiv[i],new Label("x" + i + "+"));
            }else{
                vbox2.getChildren().addAll(fObiectiv[i],new Label("x" + i));
            }
                
        }
        VBox vb = new VBox(5);
        A = new TextField[nrRestrictiiInt][nrVariabileInt];
        ineq = new ComboBox[nrRestrictiiInt];
        hbox = new HBox[nrRestrictiiInt];
        vB = new TextField[nrRestrictiiInt];
        for(int i = 0; i < nrRestrictiiInt; i++){    
            hbox[i] = new HBox(5);
            ineq[i] = new ComboBox<>();
            vB[i] = new TextField();
            ineq[i].getItems().addAll("=","<=",">=");
            for(int j = 0; j < nrVariabileInt; j++){
                A[i][j] = new TextField();
             //   A[i][j].prefColumnCountProperty().bind(fObiectiv[i].textProperty().length());
                hbox[i].getChildren().add(A[i][j]);
                hbox[i].getChildren().add(new Label("x"+j));
                if(j!= nrVariabileInt-1){
                    hbox[i].getChildren().add(new Label("+"));
                    
                }
                else{
                    hbox[i].getChildren().addAll(ineq[i],vB[i]);
                }
            }
             vb.getChildren().add(hbox[i]);
            
        }
        sistemHolder.getChildren().add(vb);
        maxMin.getItems().addAll("MAX","MIN");
        fOb.getChildren().add(vbox2);

    }
    @FXML
    public void IteratieUrmatoare(){
        if(iteratieNr<iteratii.size()-1){
            iteratieNr+=1;
            actualizeazaBaze();
            if(iteratieNr>=0)
                anterior.setDisable(false);
        }
        else{
            solutieOptima = new Label("Solutie: " + solutiiOptime.get(iteratieNr-1));
            solutieOptima.setStyle("-fx-text-fill: white");
            vInfoBaza.getChildren().remove(dreapta);
            vInfoBaza.getChildren().add(solutieOptima);
            vInfoBaza.getChildren().add(dreapta);
            urmator.setDisable(true);
        }
        printMatrix(tabelSimp,iteratii.get(iteratieNr));
    }
    @FXML
    public void IteratieAnterioara(){
        if(iteratieNr>0){
            if(iteratieNr==iteratii.size()-1){
            vInfoBaza.getChildren().remove(dreapta);
            vInfoBaza.getChildren().remove(solutieOptima);
            vInfoBaza.getChildren().add(dreapta);
            }
            iteratieNr-=1;
            actualizeazaBaze();
            if(iteratieNr<iteratii.size()-1)
                urmator.setDisable(false);
        }
        else{
            anterior.setDisable(true);
        }
        printMatrix(tabelSimp,iteratii.get(iteratieNr));
    }
    @FXML
    public void Reseteaza(){
        reset();
    }
    @FXML
    public void SalveazaCaFisier() throws IOException{
      CitesteSetari();
        System.out.println(numeFisier + numeColoana + numeSolutie+toateIteratiile);
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save");
      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", ".txt"));
      File fileToSave = fileChooser.showSaveDialog((Stage) urmator.getScene().getWindow());
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(fileToSave));
        outputWriter.write("----Date Problema de Programare Liniara---");
        outputWriter.newLine();
        outputWriter.write(textFunctieObiectiv);
        outputWriter.newLine();
        for(int i = 0; i < iteratii.get(0).length-1;i++){
            outputWriter.write(textSistemRestrictii[i]);
            outputWriter.write(textRestrictii[i]);
            outputWriter.write(textDreaptaRestrictii[i]);
            outputWriter.newLine();
        }
        outputWriter.write("---Rezolvare---");
        outputWriter.newLine();
        if(toateIteratiile){
        for (int i = 0; i < iteratii.size(); i++) {
            outputWriter.newLine();
            outputWriter.write("---- ITERATIA NUMARUL " + i +" ----");
            outputWriter.newLine();
            for(int a = 0; a < iteratii.get(i).length; a++){
               for(int b = 0; b < iteratii.get(i)[0].length;b++){
                   outputWriter.write(iteratii.get(i)[a][b]+" "); 
               }
               outputWriter.newLine();
            }
            outputWriter.write("Baza iteratiei curente este:");
           for(int c = 0; c <istoricBaze.get(i).length; c++)
               outputWriter.write(istoricBaze.get(i)[c] + " ");
           outputWriter.newLine();
        }
        }
        else{
           for(int a = 0; a < iteratii.get(iteratii.size()-1).length; a++){
               for(int b = 0; b < iteratii.get(iteratii.size()-1)[0].length;b++){
                   outputWriter.write(iteratii.get((iteratii.size()-1))[a][b]+" "); 
               }
               outputWriter.newLine();
            }
            outputWriter.write("Baza iteratiei este:");
           for(int c = 0; c <istoricBaze.get((iteratii.size()-1)).length; c++)
               outputWriter.write(istoricBaze.get(iteratii.size()-1)[c] + " ");
           outputWriter.newLine();
        }
        outputWriter.newLine();
        outputWriter.write("Solutia optima: " + solutiiOptime.get(solutiiOptime.size()-1));
        outputWriter.flush();  
        outputWriter.close();  
    }
    @FXML
    public void SalveazaSetari() throws IOException{
        incarca();
        List<String> setari = new ArrayList<>();
        setari.add(numeColoane.getText().toString());
        setari.add(numeSolutii.getText().toString());
        setari.add(numeOutput.getText().toString());
        setari.add(fiecareIteratie.isSelected() ? "true" : "false");
        numeColoana = numeColoane.getText().toString();
        System.out.println(numeColoana);
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter("setari.txt"));
        for (int i = 0; i < setari.size(); i++) {
           outputWriter.write(setari.get(i));
           outputWriter.newLine();
        }
        outputWriter.flush();  
        outputWriter.close();  
    }
    @FXML
    public void Inchide(){
        
    }
    public void adaugaInIstoric(String nrRestrictii, String nrVariabile, String fObiectiv, String restrictii, String dreaptaRestrictii, String sistemRestrictii, String baza, String sOptima){
                Database db = new Database();
        try{
            db.openConnection();
            db.ExecutaQuery("INSERT INTO istoricppl(userId, nrRestrictii, nrVariabile, fObiectiv, restrictii, dreaptaRestrictii, sistemRestrictii,baza,optim) VALUES('"+uId+"',"+"'"+nrRestrictii+"',"+"'"+nrVariabile+"',"+"'"+fObiectiv+"',"+"'"+restrictii+"',"+"'"+dreaptaRestrictii+"',"+"'"+sistemRestrictii+"','"+baza+"','"+sOptima+"')");
        }catch(Exception e){System.out.println(e);}

    }
    @FXML
    public void GenereazaTabel(){
        CitesteSetari();
        Database db = new Database();
        
        try{
            db.openConnection();
            db.ExecutaQuery("INSERT INTO istoricppl(userId, nrRestrictii, nrVariabile, fObiectiv, restrictii, dreaptaRestrictii, sistemRestrictii) VALUES('1',"+"'2',"+"'3',"+"'2,3,4',"+"'maiMic,maiMare',"+"'2,4',"+"'2,4,5,5,6,2')");
        }catch(Exception e){System.out.println(e);}
        System.out.println(numeColoana);
        double[] functieObiectiv = new double[nrVariabileInt];
        double[][] matriceRestrictii = new double[nrRestrictiiInt][nrVariabileInt];
        double[] dreaptaRestrictii = new double[nrRestrictiiInt];
        Restrictii[] restrictii = new Restrictii[nrRestrictiiInt];
        textRestrictii = new String[nrRestrictiiInt];
        textSistemRestrictii = new String[nrRestrictiiInt];
        textDreaptaRestrictii = new String[nrRestrictiiInt];
        boolean maxOrMin = true;
        switch(maxMin.getValue()){
            case("MAX"): maxOrMin = true;
                        break;
            case("MIN"): maxOrMin = false;
                        break;
        }
        if(maxOrMin)
            textFunctieObiectiv += "[MAX]f = ";
        else
            textFunctieObiectiv += "[MIN]f = ";
        for(int i = 0; i < nrVariabileInt; i++){
            functieObiectiv[i] = Double.parseDouble(fObiectiv[i].getText());
            if(i<nrVariabileInt - 1)
                textFunctieObiectiv += Double.parseDouble(fObiectiv[i].getText()) + "x" + i + " + ";
            else
                textFunctieObiectiv += Double.parseDouble(fObiectiv[i].getText()) + "x" + i;
        }
        for(int i = 0; i < nrRestrictiiInt; i++){
            dreaptaRestrictii[i] = Double.parseDouble(vB[i].getText());
            textDreaptaRestrictii[i] = vB[i].getText();
            switch(ineq[i].getValue()){
               case("="): restrictii[i]=Restrictii.egal;
                           textRestrictii[i] = "=";
                          break;
               case("<="): restrictii[i]=Restrictii.maiMic;
                            textRestrictii[i] = "<=";
                          break;
               case(">="): restrictii[i]=Restrictii.maiMare;
                            textRestrictii[i] = ">=";
                          break;                   
             }
            textSistemRestrictii[i]="";
            for(int j = 0; j < nrVariabileInt; j++){
                matriceRestrictii[i][j]=Double.parseDouble(A[i][j].getText());                
                if(j<nrVariabileInt-1)
                    textSistemRestrictii[i] += A[i][j].getText() + "x" + j + " + ";
                else
                    textSistemRestrictii[i] += A[i][j].getText() + "x" + j;
            }
        }
        /*switch(maxMin.getValue()){
            case("MAX"): maxOrMin = true;
                        break;
            case("MIN"): maxOrMin = false;
                        break;
        }*/
       int[] baza = new int[dreaptaRestrictii.length];
       int[] baza31 = new int[dreaptaRestrictii.length];
        int cateInec = 0;
        
        for (Restrictii restrictii1 : restrictii) {
            if (restrictii1 != Restrictii.egal) {
                cateInec++;
            }
        }
        double[][] matriceRestrictii3 = new double[dreaptaRestrictii.length][functieObiectiv.length];
        matriceRestrictii3=creeazaMatriceRestrictii(functieObiectiv,matriceRestrictii,dreaptaRestrictii,restrictii, cateInec);
        System.out.println("Matricea de Restrictii este:");
        for(int i = 0; i < dreaptaRestrictii.length; i++){
            for(int j = 0 ; j < cateInec+functieObiectiv.length; j++){
                System.out.print(matriceRestrictii3[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("Baza canonica care ar trebui gasita in cadrul matricii de restrictii este: ");
        baza = ContineBazaCanonica(matriceRestrictii3.length-1,matriceRestrictii3[0].length-1,matriceRestrictii3,baza);
        int[] locVar = ContineBazaCanonica2(restrictii.length,matriceRestrictii3[0].length-1,matriceRestrictii3,baza);
        double[][] Ba = new double[dreaptaRestrictii.length+1][functieObiectiv.length+dreaptaRestrictii.length+1];
        Ba=creeazaTabel(functieObiectiv,matriceRestrictii,dreaptaRestrictii,restrictii,cateInec,locVar,maxOrMin);
        double[][] test = new double[Ba.length-1][Ba[0].length-1];
        for(int i = 0; i < test.length;i++){
            System.arraycopy(Ba[i], 0, test[i], 0, test[0].length);
        }
        baza31 = ContineBazaCanonica(Ba.length-1,Ba[0].length,Ba,baza31);
        
        try{
            SimplexTest simplex = new SimplexTest(Ba,dreaptaRestrictii.length,functieObiectiv.length+cateInec, maxOrMin,cateInec,baza31);
        double[] x = simplex.primal();
        for(double[][] a : simplex.TabeleSimplexIteratii()){
            for(int i = 0; i < a.length; i++){
                for(int j = 0; j < a[0].length; j++){
                    System.out.print(a[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
                    iteratii = simplex.TabeleSimplexIteratii();
        istoricBaze = simplex.IstoricBaze();
        solutiiOptime = simplex.IstoricSolutiiOptime();
        printMatrix(tabelSimp,iteratii.get(iteratieNr));      
        reset();
        apane.getChildren().add(tabelSimp);
        scrollPane.setContent(apane);
        String sBaze=" ";
        for(int i = 0; i < nrRestrictii;i++){
            sBaze += "x["+istoricBaze.get(0)[i]+"] ";
        }
        baze = new Label("Baza curenta este: " + sBaze);
        nrRestrictii=dreaptaRestrictii.length;
        baze.setStyle("-fx-text-fill: white");
        dreapta.getChildren().add(baze);
        vInfoBaza.getChildren().add(dreapta);
        actualizeazaBaze();
        tabelSimp.prefHeightProperty().bind(apane.heightProperty());
        tabelSimp.prefWidthProperty().bind(apane.widthProperty());   
                for (double[] test1 : test) {
                    for (int j = 0; j < test[0].length; j++) {
                        System.out.print(test1[j] + " ");
                    }
                    System.out.println("");
                }
        double[] op = simplex.getOptimal();
        printMatrix2(tabelOpt,op);
        tabelOpt.setFixedCellSize(30);
        tabelOpt.prefHeightProperty().bind(tabelOpt.fixedCellSizeProperty().multiply(Bindings.size(tabelOpt.getItems()).add(1.2)));
        tabelOpt.minHeightProperty().bind(tabelOpt.prefHeightProperty());
        tabelOpt.maxHeightProperty().bind(tabelOpt.prefHeightProperty());
        vInfoSO.getChildren().add(tabelOpt);
        vInfoSO.getChildren().add(new Label("Solutie: " + solutiiOptime.get(solutiiOptime.size()-1)));
        feasibleSolution(dreaptaRestrictii,restrictii,matriceRestrictii,istoricBaze.get(iteratii.size()-1),op);
        for(int i = 0; i < op.length;i++)
            System.out.println(op[i]);
        }
        }catch(ArithmeticException e){
            JOptionPane.showMessageDialog(null, e, "PPL: " + "Unbounded", JOptionPane.ERROR_MESSAGE);
        }
        String restrictiiMYSQL = "",dreaptaRestrictiiMYSQL="",sistemRestrictiiMYSQL="",bazeMYSQL="";
        for(String s : textRestrictii)
            restrictiiMYSQL+=s + " ";
        for(String s : textDreaptaRestrictii)
            dreaptaRestrictiiMYSQL+=s + " ";
        for(String s : textSistemRestrictii)
            sistemRestrictiiMYSQL+=s + " ";
        for(int i = 0; i < nrRestrictiiInt; i++)
            bazeMYSQL+=istoricBaze.get(istoricBaze.size()-1)[i] + " ";
        /*for(int i : istoricBaze.get(istoricBaze.size()-1))
            bazeMYSQL+=(int) i + " ";*/
        adaugaInIstoric(String.valueOf(nrRestrictiiInt),String.valueOf(nrVariabileInt),textFunctieObiectiv,restrictiiMYSQL,dreaptaRestrictiiMYSQL,sistemRestrictiiMYSQL,bazeMYSQL,solutiiOptime.get(solutiiOptime.size()-1).toString());
    }
    void feasibleSolution(double[] dreaptaRestrictii, Restrictii[] restrictii, double[][] sistemRestrictii,int[] baza, double[] solutiiOptime){
        boolean feasible = true;
        String restrictiiNerespectate = "Solutia optima nu este fezabila deoarece nu sunt respectate restrictiile: ";
        for(int i = 0; i<dreaptaRestrictii.length;i++){
            double value = 0;
            for(int j = 0; j<dreaptaRestrictii.length;j++){
                if(baza[j]<sistemRestrictii[0].length)
                value += sistemRestrictii[i][baza[j]]*solutiiOptime[j];      
            }
            if(null != restrictii[i])switch (restrictii[i]) {
                case egal:
                    if(Math.round(value)==dreaptaRestrictii[i])
                        System.out.println(i +" restrictie indeplinita");
                    else{
                        feasible = false;
                        System.out.println(i+" restrictie NEindeplinita");
                        restrictiiNerespectate += i + " ";
                    }
                    break;
                case maiMic:
                    if(value<=dreaptaRestrictii[i])
                        System.out.println(i +" restrictie indeplinita");
                    else{
                        feasible = false;
                        System.out.println(i+" restrictie NEindeplinita");
                        restrictiiNerespectate += i + " ";   
                    }
                    break;
                case maiMare:
                    if(value>=dreaptaRestrictii[i])
                        System.out.println(i +" restrictie indeplinita");
                    else{
                        feasible = false;
                        System.out.println(i+" restrictie NEindeplinita");
                        restrictiiNerespectate += i + " ";   
                    }
                    break;
                default:
                    break;
            }
        }
        if(!feasible){
            JOptionPane.showMessageDialog(null, restrictiiNerespectate, "PPL: " + "Solutie nefezabila", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void reset(){
        iteratieNr=0;
        vInfoSO.getChildren().clear();
        tabelOpt.getItems().clear();
        sistemHolder.getChildren().clear();
        dreapta.getChildren().clear();
        vInfoBaza.getChildren().clear();
        apane.getChildren().clear();
        fOb.getChildren().clear();
        maxMin.getItems().clear();
        urmator.setDisable(false);
        anterior.setDisable(false);
        CitesteSetari();
    }
    void actualizeazaBaze(){
        String sBaze=" ";
        for(int i = 0; i < nrRestrictii;i++){
            sBaze += "x["+istoricBaze.get(iteratieNr)[i]+"] ";
        }
        baze.setText("Baza curenta este: " + sBaze);
    }
    /*--------------------------Printmatrix2------------------------*/
       void printMatrix2(TableView<double[]> target, double[] source) {

        target.getColumns().clear();
    target.getItems().clear();

    int numRows = 1 ;
    if (numRows == 0) return ;

    int numCols = source.length ;

    for (int i = 0 ; i < numCols ; i++) {
        TableColumn<double[], Number> column;
        if(i<numCols-1)
        column = new TableColumn<>(numeSolutie+" "+istoricBaze.get(istoricBaze.size()-1)[i]);
        else
            column= new TableColumn<>(numeSolutie+" "+istoricBaze.get(istoricBaze.size()-1)[i]);
        final int columnIndex = i ;
        column.setCellValueFactory(cellData -> {
            double[] row = cellData.getValue();
            return new SimpleDoubleProperty(row[columnIndex]);
        });
        target.getColumns().add(column);
    }

    for (int i = 0 ; i < numRows ; i++) {
        target.getItems().add(source);
    }

    target.getColumns().forEach(column -> column.setMinWidth(50));
}

    /*------------------------------------------------------*/
    void printMatrix(TableView<double[]> target, double[][] source) {
        CitesteSetari();
        target.getColumns().clear();
        target.getItems().clear();

    int numRows = source.length ;
    if (numRows == 0) return ;

    int numCols = source[0].length ;

    for (int i = 0 ; i < numCols ; i++) {
        TableColumn<double[], Number> column;
        if(i<numCols-1)
        column = new TableColumn<>(numeColoana+" "+i);
        else
            column= new TableColumn<>("xB");
        final int columnIndex = i ;
        column.setCellValueFactory(cellData -> {
            double[] row = cellData.getValue();
            return new SimpleDoubleProperty(row[columnIndex]);
        });
        
        if(i == numCols-1)
        column.setStyle("-fx-background-color:lightcoral");
        target.getColumns().add(column);
    }

    for (int i = 0 ; i < numRows ; i++) {
        target.getItems().add(source[i]);
    }

    target.getColumns().forEach(column -> column.setMinWidth(50));
}

    /*-----------------------------------------------------*/
        public static void main(String[] args) {
        launch(args);
    }
        void CitesteSetari(){  
                try{
                File setariFisier = new File("C:\\Users\\corei3\\Documents\\NetBeansProjects\\SimplexFX\\setari.txt");
                Scanner myReader = new Scanner(setariFisier);
                setari.clear();
                while(myReader.hasNextLine()){
                    setari.add(myReader.nextLine());
                }

                numeColoana = setari.get(0);
                numeSolutie = setari.get(1);               
                toateIteratiile = Boolean.valueOf(setari.get(3));
               // toateIteratiile = setari.get(3) == "true" ? true : false;
                myReader.close();
                }catch(Exception e){JOptionPane.showMessageDialog(null, e, "Rualare Setari: " + "Unbounded", JOptionPane.ERROR_MESSAGE);}
                
        }
            public enum Restrictii{
        maiMic,maiMare,egal
    }
    public  static double[][] creeazaMatriceRestrictii(double[] functieObiectiv, double[][] matriceRestrictii, double[] dreaptaRestrictii, Restrictii[] restrictii, int cateInec){
        double[][] A = new double[dreaptaRestrictii.length+1][cateInec+functieObiectiv.length+1];
        for(int i = 0; i < dreaptaRestrictii.length; i++)
            System.arraycopy(matriceRestrictii[i], 0, A[i], 0, functieObiectiv.length);
        int contor = 0;
        for(int i = 0; i <= restrictii.length-1; i++){
            int variabilaCompensare = 0;
            
            switch(restrictii[i]){
                case maiMic:variabilaCompensare=1;
                contor++;
                    break;
                case maiMare: variabilaCompensare=-1;
                contor++;
                break;
            }
            if(variabilaCompensare == 0){
                continue;
            }
            A[i][functieObiectiv.length+contor-1]=variabilaCompensare;
        }
        return A;             
    }
        public static int[] ContineBazaCanonica(int nrRestrictii, int nrVariabile, double[][] matriceRestrictii, int[] baza ){
        boolean[] contineBazaCanonica = new boolean[nrRestrictii];
        double[][] canonic = new double[nrRestrictii][nrRestrictii];
        baza = new int[nrVariabile];
        for(int i = 0; i < nrRestrictii; i++)
            for(int j = 0; j < nrRestrictii; j++){
                if(i==j)
                    canonic[i][j]=1;
                else
                    canonic[i][j]=0;
            }
        for(int i = 0; i < nrRestrictii; i++){
            for(int j = 0; j < nrRestrictii; j++){
                System.out.print(canonic[i][j] + " ");
            }
            System.out.println("");
        }
        for(int a = 0; a < nrRestrictii; a++){
            for(int j = 0; j < nrVariabile;j++){
                contineBazaCanonica[a] = true;
                    for(int i = 0; i<nrRestrictii;i++){
                        if(matriceRestrictii[i][j]!=canonic[i][a]){
                            contineBazaCanonica[a] = false;
                        
                        }
                    }
                if(contineBazaCanonica[a]){
                    baza[a]=j;
                    break;
                }
            }
        }
        System.out.println("Matricei de restrictii, ii lipsesc vectorii unitari: ");
        for(int i = 0; i < nrRestrictii; i++){
            if(!contineBazaCanonica[i])
                System.out.print("e"+i+",");
        }
        System.out.println("");
        System.out.println("Din baza va face parte: ");
         for(int i = 0; i < nrRestrictii; i++){
             System.out.print(baza[i] + " ");
         }
         return baza;
    }
        
    public  static double[][] creeazaTabel(double[] functieObiectiv, double[][] matriceRestrictii, double[] dreaptaRestrictii, Restrictii[] restrictii, int cateInec, int[] locVar, boolean minMax){
        double[][] A = new double[dreaptaRestrictii.length+1][cateInec+functieObiectiv.length+locVar.length+1];
        double[][] B = new double[dreaptaRestrictii.length+1+locVar.length][cateInec+functieObiectiv.length+1+locVar.length];
        for(int i = 0; i < dreaptaRestrictii.length; i++)
            System.arraycopy(matriceRestrictii[i], 0, A[i], 0, functieObiectiv.length);
        for(int i = 0; i < dreaptaRestrictii.length; i++)
            A[i][cateInec+functieObiectiv.length] = dreaptaRestrictii[i];
        int contor = 0;
        for(int i = 0; i <= restrictii.length-1; i++){
            int variabilaCompensare = 0;
            switch(restrictii[i]){
                case maiMic:variabilaCompensare=1;
                contor++;
                    break;
                case maiMare: variabilaCompensare=-1;
                contor++;
                break;
            }
            if(variabilaCompensare == 0){
                continue;
            }
            A[i][functieObiectiv.length+contor-1]=variabilaCompensare;
        }
        for(int j = 0; j < locVar.length; j++){
            for(int i = 0; i < restrictii.length; i++){
                if(locVar[j]==i)
                    A[i][functieObiectiv.length+contor]=1;
                else
                    A[i][functieObiectiv.length+contor]=0;
            }
            contor++;
        }
        for(int i = 0; i <A[0].length-1; i++){
            if(i<functieObiectiv.length)
                A[dreaptaRestrictii.length][i]=functieObiectiv[i];
            else if(i >= functieObiectiv.length && i < (functieObiectiv.length + cateInec))
                A[dreaptaRestrictii.length][i]=0;
            else if(i< A[0].length-1){
                if(minMax == true)
                    A[dreaptaRestrictii.length][i]=-130;
                else
                    A[dreaptaRestrictii.length][i]=130;
            }
        }
        for(int i = 0; i < dreaptaRestrictii.length; i++)
            A[i][cateInec+functieObiectiv.length+locVar.length] = dreaptaRestrictii[i];
        return A;     
    }
    
    public static int[] ContineBazaCanonica2(int nrRestrictii, int nrVariabile, double[][] matriceRestrictii, int[] baza ){
        boolean[] contineBazaCanonica = new boolean[nrRestrictii];
        double[][] canonic = new double[nrRestrictii][nrRestrictii];
        baza = new int[nrVariabile];
        int baza3[] = new int[nrRestrictii];
        for(int i = 0; i < nrRestrictii; i++)
            for(int j = 0; j < nrRestrictii; j++){
                if(i==j)
                    canonic[i][j]=1;
                else
                    canonic[i][j]=0;
            }
        for(int i = 0; i < nrRestrictii; i++){
            for(int j = 0; j < nrRestrictii; j++){
                System.out.print(canonic[i][j] + " ");
            }
            System.out.println("");
        }
        for(int a = 0; a < nrRestrictii; a++){
        for(int j = 0; j < nrVariabile;j++){
            contineBazaCanonica[a] = true;
                for(int i = 0; i<nrRestrictii;i++){
                    if(matriceRestrictii[i][j]!=canonic[i][a]){
                        contineBazaCanonica[a] = false;      
                    }
                }
                if(contineBazaCanonica[a]){
                    baza[a]=j;
                    break;
                }
                else{
                    baza3[a]=j;
                }
            }
        }
        int contor = 0;
        System.out.println("Matricei de restrictii, ii lipsesc vectorii unitari: ");
        for(int i = 0; i < nrRestrictii; i++){
            if(!contineBazaCanonica[i]){
             System.out.print("e"+i+",");
             contor++;
            }
        }
        int[] baza2 = new int[contor];
        contor = 0;
        for(int i = 0; i < nrRestrictii; i++){
            if(!contineBazaCanonica[i]){
              baza2[contor]=i;
              contor++;
            }
        }
        System.out.println("");
        System.out.println("Trebuie introduse variabilele artificiale : ");
         for(int i = 0; i < contor; i++){
             System.out.println(baza2[i]+" ");
         }
         return baza2;
    }
}
