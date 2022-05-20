/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.*;


/**
 *
 * @author oloft
 */
public class iMatMiniController implements Initializable, ShoppingCartListener {

    // MainPage
    @FXML
    private SplitPane mainPageSplitPane;

    // Front Header
    @FXML
    private Button ShoppingCartButton;

    @FXML
    private Button UserIconButton;

    // UserMeny
    @FXML
    private AnchorPane userMenyAnchorPane;

    // Shopping Pane
    @FXML
    private AnchorPane shopPane;
    @FXML
    private TextField searchField;
    @FXML
    private Label itemsLabel;
    @FXML
    private Label costLabel;
    @FXML
    private FlowPane productsFlowPane;
    @FXML
    private FlowPane productField;
    
    // Account Pane
    @FXML
    private AnchorPane accountPane;
    @FXML 
    ComboBox cardTypeCombo;

    @FXML 
    private ComboBox monthCombo;
    @FXML
    private ComboBox yearCombo;
    @FXML
    private TextField cvcField;
    @FXML
    private Label purchasesLabel;
    @FXML private Label greetingMulti;
    @FXML private StackPane multiWindow;
    @FXML private Button closeButtonMulti;

    // Användaruppgifter text
    @FXML private TextField dinaUppgifterNamn;
    @FXML private TextField dinaUppgifterEfternamn;
    @FXML private TextField dinaUppgifterMail;
    @FXML private TextField dinaUppgifterLeveransadress;
    @FXML private TextField dinaUppgifter;
    @FXML private TextField numberTextField;
    @FXML private TextField nameTextField;
    
    // Other variables
    private final Model model = Model.getInstance();

    // Shop pane actions
    @FXML
    private void handleShowAccountAction(ActionEvent event) {
        openAccountView();
    }
    
    @FXML
    private void handleSearchAction(ActionEvent event) {
        
        List<Product> matches = model.findProducts(searchField.getText());
        updateProductList(matches);
        System.out.println("# matching products: " + matches.size());
    }
    
    @FXML
    private void handleClearCartAction(ActionEvent event) {
        model.clearShoppingCart();
    }
    
    @FXML
    private void handleBuyItemsAction(ActionEvent event) {
        model.placeOrder();
        costLabel.setText("Köpet klart!");
    }
    
    // Account pane actions
     @FXML
    private void handleDoneAction(ActionEvent event) {
        closeAccountView();
    }

    @FXML
    private void openUserAction(ActionEvent event) { openUserOptionsView(); }
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        model.getShoppingCart().addShoppingCartListener(this);

        updateProductList(model.getProducts());
        updateBottomPanel();
        
        setupAccountPane();

        mainPageSplitPane.toFront();

        loadUser();

    }

    public void saveUser(){
        model.getCustomer().setFirstName(dinaUppgifterNamn.getText());
        model.getCustomer().setLastName(dinaUppgifterEfternamn.getText());
        model.getCustomer().setEmail(dinaUppgifterMail.getText());
        model.getCustomer().setAddress(dinaUppgifterLeveransadress.getText());
        System.out.println("#User saved");
    }

    public void loadUser(){
        dinaUppgifterNamn.setText(model.getCustomer().getFirstName());
        dinaUppgifterEfternamn.setText(model.getCustomer().getLastName());
        dinaUppgifterMail.setText(model.getCustomer().getEmail());
        dinaUppgifterLeveransadress.setText(model.getCustomer().getAddress());
    }
    
    // Navigation
    public void openAccountView() {
        updateAccountPanel();
        multiWindow.toFront();
        accountPane.toFront();
    }

    public void closeAccountView() {
        updateCreditCard();
        userMenyAnchorPane.toFront();
    }

    public void openUserOptionsView(){
        // behover en uppdate för namnet på profilen har
        greetingMulti.setText("Hej " + model.getCustomer().getFirstName() + "!");
        multiWindow.toFront();
        userMenyAnchorPane.toFront();
    }

    public void closeUserOptionsView(){
        mainPageSplitPane.toFront();
    }
    
    // Shope pane methods
    @Override
    public void shoppingCartChanged(CartEvent evt) {
        updateBottomPanel();
    }
   
    
    private void updateProductList(List<Product> products) {

        productField.getChildren().clear();

        for (Product product : products) {

            productField.getChildren().add(new ProductPanel(product));
        }

    }
    
    private void updateBottomPanel() {
        
        ShoppingCart shoppingCart = model.getShoppingCart();
        
        itemsLabel.setText("Antal varor: " + shoppingCart.getItems().size());
        costLabel.setText("Kostnad: " + String.format("%.2f",shoppingCart.getTotal()));
        
    }
    
    private void updateAccountPanel() {
        
        CreditCard card = model.getCreditCard();
        
        numberTextField.setText(card.getCardNumber());
        nameTextField.setText(card.getHoldersName());
        
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        cvcField.setText(""+card.getVerificationCode());
        
        purchasesLabel.setText(model.getNumberOfOrders()+ " tidigare inköp hos iMat");
        
    }
    
    private void updateCreditCard() {
        
        CreditCard card = model.getCreditCard();
        
        card.setCardNumber(numberTextField.getText());
        card.setHoldersName(nameTextField.getText());
        
        String selectedValue = (String) cardTypeCombo.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);
        
        selectedValue = (String) monthCombo.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));
        
        selectedValue = (String) yearCombo.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));
        
        card.setVerificationCode(Integer.parseInt(cvcField.getText()));

    }
    
    private void setupAccountPane() {
                
        cardTypeCombo.getItems().addAll(model.getCardTypes());
        
        monthCombo.getItems().addAll(model.getMonths());
        
        yearCombo.getItems().addAll(model.getYears());
        
    }
}
