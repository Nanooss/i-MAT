/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
public class frequentItem extends AnchorPane {

    @FXML ImageView frequentImage;
    @FXML Label frequentName;

    private Model model = Model.getInstance();

    public Product product;

    private iMatMiniController iMatController;

    public frequentItem(Product product, iMatMiniController parentController) {
        iMatController = parentController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("frequentItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;
        frequentName.setText(product.getName());
        frequentImage.setImage(model.getImage(product));
        //if (!product.isEcological()) {
        //    ecoLabel.setText("");
        //}

    }

    public void handleAddAction() {
        int index = findIndex();
            if(this.model.getShoppingCart().getItems().contains(findProduct())){
            model.getShoppingCart().getItems().get(index).setAmount(model.getShoppingCart().getItems().get(index).getAmount() + 1);
        }  else{ System.out.println("Add " + product.getName());
            model.addToShoppingCart(product);}
            iMatController.updateProduct(product);
        iMatController.updateCartAmount();
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

    private ShoppingItem findProduct(){
        for (ShoppingItem item : model.getShoppingCart().getItems()){
            if(item.getProduct() == product) {
                return item;
            }
        }
        return null;
    }

}
