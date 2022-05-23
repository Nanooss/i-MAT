/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    ComboBox wizardCardTypeCombo;

    @FXML 
    private ComboBox monthCombo;
    @FXML
    private ComboBox wizardMonthCombo;
    @FXML
    private ComboBox yearCombo;
    @FXML
    private ComboBox wizardYearCombo;
    @FXML
    private TextField cvcField;
    @FXML
    private TextField wizardCvcField;
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
    @FXML private TextField wizardNumberTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField wizardNameTextField;
    
    // Other variables
    private final Model model = Model.getInstance();


    //Wizard
    //@FXML private Button ShoppingCartButton;
    @FXML private AnchorPane wizzardPane;
    @FXML private AnchorPane wizzardVarukorg;
    @FXML private AnchorPane wizzardBetalkort;
    @FXML private AnchorPane wizardAdress;
    @FXML private AnchorPane wizardConfirm;
    @FXML private AnchorPane wizardFinal;

    @FXML private FlowPane wizzardCartFlowPane;

    @FXML private Label cartTotalValue;
    @FXML private Button buttonCart;
    @FXML private Label cartToPay;
    @FXML private Label payToAdress;
    @FXML private Label adressToConfirm;
    @FXML private TextField wizardNameField;
    @FXML private TextField wizardLastnameField;
    @FXML private TextField wizardEmailField;
    @FXML private TextField wizardAdressField;

    @FXML private Label confirmName;
    @FXML private Label confirmCard;
    @FXML private Label confirmCardDate;
    @FXML private Label confirmMail;
    @FXML private Label confirmAdress;
    @FXML private Label confirmPrice;


    //Orders
    @FXML AnchorPane multiGamlaBPane;
    @FXML AnchorPane multimultiGamla;
    @FXML FlowPane oldOrdersFlowPane;
    @FXML AnchorPane expandedOrderView;
    @FXML Label orderNumber;
    @FXML Label dateOldOrder;
    @FXML Label totalPriceOldOrder;
    @FXML FlowPane multigammelgam;

    //Category
    @FXML FlowPane categoryMenu;

    //CartUpdate
    @FXML Label cartAmount;
    @FXML Label redCircle;

    //Favorites
    @FXML AnchorPane favoriteMenuAnchorPane;
    @FXML FlowPane favoritesFlowPane;

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

        mainPageSplitPane.toFront();
        loadUser();
        setupAccountPane();
        setupPayment();
        loadOrders();
        loadCategorys();
        updateCartAmount();



    }
    // Shope pane methods
    @Override
    public void shoppingCartChanged(CartEvent evt) {
        updateBottomPanel();
    }
   
    
    void updateProductList(List<Product> products) {

        productField.getChildren().clear();

        for (Product product : products) {

            productField.getChildren().add(new ProductPanel(product, this));
        }

    }
    
    private void updateBottomPanel() {
        
        ShoppingCart shoppingCart = model.getShoppingCart();
        
        itemsLabel.setText("Antal varor: " + shoppingCart.getItems().size());
        costLabel.setText("Kostnad: " + String.format("%.2f",shoppingCart.getTotal()));
        
    }

    private void updateAccountPanel() {
        
        CreditCard card = model.getCreditCard();

        numberTextField.setText(model.getCreditCard().getCardNumber());
        nameTextField.setText(model.getCreditCard().getHoldersName());
        
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        cvcField.setText(""+card.getVerificationCode());
        
        purchasesLabel.setText(model.getNumberOfOrders()+ " tidigare inköp hos iMat");
        
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
    
    private void setupAccountPane() {
                
        cardTypeCombo.getItems().addAll(model.getCardTypes());
        
        monthCombo.getItems().addAll(model.getMonths());
        
        yearCombo.getItems().addAll(model.getYears());
    }
    public void setupPayment(){
        wizardCardTypeCombo.getItems().addAll(model.getCardTypes());

        wizardMonthCombo.getItems().addAll(model.getMonths());

        wizardYearCombo.getItems().addAll(model.getYears());
    }

    //MultiWindow
    // Navigation
    public void openUserOptionsView(){
        multiGamlaBPane.setOpacity(0);
        greetingMulti.setText("Hej " + model.getCustomer().getFirstName() + "!");
        multiWindow.toFront();
        userMenyAnchorPane.toFront();
    }

    public void closeUserOptionsView(){
        mainPageSplitPane.toFront();
        updateProductList(model.getProducts());
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

    public void openOrderView(){
        multiGamlaBPane.setOpacity(100);
        multiGamlaBPane.toFront();
        multimultiGamla.toFront();
    }

    public void closeOrderView(){
        multiGamlaBPane.setOpacity(0);
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
        updateAccountPanel();
        dinaUppgifterNamn.setText(model.getCustomer().getFirstName());
        dinaUppgifterEfternamn.setText(model.getCustomer().getLastName());
        dinaUppgifterMail.setText(model.getCustomer().getEmail());
        dinaUppgifterLeveransadress.setText(model.getCustomer().getAddress());
    }

    //WizzardPane
    //Navigation
    public void openWizard(){
        updateShoppingCart(model.getShoppingCart().getItems());
        wizzardVarukorg.toFront();
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
        updateWizardCreditCard();
        wizardAdress.toFront();
        loadAdress();

    }
    public void nextButtonAdress(){
        wizardConfirm.toFront();
        saveAdress();
        loadConfirm();
    }
    public void confirmButton(){
        loadConfirm();
        model.placeOrder();
        wizardFinal.toFront();
        loadOrders();
    }
    public void backButtonCart(){
        updateProductList(model.getProducts());
        mainPageSplitPane.toFront();
    }
    public void backButtonPayment(){
        wizzardVarukorg.toFront();
    }
    public void backButtonAdress(){
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

        CreditCard card = model.getCreditCard();

        wizardNumberTextField.setText(card.getCardNumber());
        wizardNameTextField.setText(card.getHoldersName());

        wizardCardTypeCombo.getSelectionModel().select(card.getCardType());
        wizardMonthCombo.getSelectionModel().select(""+card.getValidMonth());
        wizardYearCombo.getSelectionModel().select(""+card.getValidYear());

        wizardCvcField.setText(""+card.getVerificationCode());
    }
    public void loadAdress(){
        wizardNameField.setText(model.getCustomer().getFirstName());
        wizardLastnameField.setText(model.getCustomer().getLastName());
        wizardEmailField.setText(model.getCustomer().getEmail());
        wizardAdressField.setText(model.getCustomer().getAddress());
    }
    public void saveAdress(){
        model.getCustomer().setFirstName(wizardNameField.getText());
        model.getCustomer().setLastName(wizardLastnameField.getText());
        model.getCustomer().setEmail(wizardEmailField.getText());
        model.getCustomer().setAddress(wizardAdressField.getText());
        System.out.println("#User saved");

    }
    public void loadConfirm(){
        confirmName.setText(model.getCustomer().getFirstName() + model.getCustomer().getLastName());
        confirmCard.setText(model.getCreditCard().getCardNumber());
        confirmCardDate.setText(model.getCreditCard().getValidMonth()+"/"+model.getCreditCard().getValidYear());
        confirmMail.setText(model.getCustomer().getEmail());
        confirmAdress.setText(model.getCustomer().getAddress());
        confirmPrice.setText(String.valueOf(Math.round(model.getShoppingCart().getTotal()))+ " Kr");

    }


    //Backend
    public void updateShoppingCart(List<ShoppingItem> products) {
        if (model.getShoppingCart().getItems().size() == 0){cartToPay.setText("X");}
        else {cartToPay.setText("");}
        cartTotalValue.setText(Math.round(model.getShoppingCart().getTotal()) + " kr");
        wizzardCartFlowPane.getChildren().clear();
        int index = 0;
        for (ShoppingItem product : products) {

            wizzardCartFlowPane.getChildren().add(new cartItem(product.getProduct(), this));
            index += 1;
        }

    }

    public void updateWizardCreditCard(){
        CreditCard card = model.getCreditCard();

        if (wizardNumberTextField.getText().length() == 0) card.setCardNumber("");
        else card.setCardNumber(wizardNumberTextField.getText());
        if (wizardNameTextField.getText().length() == 0) card.setHoldersName("");
        else card.setHoldersName(wizardNameTextField.getText());

        String selectedValue = (String) wizardCardTypeCombo.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);

        selectedValue = (String) wizardMonthCombo.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));

        selectedValue = (String) wizardYearCombo.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));

        if (wizardCvcField.getText().length() == 0) card.setVerificationCode(000);
        else card.setVerificationCode(Integer.parseInt(wizardCvcField.getText()));
    }


    //Orders
    public void loadOrders(){
        oldOrdersFlowPane.getChildren().clear();
        System.out.println(model.getOrders());
        int i = 0;
        for ( Order order : model.getOrders()) {
            oldOrdersFlowPane.getChildren().add(new historyItem(order, i ,this));
            i+=1;
        }
    }

    public void closeExpanded(){
        expandedOrderView.toBack();
    }

    //Category

    public void loadCategorys(){
        categoryMenu.getChildren().clear();
        String categorys[] = {
                "Mejeri",
                "Chark",
                "Dricka",
                "Sötsaker",
                "Bröd",
                "Frukt & Grönt",
                "Kolonial"

        };
        for ( String category : categorys) {
            categoryMenu.getChildren().add(new categoryMenuButtons(category,this));
        }
    }

    //cartAmount
    public void updateCartAmount(){
        if(model.getShoppingCart().getItems().size() > 0){
            redCircle.setOpacity(100);
            cartAmount.setOpacity(100);
            int amountInCart=0;
            for (ShoppingItem item : model.getShoppingCart().getItems()){
                amountInCart += Math.round(item.getAmount());
            }
            if(amountInCart > 99) cartAmount.setText(99 + "+");
            else cartAmount.setText(String.valueOf(amountInCart));
        }else{
            redCircle.setOpacity(0);
            cartAmount.setOpacity(0);
        }
    }

    //favorites

    public void openFavorites(){
        loadFavorites();
        favoriteMenuAnchorPane.toFront();
    }

    public void loadFavorites(){
        model.getFavorites();
        favoritesFlowPane.getChildren().clear();
        for (Product product : model.getFavorites()) {

            favoritesFlowPane.getChildren().add(new cartItem(product, this));
        }

    }


}
