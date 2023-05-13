package ui.main;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Vector;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import pos.ui.popup.ItemEditorUI;
import ui.custom.ItemUI;

public class MainUI extends javax.swing.JFrame {
    
    ArrayList<ItemUI> foodItems;
    DefaultTableModel tableModel;
    
    //objects use for connecting and interacting with the DataBase
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public MainUI() {
        initComponents();
        
        tableModel = (DefaultTableModel) itemTable.getModel();
        foodItems = new ArrayList();
        
        connect(); 
        fetchData(); //loads all data to the table

        addItemToMenuContainer();
        
        
//        ItemUI item = new ItemUI("Wendel", 90.0f, "Drink");
//        item.setBounds(0, 0, item.getPreferredSize().width, item.getPreferredSize().height);
//        menuItemContainer.add(item);
        
    }
    
    //app to database methods
    private void connect() {
        //intialzing to connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully in MainUI");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost/foodterria", "root", "");
            
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void fetchData() {
        //getting all the data from database to the table
        try {
            ps = con.prepareStatement("SELECT * FROM food_items");  //prepares a statement that retrieves all row and columns of the table in database
            rs = ps.executeQuery(); // i execute and statement ug ibutang sa (rs) na object and data nga nakuha sa database
            ResultSetMetaData rss = rs.getMetaData(); //kuhaon niya ang metadata sa (rs) (para ma access lang nimo ang column sa database table mao ra)
            int columnIndex = rss.getColumnCount();
            
            tableModel.setRowCount(0); //i set and itemTable na row to 0

            //i access niya ang table sa database ug ibutang niya tanan ang data sa itemTable (kanang makita sa atong system na table)
            while(rs.next()) {
                Vector v = new Vector();
                for(int i=0; i<columnIndex; i++) {
                    v.add(rs.getString("foodType"));
                    v.add(rs.getString("foodName"));
                    v.add(rs.getFloat("price"));
                }
                
                tableModel.addRow(v);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void deleteData() {
        try {
            ps = con.prepareStatement("DELETE FROM food_items WHERE id = ?");

            ps.setInt(1, itemTable.getSelectedRow() + 1);
            
            System.out.println("Item Deleted: "+ itemTable.getSelectedRow() + 1);
            
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ///////////////////// DONT MIND THIS    ///////////////////////
    //ui methods
    private final int defaultMenuHeight = 432;
    private int posY = 0;
    private final int gap = 5;
    
    //mag add syag item sa menu scrollpane
    private void addItemToMenuContainer() {
        for(int i=0; i<itemTable.getRowCount(); i++) {
            ItemUI item = new ItemUI(
                    (String) tableModel.getValueAt(i, 0),
                    (String) tableModel.getValueAt(i, 1),
                    (float) tableModel.getValueAt(i, 2)
            );
            
            foodItems.add(item); //ibutang ang item sa foodItem array para gamiton later
            
            item.setBounds(0, posY, item.getPreferredSize().width, item.getPreferredSize().height);
            
            if(this.getRowHeight() > menuItemContainer.getPreferredSize().height) {
                menuItemContainer.setPreferredSize(new Dimension(
                        menuItemContainer.getPreferredSize().width,
                        menuItemContainer.getPreferredSize().height + item.getPreferredSize().height + 1
                ));
                
                menuItemContainer.add(item);
            }
            else {
                menuItemContainer.add(item);
            }

            posY += item.getPreferredSize().height + gap;
        }
    }
    
    private int getRowHeight() {
        int height = 0;
        
        for(int i=0; i<foodItems.size(); i++) 
            height += foodItems.get(i).getPreferredSize().height;
            
        return height;
    }
    
    //i-update and menu
    public void updateMenu() {
        menuItemContainer.removeAll(); //tangalon tanan menu sa container
        menuItemContainer.setPreferredSize(new Dimension(menuItemContainer.getPreferredSize().width, defaultMenuHeight));
        posY = 0; //back to zero ang position
        
        addItemToMenuContainer(); // i readd tanan items sa menu
    }
    
    
    
    ///////////////////// DONT MIND THIS    ///////////////////////
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        bodyPanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JTabbedPane();
        homeContainerPanel = new javax.swing.JPanel();
        menuContainerPanel = new javax.swing.JPanel();
        container = new javax.swing.JPanel();
        filterContainer = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        searchBar = new javax.swing.JTextField();
        filterComboBox = new javax.swing.JComboBox<>();
        menuItemScrollPane = new javax.swing.JScrollPane();
        menuItemContainer = new javax.swing.JPanel();
        orderPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        orderScrollPane = new javax.swing.JScrollPane();
        orderItemContainer = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        totalCostText = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cashText = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        changeText = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        itemContainerPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        deleteItemBtn = new javax.swing.JButton();
        updateItemBtn = new javax.swing.JButton();
        addItemBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FoodTerria | Main");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(33, 33, 33));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Company Name");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Sign out");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        bodyPanel.setBackground(new java.awt.Color(66, 66, 66));

        menuPanel.setBackground(new java.awt.Color(66, 66, 66));
        menuPanel.setForeground(new java.awt.Color(255, 255, 255));
        menuPanel.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        homeContainerPanel.setBackground(new java.awt.Color(66, 66, 66));

        javax.swing.GroupLayout homeContainerPanelLayout = new javax.swing.GroupLayout(homeContainerPanel);
        homeContainerPanel.setLayout(homeContainerPanelLayout);
        homeContainerPanelLayout.setHorizontalGroup(
            homeContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 792, Short.MAX_VALUE)
        );
        homeContainerPanelLayout.setVerticalGroup(
            homeContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
        );

        menuPanel.addTab("Home", homeContainerPanel);

        menuContainerPanel.setBackground(new java.awt.Color(66, 66, 66));

        container.setBackground(new java.awt.Color(66, 66, 66));
        container.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        filterContainer.setBackground(new java.awt.Color(33, 33, 33));

        icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/icons/search.png"))); // NOI18N

        searchBar.setBackground(new java.awt.Color(66, 66, 66));
        searchBar.setForeground(new java.awt.Color(204, 204, 204));
        searchBar.setText("Search item...");

        filterComboBox.setBackground(new java.awt.Color(66, 66, 66));
        filterComboBox.setForeground(new java.awt.Color(255, 255, 255));
        filterComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Filter", "Breakfast", "Lunch", "Dinner", "Dessert", "Drink" }));

        javax.swing.GroupLayout filterContainerLayout = new javax.swing.GroupLayout(filterContainer);
        filterContainer.setLayout(filterContainerLayout);
        filterContainerLayout.setHorizontalGroup(
            filterContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterComboBox, 0, 190, Short.MAX_VALUE)
                .addContainerGap())
        );
        filterContainerLayout.setVerticalGroup(
            filterContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(filterContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(filterContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuItemScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menuItemScrollPane.setPreferredSize(new java.awt.Dimension(100, 432));

        menuItemContainer.setBackground(new java.awt.Color(66, 66, 66));
        menuItemContainer.setPreferredSize(new java.awt.Dimension(457, 432));

        javax.swing.GroupLayout menuItemContainerLayout = new javax.swing.GroupLayout(menuItemContainer);
        menuItemContainer.setLayout(menuItemContainerLayout);
        menuItemContainerLayout.setHorizontalGroup(
            menuItemContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 457, Short.MAX_VALUE)
        );
        menuItemContainerLayout.setVerticalGroup(
            menuItemContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 432, Short.MAX_VALUE)
        );

        menuItemScrollPane.setViewportView(menuItemContainer);

        javax.swing.GroupLayout containerLayout = new javax.swing.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(filterContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(menuItemScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLayout.createSequentialGroup()
                .addComponent(filterContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuItemScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        orderPanel.setBackground(new java.awt.Color(66, 66, 66));

        jPanel2.setBackground(new java.awt.Color(33, 33, 33));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Name");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        orderScrollPane.setBackground(new java.awt.Color(66, 66, 66));
        orderScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        orderScrollPane.setPreferredSize(new java.awt.Dimension(100, 238));

        orderItemContainer.setBackground(new java.awt.Color(66, 66, 66));
        orderItemContainer.setPreferredSize(new java.awt.Dimension(542, 240));

        javax.swing.GroupLayout orderItemContainerLayout = new javax.swing.GroupLayout(orderItemContainer);
        orderItemContainer.setLayout(orderItemContainerLayout);
        orderItemContainerLayout.setHorizontalGroup(
            orderItemContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 542, Short.MAX_VALUE)
        );
        orderItemContainerLayout.setVerticalGroup(
            orderItemContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        orderScrollPane.setViewportView(orderItemContainer);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TOTAL:");

        totalCostText.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        totalCostText.setForeground(new java.awt.Color(255, 255, 255));
        totalCostText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        totalCostText.setText("₱999999.99");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("CASH:");

        cashText.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cashText.setForeground(new java.awt.Color(255, 255, 255));
        cashText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        cashText.setText("₱999999.99");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("CHANGE:");

        changeText.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        changeText.setForeground(new java.awt.Color(255, 255, 255));
        changeText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        changeText.setText("₱999999.99");

        jButton1.setText("Continue to Payment");

        jButton2.setText("Remove All Orders");

        javax.swing.GroupLayout orderPanelLayout = new javax.swing.GroupLayout(orderPanel);
        orderPanel.setLayout(orderPanelLayout);
        orderPanelLayout.setHorizontalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(orderScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalCostText, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cashText, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(changeText, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
        );
        orderPanelLayout.setVerticalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(totalCostText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cashText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(changeText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        orderPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2});

        javax.swing.GroupLayout menuContainerPanelLayout = new javax.swing.GroupLayout(menuContainerPanel);
        menuContainerPanel.setLayout(menuContainerPanelLayout);
        menuContainerPanelLayout.setHorizontalGroup(
            menuContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(orderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        menuContainerPanelLayout.setVerticalGroup(
            menuContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(orderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        menuPanel.addTab("Menu", menuContainerPanel);

        itemContainerPanel.setBackground(new java.awt.Color(66, 66, 66));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ITEM TABLE");

        itemTable.setBackground(new java.awt.Color(66, 66, 66));
        itemTable.setForeground(new java.awt.Color(255, 255, 255));
        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Food Type", "Food Name", "Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemTable.setGridColor(new java.awt.Color(255, 255, 255));
        itemTable.setShowGrid(false);
        itemTable.setShowHorizontalLines(true);
        itemTable.setShowVerticalLines(true);
        itemTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(itemTable);
        if (itemTable.getColumnModel().getColumnCount() > 0) {
            itemTable.getColumnModel().getColumn(0).setResizable(false);
            itemTable.getColumnModel().getColumn(1).setResizable(false);
            itemTable.getColumnModel().getColumn(2).setResizable(false);
        }

        deleteItemBtn.setText("Delete Item");
        deleteItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItemBtnActionPerformed(evt);
            }
        });

        updateItemBtn.setText("Update Item");
        updateItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateItemBtnActionPerformed(evt);
            }
        });

        addItemBtn.setText("Add Item");
        addItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout itemContainerPanelLayout = new javax.swing.GroupLayout(itemContainerPanel);
        itemContainerPanel.setLayout(itemContainerPanelLayout);
        itemContainerPanelLayout.setHorizontalGroup(
            itemContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(itemContainerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(itemContainerPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(itemContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deleteItemBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(updateItemBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addItemBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        itemContainerPanelLayout.setVerticalGroup(
            itemContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, itemContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(itemContainerPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addItemBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateItemBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(itemContainerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)))
                .addContainerGap())
        );

        itemContainerPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addItemBtn, deleteItemBtn, updateItemBtn});

        menuPanel.addTab("Items", itemContainerPanel);

        menuPanel.setSelectedIndex(1);

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPanel)
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPanel)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemBtnActionPerformed
        ItemEditorUI itemEditor = new ItemEditorUI("ADD", this, itemTable);
        itemEditor.setVisible(true);
    }//GEN-LAST:event_addItemBtnActionPerformed

    private void updateItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateItemBtnActionPerformed
        ItemEditorUI itemEditor = new ItemEditorUI("UPDATE", this, itemTable);
        itemEditor.setVisible(true);
    }//GEN-LAST:event_updateItemBtnActionPerformed

    private void deleteItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemBtnActionPerformed
        //i check niya if naa bay selected na row
        if(itemTable.getSelectedRow() >= 0) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you really want to delete this row?", 
                "Delete Row", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            //check niya ang value sa choice (0 = yes, 1 = no)
            if(choice == 0) {
                deleteData();
                tableModel.removeRow(itemTable.getSelectedRow());
                this.updateMenu();
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteItemBtnActionPerformed

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItemBtn;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JLabel cashText;
    private javax.swing.JLabel changeText;
    private javax.swing.JPanel container;
    private javax.swing.JButton deleteItemBtn;
    private javax.swing.JComboBox<String> filterComboBox;
    private javax.swing.JPanel filterContainer;
    private javax.swing.JPanel homeContainerPanel;
    private javax.swing.JLabel icon;
    private javax.swing.JPanel itemContainerPanel;
    private javax.swing.JTable itemTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel menuContainerPanel;
    private javax.swing.JPanel menuItemContainer;
    private javax.swing.JScrollPane menuItemScrollPane;
    private javax.swing.JTabbedPane menuPanel;
    private javax.swing.JPanel orderItemContainer;
    private javax.swing.JPanel orderPanel;
    private javax.swing.JScrollPane orderScrollPane;
    private javax.swing.JTextField searchBar;
    private javax.swing.JLabel totalCostText;
    private javax.swing.JButton updateItemBtn;
    // End of variables declaration//GEN-END:variables
}
