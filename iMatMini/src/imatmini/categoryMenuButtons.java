/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oloft
 */
public class categoryMenuButtons extends AnchorPane {

    //@FXML
    @FXML Label categoryName;

    private Model model = Model.getInstance();
    iMatMiniController iMatController;
    private Product product;
    private ArrayList<ProductCategory> categories = new ArrayList<>();
    private String categorySearchName;

    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public categoryMenuButtons(String category, iMatMiniController parentController) {
        categorySearchName = category;
        iMatController = parentController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CategoryItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        categoryName.setText(category);
        applyCategory();
        }
        public void applyCategory(){
            switch (categorySearchName){
                case("Mejeri"):
                    categories.add(ProductCategory.DAIRIES);
                    break;
                case("Chark"):
                    categories.add(ProductCategory.FISH);
                    categories.add(ProductCategory.MEAT);
                    break;
                case("Sötsaker"):
                    categories.add(ProductCategory.SWEET);
                    break;
                case("Dricka"):
                    categories.add(ProductCategory.HOT_DRINKS);
                    categories.add(ProductCategory.COLD_DRINKS);
                    break;
                case("Frukt & Grönt"):
                    categories.add(ProductCategory.CABBAGE);
                    categories.add(ProductCategory.BERRY);
                    categories.add(ProductCategory.CITRUS_FRUIT);
                    categories.add(ProductCategory.EXOTIC_FRUIT);
                    categories.add(ProductCategory.FRUIT);
                    categories.add(ProductCategory.MELONS);
                    categories.add(ProductCategory.HERB);
                    categories.add(ProductCategory.VEGETABLE_FRUIT);
                    break;
                case("Kolonial"):
                    categories.add(ProductCategory.POD);
                    categories.add(ProductCategory.POTATO_RICE);
                    categories.add(ProductCategory.FLOUR_SUGAR_SALT);
                    categories.add(ProductCategory.NUTS_AND_SEEDS);
                    categories.add(ProductCategory.PASTA);
                    break;
                case("Bröd"):
                    categories.add(ProductCategory.BREAD);
                    break;
            }
        }
        
        public void updateCategory(){
            ArrayList<Product> matches = new ArrayList<>();
            for (Product product : model.getProducts()){
                if(categories.contains(product.getCategory())){
                    matches.add(product);
                }
            }
            iMatController.updateProductList(matches);
        
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
