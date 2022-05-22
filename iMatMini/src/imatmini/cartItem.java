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

    private Model model = Model.getInstance();
    iMatMiniController iMatController;
    private Product product;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public cartItem(Product product, int index, iMatMiniController parentController) {

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
        itemFrameProductName.setText(product.getName());
        itemFramePrice.setText(String.valueOf(model.getShoppingCart().getItems().get(index).getTotal()) + " " + "kr");
        itemFrameImage.setImage(model.getImage(product, kImageWidth, kImageWidth*kImageRatio));
        ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
        /*if (!product.isEcological()) {
            ecoLabel.setText("");
        }*/
    }
    

    public void handleAddAction() {
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
        ItemFrameAddandDeleteItem.toFront();
        int index = findIndex();
        ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
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
        }

        catch(Exception e){
            System.out.println("#Add Error");

        }

    }

    public void multiAdd(){
        int index = findIndex();
        model.getShoppingCart().getItems().get(index).setAmount(model.getShoppingCart().getItems().get(index).getAmount() + 1);
        ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
        System.out.println(model.getShoppingCart().getItems().get(index).getTotal());
        iMatController.updateShoppingCart(model.getShoppingCart().getItems());

    }
    public void multiSub(){
        int index = findIndex();
        model.getShoppingCart().getItems().get(index).setAmount(model.getShoppingCart().getItems().get(index).getAmount() - 1);
        if (model.getShoppingCart().getItems().get(index).getAmount() <= 0){
            System.out.println("#Removed " + model.getShoppingCart().getItems().get(index).getProduct().getName());
            model.getShoppingCart().getItems().remove(index);
            ItemFrameAddandDeleteItem.toBack();
            iMatController.updateShoppingCart(model.getShoppingCart().getItems());
        }
        else{
            ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
            System.out.println(model.getShoppingCart().getItems().get(index).getTotal());
            iMatController.updateShoppingCart(model.getShoppingCart().getItems());
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
}
