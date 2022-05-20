/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

/**
 *
 * @author oloft
 */
public class ProductPanel extends AnchorPane {

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML Label ecoLabel;
    @FXML Label itemFrameLaggTill;
    @FXML AnchorPane ItemFrameAddandDeleteItem;
    @FXML TextField ItemFrameAmount;
    
    private Model model = Model.getInstance();

    private Product product;
    
    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public ProductPanel(Product product) {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemFrameNormal.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;
        nameLabel.setText(product.getName());
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        imageView.setImage(model.getImage(product, kImageWidth, kImageWidth*kImageRatio));
        if (!product.isEcological()) {
            ecoLabel.setText("");
        }
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

    }
    public void multiSub(){
        int index = findIndex();
        model.getShoppingCart().getItems().get(index).setAmount(model.getShoppingCart().getItems().get(index).getAmount() - 1);
        if (model.getShoppingCart().getItems().get(index).getAmount() <= 0){
            model.getShoppingCart().getItems().remove(index);
            ItemFrameAddandDeleteItem.toBack();

        }
        else{
            ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
            System.out.println(model.getShoppingCart().getItems().get(index).getTotal());
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
