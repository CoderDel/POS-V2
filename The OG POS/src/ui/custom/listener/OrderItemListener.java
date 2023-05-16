package ui.custom.listener;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import ui.custom.ItemUI;
import ui.custom.OrderItemPanel;

public class OrderItemListener implements MouseListener {

    ItemUI item;
    JPanel orderCnt;
    
    public OrderItemListener(ItemUI item, JPanel orderContainer) {
        this.item = item;
        orderCnt = orderContainer;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        OrderItemPanel orderItem = new OrderItemPanel(
                item.getFoodType(),
                item.getFoodName(),
                item.getPrice()
        );
        
        orderItem.setBounds(0, 0, orderItem.getPreferredSize().width, orderItem.getPreferredSize().height);
        
        orderCnt.add(orderItem);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        item.setBackground(new Color(66,66,66));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        item.setBackground(new Color(33, 33, 33));
    }
    
}
