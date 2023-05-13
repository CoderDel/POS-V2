package pos.ui.popup;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.main.MainUI;

public class ItemEditorUI extends javax.swing.JFrame {

    private Color selectedColor;
    private int selectedRow;
    //objects use for connecting and interacting with the DataBase
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    JTable itemTable;
    DefaultTableModel tableModel;
    
    public ItemEditorUI(String function, MainUI main, JTable itemTable) {
        initComponents();
        
        functionBtn.setText(function);
        initialize(itemTable);
        
        connect();
    }
    
    private void initialize(JTable itemTable) {
        this.itemTable = itemTable;
        this.tableModel = (DefaultTableModel) this.itemTable.getModel();
        
        selectedRow = itemTable.getSelectedRow(); //kuhaon ang value sa selected row
        System.out.println("Selected row: "+selectedRow);
        
        
        //set lang if update ang tuyo
        if(functionBtn.getText().equalsIgnoreCase("UPDATE")) {
            System.out.println("Selected row "+selectedRow);
            String foodType = (String) tableModel.getValueAt(selectedRow, 0);
            String foodName = (String) itemTable.getValueAt(selectedRow, 1);
            float price = (float) itemTable.getValueAt(selectedRow, 2);
            
            //butangan ug values ang input depnde sa asa nga row sa table ang gi select
            foodTypeComboBox.setSelectedItem(foodType);
            foodNameField.setText(foodName);
            priceField.setText(String.valueOf(price));
        }
    }
    
    private void connect() {
        //intialzing to connect to the databases
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully in ItemEditor");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost/foodterria", "root", "");
            
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void addItemToDatabase(String type, String name, float price) {
        try {
            ps = con.prepareStatement("INSERT INTO food_items(foodType, foodName, price) VALUES(?,?,?)");
            ps.setString(1, type);
            ps.setString(2, name);
            ps.setFloat(3, price);
            
            ps.executeUpdate();
            
            System.out.println("Database Item Added!");
        }
        catch(SQLException e) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void UpdateItemToDatabase(String type, String name, float price, int selectedRow) {
        try {
            ps = con.prepareStatement("UPDATE food_items SET foodType = ?, foodName = ?, price = ? WHERE id=?");
            ps.setString(1, type);
            ps.setString(2, name);
            ps.setFloat(3, price);
            ps.setInt(4, selectedRow+1);
            
            ps.executeUpdate();
            
            System.out.println("Database Item Updated!");
        }
        catch(SQLException e) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rootPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        foodNameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        foodTypeComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        priceField = new javax.swing.JTextField();
        functionBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FoodTerria | Item Editor");
        setResizable(false);

        rootPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("ITEM EDITOR");

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setText("Food Name");

        jLabel3.setText("Food Type");

        foodTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner", "Dessert", "Drink" }));
        foodTypeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                foodTypeComboBoxItemStateChanged(evt);
            }
        });

        jLabel4.setText("Price");

        priceField.setText("0.00");

        functionBtn.setText("HEHE");
        functionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                functionBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("CANCEL");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rootPanelLayout = new javax.swing.GroupLayout(rootPanel);
        rootPanel.setLayout(rootPanelLayout);
        rootPanelLayout.setHorizontalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(foodNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(foodTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addComponent(functionBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        rootPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {foodNameField, foodTypeComboBox});

        rootPanelLayout.setVerticalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(foodNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(foodTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(functionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn))
                .addContainerGap())
        );

        rootPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cancelBtn, functionBtn});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void foodTypeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_foodTypeComboBoxItemStateChanged
        switch(foodTypeComboBox.getSelectedItem().toString()) {
            case "Breakfast":
                selectedColor = new Color(255,245,157);
            break;
            case "Lunch":
                selectedColor = new Color(255,204,128);
            break;
            case "Dinner":
                selectedColor = new Color(206, 147, 216);
            break;
            case "Dessert":
                selectedColor = new Color(248,187,208);
            break;
            case "Drink":
                selectedColor = new Color(128,222,234);
            break;
        }
        
        topPanel.setBackground(selectedColor);
        functionBtn.setBackground(selectedColor);
    }//GEN-LAST:event_foodTypeComboBoxItemStateChanged

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.dispose(); //closes the ItemEditorUI
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void functionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_functionBtnActionPerformed
        
        if(functionBtn.getText().equalsIgnoreCase("ADD")) {
            //i add ang data sa database
            String foodType = (String)foodTypeComboBox.getSelectedItem();
            String foodName = foodNameField.getText();
            float price = Float.parseFloat(priceField.getText());
            
            addItemToDatabase(foodType, foodName, price); //nagbuhat kog function aron dli sya katag tan awon
            
            //i add ang data sa table
            Vector newRow = new Vector();
            
            for(int i=0; i<3; i++) {
                newRow.add(foodType);
                newRow.add(foodName);
                newRow.add(price);
            }
            
            tableModel.addRow(newRow);
        }
        else if(functionBtn.getText().equalsIgnoreCase("UPDATE")) {
            String type = foodTypeComboBox.getSelectedItem().toString();
            String name = foodNameField.getText();
            float price = Float.parseFloat(priceField.getText());
            
            UpdateItemToDatabase(type, name, price, selectedRow);
            
            //and purpose is i update ang value sa table (visually)
            tableModel.setValueAt(type, selectedRow, 0);
            tableModel.setValueAt(name, selectedRow, 1);
            tableModel.setValueAt(price, selectedRow, 2);
        }
        
        setVisible(false); //closes item editor
    }//GEN-LAST:event_functionBtnActionPerformed

//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ItemEditorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ItemEditorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ItemEditorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ItemEditorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ItemEditorUI().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField foodNameField;
    private javax.swing.JComboBox<String> foodTypeComboBox;
    private javax.swing.JButton functionBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField priceField;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
