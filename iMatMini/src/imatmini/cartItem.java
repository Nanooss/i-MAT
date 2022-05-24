/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class cartItem extends AnchorPane {

    //@FXML
    @FXML private ImageView itemFrameImage;
    @FXML private AnchorPane ItemFrameAddandDeleteItem;
    @FXML private TextField ItemFrameAmount;
    @FXML private AnchorPane itemFrameSubstractItem;
    @FXML private AnchorPane itemFrameAddItem;
    @FXML private Label itemFrameProductName;
    @FXML private Label itemFramePrice;
    @FXML private AnchorPane ItemFrameLaggTIll;


    private Model model = Model.getInstance();
    iMatMiniController iMatController;
    private Product product;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public cartItem(Product product, iMatMiniController parentController) {

        iMatController = parentController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemFrameWideWthAdd.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.product = product;

        ItemFrameLaggTIll.toBack();
        if(!(containsProduct(product))) {ItemFrameLaggTIll.toFront();
        }
        else{
            ItemFrameAddandDeleteItem.toFront();
            itemFramePrice.setText(String.valueOf(model.getShoppingCart().getItems().get(findIndex()).getTotal()) + " " + "kr");
            ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(findIndex()).getAmount())));}


        itemFrameProductName.setText(product.getName());

        itemFrameImage.setImage(model.getImage(product, kImageWidth, kImageWidth*kImageRatio));


        /*if (!product.isEcological()) {
            ecoLabel.setText("");
        }*/
    }
    

    public void handleAddAction() {
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
        ItemFrameLaggTIll.toBack();
        int index = findIndex();
        ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
        itemFramePrice.setText(model.getShoppingCart().getItems().get(findIndex()).getTotal() + " Kr");
        iMatController.updateCartAmount();
    }

    @FXML
    private void handleMultiAdd(ActionEvent event) {

        int index = findIndex();

        try{
            model.getShoppingCart().getItems().get(index).setAmount(Double.parseDouble(ItemFrameAmount.getText()));
            System.out.println(model.getShoppingCart().getItems().get(index).getTotal());
            iMatController.updateShoppingCart(model.getShoppingCart().getItems());
            if (model.getShoppingCart().getItems().get(index).getAmount() <= 0){
                System.out.println("#Removed " + model.getShoppingCart().getItems().get(index).getProduct().getName());
                model.getShoppingCart().getItems().remove(index);
                ItemFrameAddandDeleteItem.toBack();
                iMatController.updateShoppingCart(model.getShoppingCart().getItems());
            }
            itemFramePrice.setText(model.getShoppingCart().getItems().get(findIndex()).getTotal() + " Kr");
        }

        catch(Exception e){
            System.out.println("#Add Error");

        }
        iMatController.updateCartAmount();

    }

    public void multiAdd(){
        int index = findIndex();
        model.getShoppingCart().getItems().get(index).setAmount(model.getShoppingCart().getItems().get(index).getAmount() + 1);
        ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
        System.out.println(model.getShoppingCart().getItems().get(index).getTotal());
        iMatController.updateShoppingCart(model.getShoppingCart().getItems());
        itemFramePrice.setText(model.getShoppingCart().getItems().get(findIndex()).getTotal() + " Kr");

    }
    public void multiSub(){
        int index = findIndex();
        model.getShoppingCart().getItems().get(index).setAmount(model.getShoppingCart().getItems().get(index).getAmount() - 1);
        if (model.getShoppingCart().getItems().get(index).getAmount() <= 0){
            System.out.println("#Removed " + model.getShoppingCart().getItems().get(index).getProduct().getName());
            model.getShoppingCart().getItems().remove(index);
            ItemFrameAddandDeleteItem.toBack();
            iMatController.updateShoppingCart(model.getShoppingCart().getItems());
            itemFramePrice.setText("0,00 Kr");
        }
        else{
            ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
            System.out.println(model.getShoppingCart().getItems().get(index).getTotal());
            iMatController.updateShoppingCart(model.getShoppingCart().getItems());
            itemFramePrice.setText(model.getShoppingCart().getItems().get(findIndex()).getTotal() + " Kr");
        }



    }

    private int findIndex(){
        int index = 0;
        for (ShoppingItem item : model.getShoppingCart().getItems()){
            if(item.getProduct() == product) {
            break;
            }
            index += 1;
        }
        return index;
    }

    private boolean containsProduct(Product product) {
        for (ShoppingItem item : model.getShoppingCart().getItems()){
            if(item.getProduct() == product) return true;
        }
        return false;
    }
}
