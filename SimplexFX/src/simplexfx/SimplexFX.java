/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package simplexfx;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javax.swing.JOptionPane;
/**
 *
 * @author corei3
 */
public class SimplexFX extends Application {
    Stage openStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Sesiunea de Comunicari Stiintifice Studentesti - Raducu Alexandru-Florian");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        openStage=primaryStage;
        primaryStage.setResizable(false);
        
    }
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField user;
    @FXML
    private PasswordField pw;
    @FXML
    private TextField nume;
    @FXML
    private TextField email;
    @FXML
    private Button close;
    @FXML
    private void Inregistreaza() throws SQLException {
        String username = user.getText();
        String password = pw.getText();
        String numeThis = nume.getText();
        String emailText = email.getText();
        if(username.length()>5 && password.length()>5 && numeThis.length()>5){
            Database db = new Database();
            db.openConnection();
            if(db.CreeazaCont(username,password,numeThis,emailText)){
                JOptionPane.showMessageDialog(null, "Cont creeat cu succes!", "Informatii inregistrare: " + "Inregistrare", JOptionPane.INFORMATION_MESSAGE);
                Stage stage = (Stage) close.getScene().getWindow();
                stage.close();
            }else
                JOptionPane.showMessageDialog(null, "Numele de utilizator deja exista!", "Informatii inregistrare: " + "Inregistrare", JOptionPane.ERROR_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(null, "Numele de utilizator, parola sau numele au prea putine caractere!", "Informatii inregistrare: " + "Inregistrare", JOptionPane.ERROR_MESSAGE);
        
    }
    @FXML
    private void DeschideFormLogin() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Stage registerStage = new Stage();
        registerStage.setTitle("Sesiunea de Comunicari Stiintifice Studentesti - Raducu Alexandru-Florian");
        registerStage.setScene(new Scene(root));
        registerStage.show();
        registerStage.setResizable(false);
    }
    @FXML
    private void EfectueazaLogin() throws SQLException {
        Database db = new Database();
        db.openConnection();
        
        System.out.println("Salut, m-am logat!");
        String user = username.getText();
        String parola = password.getText();
        if(db.verificaCont(user,parola)){
            try{
            Parent root = FXMLLoader.load(getClass().getResource("principal.fxml"));
            Stage principal = new Stage();
            principal.setTitle("Rezolvarea P.P.L. utilizand Algoritmul Simplex");
            principal.setScene(new Scene(root));
            principal.show();
            principal.setResizable(false);
            JOptionPane.showMessageDialog(null, "Te-ai logat cu succes!", "Informatii logare: " + "Logare", JOptionPane.INFORMATION_MESSAGE);
            Stage stage = (Stage) username.getScene().getWindow();
            stage.close();
            }catch(Exception e){}
        }else
            JOptionPane.showMessageDialog(null, "Nume de utilizator sau parola gresite!", "Informatii logare: " + "Logare", JOptionPane.ERROR_MESSAGE);
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
