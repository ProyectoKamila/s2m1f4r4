/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semaforo;

import static com.sun.scenario.effect.impl.prism.PrEffectHelper.render;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import static org.apache.log4j.DefaultThrowableRenderer.render;
import semaforo.Semaforo.ResetCellRenderer;
import static semaforo.Semaforo.colorFontCeldaWeeks;

/**
 *
 * @author Developer
 */
public class newLowHigh extends javax.swing.JFrame {
    /**
     * Creates new form newLowHigh
     */
    public static ResultSet reultado;
    
    public newLowHigh() throws SQLException {
        initComponents();
        //formateaCabeceroTicker();
        setUp();
        formateaCabeceroTicker();
        actualizarTablaLow(low, high);
        timer.start();
    }
    public void setUp(){
        URL hj = getClass().getResource("resources/semaforo.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(hj));
    }
    public void actualizarTablaLow (JTable tablaL, JTable tablaH) throws SQLException{
        DefaultTableModel modelL = (DefaultTableModel) tablaL.getModel();
        DefaultTableModel modelH = (DefaultTableModel) tablaH.getModel();
        int i=tablaL.getRowCount()-1;
        while(i>=0){
            modelL.removeRow(i);
            modelH.removeRow(i);
            i--;
        }
        ResultSet contar = DDBB.BuscarTickers();
        int cantidad = 0;
        while(contar.next()){
            cantidad++;
        }
        Object rowSL[][] = new Object[cantidad][2];
        Object rowSH[][] = new Object[cantidad][2];
        ResultSet tickers = DDBB.BuscarTickers();
        int rowsCount = 0;
        while(tickers.next()){
            ResultSet resTickers = DDBB.lowHigh(tickers.getString("name"));
            int p=2, u=0;
            Object rowL[] = new Object[tablaL.getColumnCount()];
            Object rowH[] = new Object[tablaH.getColumnCount()];

            while (resTickers.next() && p<=17) {
   
                    rowL[p]=resTickers.getString("low_dia")/*+"+"+resTickers.getString("fecha_low_high")*/; 
                    rowL[0]=resTickers.getString("nombre_ticker");
                    rowL[1]=resTickers.getString("precio_ticker").replace(",", ".");
                    rowL[18]=resTickers.getString("low_low");
                    rowL[17]=resTickers.getString("precio_ticker").replace(",", ".");
                    rowL[19]=tickers.getString("low_13");
//                    System.out.println(""+p);
                    rowH[p]=resTickers.getString("high_dia")/*+"+"+resTickers.getString("fecha_low_high")*/; 
                    rowH[0]=resTickers.getString("nombre_ticker");
                    rowH[1]=resTickers.getString("precio_ticker").replace(",", ".");;
                    rowH[18]=resTickers.getString("high_high");
                    rowH[17]=resTickers.getString("precio_ticker").replace(",", ".");
                    rowH[19]=tickers.getString("high_13");
                    p++;
            }
            p=0;
            
            rowSL[rowsCount][0]=rowL;
            rowSL[rowsCount][1]=rowL[0];
            
            rowSH[rowsCount][0]=rowH;
            rowSH[rowsCount][1]=rowL[0];

            //modelL.addRow(rowL);
            //modelH.addRow(rowH);
   
            rowL=null;
            rowH=null;
            rowsCount++;
            u++;
        }
        
        for(i=0; i<cantidad;i++){
            Object rowT[] = new Object[tablaL.getColumnCount()];
            int cantidadRojos=0;
            int iteracion=0;
            int iteracionSig=1;
            int inicio=-1;
            rowT = (Object[]) rowSL[i][0];
            for(int v=tablaL.getColumnCount()-5;v>=2;v--){
//                System.out.println("  if"+rowT[16].toString().replace(",", "."));
                if(rowT[v]!=null && rowT[v-1]!=null)
                if(!rowT[v].toString().isEmpty())
                if(Float.parseFloat(rowT[v].toString().replace(",", "."))<Float.parseFloat(rowT[v-1].toString().replace(",", "."))){
                    cantidadRojos++;
                    if(iteracion==0){
                        inicio=v;
                    }
                    iteracion++;
//                    
                }else if(Float.parseFloat(rowT[v].toString().replace(",", "."))==Float.parseFloat(rowT[v-1].toString().replace(",", "."))){
                    cantidadRojos++;
                    if(iteracion==0){
                        inicio=v;
                    }
                    iteracion++;
//                    
                }else if(inicio!=0){
                   if(rowT[v]!=null)
                   DDBB.insertIteracion(rowT[0].toString(), iteracion, inicio, cantidadRojos, i, "l");  
//                   System.out.println(rowT[0].toString()+":"+inicio+":"+iteracion+":"+cantidadRojos+":"+rowT[v].toString().replace(",", ".")+":"+v);
                   cantidadRojos=0;
                   iteracion=0;
                   inicio=0;
//                   System.out.println("else"+v);
                }               
            }
//            System.out.println(""+cantidadRojos+":"+rowSL[i][1]);
            
        }
        
        int j = 0, cont = 0;
        //for(i=16; i>=2;i--){
           for(j=17; j>=2;j--){
               if(cont<=cantidad){
                ResultSet iteraciones= DDBB.Iteraciones(i, j, "inicio", "l");
                while(iteraciones.next()){
                    System.out.println(""+iteraciones.getString("inicio")+":"+iteraciones.getString("iteracion")+":"+iteraciones.getString("name_iteracion"));
                    if(rowSL[iteraciones.getInt("posicion_vector")][1]!=null){
                        modelL.addRow((Object[]) (rowSL[iteraciones.getInt("posicion_vector")][0]));
                        DDBB.deleteIteracion(iteraciones.getString("name_iteracion"), "l");
                        rowSL[iteraciones.getInt("posicion_vector")][1]=null;
                        cont++;
                    }
                }
               }
           //} 
        }
        //////////////////////////////////////////////////////////
        for(i=0; i<cantidad;i++){
            Object rowT[] = new Object[tablaH.getColumnCount()];
            int cantidadVerdes=0;
            int iteracion=0;
            int iteracionSig=1;
            int inicio=-1;
            rowT = (Object[]) rowSH[i][0];
            for(int v=tablaH.getColumnCount()-5;v>=2;v--){
                
                if(rowT[v]!=null && rowT[v-1]!=null)
                if(!rowT[v].toString().isEmpty())
                if(Float.parseFloat(rowT[v].toString().replace(",", "."))>Float.parseFloat(rowT[v-1].toString().replace(",", "."))){
                    cantidadVerdes++;     
                    if(iteracion==0){
                        inicio=v;
                    } 
                    iteracion++;
                }else if(Float.parseFloat(rowT[v].toString().replace(",", "."))==Float.parseFloat(rowT[v-1].toString().replace(",", "."))){
                    cantidadVerdes++;     
                    if(iteracion==0){
                        inicio=v;
                    } 
                    iteracion++;
                }else if(inicio!=0){
                   if(rowT[v]!=null)
                   DDBB.insertIteracion(rowT[0].toString(), iteracion, inicio, cantidadVerdes, i, "h");
                   //System.out.println(rowT[0].toString()+":"+inicio+":"+iteracion+":"+cantidadVerdes);
                   cantidadVerdes = 0;
                   iteracion=0;
                   inicio=0;
                }
            }   
        }
        
        cont=0;
        //for(i=16; i>=2;i--){
           for(j=17; j>=2;j--){
               if(cont<=cantidad){
                ResultSet iteraciones= DDBB.Iteraciones(i, j, "inicio", "h");
                while(iteraciones.next()){
                    System.out.println("---------------"+iteraciones.getString("inicio")+":"+iteraciones.getString("iteracion")+":"+iteraciones.getString("name_iteracion"));
                    if(rowSH[iteraciones.getInt("posicion_vector")][1]!=null){
                        modelH.addRow((Object[]) (rowSH[iteraciones.getInt("posicion_vector")][0]));
                        DDBB.deleteIteracion(iteraciones.getString("name_iteracion"), "h");
                        rowSH[iteraciones.getInt("posicion_vector")][1]=null;
                        cont++;
                    }
                }
               }
           //} 
        }

        int e=20;/*iteracion para pintar celdas por columnas*/
        while (e>=0) {
            //low.getColumnModel().getColumn(e).setCellRenderer((TableCellRenderer) new CustomRendererCell().getTableCellRendererComponentCell(low, Color.RED, null, true, true, tabla.getRowCount(), e));
            tablaL.getColumnModel().getColumn(e).setCellRenderer(new MyCellRenderer(tablaL.getDefaultRenderer(Object.class
            ), 1, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, null, e));
            e--;            
        }
        e=20;
        while (e>=0) {
            //low.getColumnModel().getColumn(e).setCellRenderer((TableCellRenderer) new CustomRendererCell().getTableCellRendererComponentCell(low, Color.RED, null, true, true, tabla.getRowCount(), e));
            tablaH.getColumnModel().getColumn(e).setCellRenderer(new MyCellRenderer(tablaL.getDefaultRenderer(Object.class
            ), 0, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, null, e));
            e--;            
        }
        tablaL.getColumnModel().getColumn(1).setMaxWidth(0);
        tablaL.getColumnModel().getColumn(1).setMinWidth(0);
        tablaL.getColumnModel().getColumn(1).setPreferredWidth(0);
        tablaH.getColumnModel().getColumn(1).setMaxWidth(0);
        tablaH.getColumnModel().getColumn(1).setMinWidth(0);
        tablaH.getColumnModel().getColumn(1).setPreferredWidth(0);
        
        tablaL.getColumnModel().getColumn(2).setMaxWidth(0);
        tablaL.getColumnModel().getColumn(2).setMinWidth(0);
        tablaL.getColumnModel().getColumn(2).setPreferredWidth(0);
        tablaH.getColumnModel().getColumn(2).setMaxWidth(0);
        tablaH.getColumnModel().getColumn(2).setMinWidth(0);
        tablaH.getColumnModel().getColumn(2).setPreferredWidth(0);
    }
    
    Timer timer = new Timer (1000, new ActionListener () { 
        public void actionPerformed(ActionEvent e) 
        { 
            try {
                actualizarTablaLow(low, high);
            } catch (SQLException ex) {
                Logger.getLogger(newLowHigh.class.getName()).log(Level.SEVERE, null, ex);
            }
         } 
    }); 



    
    public void calculoColorPorcentajeGroups(JLabel label, double porcentajeD){
        int porcentaje=0;
        if(porcentajeD!=0)porcentaje=(int) ((int)100-porcentajeD);
        if(porcentaje<0){
            label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/verde-c.png")));
            porcentaje=0;
        }
        label.setText(porcentaje+"%");
        if(porcentaje>= 0 && porcentaje<=50){
            label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/verde-c.png")));
        }
        if(porcentaje>= 51 && porcentaje<=75){
            label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/amarillo-c.png")));
        }
        if(porcentaje>= 76){
            label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/rojo-c.png")));
        }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        high = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        low = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New low - New High");
        setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        high.setBackground(new java.awt.Color(204, 204, 204));
        high.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        high.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ticker", "Price", "15", "14", "13", "12", "11", "10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "Price", "higher", "13 WEEK HIGH", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        high.setGridColor(new java.awt.Color(0, 0, 0));
        high.setPreferredSize(new java.awt.Dimension(975, 600));
        high.setRowHeight(30);
        high.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(high);
        if (high.getColumnModel().getColumnCount() > 0) {
            high.getColumnModel().getColumn(19).setMinWidth(120);
            high.getColumnModel().getColumn(19).setPreferredWidth(120);
        }

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("NEW LOW");

        low.setBackground(new java.awt.Color(204, 204, 204));
        low.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        low.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ticker", "Price", "15", "14", "13", "12", "11", "10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "Price", "Lower", "13 WEEK LOW", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        low.setGridColor(new java.awt.Color(0, 0, 0));
        low.setPreferredSize(new java.awt.Dimension(975, 600));
        low.setRequestFocusEnabled(false);
        low.setRowHeight(30);
        low.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(low);
        if (low.getColumnModel().getColumnCount() > 0) {
            low.getColumnModel().getColumn(19).setMinWidth(120);
            low.getColumnModel().getColumn(19).setPreferredWidth(120);
        }

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("NEW HIGH");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 908, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(newLowHigh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(newLowHigh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(newLowHigh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(newLowHigh.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    newLowHigh LH = new newLowHigh();
                    LH.setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(newLowHigh.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void formateaCabeceroTicker() {
        for (int i = 0; i < low.getColumnCount(); i++) {

            low.getColumnModel().getColumn(i).setHeaderRenderer(new ColumnHeaderRenderer());
            high.getColumnModel().getColumn(i).setHeaderRenderer(new ColumnHeaderRenderer());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable high;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable low;
    // End of variables declaration//GEN-END:variables



public class MyCellRenderer extends javax.swing.table.DefaultTableCellRenderer {

        TableCellRenderer render;
       
         Border b;
         int indexG;

        public MyCellRenderer(TableCellRenderer r, int index, Color top, Color left, Color bottom, Color right, int _position[], int numColumn) {
            render = r;
            indexG=index;
        }



//$$$ RENDERER COMPONENT DE LAS WEEKS
        public Component getTableCellRendererComponent(javax.swing.JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 1, 1, 0, Color.WHITE));
            //System.out.println("r"+row+"-"+column);
            setHorizontalAlignment(CENTER);
            if(column>=2){
              if(column<18){
                String celdaAnterior = table.getValueAt(row, column-1).toString().replace(",", ".");
                String celdaCurrent = table.getValueAt(row, column).toString().replace(",", ".");
                if(!celdaAnterior.isEmpty() && !celdaCurrent.isEmpty()){
                    double anterior = Float.parseFloat(celdaAnterior);
                    double current = Float.parseFloat(celdaCurrent);
                    
                    if(indexG==1) {
                        if(current<anterior){
                            cellComponent.setBackground(Color.RED);
                        }else if(current==anterior){
                            cellComponent.setBackground(Color.RED);
                        }else{
                            cellComponent.setBackground(new Color(0, 180, 0));
                        }
                        
                    }else{
                        if(current<anterior){
                            cellComponent.setBackground(Color.RED);
                        }else if(current==anterior){
                            cellComponent.setBackground(new Color(0, 180, 0));
                        }else{
                            cellComponent.setBackground(new Color(0, 180, 0));
                        }
                    }
                    
                }
            }
            if(column==18){ 
                String celdaAnterior = table.getValueAt(row, column-2).toString().replace(",", ".");
                String celdaCurrent = table.getValueAt(row, column).toString().replace(",", ".");
                if(!celdaAnterior.isEmpty() && !celdaCurrent.isEmpty()){
                    double anterior = Float.parseFloat(celdaAnterior);
                    double current = Float.parseFloat(celdaCurrent);
                    if(indexG==1) {
                        if(current<anterior){
                            cellComponent.setBackground(Color.RED);
                        }else if(current==anterior){
                            cellComponent.setBackground(Color.RED);
                        }else{
                            cellComponent.setBackground(new Color(0, 180, 0));
                        }
                        
                    }else{
                        if(current<anterior){
                            cellComponent.setBackground(Color.RED);
                        }else if(current==anterior){
                            cellComponent.setBackground(new Color(0, 180, 0));
                        }else{
                            cellComponent.setBackground(new Color(0, 180, 0));
                        }
                    }
                   
                }
            }
            if(column==20){ 
                String diaActual = table.getValueAt(row, 18).toString().replace(",", ".");
                String low13 = table.getValueAt(row, 19).toString().replace(",", ".");
                
//                
               if(!diaActual.isEmpty() && !low13.isEmpty()){
               
                
                Calendar cal = Calendar.getInstance();
                int current_day = cal.get(Calendar.DAY_OF_MONTH);
                int current_month = cal.get(Calendar.MONTH) + 1;
                int current_year = cal.get(Calendar.YEAR);
                String fecha = current_year+"-"+current_month+"-"+current_day;
                
                    double numDiaACtual = Float.parseFloat(diaActual);
                    double numLow13 = Float.parseFloat(low13);
                    
                    
                    if(indexG==1){
                        if(numDiaACtual<numLow13){
//                            JOptionPane.showMessageDialog(null,diaActual+"-"+low13, "", JOptionPane.ERROR_MESSAGE);
                            cellComponent.setBackground(Color.YELLOW);
                            DDBB.insertTickerLow13(table.getValueAt(row, 0).toString(), 1, fecha, "L");
                            DDBB.deleteLow13(table.getValueAt(row, 0).toString(), fecha, "L");
                        }else{
                            ResultSet busCambio = DDBB.TickersLow13(table.getValueAt(row, 0).toString(), fecha, "L");
                            try {
                                if(busCambio.next()){
                                    cellComponent.setBackground(Color.YELLOW);
                                }else{
                                    cellComponent.setBackground(Color.BLACK);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(newLowHigh.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                    }else{
                        if(numDiaACtual>numLow13){
                            cellComponent.setBackground(Color.RED);
                            DDBB.insertTickerLow13(table.getValueAt(row, 0).toString(), 1, fecha, "H");
                            DDBB.deleteLow13(table.getValueAt(row, 0).toString(), fecha, "H");
                            
                        }else{
                            ResultSet busCambio = DDBB.TickersLow13(table.getValueAt(row, 0).toString(), fecha, "H");
                            try {
                                if(busCambio.next()){
                                    cellComponent.setBackground(Color.YELLOW);
                                }else{
                                    cellComponent.setBackground(Color.BLACK);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(newLowHigh.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    
            } 
            }  
            }
            
            
            cellComponent.setFont(new Font("Arial", Font.BOLD, 17));
            ((JComponent) cellComponent).setBorder(b);
            b = null;
            return cellComponent;
        }
    }

 public class ColumnHeaderRenderer extends JLabel implements TableCellRenderer {
     
     Border b;
     
        public ColumnHeaderRenderer() {
            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
            setFont(new Font("Arial", Font.BOLD, 18));
            //setFont(tipoLetraWeeks);
            //setAlignmentX(CENTER);
            setHorizontalAlignment(CENTER);
            setOpaque(true);
            setForeground(Color.lightGray);  
            setBackground(Color.black);
            //setBorder(BorderFactory.createEtchedBorder());
           
        }

        public ColumnHeaderRenderer(Color color) {
            setFont(new Font("Arial", Font.BOLD, 16));
            //setFont(tipoLetraWeeks);
            //setAlignmentX(CENTER);
            setHorizontalAlignment(CENTER);
            setOpaque(true);
            setForeground(color);
            setBackground(Color.black);
            //setBorder(BorderFactory.createEtchedBorder());
             
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setBorder(b);
            this.setHorizontalAlignment(SwingConstants.CENTER);
            return this;
        }

    }

}


