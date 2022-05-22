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
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class wideHistory extends AnchorPane {

    //@FXML
    @FXML private ImageView itemFrameImage;
    @FXML private Label itemFrameProductName;
    @FXML private Label itemFramePrice;
    @FXML private Label ItemFrameAmount;

    private Model model = Model.getInstance();
    iMatMiniController iMatController;
    private Product product;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public wideHistory(ShoppingItem item, int index, iMatMiniController parentController) {

        iMatController = parentController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemFrameWideHistory.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        itemFrameImage.setImage(model.getImage(item.getProduct()));
        itemFrameProductName.setText(item.getProduct().getName());
        itemFramePrice.setText(String.valueOf(Math.round(item.getTotal())) + " Kr");
        ItemFrameAmount.setText(String.valueOf(item.getAmount()));

        /*
        @FXML private ImageView itemFrameImage;
        @FXML private Label itemFrameProductName;
        @FXML private Label itemFramePrice;
        @FXML private Label ItemFrameAmount;
        */

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
