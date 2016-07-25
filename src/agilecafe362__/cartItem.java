/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
        Label addonInfo = new Label();
        Button removeButton = new Button("Remove");
        Spinner<Integer> spinBox = new Spinner<>(0,10,0);
        ComboBox<Integer> cb = new ComboBox<>();
        Item item;
        Boolean isInCart = false;
        int quantityOrdered;

        cartItem(){ 
            quantityOrdered=0;
            spinBox.setMaxWidth(65);
            cb.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
        }
        
        public void setAddonListUnchecked(){
            if(!item.getAddonList().isEmpty()){
            for(int i=0; i<item.getAddonList().size();i++){
                while(item.getAddonList().get(i).checkBox.isSelected()==true)
                {
                    item.getAddonList().get(i).checkBox.fire();
                }
                item.getAddonList().get(i).setChecked(false);
            }
            }
        }
        
        public void setAddonLabelInfo(){
            String tempText="With ";
            if(!item.getAddonList().isEmpty()){
                for(int i=0;i<item.getAddonList().size();i++)
                {
                    tempText+=item.getAddonList().get(i).getName()+" ($"+Double.toString(item.getAddonList().get(i).getPrice())+") ";
                }
            }
            addonInfo.setText(tempText);
        }
        
    }
