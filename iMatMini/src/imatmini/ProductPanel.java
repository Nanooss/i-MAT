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
import javafx.scene.control.Button;
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
    @FXML
    Button toggleFavorites;
    
    private Model model = Model.getInstance();

    public Product product;

    private iMatMiniController iMatController;
    
    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;
    int productIndex;

    public ProductPanel(Product product,int productIndex,iMatMiniController parentController) {
        this.productIndex=productIndex;
        iMatController = parentController;
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
        imageView.setImage(model.getImage(product));
        if (!product.isEcological()) {
            ecoLabel.setText("");
        }

        for (ShoppingItem item : model.getShoppingCart().getItems()) {
            if (item.getProduct() == product) {
                int index = findIndex();
                ItemFrameAddandDeleteItem.toFront();
                ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
            }
        }
        if(model.getFavorites().contains(product)) toggleFavorites.setText("Remove");
        else toggleFavorites.setText("Add");
    }
    

    public void handleAddAction() {
        System.out.println("Add " + product.getName());
        model.addToShoppingCart(product);
        ItemFrameAddandDeleteItem.toFront();
        int index = findIndex();
        ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
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
        iMatController.updateCartAmount();

    }
    public void multiSub(){
        int index = findIndex();
        model.getShoppingCart().getItems().get(index).setAmount(model.getShoppingCart().getItems().get(index).getAmount() - 1);
        if (model.getShoppingCart().getItems().get(index).getAmount() <= 0){
            model.getShoppingCart().getItems().remove(index);
            ItemFrameAddandDeleteItem.toBack();
            iMatController.updateCartAmount();


        }
        else{
            ItemFrameAmount.setText(String.valueOf(Math.round(model.getShoppingCart().getItems().get(index).getAmount())));
            System.out.println(model.getShoppingCart().getItems().get(index).getTotal());
            iMatController.updateCartAmount();
        }



    }

    public void addToFavorites(){
        model.addFavorites(product);
    }
    public void removeFavorite(){
        model.removeFavorites(product);
    }

    public void toggleFavorite(){
        if(model.getFavorites().contains(product)) {
            removeFavorite();
            toggleFavorites.setText("Add");
        }
        else{
            addToFavorites();
            toggleFavorites.setText("Remove");
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
