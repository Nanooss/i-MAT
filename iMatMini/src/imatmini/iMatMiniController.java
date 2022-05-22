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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    ComboBox cardTypeCombo1;

    @FXML 
    private ComboBox monthCombo;
    @FXML
    private ComboBox monthCombo1;
    @FXML
    private ComboBox yearCombo;
    @FXML
    private ComboBox yearCombo1;
    @FXML
    private TextField cvcField;
    @FXML
    private TextField cvcField1;
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
    @FXML private TextField numberTextField1;
    @FXML private TextField nameTextField;
    @FXML private TextField nameTextField1;
    
    // Other variables
    private final Model model = Model.getInstance();


    //Wizard
    //@FXML private Button ShoppingCartButton;
    @FXML private AnchorPane wizzardPane;
    @FXML private AnchorPane wizzardVarukorg;
    @FXML private AnchorPane wizzardBetalkort;
    @FXML private AnchorPane wizardAdress;
    @FXML private AnchorPane wizardConfirm;

    @FXML private FlowPane wizzardCartFlowPane;

    @FXML private Label cartTotalValue;
    @FXML private Button buttonCart;
    @FXML private Label cartToPay;
    @FXML private Label payToAdress;
    @FXML private Label adressToConfirm;


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
        model.getShoppingCart().clear();
        model.getShoppingCart().addShoppingCartListener(this);

        updateProductList(model.getProducts());
        updateShoppingCart(model.getShoppingCart().getItems());
        updateBottomPanel();
        
        setupAccountPane();

        mainPageSplitPane.toFront();

        loadUser();





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

        if (!(numberTextField.getText().length() == 0)) numberTextField.setText(card.getCardNumber());
        if (!(nameTextField.getText().length() == 0))nameTextField.setText(card.getHoldersName());
        
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        cvcField.setText(""+card.getVerificationCode());
        
        purchasesLabel.setText(model.getNumberOfOrders()+ " tidigare inköp hos iMat");
        
    }
    private void updateAccountPanel1() {

        CreditCard card = model.getCreditCard();

        if (!(numberTextField1.getText().length() == 0)) numberTextField1.setText(card.getCardNumber());
        if (!(nameTextField1.getText().length() == 0))nameTextField1.setText(card.getHoldersName());

        cardTypeCombo1.getSelectionModel().select(card.getCardType());
        monthCombo1.getSelectionModel().select(""+card.getValidMonth());
        yearCombo1.getSelectionModel().select(""+card.getValidYear());

        cvcField1.setText(""+card.getVerificationCode());

    }
    
    private void updateCreditCard() {
        
        CreditCard card = model.getCreditCard();

        if (numberTextField.getText().length() == 0) card.setCardNumber("");
            else card.setCardNumber(numberTextField.getText());
        if (nameTextField.getText().length() == 0) card.setHoldersName("");
            else card.setHoldersName(nameTextField.getText());
        
        String selectedValue = (String) cardTypeCombo.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);
        
        selectedValue = (String) monthCombo.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));
        
        selectedValue = (String) yearCombo.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));

        if (cvcField.getText().length() == 0) card.setVerificationCode(000);
        else card.setVerificationCode(Integer.parseInt(cvcField.getText()));

    }
    private void updateCreditCard1() {

        CreditCard card = model.getCreditCard();

        if (numberTextField1.getText().length() == 0) card.setCardNumber("");
        else card.setCardNumber(numberTextField1.getText());
        if (nameTextField1.getText().length() == 0) card.setHoldersName("");
        else card.setHoldersName(nameTextField1.getText());

        String selectedValue = (String) cardTypeCombo1.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);

        selectedValue = (String) monthCombo1.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));

        selectedValue = (String) yearCombo1.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));

        if (cvcField1.getText().length() == 0) card.setVerificationCode(000);
        else card.setVerificationCode(Integer.parseInt(cvcField1.getText()));

    }
    
    private void setupAccountPane() {
                
        cardTypeCombo.getItems().addAll(model.getCardTypes());
        
        monthCombo.getItems().addAll(model.getMonths());
        
        yearCombo.getItems().addAll(model.getYears());

        cardTypeCombo1.getItems().addAll(model.getCardTypes());

        monthCombo1.getItems().addAll(model.getMonths());

        yearCombo1.getItems().addAll(model.getYears());
        
    }

    //MultiWindow
    // Navigation
    public void openUserOptionsView(){
        // behover en uppdate för namnet på profilen har
        greetingMulti.setText("Hej " + model.getCustomer().getFirstName() + "!");
        multiWindow.toFront();
        userMenyAnchorPane.toFront();
    }

    public void closeUserOptionsView(){
        mainPageSplitPane.toFront();
    }

    public void openAccountView() {
        updateAccountPanel();
        multiWindow.toFront();
        accountPane.toFront();
    }

    public void closeAccountView() {
        updateCreditCard();
        userMenyAnchorPane.toFront();
    }

    //AccountPane
    public void saveUser(){
        model.getCustomer().setFirstName(dinaUppgifterNamn.getText());
        model.getCustomer().setLastName(dinaUppgifterEfternamn.getText());
        model.getCustomer().setEmail(dinaUppgifterMail.getText());
        model.getCustomer().setAddress(dinaUppgifterLeveransadress.getText());
        System.out.println("#User saved");
        updateCreditCard();
    }

    public void loadUser(){
        dinaUppgifterNamn.setText(model.getCustomer().getFirstName());
        dinaUppgifterEfternamn.setText(model.getCustomer().getLastName());
        dinaUppgifterMail.setText(model.getCustomer().getEmail());
        dinaUppgifterLeveransadress.setText(model.getCustomer().getAddress());
    }


    //WizzardPane
    //Navigation
    public void openWizard(){
        updateShoppingCart(model.getShoppingCart().getItems());
        wizzardPane.toFront();
    }
    public void backToHome(){
        updateProductList(model.getProducts());
        mainPageSplitPane.toFront();
    }
    public void nextButtonCart(){
        loadPayment();
        if (checkCart()) wizzardBetalkort.toFront();

    }
    public void nextButtonPayment(){
        updateCreditCard1();
        wizardAdress.toFront();

    }
    public void nextButtonAdress(){
        saveAdress();
        wizardConfirm.toFront();
    }
    public void confirmButton(){
        loadConfirm();
        model.placeOrder();
    }
    public void backButtonCart(){
        updateProductList(model.getProducts());
        mainPageSplitPane.toFront();
    }
    public void backButtonPayment(){
        wizzardVarukorg.toFront();
    }
    public void backButtonAdress(){
        saveAdress();
        wizzardBetalkort.toFront();
    }
    public void backButtonConfirm(){
        wizardAdress.toFront();
    }

    //Functionality
    public boolean checkCart(){
        if (model.getShoppingCart().getItems().size() > 0) return true;
        else return false;
    }
    public void loadPayment(){
        updateAccountPanel1();
    }
    public void saveAdress(){}
    public void loadConfirm(){}

    //Backend
    public void updateShoppingCart(List<ShoppingItem> products) {
        if (model.getShoppingCart().getItems().size() == 0){cartToPay.setText("X");}
        else {cartToPay.setText("");}
        cartTotalValue.setText(Math.round(model.getShoppingCart().getTotal()) + " kr");
        wizzardCartFlowPane.getChildren().clear();
        int index = 0;
        for (ShoppingItem product : products) {

            wizzardCartFlowPane.getChildren().add(new cartItem(product.getProduct(), index, this));
            index += 1;
        }

    }

    public void updateWizardCreditCard(){

    }

}
