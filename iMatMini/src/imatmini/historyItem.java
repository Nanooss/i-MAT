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
public class historyItem extends AnchorPane {

    //@FXML
    @FXML private Label orderNrID;
    @FXML private Label orderDate;
    @FXML private Label orderCard;
    @FXML private Label orderCardNumbers;
    @FXML private Label orderTotalPrice;



    private Model model = Model.getInstance();
    iMatMiniController iMatController;
    private Order order;
    private int index;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public historyItem(Order x,int i, iMatMiniController parentController) {
        order = x;
        index = i;
        iMatController = parentController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("previousOrder.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        loadOrder();
    }

    private void loadOrder() {
        orderNrID.setText(String.valueOf(model.getOrders().get(index).getOrderNumber()));
        orderDate.setText(setDate());
        double totalPrice = 0;
        for (ShoppingItem item : order.getItems()){
            totalPrice += item.getTotal();
        }
        orderTotalPrice.setText(String.valueOf(Math.round(totalPrice)) + "Kr");

    }

    public String setDate(){
        return String.valueOf(order.getDate().getDate()) +"-"+String.valueOf(order.getDate().getMonth()+1) +"-"+ String.valueOf(order.getDate().getYear()+1900);
    }


}
