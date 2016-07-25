/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

/**
 *
 * @author KV
 */
    public class cartItem {
        Label id = new Label();
        Label name = new Label();
        Label desc = new Label();
        Label price = new Label();
        Button removeButton = new Button("Remove");
        Spinner<Integer> spinBox = new Spinner<Integer>(0,10,0);
        ComboBox<Integer> cb = new ComboBox<Integer>();
        Item item;
        Boolean isInCart = false;
        int quantityOrdered;

        cartItem(){ 
            quantityOrdered=0;
            spinBox.setMaxWidth(65);
            cb.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
        }

    }
