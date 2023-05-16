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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import pos.ui.popup.ItemEditorUI;
import pos.ui.popup.PaymentUI;
import ui.custom.ItemUI;

public class MainUI extends javax.swing.JFrame {
    
    ArrayList<ItemUI> foodItems;
    DefaultTableModel tableModel;
    DefaultTableModel orderModel;
    
    int selectedOrderRow;
    
    //objects use for connecting and interacting with the DataBase
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public MainUI() {
        initComponents();
        
        totalCostTxt.setText("₱ 0.00");
        cashTxt.setText("₱ 0.00");
        changeTxt.setText("₱ 0.00");
        
        tableModel = (DefaultTableModel) itemTable.getModel();
        foodItems = new ArrayList();
        
        connect(); //connect sa database
        fetchData(); //loads all data to the table

        addItemToMenuContainer();
        
        //i-detect lang ani na listener if there are changes sa orderTable kay since gusto
        //nato na machange ang total cost visually
        //therefore, if naay changes (INSERT or DELETE), mag calculate sa total cost
        TableModelListener tml = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                calculateTotalCost();
            }
        };
        
        orderModel = (DefaultTableModel)orderTable.getModel();
        orderModel.addTableModelListener(tml);
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
        int selectedRow = itemTable.getSelectedRow();
        
        for(int i=0; i<foodItems.size(); i++) {
            if(foodItems.get(i).getFoodName()
                    .equals(itemTable.getValueAt(selectedRow, 1).toString())) {
                foodItems.remove(i);
            }
        }
        
        try {
            ps = con.prepareStatement("DELETE FROM food_items WHERE foodName = ?");

            
            ps.setString(1, itemTable.getValueAt(selectedRow, 1).toString());
            
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ///////////////////// FOR MENU ITEM CONTAINER METHODS   ///////////////////////
    //ui methods
    private final int defaultMenuHeight = 432;
    private int posY = 0;
    private final int gap = 5;
    
    //mag add syag item sa menu scrollpane
    private void addItemToMenuContainer() {
        for(int i=0; i<itemTable.getRowCount(); i++) {
            ItemUI item = new ItemUI(
                    (String) itemTable.getValueAt(i, 0),
                    (String) itemTable.getValueAt(i, 1),
                    (float) itemTable.getValueAt(i, 2),
                    orderTable
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
        
        for(int i=0; i<foodItems.size(); i++) {
            height += foodItems.get(i).getPreferredSize().height;
        }
        
        return height;
    }
    
    //i-update and menu
    public void updateMenu() {
        menuItemContainer.removeAll(); //tangalon tanan menu sa container
        
        //i-remove tanan items sa foodItems array
        foodItems.clear();
        
        //i-reset ang size sa menuItemContainer
        menuItemContainer.setPreferredSize(new Dimension(
                menuItemContainer.getPreferredSize().width, 
                defaultMenuHeight
        ));
        
        posY = 0; //back to zero ang y-position
        
        //System.out.println("Menue Height in Update: "+menuItemContainer.getPreferredSize().height);
        addItemToMenuContainer(); // i readd tanan items sa menu
        
        menuItemContainer.revalidate();
        menuItemContainer.repaint();
    }
    
    
    
    ///////////////////// ORDER TABLE METHODS    ///////////////////////
    public void calculateTotalCost() {
        float totalPrice = 0;
        
        for(int i=0; i<orderTable.getRowCount(); i++) {
            totalPrice += Float.parseFloat(orderTable.getValueAt(i, 2).toString()) * Float.parseFloat(orderTable.getValueAt(i, 3).toString());
        }
        
        totalCostTxt.setText("₱ "+totalPrice);
    }
    
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
        welcomeLabel = new javax.swing.JLabel();
        menuContainerPanel = new javax.swing.JPanel();
        container = new javax.swing.JPanel();
        filterContainer = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        filterComboBox = new javax.swing.JComboBox<>();
        menuItemScrollPane = new javax.swing.JScrollPane();
        menuItemContainer = new javax.swing.JPanel();
        orderPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        totalCostTxt = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cashTxt = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        changeTxt = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        removeOrderBtn = new javax.swing.JButton();
        reduceQtyBtn = new javax.swing.JButton();
        removeAllBtn = new javax.swing.JButton();
        payBtn = new javax.swing.JButton();
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

        welcomeLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        welcomeLabel.setForeground(new java.awt.Color(255, 255, 255));
        welcomeLabel.setText("Hello <Name Here>!, Welcome to FoodTerria POS System");

        javax.swing.GroupLayout homeContainerPanelLayout = new javax.swing.GroupLayout(homeContainerPanel);
        homeContainerPanel.setLayout(homeContainerPanelLayout);
        homeContainerPanelLayout.setHorizontalGroup(
            homeContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        homeContainerPanelLayout.setVerticalGroup(
            homeContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeContainerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeLabel)
                .addContainerGap(542, Short.MAX_VALUE))
        );

        menuPanel.addTab("Home", homeContainerPanel);

        menuContainerPanel.setBackground(new java.awt.Color(66, 66, 66));

        container.setBackground(new java.awt.Color(66, 66, 66));
        container.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        filterContainer.setBackground(new java.awt.Color(33, 33, 33));

        icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/icons/search.png"))); // NOI18N

        filterComboBox.setBackground(new java.awt.Color(66, 66, 66));
        filterComboBox.setForeground(new java.awt.Color(255, 255, 255));
        filterComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Filter", "Breakfast", "Lunch", "Dinner", "Dessert", "Drink" }));
        filterComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                filterComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout filterContainerLayout = new javax.swing.GroupLayout(filterContainer);
        filterContainer.setLayout(filterContainerLayout);
        filterContainerLayout.setHorizontalGroup(
            filterContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterComboBox, 0, 404, Short.MAX_VALUE)
                .addContainerGap())
        );
        filterContainerLayout.setVerticalGroup(
            filterContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(filterContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(icon)
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
            .addGap(0, 522, Short.MAX_VALUE)
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
                .addComponent(menuItemScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))
        );

        orderPanel.setBackground(new java.awt.Color(66, 66, 66));

        jPanel2.setBackground(new java.awt.Color(33, 33, 33));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ORDER");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TOTAL:");

        totalCostTxt.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        totalCostTxt.setForeground(new java.awt.Color(255, 255, 255));
        totalCostTxt.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        totalCostTxt.setText("₱ 999999.99");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("CASH:");

        cashTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cashTxt.setForeground(new java.awt.Color(255, 255, 255));
        cashTxt.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        cashTxt.setText("₱ 999999.99");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("CHANGE:");

        changeTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        changeTxt.setForeground(new java.awt.Color(255, 255, 255));
        changeTxt.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        changeTxt.setText("₱ 999999.99");

        orderTable.setBackground(new java.awt.Color(66, 66, 66));
        orderTable.setForeground(new java.awt.Color(255, 255, 255));
        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Food Type", "Food Name", "Price", "Qty."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderTable.setGridColor(new java.awt.Color(255, 255, 255));
        orderTable.getTableHeader().setReorderingAllowed(false);
        orderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(orderTable);
        if (orderTable.getColumnModel().getColumnCount() > 0) {
            orderTable.getColumnModel().getColumn(0).setResizable(false);
            orderTable.getColumnModel().getColumn(1).setResizable(false);
            orderTable.getColumnModel().getColumn(2).setResizable(false);
            orderTable.getColumnModel().getColumn(3).setResizable(false);
            orderTable.getColumnModel().getColumn(3).setPreferredWidth(10);
        }

        removeOrderBtn.setText("Remove Order");
        removeOrderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeOrderBtnActionPerformed(evt);
            }
        });

        reduceQtyBtn.setText("Reduce Qty.");
        reduceQtyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reduceQtyBtnActionPerformed(evt);
            }
        });

        removeAllBtn.setText("Remove All Orders");
        removeAllBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllBtnActionPerformed(evt);
            }
        });

        payBtn.setText("Continue to Payment");
        payBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout orderPanelLayout = new javax.swing.GroupLayout(orderPanel);
        orderPanel.setLayout(orderPanelLayout);
        orderPanelLayout.setHorizontalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addComponent(reduceQtyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeOrderBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(removeAllBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(totalCostTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(orderPanelLayout.createSequentialGroup()
                        .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cashTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(changeTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(payBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        orderPanelLayout.setVerticalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reduceQtyBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeAllBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(totalCostTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cashTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(changeTxt))
                .addGap(18, 18, 18)
                .addComponent(payBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        orderPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {payBtn, reduceQtyBtn, removeAllBtn, removeOrderBtn});

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
                            .addComponent(deleteItemBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)))
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

    private void payBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payBtnActionPerformed
        PaymentUI payment = new PaymentUI(totalCostTxt, cashTxt, changeTxt);
        payment.setVisible(true);
    }//GEN-LAST:event_payBtnActionPerformed

    //i remove tanan item sa orderTable
    private void removeAllBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllBtnActionPerformed
        orderModel.setRowCount(0);
        
        //reset ang cash & change value
        cashTxt.setText("₱  0.00");
        changeTxt.setText("₱  0.00");
    }//GEN-LAST:event_removeAllBtnActionPerformed

    private void reduceQtyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reduceQtyBtnActionPerformed
        System.out.println(selectedOrderRow);
        
        if(orderTable.getSelectedRow() > -1) {
            int qty = Integer.parseInt(orderTable.getValueAt(selectedOrderRow, 3).toString());
            if(qty > 1) {   
                orderTable.setValueAt(--qty, selectedOrderRow, 3);
                System.out.println("New Qty: "+orderTable.getValueAt(selectedOrderRow, 3)); 
            }
            else {
                orderModel.removeRow(selectedOrderRow);
            }
        }
        else {
            //show error dialog
            JOptionPane.showMessageDialog(this, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_reduceQtyBtnActionPerformed

    private void orderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderTableMouseClicked
        selectedOrderRow = orderTable.getSelectedRow();
    }//GEN-LAST:event_orderTableMouseClicked

    private void removeOrderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeOrderBtnActionPerformed
        
        if(orderTable.getRowCount() > 0 && orderTable.getSelectedRow() > -1)
            orderModel.removeRow(selectedOrderRow);
        else 
            JOptionPane.showMessageDialog(this, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_removeOrderBtnActionPerformed

    /////////////////////////////////////////////////
    /////////////////   FOR FILTER  /////////////////
    /////////////////////////////////////////////////
    private void filterComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterComboBoxItemStateChanged

        switch(filterComboBox.getSelectedItem().toString()) {
            case "No Filter":
                updateMenu();
                break;
            case "Breakfast":
                showMenuFromFilter(filterComboBox.getSelectedItem().toString());
                break;
            case "Lunch":
                showMenuFromFilter(filterComboBox.getSelectedItem().toString());
                break;
            case "Dinner":
                showMenuFromFilter(filterComboBox.getSelectedItem().toString());
                break;
            case "Dessert":
                showMenuFromFilter(filterComboBox.getSelectedItem().toString());
                break;
            case "Drink":
                showMenuFromFilter(filterComboBox.getSelectedItem().toString());
                break;
        }
    }//GEN-LAST:event_filterComboBoxItemStateChanged

    private void showMenuFromFilter(String filterType) {
        ArrayList<ItemUI> foods = new ArrayList();
        foods.clear();
        
        for(int i=0; i<foodItems.size(); i++) {
            if(foodItems.get(i).getFoodType().equalsIgnoreCase(filterType)) {
                foods.add(foodItems.get(i));
            }
        }
        
        posY = 0;
        menuItemContainer.removeAll();
        
        for(int i=0; i<foods.size(); i++) {
            foods.get(i).setBounds(0, posY, 
                    foods.get(i).getPreferredSize().width, 
                    foods.get(i).getPreferredSize().height
            );
            
            if(this.getRowHeight() > menuItemContainer.getPreferredSize().height) {
                menuItemContainer.setPreferredSize(new Dimension(
                        menuItemContainer.getPreferredSize().width,
                        menuItemContainer.getPreferredSize().height + foods.get(i).getPreferredSize().height + 1
                ));
                
                menuItemContainer.add(foods.get(i));
            }
            else {
                menuItemContainer.add(foods.get(i));
            }

            posY += foods.get(i).getPreferredSize().height + gap;
        }
        
        menuItemContainer.revalidate();
        menuItemContainer.repaint();
    }
    
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
    private javax.swing.JLabel cashTxt;
    private javax.swing.JLabel changeTxt;
    private javax.swing.JPanel container;
    private javax.swing.JButton deleteItemBtn;
    private javax.swing.JComboBox<String> filterComboBox;
    private javax.swing.JPanel filterContainer;
    private javax.swing.JPanel homeContainerPanel;
    private javax.swing.JLabel icon;
    private javax.swing.JPanel itemContainerPanel;
    private javax.swing.JTable itemTable;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel menuContainerPanel;
    private javax.swing.JPanel menuItemContainer;
    private javax.swing.JScrollPane menuItemScrollPane;
    private javax.swing.JTabbedPane menuPanel;
    private javax.swing.JPanel orderPanel;
    private javax.swing.JTable orderTable;
    private javax.swing.JButton payBtn;
    private javax.swing.JButton reduceQtyBtn;
    private javax.swing.JButton removeAllBtn;
    private javax.swing.JButton removeOrderBtn;
    private javax.swing.JLabel totalCostTxt;
    private javax.swing.JButton updateItemBtn;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
