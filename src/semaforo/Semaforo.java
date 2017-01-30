/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semaforo;

import com.ib.client.EWrapperMsgGenerator;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.formatDate;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import static semaforo.ConsultaIndices.INDICE_DJI;
import static semaforo.ConsultaIndices.INDICE_NASDAQ;
import static semaforo.ConsultaIndices.INDICE_SandP;

import static semaforo.Controller.calculateColorLock;
import semaforo.Settings.Ticker;
import semaforo.dialog.LoadingDialog;
import view.Synchronizer;
import java.lang.String;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.NULL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javafx.scene.chart.CategoryAxis;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import static kobytest.KobyTest.*;
import static kobytest.KobyTest.count;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import semaforo.LogFile;

/**
 *
 * @author fernando
 */
public class Semaforo extends javax.swing.JFrame {
    public static boolean cfd = false, bull = false, bear = false;
    public static ChartPanel panel = null;
    public static double countBull = 0;
    public static double countBear = 0;
    public static double countCfd = 0;
    public static double finalcountBull = 0;
    public static double finalcountBear = 0;
    public static double finalcountCfd = 0;
    public static int edit = 0;
    public static String currentSym = "";
    public static newLow newL;
    public static boolean newLboleano = false;
    public static newHigh newH;
    public static boolean newHboleano = false;
    public static Map<String, List<Integer>> maP = null;
    public static boolean seguir = true;
    public static boolean Tabla13weeks = true;
    public static boolean Tabla26weeks = true;
    public static boolean Tabla52weeks = true;
    public static JFrame  jframe;
    public static Boolean isDebugMode = false;
    public Boolean isDBCapitalLeida = false;
    public int lecturasCapital = 0;
    public Map<String, ElementoCapitalDB> valoresTickerCapital = new HashMap<>();

    int timerMilisegundosIndicesRefresh = 5000;
    int timerRecomendaciones = 5000;

    Font letraTituloTablas = new java.awt.Font("Arial", 1, 16);
    Color colorTituloTablas = java.awt.Color.BLACK;
    float alineacionTituloTablas = CENTER_ALIGNMENT;
    Font tipoLetraWeeks = new java.awt.Font("ARIAL", 0, 18);

    static Color colorFontCeldaTickers = Color.white;
    static Color colorFontCeldaWeeks = Color.white;

    static Color colorImparCeldaTicker = java.awt.Color.BLACK;
    static Color colorParCeldaTicker = java.awt.Color.DARK_GRAY;
    static Color colorImparCeldaWeeks = java.awt.Color.BLACK;
    static Color colorParCeldaWeeks = java.awt.Color.DARK_GRAY;

    static Color colorBotonesPaneles = Color.RED.darker();
    int porcentajeCasillaG = 12;
    int porcentajeCasillaF = 13 + porcentajeCasillaG;
    int porcentajeCasillaE = 14 + porcentajeCasillaF;
    int porcentajeCasillaD = 14 + porcentajeCasillaE;
    int porcentajeCasillaC = 15 + porcentajeCasillaD;
    int porcentajeCasillaB = 15 + porcentajeCasillaC; //% de CFD
    int porcentajeCasillaA = 17 + porcentajeCasillaB; //% de CFD (acciones que se pueden comprar)

    int[] porcentajes = {porcentajeCasillaA, porcentajeCasillaB, porcentajeCasillaC, porcentajeCasillaD, porcentajeCasillaE, porcentajeCasillaF, porcentajeCasillaG}; //int[6];

    private static final int WEEK1 = 0;
    private static final int WEEK2 = 1;
    private static final int WEEK3 = 2;
    public static int tablaSelectedIndex = 0;

    private boolean isload = false;
    // private boolean isloadHistory = true;

    UpdateTableListener listener = null;
    Timer timer = null;

    private static final Color[] colors = new Color[]{
        new Color(0, 180, 0), // Green
        new Color(30, 190, 30), // Light Green
        new Color(60, 200, 60), // 
        new Color(80, 210, 80), // Light Yellow
        new Color(100, 220, 100), // Light Yellow
        new Color(120, 250, 120), // Light Yellow
        // VERDES HASTA ACA - vienen los amarillos
        new Color(255, 255, 0),
        new Color(255, 255, 25),
        new Color(255, 255, 50),
        new Color(255, 255, 75),
        new Color(255, 255, 100),
        //rojos
        new Color(244, 130, 0), // Pink 
        new Color(198, 89, 17), // Brown
        new Color(255, 70, 70), // Red
        new Color(255, 40, 40), // Red CLARO
        new Color(255, 0, 0), // Red
        new Color(255, 255, 50)
    };

    public int num_positions;

    Settings settings = null;
    //Controller controller = null;

    /**
     * Creates new form Semaforo
     */
    int Mcol = 0;
    int Mrow = 0;

    Long tiempoInicio; // Inicio de Ejecucion del Programa
    Boolean recomentacionHecha = false;
    int cuenta = 0;
    public static FileInputStream fis = null;
    public static FileInputStream fis2 = null;
    public static FileInputStream fis3 = null;
    public Player player = null;
    public Player player2 = null;
    public Player player3 = null;

        
    public static BufferedInputStream bis = null;
    public static BufferedInputStream bis2 = null;
    public static BufferedInputStream bis3 = null;
    

    public Semaforo() throws JavaLayerException, FileNotFoundException, SQLException, InterruptedException {

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                   
                    ResultSet tickers = DDBB.TickersAll();
                    try {
                        while(tickers.next()){
                            Curl.jsonDecode(Curl.preciosTicker16d(tickers.getString("name")), tickers.getString("name"));
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                       
                }
            });
            t.start();
            
       
        
        
        System.setProperty("-Dsun.java2d.translaccel", "true");
        //System.out.print(EWrapperMsgGenerator.contractDetailsEnd(7));
        semaforo.LogFile.logFile("");
        semaforo.LogFile.logFile("************** Inicio ejecucion semaforo ****************");
        tiempoInicio = System.currentTimeMillis();  // Inicio de Ejecucion del Programa
        initComponents();                           // Inicializa los componentes de Swing
        setUp();                                    // Inicia el setup de la aplicacion
        precargaCapitalDB();                        // Lee la base de datos para precargar la tabla de Capital de la BBDD
        realTime();                                 // Inicio de threads de lectura de datos del TWS
        ajustaDetallesFrame();                      // Modificacion de bordes y otros elementos de look and feel
        syncScroll();                               // Crea los listeners para la sincronizacion del Scroll
//        hiloConsultaIndices();                      // Carga los valores e imagenes de indices de Yahoo Financials
//        hiloConsultaHistorial();
        
        
           
                            
        
        
        jTabbedPane1.addTab("13 WEEKS", null, PanelWeek1,"13 WEEKS");
        jTabbedPane1.addTab("26 WEEKS", null, PanelWeek2,"26 WEEKS");
        jTabbedPane1.addTab("52 WEEKS", null, PanelWeek3,"52 WEEKS");
//        addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent evt) {
//                DDBB.deleteLowHigh();
//                System.exit(0);
//            }
//        });


        /*oculto las consulta de indices*/
        jLabel5.setVisible(false);
        jLabelSemaforo.setVisible(false);
        jLabelImagenNASDAQ.setVisible(false);
        jLabelImagenSandP.setVisible(false);
        jLabelImagenDJI.setVisible(false);

        /*oculto label de jLabelNumPos*/
        jLabelNumPos.setVisible(false);
        jLabelPositionNum.setVisible(false);
        etiquetaGrupo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/verde-c.png")));
        etiquetaGrupo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/rojo-c.png")));
        etiquetaGrupo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/verde-c.png")));
        etiquetaGrupo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/amarillo-c.png")));
        etiquetaGrupo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/verde-c.png")));
        etiquetaGrupo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/rojo-c.png")));
        
        refrescarPorcentajeGroups();
  
        TableTicker.getColumnModel().getColumn(7).setMaxWidth(0);
        TableTicker.getColumnModel().getColumn(7).setMinWidth(0);
        TableTicker.getColumnModel().getColumn(7).setPreferredWidth(0);
//        TableTicker.getColumnModel().getColumn(6).setMaxWidth(0);
//        TableTicker.getColumnModel().getColumn(6).setMinWidth(0);
//        TableTicker.getColumnModel().getColumn(6).setPreferredWidth(0);



        
        
 
        
    }
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
    
    public void refrescarPorcentajeGroups() throws SQLException{
        ResultSet resGrupos = DDBB.Grupos();
        
         while (resGrupos.next()) {
                int id = resGrupos.getInt("id");      
                int maximo_a_invertir= resGrupos.getInt("maximo_a_invertir");
                double porcentaje=0;
                int invertido=0;
                ResultSet resGruposTickers = DDBB.GruposTickersSumarInvertido(resGrupos.getString("nombre"));
                if(resGruposTickers.next()){
                            invertido=resGruposTickers.getInt(1);
                            if(maximo_a_invertir!=0)porcentaje=(invertido*100)/maximo_a_invertir;
                            else porcentaje=0;             
                }
                
                switch (id){
                    case 1:
                        jLabel9.setText(resGrupos.getString("nombre").substring(0, 3));
                        calculoColorPorcentajeGroups(etiquetaGrupo1, porcentaje);
                    break;
                    case 2:
                        jLabel11.setText(resGrupos.getString("nombre").substring(0, 3));
                        calculoColorPorcentajeGroups(etiquetaGrupo2, porcentaje);
                    break;
                    case 3:
                        jLabel13.setText(resGrupos.getString("nombre").substring(0, 3));
                        calculoColorPorcentajeGroups(etiquetaGrupo3, porcentaje);
                    break;
                    case 4:
                        jLabel15.setText(resGrupos.getString("nombre").substring(0, 3));
                        calculoColorPorcentajeGroups(etiquetaGrupo4, porcentaje);
                    break;
                    case 5:
                        jLabel17.setText(resGrupos.getString("nombre").substring(0, 3));
                        calculoColorPorcentajeGroups(etiquetaGrupo5, porcentaje);
                    break;
                    case 6:
                        jLabel19.setText(resGrupos.getString("nombre").substring(0, 3));
                        calculoColorPorcentajeGroups(etiquetaGrupo6, porcentaje);
                    break;
                }
         }
    }

    public class ColumnHeaderRenderer extends JLabel implements TableCellRenderer {

        public ColumnHeaderRenderer() {
            setFont(new Font("Arial", Font.BOLD, 16));
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
            return this;
        }

    }

    //Custom renderer - do what the natural renderer would do, just add a border
    public static class CustomRenderer implements TableCellRenderer {

        TableCellRenderer render;
        Border b;

        public CustomRenderer(TableCellRenderer r, Color top, Color left, Color bottom, Color right) {
            render = r;

            //It looks funky to have a different color on each side - but this is what you asked
            //You can comment out borders if you want too. (example try commenting out top and left borders)
//$$$ Elimina bordes 
//            b = BorderFactory.createCompoundBorder();
//            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, top));
////            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 1, 0, 0, left));
//            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, bottom));
//            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 0, 1, right));

           b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            JComponent result = (JComponent) render.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            rowsCopy tablaCopia = new rowsCopy(0, 0, 2);

            int[] numerosComprar = new int[500];

            numerosComprar = tablaCopia.retornaNumerosComprar();
            /*Aqui se pinta solo la linea 6*/
            String name = (String) table.getValueAt(row, 0);

//            int p = 0;
//            ResultSet resTiker = DDBB.BuscarTickers(name);
//            try {
//                if (resTiker.next()) {
//                    p = resTiker.getInt("hedge");
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
//            }

//            if (column == 6) {
//                Font normal = new Font("Arial", Font.BOLD, 24);
//                result.setFont(normal);//tipo de fuente
//
//                if (p == 1) {
//                    //result.setBackground(Color.RED);
//                    result.setForeground(Color.RED);
//                }
//
//                if (p == 2) {
//                    //result.setBackground(Color.GREEN);
//                    result.setForeground(Color.GREEN);
//                }
//                if (p == 3) {
//                    if (row % 2 == 0) {
//                        //result.setBackground(Color.DARK_GRAY);
//                        result.setForeground(Color.DARK_GRAY);
//                    } else {
//                        //result.setBackground(Color.BLACK);
//                        result.setForeground(Color.BLACK);
//                    }
//                }
//            } else if (row % 2 == 0) {
//                result.setBackground(java.awt.Color.DARK_GRAY);
//                result.setForeground(Color.WHITE);
//            } else {
//                result.setBackground(Color.BLACK);
//                result.setForeground(Color.WHITE);
//            }
                
                    if (row % 2 == 0) {
                        result.setBackground(java.awt.Color.DARK_GRAY);
                        result.setForeground(Color.WHITE);
                    } else {
                        result.setBackground(Color.BLACK);
                        result.setForeground(Color.WHITE);
                    }
                
            //PARA COMPRAR HOY?????
            //JOptionPane.showMessageDialog(null,row+"", "", JOptionPane.ERROR_MESSAGE);
//            
            Object tickers[][] = new Object[500][9];
            tickers = tablaCopia.retornaCopiaTabla();
            int valorCompro;
            /*if (column == 7) {
                try {
                    ResultSet resTickers = DDBB.BuscarTickers();
                    ResultSet resCompras;
                    Calendar cal3 = Calendar.getInstance();
                    SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaHoyConsulta = format3.format(cal3.getTime());
                    while (resTickers.next()) {
                        try {
                            resCompras = DDBB.BuscarComprasFecha(resTickers.getString("name"), fechaHoyConsulta);
                            if (resCompras.next()) {
                                //if(tickers[row][8]==null) JOptionPane.showMessageDialog(null,tickers[row][8]+"+"+row, "", JOptionPane.ERROR_MESSAGE);
                                if (tickers[row][8] != null) {
                                    valorCompro = Integer.parseInt(tickers[row][8].toString());
                                    if (valorCompro == 1) {
                                        result.setForeground(Color.GREEN);
                                    }
                                    if (valorCompro == 0) {
                                        result.setForeground(Color.RED);
                                    }
                                } else {
                                    result.setForeground(Color.RED);
                                }

                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }*/

            //result.setBackground(Color.black);
            ((JComponent) result).setBorder(b);
            ((JLabel) result).setHorizontalAlignment(SwingConstants.CENTER);
//$$$ Elimina bordes resultados weeks
//            result.setBorder(b);

            return result;
        }

    }

// ############################################################################     
// #                        CLASS MY CELL RENDERER       
// linea roja recomendacion, sugerencia    
// ############################################################################     
    public class MyCellRenderer extends javax.swing.table.DefaultTableCellRenderer {

        TableCellRenderer render;
        int positions_render[];
        Border b;
        int index;

        public MyCellRenderer(TableCellRenderer r, int index, Color top, Color left, Color bottom, Color right, int _position[], int numColumn) {
            render = r;
            this.index = index;

            this.positions_render = _position;
            //It looks funky to have a different color on each side - but this is what you asked
            //You can comment out borders if you want too. (example try commenting out top and left borders)

//$$$ ELIMINADO BORDES WEEKS
//            b = BorderFactory.createCompoundBorder();
//            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, top));
//            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 1, 0, 0, left));
//            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, bottom));
            // index 0 es porque la linea roja solo se prende en week 1 
            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));

            if (numColumn == 7 && index == 0) {
            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 0, 1, Color.RED));
            }
            

        }

        public java.awt.Component setTableCellRendererComponent(javax.swing.JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component cellComponentSig = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Font normal = new Font("Arial", Font.PLAIN, 10);
            cellComponentSig.setFont(normal);//tipo de fuente

            return cellComponentSig;
        }

//$$$ RENDERER COMPONENT DE LAS WEEKS
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            java.awt.Component cellComponentSig = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column + 1);

            
              
            
            Double valorCFD;
            Double valorDiferencia;
            Double division;
            int p=0;
            cellComponent.setForeground(colorFontCeldaWeeks);
//            cellComponent.setBackground(colorImparCeldaWeeks);
            
            if (row % 2 == 0) {
                cellComponent.setBackground(colorParCeldaWeeks);
            } else {
                cellComponent.setBackground(colorImparCeldaWeeks);

            }
            Font normal = new Font("Arial", Font.BOLD, 11);
            cellComponent.setFont(normal);//tipo de fuente

            if (column == 0) {
                normal = new Font("Arial", Font.PLAIN, 18);
                cellComponent.setFont(normal);//tipo de fuente
            }
            if(column == 16){
                String precio = null;
                String high = null;        
                        try{
                            precio = (String) TableTicker.getValueAt(row, 1);
                            high = (String) table.getValueAt(row, 17);
                        }catch(Exception ex){
                            
                        }
                        if(precio!=null && high!=null){
                            high = high.replace(",", ".");
                            precio = precio.replace(",", ".");
                            double precioTobombillo = Double.parseDouble(precio);
                            double highTobombillo = Double.parseDouble(high);
//                            if(precioTobombillo>highTobombillo){
                            if(precioTobombillo>highTobombillo){
                                table.getColumnModel().getColumn(column+2).setCellRenderer((TableCellRenderer) new CustomRendererCell().getTableCellRendererComponentCell(TableTicker, new Color(255,255,0), null, true, true, 1, 6));
                            }
                        }
//                        JOptionPane.showMessageDialog(null,"", "", JOptionPane.ERROR_MESSAGE);
            } 
            if (this.positions_render.length > row && this.positions_render.length > 0 && this.positions_render[row] == column) {
                if (column > 0) { 
                    if (column <= 7 & index == 0) {
                        //Comentado para que el timer de parpadeo funcione y no sea suplantado el render de la tabla
                        // if (((System.currentTimeMillis() - tiempoInicio) > timerRecomendaciones) & (cuenta < 200)) {
//                        for (int i = 1; i < 7; i++) {
//                            //if (i != column)
//                            table.setValueAt("", row, i);
//                        }
                        DecimalFormat decimales = new DecimalFormat(".00");
                        if (column < 8) {
                            String precio = (String) TableTicker.getValueAt(row, 1);
                            precio = precio.replace(",", ".");
                            String low = table.getValueAt(row, 0).toString();
                            low = low.replace(",", ".");
                            String high = table.getValueAt(row, 17).toString();
                            high = high.replace(",", ".");
                            double costo, min, max;
                            costo = Double.parseDouble(precio);
                            min = Double.parseDouble(low);
                            max = Double.parseDouble(high);
                            min = min + (((max - min) / 16) * (column - 1));
                            max = max - ((max - min) / 16) * (16 - column - 1);
                            table.setValueAt(decimales.format(min), row, column + 1);
                            table.setValueAt(decimales.format(max), row, column + 2);
                            if (column == 7) {
                                normal = new Font("Arial", Font.PLAIN, 18);
                                cellComponent.setFont(normal);//tipo de fuente

                                valorCFD = 0.0;
                                valorDiferencia = 0.0;
                                String tempStrValCFD = "";
                                String tempStrValPosicion = "";

                                try {

                                    //CFD
                                    tempStrValCFD = TableTicker.getModel().getValueAt(row, 3).toString().replace(",", ".");

                                    //POSICION
                                    Object obj;
                                    obj = TableTicker.getModel().getValueAt(row, 4); //.toString().replace(",", ".");

                                    if (!tempStrValCFD.isEmpty()) {

                                        valorCFD = Double.parseDouble(tempStrValCFD);

                                        //RECOMENDACION
                                        division = 14 * (1.0 * valorCFD) / 100;

                                        if (obj != null) {
                                            tempStrValPosicion = obj.toString().replace(",", ".");
                                            if (!tempStrValPosicion.isEmpty()) {
                                                division -= Double.parseDouble(tempStrValPosicion);
                                            }
                                        }

                                        if (division < 0) {
                                            division = 0.0;
                                        }

                                        table.setValueAt(division.intValue(), row, column);

                                        normal = new Font("Arial", Font.PLAIN, 18);
                                        cellComponent.setFont(normal);//tipo de fuente

                                    } else {
                                        table.setValueAt("", row, column);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        cellComponent.setForeground(Color.RED.darker());

//########################################
//$$$ ESTA ES LA RECOMENDACION
                        valorCFD = 0.0;
                        valorDiferencia = 0.0;
                        String tempStrValCFD = "";
                        String tempStrValPosicion = "";
                        if (column < 8) {
                            try {
                                    
                                //CFD
                                tempStrValCFD = TableTicker.getModel().getValueAt(row, 3).toString().replace(",", ".");

                                //POSICION
                                Object obj;
                                obj = TableTicker.getModel().getValueAt(row, 4); //.toString().replace(",", ".");

                                if (!tempStrValCFD.isEmpty()) {

                                    valorCFD = Double.parseDouble(tempStrValCFD);

                                    //RECOMENDACION
                                    division = porcentajes[column - 1] * (1.0 * valorCFD) / 100;

                                    if (obj != null) {
                                        tempStrValPosicion = obj.toString().replace(",", ".");

                                        if (!tempStrValPosicion.isEmpty()) {
                                            division -= Double.parseDouble(tempStrValPosicion);
                                        }
                                    }

                                    if (division < 0) {
                                        division = 0.0;
                                    }

                                    table.setValueAt(division.intValue(), row, column);
                                    normal = new Font("Arial", Font.PLAIN, 18);
                                    cellComponent.setFont(normal);//tipo de fuente
                                    
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // } 

                    }             
//                                    String id_ticker=TableTicker.getValueAt(row, 0).toString();
////                                  JOptionPane.showMessageDialog(null, id_ticker+"");
//                                    System.out.println(id_ticker+"---------------------------");
//                                    ResultSet resParpadeo = DDBB.buscarParpadeo(id_ticker);
//                                    try {
//                                        if (resParpadeo.next()) {
//                                            p = resParpadeo.getInt("contador");
//                                            System.out.println(p+"**************************************");
//                                            if(p>0){
//                                                DDBB.updateParpadeo(id_ticker, p-1, true);       
//                                            }else{
//                                                DDBB.deleteParpadeo(id_ticker);
//                                            }                                            
//                                        }
//                                    } catch (SQLException ex) {
//                                        Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
//                                    }
                             
//                       if(p>0 && p%2==0){
//                           cellComponent.setBackground(colors[this.positions_render[row] - 1]);
//                       }else if(p%1==0){
//                           cellComponent.setBackground(new Color(0,0,0));
//                       }
//                       if(p==0){
//                           cellComponent.setBackground(colors[this.positions_render[row] - 1]);
//                       }
                       cellComponent.setBackground(colors[this.positions_render[row] - 1]);
                        if(Tabla26weeks && column<8){
                            DecimalFormat decimales = new DecimalFormat(".00");
                            String precio = (String) TableTicker.getValueAt(row, 1);
                            precio = precio.replace(",", ".");
                            String low = table.getValueAt(row, 0).toString();
                            low = low.replace(",", ".");
                            String high = table.getValueAt(row, 17).toString();
                            high = high.replace(",", ".");
                            double costo, min, max;
                            costo = Double.parseDouble(precio);
                            min = Double.parseDouble(low);
                            max = Double.parseDouble(high);
                            min = min + (((max - min) / 16) * (column - 1));
                            max = max - ((max - min) / 16) * (16 - column - 1);
                            table.setValueAt(decimales.format(min), row, column + 1);
                            table.setValueAt(decimales.format(max), row, column + 2);
                        }  
                } else {  
                    cellComponent.setBackground(colors[0]);
                }
            } else if (index == 0 && column > 0 && (cellComponent.getBackground().equals(colorImparCeldaWeeks) || cellComponent.getBackground().equals(colorParCeldaWeeks))) {
                table.setValueAt("", row, column);
            }
            
             
    

            ((JComponent) cellComponent).setBorder(b);

            ((JLabel) cellComponent).setHorizontalAlignment(SwingConstants.CENTER);

            return cellComponent;
        }
    }

    public class ResetCellRenderer extends javax.swing.table.DefaultTableCellRenderer {

        TableCellRenderer render;
        Border b;

        public ResetCellRenderer(TableCellRenderer r, Color top, Color left, Color bottom, Color right, int numColumn) {
            render = r;

            //It looks funky to have a different color on each side - but this is what you asked
            //You can comment out borders if you want too. (example try commenting out top and left borders)
//$$$ Elimina borders            
//            b = BorderFactory.createCompoundBorder();
//            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, top));
////            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 1, 0, 0, left));
//            b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, bottom));
//            if (numColumn == 4) {
//               // b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 0, 1, Color.RED));
//            }
        }

        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            return cellComponent;
        }
    }
    

//############################################################
//                         UPDATE PANEL SEMAFORO
//############################################################
    public void updatePanelSemaforo() {

        if (semaforo.Semaforo.isDebugMode) {
            System.out.println("* * * * * * * * * * * * * UPDATE PANEL SEMAFORO * * * * * * * * * * * * * *");
        }
        if (semaforo.Semaforo.isDebugMode) {
            System.out.println("");
        }

        try {

            /*Verifico el numero de posiciones para dar color al circulo*/
            int NumPos = Controller.getSettings().getNumPos();
            if (NumPos < 10) {
                jLabelPositionNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/verde.png")));
                jLabelPositionNum.setText("" + NumPos + "");
            }
            if (NumPos >= 10 && NumPos < 12) {
                jLabelPositionNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/amarillo.png")));
                jLabelPositionNum.setText("" + NumPos + "");
            }
            if (NumPos >= 12) {
                jLabelPositionNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/rojo.png")));
                jLabelPositionNum.setText("" + NumPos + "");
            }
            jLabelPositionNum.setVisible(true);
            /*jLabelNumPos.setText("" + Controller.getSettings().getNumPos());*/
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }

        String nombre = Controller.getSettings().getPorcentCapital().toString();
        double base = Double.parseDouble(nombre);
//        int base = 71;

        if (base >= 0 && base < 51) { //LUZ VERDE
            jLabelInvested.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/verde.png")));
        } else if (base >= 51 && base < 71) { //LUZ AMARILLA
            jLabelInvested.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/amarillo.png")));

        } else if (base > 70 && base < 101) { //LUZ ROJA
            jLabelInvested.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/rojo.png")));

        }

        jLabelInvested.setText("" + String.format("%.2f", Controller.getSettings().getPorcentCapital()) + "%");

//############################################################
//                         POSICIONES
//############################################################
        kobytest.KobyTest.posiciones();
        kobytest.KobyTest.miPortfolioUpdates();

    }

    class RealTimeListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if(seguir)
            SwingUtilities.invokeLater(new Runnable() {
              
                @Override
                public void run(){
                    
                    
                    core();
                    
                    
                }
            }
                    
            );

        }
    }
    
    public void core(){
            //                    double cfd=0, bull=0, bear=0;
//                    if(finalcountCfd!=0)cfd = ((finalcountCfd*100)/(finalcountBear+finalcountBull+finalcountCfd));
//                    if(finalcountBull!=0)bull = ((finalcountBull*100)/(finalcountBear+finalcountBull+finalcountCfd));
//                    if(finalcountBear!=0)bear = ((finalcountBear*100)/(finalcountBear+finalcountBull+finalcountCfd));
//                    Semaforo.l1.setText("CFD ("+cfd+"%)");

                    
                    
                    if(countCfd>0 && !cfd){
                        Semaforo.l1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/redL.png")));
                        cfd=true;
                    }else if(countBull>0 && !bull){
                        Semaforo.l2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/redL.png")));
                        bull=true;
                    }else if(countBear>0 && !bear){
                        Semaforo.l3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/redL.png")));
                        bear=true;
                    }
                    
                    if(countCfd>0 && !cfd){
                        Semaforo.l1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/blueL.png")));
                        cfd=true;
                    }else if(countBull>0 && !bull){
                        Semaforo.l2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/blueL.png")));
                        bull=true;
                    }else if(countBear>0 && !bear){
                        Semaforo.l3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/blueL.png")));
                        bear=true;
                    }
                    
                    if(countCfd>0 && !cfd){
                        Semaforo.l1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/greenL.png")));
                        cfd=true;
                    }else if(countBull>0 && !bull){
                        Semaforo.l2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/greenL.png")));
                        bull=true;
                    }else if(countBear>0 && !bear){
                        Semaforo.l3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/greenL.png")));
                        bear=true;
                    }
                    
                    if(countCfd<=0){
                        Semaforo.l1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/blackL.png")));
                    }
                    if(countBull<=0){
                        Semaforo.l2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/blackL.png")));
                    }
                    if(countBear<=0){
                        Semaforo.l3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/blackL.png")));
                    }
//                 
                    try {
                        refrescarPorcentajeGroups();
                    } catch (SQLException ex) {
                        Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    Random r = new Random(System.currentTimeMillis());
                    //Mcol = r.nextInt(8) + 1;

                    while (update == 2) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    synchronized (updateLock) {
                        update = 0;
                    }

                    final Settings settings = Controller.getSettings();
                    Hashtable<String, List<Integer>> ht = Controller.getTickersValue();

                    while (Controller.isCalculatingColor) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    synchronized (calculateColorLock) {
                        Controller.isCalculatingColor = true;
                    }

                    for (int t = 0; t < settings.getTickers().size(); t++) {
                        Controller.calcular_color(settings.getTickers().get(t).getName(), settings.getTickers().get(t).getCurrentPrice());
                    }

                    synchronized (Controller.calculateColorLock) {
                        Controller.isCalculatingColor = false;
                    }

//Aquí se aplica el ordenamiento
                    final Map<String, List<Integer>> map = sortByValues(ht);
                    maP = map;
                    TableModel temp = TableTicker.getModel();

//              
                    updatePanelSemaforo();

//$$$ SE SETEA LAS COLUMNAS DE TICKER
                    int capital = 0;
                    Object obje = null;

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaAyer = format1.format(cal.getTime());

                    Calendar cal2 = Calendar.getInstance();
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaHoy = format2.format(cal2.getTime());

                    int compradasOld, compradasHoy;

                    String columnsTitle[] = {"Ticker", "Price", "To Invest", "CFD", "Bought", "Remain", "LOW DAY", "Comprar?"};
                    Object rows[][] = new Object[settings.getTickers().size()][9];

                    rowsCopy tablaCopia = new rowsCopy(settings.getTickers().size(), 7, 2);
                    Object TablaConHedge[][] = new Object[500][8];
                    int[] numerosEstado = new int[500];

                    TablaConHedge = tablaCopia.retornaCopiaTabla();
                    numerosEstado = tablaCopia.retornaNumerosEstado();
                    //int size = TableTicker.getRowCount();
                    int fila = 0;

                    int cuentaTickersBaja = 0;
                    int cuentaTickersValidos = 0;
                    boolean actualizar = false;
                    Iterator iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry entry = (Entry) iterator.next();
                        for (Ticker ticker : settings.getTickers()) {
                            if (ticker.getName().equalsIgnoreCase(entry.getKey().toString())) {
//$$$ 0 Nombre del Ticker
                                rows[fila][0] = ticker.getName();
//$$$ 1 Valor del Ticker    
                                currentSym = rows[fila][0].toString();

                                Thread t = new Thread(new Runnable() {
                                    
                                    public String t = currentSym;
                                    @Override
                                    public void run() {
                                        Curl.jsonDecode(Curl.preciosTicker(t), t);
                                    }
                                });
                                t.start();
                                

                                rows[fila][1] = String.format("%.2f", ticker.getCurrentPrice());
                                DDBB.updatePriceTicker(rows[fila][0].toString(), rows[fila][1].toString());
                                
                                ResultSet resTickers = DDBB.lowHigh(rows[fila][0].toString());
                                try {
                                    if (resTickers.next()) {
//                                       String low=resTickers.getString("low_low").toString().replace(",", ".");
//                                       String high=resTickers.getString("high_high").toString().replace(",", ".");
                                       rows[fila][6]=resTickers.getString("low_low").toString().replace(",", ".");
//                                       String pre=rows[fila][1].toString().replace(",", ".");
//                                       if(pre.isEmpty())pre=rows[fila][1].toString();                                      
//                                       double precio = Float.parseFloat(pre);
//                                       double low_low = Float.parseFloat(low);
//                                       double high_high = Float.parseFloat(high);
                                       
                                       //kobytest.KobyTest.pedir_historico(Settings.getTickerID(rows[fila][0].toString()), rows[fila][0].toString(), kobytest.KobyTest.total_weeks_ago);
//                                       if(precio>high_high){
//                                           DDBB.updateHighHighTicker(rows[fila][0].toString(), rows[fila][1].toString());
//                                       }
//                                       if(precio<low_low){
//                                           DDBB.updateLowLowTicker(rows[fila][0].toString(), rows[fila][1].toString());
//                                       }
                                       
                                    }
//$$$ 2 CAPITAL
                                } catch (SQLException ex) {
                                    Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                                }

//$$$ rutina para precarga de DDBB
                                //int capitalDB = Controller.getSettings().getCapital(ticker.getName());
                                //valoresTickerCapital.put(ticker.getName(), capitalDB);
                                //System.out.println("***************************** CAPITAL HASHMAP  " + valoresTickerCapital.size() + "  " + valoresTickerCapital.toString());
                                int tempFila = getFilaTickerFrontEnd(temp, ticker.getName());
                                if (tempFila > -1) {
                                    obje = temp.getValueAt(tempFila, 2);
                                }

                                try {
                                    capital = Integer.parseInt((obje == null || obje.toString().isEmpty()) ? "0" : obje.toString());

                                    ElementoCapitalDB elemCapitalDB = valoresTickerCapital.get(ticker.getName());

                                    if ((elemCapitalDB != null) && !(valoresTickerCapital.get(ticker.getName()).isChequeado)) {

                                        if (semaforo.Semaforo.isDebugMode) {
                                            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Lectura de DDBB  " + elemCapitalDB);
                                        }
                                        if (semaforo.Semaforo.isDebugMode) {
                                            System.out.println("");
                                        }
                                        if (semaforo.Semaforo.isDebugMode) {
                                            System.out.println("");
                                        }

                                        rows[fila][2] = valoresTickerCapital.get(ticker.getName()).getCapital();
                                        valoresTickerCapital.get(ticker.getName()).setIsChequeado(true);

                                    } else {
                                        rows[fila][2] = (capital == 0) ? "" : capital;
                                    }

                                    if (semaforo.Semaforo.isDebugMode) {
                                        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ ROWS FILA " + fila + " COLUMNA  2 VALE: " + rows[fila][2]);
                                    }

                                    elemCapitalDB = valoresTickerCapital.get(ticker.getName());
                                    if (elemCapitalDB == null) {
                                        elemCapitalDB = new ElementoCapitalDB();
                                        elemCapitalDB.setCapital(0);
                                        elemCapitalDB.setIsChequeado(true);
                                    }

                                    int capitalDB = elemCapitalDB.getCapital();
                                    //rows[fila][2] = capitalDB;

                                    if ((capitalDB != capital)) {
                                        Controller.getSettings().setCapital(ticker.getName(), capital);
                                        elemCapitalDB.setCapital(capital);
                                        elemCapitalDB.setIsChequeado(true);
                                        valoresTickerCapital.put(ticker.getName(), elemCapitalDB);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    rows[fila][2] = "";
                                }

                                //rows[fila][2]
                                //.....
//$$$ 3 CFD                               
                                rows[fila][3] = String.format("", Math.round((capital / ticker.getCurrentPrice())));

                                if ( /*!rows[fila][2].equals(null) ||*/!rows[fila][2].toString().isEmpty()) {
                                    rows[fila][3] = String.format("%.0f", Math.floor(capital / ticker.getCurrentPrice()));
                                }
//$$$ Bought

                                Map misPosiciones = Controller.getSettings().getValorPosiciones();
                                rows[fila][4] = 0;
                                if (misPosiciones != null) {
                                    Iterator iterator2 = misPosiciones.keySet().iterator();

                                    while (iterator2.hasNext()) {
                                        String key = iterator2.next().toString();
                                        int value = (Integer) misPosiciones.get(key);

                                        if (ticker.getName().equals(key)) {
                                            rows[fila][4] = value;
                                        }

                                        System.out.println(key + " " + value);
                                    }

                                }

                                //rows[fila][4] = ticker.getCurrentPrice();         
                                try {
                                    //if(rows[fila][3] != "" || rows[fila][4] != ""){
                                    if (rows[fila][3] != null && !rows[fila][3].toString().isEmpty()) {
                                        if (rows[fila][4] != null && !rows[fila][4].toString().isEmpty()) {

                                            //tempStr = TableTicker.getModel().getValueAt(row, 3).toString().replace(",", ".");
// Remain
                                            rows[fila][5] = Integer.parseInt(rows[fila][3].toString()) - Integer.parseInt(rows[fila][4].toString());

                                        } else {
                                            rows[fila][5] = rows[fila][3];
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                byte ptext[] = "&#1422".getBytes(ISO_8859_1); 
//                                String value = new String(ptext, UTF_8); 
//                                rows[fila][6] = value;

                                //rows[fila][6] = "<>";

                                rows[fila][7] = "O";

                                String compradasDDBB = "0"; 
                                String id_ticker = rows[fila][0].toString();
                                String compradasString;

                                compradasString = rows[fila][4] + "";

                                rows[fila][8] = 1;
                                // COMPRADAS HOY SI O NO
                                DDBB.deleteComprasFecha(fechaHoy);
                                ResultSet resCompras = DDBB.BuscarComprasFecha(id_ticker, fechaHoy);
                                try {
                                    if (resCompras.next()) {
                                        rows[fila][8] = resCompras.getInt("compro");
                                        tablaCopia.cambiarCopiaTabla(fila, 8, resCompras.getInt("compro"));
                                        //JOptionPane.showMessageDialog(null,resCompras.getInt("compradas")+"", "", JOptionPane.ERROR_MESSAGE);
                                        compradasDDBB = resCompras.getString("compradas");
                                        if (!compradasDDBB.isEmpty() && !compradasString.isEmpty()) {
                                            compradasOld = Integer.parseInt(compradasDDBB);
                                            compradasHoy = Integer.parseInt(compradasString);
                                            //JOptionPane.showMessageDialog(null,compradasOld+"", "", JOptionPane.ERROR_MESSAGE);
                                            //
                                            if (compradasHoy > compradasOld) {
//                                                JOptionPane.showMessageDialog(null,compradasOld+"-"+compradasHoy+"-"+resCompras.getInt("compro"), "", JOptionPane.ERROR_MESSAGE);
                                                //ROJO

                                                DDBB.deleteCompras(id_ticker);
                                                DDBB.insertCompras(id_ticker, compradasString, fechaHoy, 0);
                                                rows[fila][8] = 0;
                                                tablaCopia.cambiarCopiaTabla(fila, 8, "0"); 
                                                //  tablaCopia.cambiarNumerosComprar(fila, 0);

                                            }else if(compradasHoy == compradasOld){
                                                DDBB.deleteCompras(id_ticker);
                                                DDBB.insertCompras(id_ticker, compradasString, fechaHoy, 1);
                                                rows[fila][8] = 1;    
                                                tablaCopia.cambiarCopiaTabla(fila, 8, "1"); 
                                            }
                                            //DDBB.insertCompras(id_ticker, compradasString, fechaHoy);
                                        } else {

                                        }
                                    } else {
                                        DDBB.insertCompras(id_ticker, compradasString, fechaHoy, 1);
                                        rows[fila][8] = 1;
                                        tablaCopia.cambiarCopiaTabla(fila, 8, "1");
                                        //JOptionPane.showMessageDialog(null,"", "", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                //System.out.println("INIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIITTTTT");
                                Map misCierres = Controller.getSettings().getValorCierres();

                                if (misCierres != null) {

                                    try {
                                        Double restaCierre = (Double) misCierres.get(ticker.getName()) - ticker.getCurrentPrice();
                                        //System.out.println("RESTAAAA #################  " + restaCierre);
                                        cuentaTickersValidos++;

                                        cuentaTickersBaja = cuentaTickersBaja + (restaCierre > 0 ? 1 : 0);

//                                        System.out.println("TICKER " + ticker.getName() + " CIERRE " + misCierres.get(ticker.getName()) + " PRECIO: " + ticker.getCurrentPrice() );
//                                        System.out.println("#################### PROMEDIO " + ((1.00 * cuentaTickersBaja) / (1.00 * cuentaTickersValidos)) *100 + " VALIDOS " + cuentaTickersValidos + " BAJAS " + cuentaTickersBaja);
                                    } catch (Exception e) {
                                    }

//                                    Iterator iterator3 = misCierres.keySet().iterator();
//
//                                    while (iterator3.hasNext()) {
//                                        String key = iterator3.next().toString();
//                                        Double valueDouble =  (Double)misCierres.get(key);
//                                        
//
//                                        
//                                        System.out.println(key + " " + valueDouble);
//                                    }
                                }

                                //System.out.println("FIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIINN");
                                fila++;
                            }
                        }
                    }

                    double promedioBajasTickers;

                    promedioBajasTickers = ((1.00 * cuentaTickersBaja) / (1.00 * cuentaTickersValidos) * 100);

                    String promedio = Double.toString(promedioBajasTickers);
                    promedio = promedio.substring(0, promedio.indexOf(".") + 2);

                    if (semaforo.Semaforo.isDebugMode) {
                        System.out.println("#################### PROMEDIO " + promedioBajasTickers);
                    }

                    if (promedioBajasTickers < 50.0) {
                        jLabelLuzPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/verdep.png")));
                        jLabelLuzPrincipal.setText(promedio + "%");
//                        jLabelSemaphore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/SemVERDE_HOR.png")));

                    } else if (promedioBajasTickers < 70.0) {
                        jLabelLuzPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/amarillop.png")));
                        jLabelLuzPrincipal.setText(promedio + "%");
//                        jLabelSemaphore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/SemAMARILLO_HOR.png")));

                    } else {
                        jLabelLuzPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/rojop.png")));
                        jLabelLuzPrincipal.setText(promedio + "%");
//                        jLabelSemaphore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/SemROJO_HOR.png")));
                    }

// $$$ GENERACION DEL MODELO CON LA MEJORA DE NO EDICION DE LAS OTRAS COLUMNAS                    
                    TableModel newModel = new DefaultTableModel(rows, columnsTitle) {
                        boolean[] canEdit = new boolean[]{
                            false, false, true, false, false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return canEdit[columnIndex];
                        }
                    };

//$$$ RUTINA ESPECIAL PARA EL MANEJO DEL MODELO QUE SE BORRA Y LA EDICION DEL CAMPO CASH
                    if (!TableTicker.isEditing()) {
                        TableTicker.setModel(newModel);
                        formateaCabeceroTicker();
                    }
                        
                    
                    int num = Math.min(settings.getTickers().size(), TableWeek1.getModel().getRowCount());
                    int[] my_positions = new int[num];
                    int[] my_positions_old = new int[500];

                    rowsCopy doblePosiciones = new rowsCopy(0, 0, 2);
                    my_positions_old = doblePosiciones.getPosiciones();
                    doblePosiciones.setPosiciones(my_positions);

                    for (int row = 0; row < num; row++) {

                        if (map.get(TableTicker.getValueAt(row, 0)) != null) {
                            List<Integer> listInt = map.get(TableTicker.getValueAt(row, 0) /*settings.getTickers().get(row).getName()*/);

                            my_positions[row] = -1;
                            if (!listInt.isEmpty() && settings.getTickers().get(row).isHistory()) {
//                    int col = ht.get(TableTicker.getValueAt(row, 0)).get(index) + 1;
                                int col = map.get(TableTicker.getValueAt(row, 0)).get(0);
                                my_positions[row] = col;
                                if (my_positions_old.length > 0) {
                                    
                                    String id_ticker = rows[row][0].toString();
//                                    JOptionPane.showMessageDialog(null, id_ticker+"");
                                    //if(col == my_positions_old[row]){DDBB.deleteParpadeo(id_ticker);}
                                    if (col < my_positions_old[row] && col <= 7) { 
                                        DDBB.insertParpadeo(id_ticker, 30);     
                                        try {
                                            /*al hacer el ejecutable se debe cambiar a solo notificacion.mp3*/
                                            //fis = new FileInputStream("C:\\Users\\maikolleon\\Documents\\NetBeansProjects\\s2m1f4r4\\src\\semaforo\\resources\\notificacion.mp3");
                                            fis = new FileInputStream("notificacion.mp3");
                                            bis = new BufferedInputStream(fis);
                                            player = new Player(bis);
                                            player.play();
                                            
//                                            fis2 = new FileInputStream("notificacion.mp3");
//                                            bis2 = new BufferedInputStream(fis2);
//                                            player2 = new Player(bis2);
//                                            player2.play();
//                                            
//                                            fis3 = new FileInputStream("notificacion.mp3");
//                                            bis3 = new BufferedInputStream(fis3);
//                                            player3 = new Player(bis3);
//                                            player3.play();
                                            
                                            

                                        } catch (JavaLayerException ex) {
                                            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (FileNotFoundException ex) {
                                            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        hiloParpadeo(id_ticker, row, col);
                                    } 
                                }
                            }

                        }

                    }
                    
                   
                                updateTableTickers();

                                updateTableWeek(TableWeek1, settings.getVaribable("rango_1"), 17);
                                loadTableCells(TableWeek1, WEEK1, map, 17);
                               
                                updateTableWeek(TableWeek2, settings.getVaribable("rango_2"), 17);
                                loadTableCells(TableWeek2, WEEK2, map, 17);
                               
                                updateTableWeek(TableWeek3, settings.getVaribable("rango_3"), 17);
                                loadTableCells(TableWeek3, WEEK3, map, 17);
                            
                       
                    TableTicker.getColumnModel().getColumn(7).setMaxWidth(0);
                    TableTicker.getColumnModel().getColumn(7).setMinWidth(0);
                    TableTicker.getColumnModel().getColumn(7).setPreferredWidth(0);
//                    TableTicker.getColumnModel().getColumn(6).setMaxWidth(0);
//                    TableTicker.getColumnModel().getColumn(6).setMinWidth(0);
//                    TableTicker.getColumnModel().getColumn(6).setPreferredWidth(0);
                    

                    
                    //Redraw the window
                    synchronized (updateLock) {
                        update = 1;
                    }

                    validate();

                    repaint();
                    

                }

                private int getFilaTickerFrontEnd(TableModel temp, String name) {

                    int i = 0;

                    String tickerName = "";
                    Object objTicker = null;

                    while (i < temp.getRowCount()) {
                        objTicker = temp.getValueAt(i, 0);
                        if (objTicker != null) {
                            tickerName = objTicker.toString();
                            if (tickerName.compareTo(name) == 0) {
                                break;
                            }

                        }
                        i++;

                    }

                    return (i == temp.getRowCount()) ? -1 : i;
    }

    double RoundTo2Decimals(double val){
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }

    public void formateaCabeceroTicker() {
        for (int i = 0; i < TableTicker.getColumnCount(); i++) {

            TableTicker.getColumnModel().getColumn(i).setHeaderRenderer(new ColumnHeaderRenderer());
        }
    }

    public void formateaCabeceroWeeks(JTable table) {

        table.getColumnModel().getColumn(0).setHeaderRenderer(new ColumnHeaderRenderer());
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setHeaderRenderer(new ColumnHeaderRenderer());

        for (int i = 1; i < table.getColumnCount() - 1; i++) {

            table.getColumnModel().getColumn(i).setHeaderRenderer(new ColumnHeaderRenderer(new Color(240, 240, 120)));
        }
    }

    public static <K extends Comparable, V extends Comparable> Map<K, List<Integer>> sortByValues(Map<K, List<Integer>> map) {
        List<Map.Entry<K, List<Integer>>> entries = new LinkedList<Map.Entry<K, List<Integer>>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, List<Integer>>>() {

            @Override
            public int compare(Map.Entry<K, List<Integer>> o1, Map.Entry<K, List<Integer>> o2) {
                int comparation = o1.getValue().get(tablaSelectedIndex).compareTo(o2.getValue().get(tablaSelectedIndex));
                if (comparation == 0) {
                    return o1.getKey().compareTo(o2.getKey());
                }
                return comparation;
            }
        });
        Map<K, List<Integer>> sortedMap = new LinkedHashMap<K, List<Integer>>();

        for (Map.Entry<K, List<Integer>> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public void loadTableCells(JTable TableWeek, int index, Map<String, List<Integer>> ht, int tamano) {
        System.out.println("---------------------------->"+TableWeek.getName());
        Settings settings = Controller.getSettings();

        for (int i = 0; i < tamano - 1; i++) {
            TableWeek.getColumnModel().getColumn(i).setCellRenderer(new ResetCellRenderer(TableWeek.getDefaultRenderer(Object.class
            ), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, i));
        }

        int[] my_positions = new int[num_positions];
        boolean[] paint = new boolean[num_positions];

        for (int j = 0; j < num_positions; j++) {
            my_positions[j] = -1;
            paint[j] = false;
        }

        //int num = settings.getTickers().size();
        // if (TableWeek.getModel().getRowCount() > 0) {
        int num = Math.min(settings.getTickers().size(), TableWeek.getModel().getRowCount());
        //   }

        // synchronized (Controller.positionLock) {
        for (int row = 0; row < num; row++) {

            if (ht.get(TableTicker.getValueAt(row, 0)) != null) {
//                if(row>24)JOptionPane.showMessageDialog(null, row+"");
                List<Integer> listInt = ht.get(TableTicker.getValueAt(row, 0) /*settings.getTickers().get(row).getName()*/);
                my_positions[row] = -1;
                if (!listInt.isEmpty() && settings.getTickers().get(row).isHistory()) {
//                    int col = ht.get(TableTicker.getValueAt(row, 0)).get(index) + 1;
                    int col = ht.get(TableTicker.getValueAt(row, 0)).get(index);
                    my_positions[row] = col;
                }

            }
        }

        // }
        //Modify the cell
        // if (num > 0) {
        for (int col = 0; col < tamano/*settings.getTickers().size()*/; col++) {

            //     if (paint[col]) {
            TableWeek.getColumnModel().getColumn(col).setCellRenderer(new MyCellRenderer(TableWeek.getDefaultRenderer(Object.class
            ), index, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, my_positions, col));
            //     }

        }
        //  }
    }

    public void precargaCapitalDB() {

        valoresTickerCapital = DDBB.queryCapital();

        if (semaforo.Semaforo.isDebugMode) {
            System.out.println("***************************** CAPITAL HASHMAP  " + valoresTickerCapital.size() + "  " + valoresTickerCapital.toString());
        }

//        int numFilasSettings = Controller.getSettings().getTickers().size();
//        
//        for (int i = 0; i < numFilasSettings; i++) {
//            String nombreTicker = Controller.getSettings().getTickers().get(i).name;
//            valoresTickerCapital.put(nombreTicker, Controller.getSettings().getCapital(nombreTicker) );
//
//        }
//        System.out.println("***************************** CAPITAL HASHMAP  " + valoresTickerCapital.size() + "  " + valoresTickerCapital.toString());
    }

    /**
     * THREAD PARA ACTUALIZAR LOS INDICES
     */
    public void hiloConsultaIndices() {
        try {

            long startTime = System.currentTimeMillis();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        // LLAMADA A LOS METODOS DE CONSULTA
                        do {
                            refrescaIndices();
                            actualizaImagenesIndices();
                            Thread.sleep(timerMilisegundosIndicesRefresh);
                        } while (true);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t.start();

        } catch (Exception ex) {
            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void hiloConsultaHistorial() {
        try {

            Thread t;
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                   
                    do {
                        ResultSet tickers = DDBB.TickersAll();
                        try {
                            while(tickers.next()){
                                Curl.jsonDecode(Curl.preciosTicker(tickers.getString("name")), tickers.getString("name"));
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    } while (true);
                    
                }
            });
            t.start();

        } catch (Exception ex) {
            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void hiloParpadeo(final String ticker, final int row, final int col) {
        try {
            
            long startTime = System.currentTimeMillis();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // LLAMADA A LOS METODOS DE CONSULTA
                        int p = 0;
                        do {
                            
                            ResultSet resParpadeo = DDBB.buscarParpadeo(ticker);
                            try {
                                if (resParpadeo.next()) {
                                    //JOptionPane.showMessageDialog(null, row+"-"+col, "", JOptionPane.ERROR_MESSAGE);
                                    p = resParpadeo.getInt("contador");
                                    if(p>0){
                                        DDBB.updateParpadeo(ticker, p-1, true);
                                        if(p%2==0){
                                            TableWeek1.setValueAt("[+]", row, col);
                                        }else{
                                            TableWeek1.setValueAt("[-]", row, col);
                                        }
                                    }else{
                                        DDBB.deleteParpadeo(ticker);
                                        TableWeek1.setValueAt("", row, col);
                                    }                                            
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            Thread.sleep(1000);
                            
                        } while (p>0);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t.start();

        } catch (Exception ex) {
            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * ACTUALIZA LOS VALORES DE PANTALLA CON LOS VALORES DE INDICE CARGADOS
     */
    public void refrescaIndices() throws InterruptedException {

        ConsultaIndices indicesUtil = new ConsultaIndices();

        Indice indiceNASDAQ = indicesUtil.ObtenerValorIndice(INDICE_NASDAQ);
        Indice indiceSandP = indicesUtil.ObtenerValorIndice(INDICE_SandP);
        Indice indiceDJI = indicesUtil.ObtenerValorIndice(INDICE_DJI);

        Double valorIndiceNASDAQ = indiceNASDAQ.valor;
        Double valorIndiceSandP = indiceSandP.valor;
        Double valorIndiceDJI = indiceDJI.valor;

        Double valorCierreNASDAQ = indiceNASDAQ.cierre;
        Double valorCierreSandP = indiceSandP.cierre;
        Double valorCierreDJI = indiceDJI.cierre;

        jLabelImagenNASDAQ.setText("NASDAQ: " + String.format("%.2f (%.2f%%)", valorIndiceNASDAQ, 100 * (valorIndiceNASDAQ - valorCierreNASDAQ) / valorCierreNASDAQ));
        jLabelImagenSandP.setText("S&P: " + String.format("%.2f (%.2f%%)", valorIndiceSandP, 100 * (valorIndiceSandP - valorCierreSandP) / valorCierreSandP));
        jLabelImagenDJI.setText("DJI: " + String.format("%.2f (%.2f%%)", valorIndiceDJI, 100 * (valorIndiceDJI - valorCierreDJI) / valorCierreDJI));

        jLabelImagenNASDAQ.setForeground((indiceNASDAQ.tendencia.equals(Indice.ALZA)) ? Color.GREEN : Color.RED);
        jLabelImagenSandP.setForeground((indiceSandP.tendencia.equals(Indice.ALZA)) ? Color.GREEN : Color.RED);
        jLabelImagenDJI.setForeground((indiceDJI.tendencia.equals(Indice.ALZA)) ? Color.GREEN : Color.RED);

        // PRENDE SEMAFORO
        if (semaforo.Semaforo.isDebugMode) {
            System.out.println("TENDENCIAS : " + indiceNASDAQ.tendencia + " " + indiceSandP.tendencia + " " + indiceDJI.tendencia);
        }

        if ((indiceNASDAQ.tendencia.equals(Indice.BAJA)) & (indiceSandP.tendencia.equals(Indice.BAJA)) & (indiceDJI.tendencia.equals(Indice.BAJA))) {
            jLabelSemaforo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/semaforoRojo.png")));
        } else {
            jLabelSemaforo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/semaforoVerde.png")));
        }

    }

    /**
     * ***** IMAGENES INDICE
     */
    public void actualizaImagenesIndices() {

        ConsultaIndices indicesUtil = new ConsultaIndices();
        jLabelImagenNASDAQ.setIcon(indicesUtil.ObtenerImagenIndice(INDICE_NASDAQ));
        jLabelImagenSandP.setIcon(indicesUtil.ObtenerImagenIndice(INDICE_SandP));
        jLabelImagenDJI.setIcon(indicesUtil.ObtenerImagenIndice(INDICE_DJI));
    }
//$$$ Este era el codigo que traia peos     
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//        }

    public void ajustaDetallesFrame() {

        tickerContainer.getViewport().setBackground(Color.black);
        firstWeeksContainer.getViewport().setBackground(Color.black);
        secondWeeksContainer.getViewport().setBackground(Color.black);
        thirdWeeksContainer.getViewport().setBackground(Color.black);

// PARA OCULTAR LOS WEEKS 2 Y 3 AL INICIAR LA APLICACION   
        PanelWeek1.setVisible(true);
        PanelWeek2.setVisible(false);
        PanelWeek3.setVisible(false);
//        jButton13Weeks.setForeground(colorBotonesPaneles);
//        jButton26Weeks.setForeground(Color.BLACK);
//        jButton52Weeks.setForeground(Color.BLACK);

// PARA PONER EL FRAME EN COLOR NEGRO
        getContentPane().setBackground(Color.BLACK);
//        firstWeeksContainer.setBackground(Color.BLACK);
//        TableWeek1.setBackground(Color.BLACK);
//        PanelWeek1.setBackground(Color.BLACK);

// ELIMINA LA DECORACION DEL BORDE, USANDO EL BORDER EMPTY 
        TitledBorder title;
        Border empty = BorderFactory.createEmptyBorder();

//        TableTicker.getTableHeader().setForeground(colorTituloTablas);
//        TableTicker.getTableHeader().setFont(letraTituloTablas);
        TableWeek1.getTableHeader().setForeground(colorTituloTablas);
        TableWeek1.getTableHeader().setFont(letraTituloTablas);
        //TableWeek1.getColumnModel().getColumn(0).setHeaderRenderer(new ColumnHeaderRenderer());
        formateaCabeceroTicker();

        formateaCabeceroWeeks(TableWeek1);
        formateaCabeceroWeeks(TableWeek2);
        formateaCabeceroWeeks(TableWeek3);

// $$$WEEKS23  
        TableWeek2.getTableHeader().setForeground(colorTituloTablas);
        TableWeek2.getTableHeader().setFont(letraTituloTablas);

        TableWeek3.getTableHeader().setForeground(colorTituloTablas);
        TableWeek3.getTableHeader().setFont(letraTituloTablas);

        TableWeek3.getTableHeader().setAlignmentY(alineacionTituloTablas);

        //$$$ MOD
        TableTicker.setFont(tipoLetraWeeks); // NOI18N
        TableTicker.setAlignmentX(LEFT_ALIGNMENT);

        TableWeek1.setFont(tipoLetraWeeks); // NOI18N        
// $$$WEEKS23        
        TableWeek2.setFont(tipoLetraWeeks); // NOI18N
        TableWeek3.setFont(tipoLetraWeeks); // NOI18N
    }

    public void syncScroll() {
        //$$$ SINCRONIZA SCROLL BARS
        Synchronizer synchronizer = new Synchronizer(tickerContainer, firstWeeksContainer, secondWeeksContainer, thirdWeeksContainer);
        tickerContainer.getVerticalScrollBar().addAdjustmentListener(synchronizer);
        //tickerContainer.getHorizontalScrollBar().addAdjustmentListener(synchronizer);
        firstWeeksContainer.getVerticalScrollBar().addAdjustmentListener(synchronizer);
        //firstWeeksContainer.getHorizontalScrollBar().addAdjustmentListener(synchronizer);
        secondWeeksContainer.getVerticalScrollBar().addAdjustmentListener(synchronizer);
        //secondWeeksContainer.getHorizontalScrollBar().addAdjustmentListener(synchronizer);
        thirdWeeksContainer.getVerticalScrollBar().addAdjustmentListener(synchronizer);
        //thirdWeeksContainer.getHorizontalScrollBar().addAdjustmentListener(synchronizer);
    }

    public void realTime() {
        timer = new Timer(Controller.getSettings().getVaribable(DDBB.RATIO_REFRESCO), new RealTimeListener());
        timer.start();
    }
    
    public static void SemaforoGrafico(JFreeChart chart){
  
        // set a custom background for the chart
        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), Color.BLACK, new Point(400, 200), Color.BLACK));

        // customise the title position and font
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setPaint(new Color(240, 240, 240));
        t.setFont(new Font("Arial", Font.BOLD, 0));
        PiePlot plot = null;
        plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.00);
        plot.setOutlineVisible(true);

        // use gradients and white borders for the section colours
       
        plot.setBaseSectionOutlinePaint(Color.BLACK);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(0.0f));

        // customise the section label appearance
        plot.setLabelFont(new Font("Courier New", Font.BOLD, 0));
        plot.setLabelLinkPaint(Color.BLACK);
        plot.setLabelLinkStroke(new BasicStroke(0.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.BLACK);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelBackgroundPaint(Color.BLACK);
        plot.setLabelShadowPaint(Color.BLACK);
  
        // add a subtitle giving the data source       
        
        // Mostramos la grafica dentro del jPanel1
        panel = new ChartPanel(chart);   
        panel.setBackground(Color.BLACK);
        panel.repaint();
        
        jPanel3.setLayout(null);
        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.remove(panel);
        jPanel3.add(panel); 
        
        
        jPanel3.repaint();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jTabbedPane1.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
    }
    
    public static void editGrafico(){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        double cfd=0.0;
        double bull=0.0;
        double bear=0.0;
        if(countCfd!=0)cfd = (countCfd*100)/(countBear+countBull+countCfd);
        if(countBull!=0)bull = ((countBull*100)/(countBear+countBull+countCfd));
        if(countBear!=0)bear = ((countBear*100)/(countBear+countBull+countCfd));
        Semaforo.l1.setText("CFD ("+String.format("%.2f", cfd)+"%)");
        Semaforo.l2.setText("BULL ("+String.format("%.2f", bull)+"%)");
        Semaforo.l3.setText("BEAR ("+String.format("%.2f", bear)+"%)");
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("CFD ("+cfd+"%)", new Integer((int)countCfd));
        pieDataset.setValue("BULL ("+bull+"%)", new Integer((int)countBull));
        pieDataset.setValue("BEAR ("+bear+"%)", new Integer((int)countBear));
        JFreeChart chart = null;
        chart = ChartFactory.createPieChart(
            "",  // chart title
            pieDataset,            // data
            false,              // no legend
            false,               // tooltips
            false               // no URL generation
        );


        // set a custom background for the chart
        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), Color.BLACK, new Point(400, 200), Color.BLACK));

        // customise the title position and font
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setPaint(new Color(240, 240, 240));
        t.setFont(new Font("Arial", Font.BOLD, 0));
        PiePlot plot = null;
        plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.00);
        plot.setOutlineVisible(true);

        // use gradients and white borders for the section colours

        plot.setBaseSectionOutlinePaint(Color.BLACK);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(0.0f));

        // customise the section label appearance
        plot.setLabelFont(new Font("Courier New", Font.BOLD, 0));
        plot.setLabelLinkPaint(Color.BLACK);
        plot.setLabelLinkStroke(new BasicStroke(0.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.BLACK);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelBackgroundPaint(Color.BLACK);
        plot.setLabelShadowPaint(Color.BLACK);

        // add a subtitle giving the data source       

        // Mostramos la grafica dentro del jPanel1
        Semaforo.panel.setChart(chart);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelWeek1 = new javax.swing.JPanel();
        firstWeeksContainer = new javax.swing.JScrollPane();
        TableWeek1 = new javax.swing.JTable();
        PanelTicker = new javax.swing.JPanel();
        tickerContainer = new javax.swing.JScrollPane();
        TableTicker = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        PanelWeek2 = new javax.swing.JPanel();
        secondWeeksContainer = new javax.swing.JScrollPane();
        TableWeek2 = new javax.swing.JTable();
        PanelWeek3 = new javax.swing.JPanel();
        thirdWeeksContainer = new javax.swing.JScrollPane();
        TableWeek3 = new javax.swing.JTable();
        PanelIndex = new javax.swing.JPanel();
        jLabelSemaforo = new javax.swing.JLabel();
        jLabelImagenNASDAQ = new javax.swing.JLabel();
        jLabelImagenSandP = new javax.swing.JLabel();
        jLabelImagenDJI = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabelNumPos = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabelPositionNum = new javax.swing.JLabel();
        jLabelInvested = new javax.swing.JLabel();
        jLabelLuzPrincipal = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        etiquetaGrupo2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        etiquetaGrupo3 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        etiquetaGrupo4 = new javax.swing.JLabel();
        etiquetaGrupo1 = new javax.swing.JLabel();
        etiquetaGrupo6 = new javax.swing.JLabel();
        etiquetaGrupo5 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        l1 = new javax.swing.JLabel();
        l2 = new javax.swing.JLabel();
        l3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        PanelWeek1.setBackground(new java.awt.Color(0, 0, 0));
        PanelWeek1.setToolTipText("");
        PanelWeek1.setName("Week 15"); // NOI18N

        firstWeeksContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        //TableWeek1.setTableHeader(null);
        TableWeek1.setBackground(new java.awt.Color(51, 51, 51));
        TableWeek1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Low", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "High"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        /*
        TableWeek1.setToolTipText("week");
        TableWeek1.getTableHeader().setReorderingAllowed(false);
        */
        firstWeeksContainer.setViewportView(TableWeek1);
        if (TableWeek1.getColumnModel().getColumnCount() > 0) {
            TableWeek1.getColumnModel().getColumn(0).setPreferredWidth(250);
            TableWeek1.getColumnModel().getColumn(17).setPreferredWidth(250);
        }

        javax.swing.GroupLayout PanelWeek1Layout = new javax.swing.GroupLayout(PanelWeek1);
        PanelWeek1.setLayout(PanelWeek1Layout);
        PanelWeek1Layout.setHorizontalGroup(
            PanelWeek1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(firstWeeksContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
        );
        PanelWeek1Layout.setVerticalGroup(
            PanelWeek1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(firstWeeksContainer)
        );

        PanelTicker.setBackground(new java.awt.Color(0, 0, 0));
        PanelTicker.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PanelTicker.setForeground(new java.awt.Color(255, 255, 255));
        PanelTicker.setFocusable(false);
        PanelTicker.setMaximumSize(new java.awt.Dimension(700, 32767));
        PanelTicker.setMinimumSize(new java.awt.Dimension(500, 0));
        PanelTicker.setPreferredSize(new java.awt.Dimension(550, 466));

        tickerContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tickerContainer.setMaximumSize(new java.awt.Dimension(500, 32767));

        //TableTicker.setTableHeader(null);
        TableTicker.setBackground(new java.awt.Color(0, 0, 0));
        TableTicker.setForeground(new java.awt.Color(255, 255, 255));
        TableTicker.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ticker", "Price", "to Invest", "cambio", "CFD", "Bought", "Remain", "LOW", "Compra?"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableTicker.setIntercellSpacing(new java.awt.Dimension(0, 0));
        TableTicker.setMaximumSize(new java.awt.Dimension(500, 0));
        TableTicker.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        TableTicker.getTableHeader().setReorderingAllowed(false);
        TableTicker.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableTickerMouseClicked(evt);
            }
        });
        tickerContainer.setViewportView(TableTicker);
        if (TableTicker.getColumnModel().getColumnCount() > 0) {
            TableTicker.getColumnModel().getColumn(0).setPreferredWidth(90);
            TableTicker.getColumnModel().getColumn(0).setMaxWidth(90);
            TableTicker.getColumnModel().getColumn(1).setPreferredWidth(90);
            TableTicker.getColumnModel().getColumn(2).setPreferredWidth(120);
            TableTicker.getColumnModel().getColumn(2).setMaxWidth(120);
            TableTicker.getColumnModel().getColumn(4).setPreferredWidth(90);
            TableTicker.getColumnModel().getColumn(4).setMaxWidth(90);
            TableTicker.getColumnModel().getColumn(5).setPreferredWidth(90);
            TableTicker.getColumnModel().getColumn(5).setMaxWidth(90);
            TableTicker.getColumnModel().getColumn(6).setPreferredWidth(90);
            TableTicker.getColumnModel().getColumn(6).setMaxWidth(90);
            TableTicker.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("TICKERS");

        javax.swing.GroupLayout PanelTickerLayout = new javax.swing.GroupLayout(PanelTicker);
        PanelTicker.setLayout(PanelTickerLayout);
        PanelTickerLayout.setHorizontalGroup(
            PanelTickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tickerContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
            .addGroup(PanelTickerLayout.createSequentialGroup()
                .addGap(223, 223, 223)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelTickerLayout.setVerticalGroup(
            PanelTickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTickerLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tickerContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tickerContainer.getAccessibleContext().setAccessibleParent(PanelWeek1);

        PanelWeek2.setBackground(new java.awt.Color(0, 0, 0));

        secondWeeksContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        //TableWeek2.setTableHeader(null);
        TableWeek2.setBackground(new java.awt.Color(51, 51, 51));
        TableWeek2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Low", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "High"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        /*
        */
        secondWeeksContainer.setViewportView(TableWeek2);
        if (TableWeek2.getColumnModel().getColumnCount() > 0) {
            TableWeek2.getColumnModel().getColumn(0).setPreferredWidth(250);
            TableWeek2.getColumnModel().getColumn(17).setPreferredWidth(250);
        }

        javax.swing.GroupLayout PanelWeek2Layout = new javax.swing.GroupLayout(PanelWeek2);
        PanelWeek2.setLayout(PanelWeek2Layout);
        PanelWeek2Layout.setHorizontalGroup(
            PanelWeek2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(secondWeeksContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
        );
        PanelWeek2Layout.setVerticalGroup(
            PanelWeek2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(secondWeeksContainer)
        );

        PanelWeek3.setBackground(new java.awt.Color(0, 0, 0));

        thirdWeeksContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        //TableWeek3.setTableHeader(null);
        TableWeek3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Low", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "High"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        /*
        TableWeek3.getTableHeader().setReorderingAllowed(false);
        */
        thirdWeeksContainer.setViewportView(TableWeek3);
        if (TableWeek3.getColumnModel().getColumnCount() > 0) {
            TableWeek3.getColumnModel().getColumn(0).setPreferredWidth(250);
            TableWeek3.getColumnModel().getColumn(17).setPreferredWidth(250);
        }

        javax.swing.GroupLayout PanelWeek3Layout = new javax.swing.GroupLayout(PanelWeek3);
        PanelWeek3.setLayout(PanelWeek3Layout);
        PanelWeek3Layout.setHorizontalGroup(
            PanelWeek3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelWeek3Layout.createSequentialGroup()
                .addComponent(thirdWeeksContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelWeek3Layout.setVerticalGroup(
            PanelWeek3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(thirdWeeksContainer)
        );

        PanelIndex.setBackground(new java.awt.Color(0, 0, 0));

        jLabelSemaforo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSemaforo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/semaforo/resources/semaforoOLD.png"))); // NOI18N
        jLabelSemaforo.setText("jLabel5");

        jLabelImagenNASDAQ.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelImagenNASDAQ.setForeground(new java.awt.Color(102, 255, 102));
        jLabelImagenNASDAQ.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelImagenNASDAQ.setToolTipText("");
        jLabelImagenNASDAQ.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabelImagenNASDAQ.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.gray, null));
        jLabelImagenNASDAQ.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelImagenNASDAQ.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jLabelImagenSandP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelImagenSandP.setForeground(new java.awt.Color(102, 255, 102));
        jLabelImagenSandP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelImagenSandP.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabelImagenSandP.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.gray, null));
        jLabelImagenSandP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelImagenSandP.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jLabelImagenDJI.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelImagenDJI.setForeground(new java.awt.Color(102, 255, 102));
        jLabelImagenDJI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelImagenDJI.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabelImagenDJI.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.gray, null));
        jLabelImagenDJI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelImagenDJI.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("INDEX:");

        javax.swing.GroupLayout PanelIndexLayout = new javax.swing.GroupLayout(PanelIndex);
        PanelIndex.setLayout(PanelIndexLayout);
        PanelIndexLayout.setHorizontalGroup(
            PanelIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelIndexLayout.createSequentialGroup()
                .addGroup(PanelIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelIndexLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelSemaforo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelImagenSandP, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelImagenNASDAQ, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelImagenDJI, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 1, Short.MAX_VALUE))
        );
        PanelIndexLayout.setVerticalGroup(
            PanelIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelIndexLayout.createSequentialGroup()
                .addGroup(PanelIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelIndexLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabelSemaforo, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelIndexLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel5)))
                .addGap(95, 95, 95)
                .addComponent(jLabelImagenNASDAQ, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelImagenSandP, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelImagenDJI, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel1MouseExited(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("NUM POSITIONS - (MAX: 12)");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("% INVESTED CAPITAL ");

        jLabelNumPos.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelNumPos.setForeground(new java.awt.Color(240, 240, 120));
        jLabelNumPos.setText("9");

        jLabel8.setText("jLabel8");

        jLabelPositionNum.setFont(new java.awt.Font("Tahoma", 1, 25)); // NOI18N
        jLabelPositionNum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPositionNum.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelPositionNum.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabelInvested.setFont(new java.awt.Font("Tahoma", 1, 25)); // NOI18N
        jLabelInvested.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelInvested.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelInvested.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabelLuzPrincipal.setBackground(new java.awt.Color(0, 0, 255));
        jLabelLuzPrincipal.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabelLuzPrincipal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLuzPrincipal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelLuzPrincipal.setPreferredSize(new java.awt.Dimension(120, 200));

        jButton1.setText("GROUPS");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        etiquetaGrupo2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        etiquetaGrupo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo2.setText("100%");
        etiquetaGrupo2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo2.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("BAN");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("TEC");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("FAR");

        etiquetaGrupo3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        etiquetaGrupo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo3.setText("100%");
        etiquetaGrupo3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo3.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("BIO");

        etiquetaGrupo4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        etiquetaGrupo4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo4.setText("100%");
        etiquetaGrupo4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo4.setPreferredSize(new java.awt.Dimension(60, 60));

        etiquetaGrupo1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        etiquetaGrupo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo1.setText("100%");
        etiquetaGrupo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo1.setPreferredSize(new java.awt.Dimension(60, 60));

        etiquetaGrupo6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        etiquetaGrupo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo6.setText("100%");
        etiquetaGrupo6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo6.setPreferredSize(new java.awt.Dimension(60, 60));

        etiquetaGrupo5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        etiquetaGrupo5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo5.setText("100%");
        etiquetaGrupo5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        etiquetaGrupo5.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("BEV");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("ALI");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etiquetaGrupo1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(etiquetaGrupo2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(etiquetaGrupo3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(etiquetaGrupo4, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(etiquetaGrupo5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(etiquetaGrupo6, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(etiquetaGrupo2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etiquetaGrupo3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etiquetaGrupo4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etiquetaGrupo5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etiquetaGrupo6, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etiquetaGrupo1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jButton2.setText("NEW LOW");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setText("NEW HIGH");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setMinimumSize(new java.awt.Dimension(250, 50));
        jPanel3.setPreferredSize(new java.awt.Dimension(250, 50));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        l1.setBackground(new java.awt.Color(255, 0, 0));
        l1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        l1.setForeground(new java.awt.Color(255, 255, 255));
        l1.setText("CFD");

        l2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        l2.setForeground(new java.awt.Color(255, 255, 255));
        l2.setText("BULL");

        l3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        l3.setForeground(new java.awt.Color(255, 255, 255));
        l3.setText("BEAR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabelPositionNum, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(103, 103, 103)
                        .addComponent(jLabelInvested, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelNumPos)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(l1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l3, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                .addGap(42, 42, 42)
                .addComponent(jLabelLuzPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelLuzPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabelNumPos)
                            .addComponent(jLabel7))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel8)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabelPositionNum, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                    .addComponent(jLabelInvested, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(9, 9, 9))))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(l1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(l2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(l3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(1600, 2500));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1200, 5));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1200, 5));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseExited(evt);
            }
        });

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 0));

        jMenu2.setBackground(new java.awt.Color(153, 153, 153));
        jMenu2.setText("Settings");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenu2MousePressed(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(PanelTicker, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(389, 389, 389)
                        .addComponent(PanelWeek1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanelWeek2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanelWeek3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0)
                .addComponent(PanelIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelWeek3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelWeek1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelWeek2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelTicker, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(PanelIndex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MousePressed
        // TODO add your handling code here:
        //  if (SettingsGUI.openWindow()) {
        if(newLboleano)newL.dispose();
        if(newHboleano)newH.dispose();
        seguir = false;
        
        settingsGUI.setup();
        settingsGUI.setVisible(true);
        //  }
    }//GEN-LAST:event_jMenu2MousePressed

    private void TableTickerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableTickerMouseClicked
//
//        int row = TableTicker.rowAtPoint(evt.getPoint());
//        int col = TableTicker.columnAtPoint(evt.getPoint());
//        int[] position = new int[num_positions];
//
//        JLabel label1 = new JLabel("Image and Text", JLabel.CENTER);
//
//        if (col == 6) {
//
//            int num = 0;
//            String valor = (String) TableTicker.getValueAt(row, col);
//            String ticker = (String) TableTicker.getValueAt(row, 0);
//            ResultSet Ticker = DDBB.BuscarTickers(ticker);
//            try {
//                if (Ticker.next()) {
//                    num = Ticker.getInt("hedge");
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            if (num == 2) {
//                DDBB.updateTicker(ticker, 3);
//                if (row % 2 == 0){
//                    TableTicker.getColumnModel().getColumn(col).setCellRenderer((TableCellRenderer) new CustomRendererCell().getTableCellRendererComponentCell(TableTicker, Color.DARK_GRAY, null, true, true, 1, 6));
//                } else {
//                    TableTicker.getColumnModel().getColumn(col).setCellRenderer((TableCellRenderer) new CustomRendererCell().getTableCellRendererComponentCell(TableTicker, Color.BLACK, null, true, true, 1, 6));
//                }
//                TableTicker.setValueAt("\u25C9", row, col);
//            }
//            if (num == 1) {
//                DDBB.updateTicker(ticker, 2);
//                TableTicker.getColumnModel().getColumn(col).setCellRenderer((TableCellRenderer) new CustomRendererCell().getTableCellRendererComponentCell(TableTicker, Color.GREEN, null, true, true, 1, 6));
//                TableTicker.setValueAt("\u25C9", row, col);
//            }
//            if (num == 3) {
//                DDBB.updateTicker(ticker, 1);
//                TableTicker.getColumnModel().getColumn(col).setCellRenderer((TableCellRenderer) new CustomRendererCell().getTableCellRendererComponentCell(TableTicker, Color.RED, null, true, true, 1, 6));
//                TableTicker.setValueAt("\u25C9", row, col);
//            }
//            // TODO add your handling code here:
//        }


    }//GEN-LAST:event_TableTickerMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        try {
            // TODO add your handling code here:
            seguir = false;
            new Groups().setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        try {
            // TODO add your handling code here:
            settingsGUI.dispose();
            newL = new newLow();
            newL.setVisible(true);
            newLboleano = true;
        } catch (SQLException ex) {
            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton2MouseClicked

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered
        // TODO add your handling code here:
        seguir = false;
        timer.stop();
        //JOptionPane.showMessageDialog(null,seguir+"", "", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jPanel1MouseEntered

    private void jPanel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseExited
        // TODO add your handling code here:
        seguir = true;
        timer.start();
       //JOptionPane.showMessageDialog(null,seguir+"", "", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jPanel1MouseExited

    private void jTabbedPane1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseEntered
        // TODO add your handling code here:
        seguir = false;
        timer.stop();
    }//GEN-LAST:event_jTabbedPane1MouseEntered

    private void jTabbedPane1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseExited
        // TODO add your handling code here:
        seguir = true;
        timer.start();
    }//GEN-LAST:event_jTabbedPane1MouseExited

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            settingsGUI.dispose();
            newH = new newHigh();
            newH.setVisible(true);
            newHboleano = true;
        } catch (SQLException ex) {
            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3MouseClicked

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        tablaSelectedIndex = jTabbedPane1.getSelectedIndex();
//      JOptionPane.showMessageDialog(null,tablaSelectedIndex+"", "", JOptionPane.ERROR_MESSAGE);
        core();
    }//GEN-LAST:event_jTabbedPane1StateChanged

    public int numPosiciones = 0;

    int update = 0;
    Object updateLock = new Object();
    LoadingDialog loadingDialog = null;
    SettingsGUI settingsGUI = null;
    Features features = null;

    public void setUp() {

        this.setTitle("Semaforo");
        URL hj = getClass().getResource("resources/semaforo.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(hj));

        listener = new UpdateTableListener() {

            @Override
            public void addTickers() {

                // Add in Tickers Table
                Settings settings = Controller.getSettings();
                synchronized (updateLock) {
                    update = 1;
                }
                CustomRenderer cr = new CustomRenderer(TableTicker.getDefaultRenderer(Object.class
                ), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);

                TableTicker.setDefaultRenderer(Object.class, cr);
                DefaultTableModel model = (DefaultTableModel) TableTicker.getModel();

                Object[] o = new Object[2];
                for (int i = model.getRowCount();
                        i < settings.getTickers()
                        .size(); i++) {

                    o[0] = settings.getTickers().get(i).getName();
                    o[1] = 0;
                    model.addRow(o);
                }

                // Resize the vector of values
                num_positions = Controller.getSettings().getTickers().size();

                num_positions++;

                // Add in Week Tables
                CustomRenderer cr1 = new CustomRenderer(TableWeek1.getDefaultRenderer(Object.class), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);
                CustomRenderer cr2 = new CustomRenderer(TableWeek2.getDefaultRenderer(Object.class), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);
                CustomRenderer cr3 = new CustomRenderer(TableWeek3.getDefaultRenderer(Object.class), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);

                // Table Week 1
                TableWeek1.setDefaultRenderer(Object.class, cr1);
                DefaultTableModel model1 = (DefaultTableModel) TableWeek1.getModel();

                Object[] o1 = new Object[10];

                // Table Week 2
                TableWeek2.setDefaultRenderer(Object.class, cr2);
                DefaultTableModel model2 = (DefaultTableModel) TableWeek2.getModel();

                Object[] o2 = new Object[10];

                // Table Week 3
                TableWeek3.setDefaultRenderer(Object.class, cr3);
                DefaultTableModel model3 = (DefaultTableModel) TableWeek3.getModel();

                Object[] o3 = new Object[10];

                for (int i = model1.getRowCount();
                        i < settings.getTickers()
                        .size(); i++) {

                    model1.addRow(o1);

                    model2.addRow(o2);

                    model3.addRow(o3);
                }

                synchronized (updateLock) {
                    update = 0;
                }

            }

            @Override
            public boolean canUpdate() {
                return update == 1;
            }

            @Override
            public void stopThread() {
                synchronized (updateLock) {
                    update = 2;
                }
            }

            @Override
            public void updateTickers() {
                synchronized (updateLock) {
                    Settings settings = Controller.getSettings();
                    update = 2;

                    CustomRenderer cr = new CustomRenderer(TableTicker.getDefaultRenderer(Object.class), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);

                    TableTicker.setDefaultRenderer(Object.class, cr);

                    for (int i = 0; i < settings.getTickers().size(); i++) {

                        TableTicker.setValueAt(settings.getTickers().get(i).getName(), i, 0);
                    }

                    update = 0;
                }
            }

            @Override
            public void updateVariables() {
                Settings settings = Controller.getSettings();
                validate();
                repaint();

                timer.setDelay(settings.getVaribable(DDBB.RATIO_REFRESCO));
            }

            @Override
            public void reomveTicker(int index) {
                synchronized (updateLock) {
                    // Add in Tickers Table

                    update = 2;

                    DefaultTableModel model = (DefaultTableModel) TableTicker.getModel();
                    model.removeRow(index);

                    // Remove row in Week Tables
                    // Table Week 1
                    DefaultTableModel model1 = (DefaultTableModel) TableWeek1.getModel();

                    // Table Week 2
                    DefaultTableModel model2 = (DefaultTableModel) TableWeek2.getModel();

                    // Table Week 3
                    DefaultTableModel model3 = (DefaultTableModel) TableWeek3.getModel();

                    // Table Week 1
                    model1.removeRow(index);

                    // Table Week 2
                    model2.removeRow(index);

                    // Table Week 3
                    model3.removeRow(index);

                    num_positions = Controller.getSettings().getTickers().size();

                    update = 0;

                }
            }
        };

        Controller.setup(listener);/* = new Controller();*/

        Settings settings = Controller.getSettings();

        num_positions = Controller.getSettings().getTickers().size();

        loadTableTickers();

        loadTableWeek(TableWeek1, WEEK1);

        loadTableWeek(TableWeek2, WEEK2);

        loadTableWeek(TableWeek3, WEEK3);

        String cad = settings.getVaribable(DDBB.RANGO_1) > 1 ? "weeks" : "week";

        cad = settings.getVaribable(DDBB.RANGO_2) > 1 ? "weeks" : "week";

        cad = settings.getVaribable(DDBB.RANGO_3) > 1 ? "weeks" : "week";

        TableTicker.setCellSelectionEnabled(
                false);
        TableWeek1.setCellSelectionEnabled(
                false);
        TableWeek2.setCellSelectionEnabled(
                false);
        TableWeek3.setCellSelectionEnabled(
                false);
        /*validate();
         repaint();*/
        settingsGUI = new SettingsGUI(listener);
        features = new Features();
        features.setBackground(java.awt.Color.BLACK);

    }

    public synchronized void loadTableTickers() {
        Settings settings = Controller.getSettings();

        CustomRenderer cr = new CustomRenderer(TableTicker.getDefaultRenderer(Object.class
        ), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);
        TableTicker.setDefaultRenderer(Object.class, cr);
        DefaultTableModel model = (DefaultTableModel) TableTicker.getModel();
        TableTicker.setRowHeight(20); //40
        TableTicker.setFont(new Font("Arial", Font.BOLD, 12)); //18
        TableTicker.getColumnModel().getColumn(0).setPreferredWidth(10);
        TableTicker.getColumnModel().getColumn(1).setPreferredWidth(30);

        JTableHeader header = TableTicker.getTableHeader();
        header.setPreferredSize(new Dimension(100, 30));

        TableTicker.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));

        Object[] o = new Object[2];
        for (int i = 0;
                i < settings.getTickers()
                .size(); i++) {

            o[0] = settings.getTickers().get(i).getName();
            o[1] = 0;
            model.addRow(o);
        }
    }

    public synchronized void updateTableTickers() {
        Settings settings = Controller.getSettings();

        //  TableWeek.getColumnModel().getColumn(0).setPreferredWidth(120);
        //  TableWeek.getColumnModel().getColumn(9).setPreferredWidth(120);
        // CustomRenderer cr = new CustomRenderer(TableWeek.getDefaultRenderer(Object.class), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);
        //  TableWeek.setDefaultRenderer(Object.class, cr);
        DefaultTableModel model = (DefaultTableModel) TableTicker.getModel();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        int num = Math.min(settings.getTickers().size(), model.getRowCount());
        if (TableTicker.getModel().getRowCount() > 0) {
            num = Math.min(settings.getTickers().size(), TableTicker.getModel().getRowCount());
        }

        isload = false;

        for (int i = 0; i < num; i++) {
            if (settings.getTickers().get(i).getCurrentPrice() <= 0) {
                isload = true;
            }
        }

        new Thread() {

            @Override
            public void run() {
                if (loadingDialog == null) {
                    JFrame frame = new JFrame();
                    loadingDialog = new LoadingDialog(null, "Please Wait", "Loading Data");
                    loadingDialog.setEnabled(false);
                    loadingDialog.setAlwaysOnTop(false);

                }

                if (loadingDialog.isShowing() && isload == false /*&& isloadHistory == false*/) {
                    loadingDialog.setVisible(false);
                } else if (!loadingDialog.isShowing() && isload == true /*&& isloadHistory == true*/) {
                    loadingDialog.setVisible(true);
                }

            }
        }.start();

    }

    public synchronized void loadTableWeek(JTable TableWeek, int index) {
        Settings settings = Controller.getSettings();

        //TableWeek.getColumnModel().getColumn(0).setPreferredWidth(350); //150
        TableWeek.setRowHeight(20); //40
        //TableWeek.getColumnModel().getColumn(9).setPreferredWidth(350);//150
        TableWeek.setFont(new Font("Arial", Font.BOLD, 12));//18

        CustomRenderer cr = new CustomRenderer(TableWeek.getDefaultRenderer(Object.class), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);
        TableWeek.setDefaultRenderer(Object.class, cr);
        DefaultTableModel model = (DefaultTableModel) TableWeek.getModel();
        Object[] o = new Object[10];

        JTableHeader header = TableWeek.getTableHeader();
        header.setPreferredSize(new Dimension(10, 30)); //100
        TableWeek.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));

        /* DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
         centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
         TableWeek.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
         TableWeek.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
         TableWeek.setDefaultRenderer(String.class, centerRenderer);*/
        int num = settings.getTickers().size();

        /*   while(DDBB.loadData) {
         try {
         Thread.sleep(500);
         } catch (InterruptedException ex) {
         Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
         }
         }*/
        if (TableWeek.getModel().getRowCount() > 0) {
            num = Math.min(settings.getTickers().size(), TableWeek.getModel().getRowCount());
        }

        for (int i = 0; i < num; i++) {
            o[0] = settings.getTickers().get(i).getMinValue(index);
            o[9] = settings.getTickers().get(i).getMaxValue(index);
            model.addRow(o);
        }
    }

    public synchronized void updateTableWeek(JTable TableWeek, int index, int tamano) {

        Settings settings = Controller.getSettings();

        //  TableWeek.getColumnModel().getColumn(0).setPreferredWidth(120);
        //  TableWeek.getColumnModel().getColumn(9).setPreferredWidth(120);
        // CustomRenderer cr = new CustomRenderer(TableWeek.getDefaultRenderer(Object.class), Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);
        //  TableWeek.setDefaultRenderer(Object.class, cr);
        DefaultTableModel model = (DefaultTableModel) TableWeek.getModel();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);

        int num = Math.min(settings.getTickers().size(), model.getRowCount());
        if (TableWeek.getModel().getRowCount() > 0) {
            num = Math.min(settings.getTickers().size(), TableWeek.getModel().getRowCount());
        }

        for (int i = 0; i < num; i++) {
            for (int m = 0; m < TableTicker.getModel().getRowCount(); m++) {
                if (TableTicker != null && TableTicker.getModel() != null) {
                    if (TableTicker.getModel().getValueAt(m, 0) != null) {
                        if (TableTicker.getModel().getValueAt(m, 0).equals(settings.getTickers().get(i).getName())) {
                            model.setValueAt(String.format("%.2f", settings.getTickers().get(i).getMinValue(index)), m, 0);
                            model.setValueAt(String.format("%.2f", settings.getTickers().get(i).getMaxValue(index)), m, tamano); //TODO: Parametrizar
//                            JOptionPane.showMessageDialog(null,index+"", "", JOptionPane.ERROR_MESSAGE);
                            if(index==13){
                                DDBB.updateTicker(TableTicker.getModel().getValueAt(m, 0).toString(), String.format("%.2f", settings.getTickers().get(i).getMinValue(index)), String.format("%.2f", settings.getTickers().get(i).getMaxValue(index)));
                            }
                        }
                    }
                }
            }
        }
    }

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
            java.util.logging.Logger.getLogger(Semaforo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Semaforo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Semaforo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Semaforo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //if(Curl.initComponentSwing()==1)
                try {
                    jframe = new Semaforo();
                    jframe.setVisible(true);
                } catch (JavaLayerException ex) {
                    Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelIndex;
    private javax.swing.JPanel PanelTicker;
    private javax.swing.JPanel PanelWeek1;
    private javax.swing.JPanel PanelWeek2;
    private javax.swing.JPanel PanelWeek3;
    private javax.swing.JTable TableTicker;
    private javax.swing.JTable TableWeek1;
    private javax.swing.JTable TableWeek2;
    private javax.swing.JTable TableWeek3;
    private javax.swing.JLabel etiquetaGrupo1;
    private javax.swing.JLabel etiquetaGrupo2;
    private javax.swing.JLabel etiquetaGrupo3;
    private javax.swing.JLabel etiquetaGrupo4;
    private javax.swing.JLabel etiquetaGrupo5;
    private javax.swing.JLabel etiquetaGrupo6;
    private javax.swing.JScrollPane firstWeeksContainer;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelImagenDJI;
    private javax.swing.JLabel jLabelImagenNASDAQ;
    private javax.swing.JLabel jLabelImagenSandP;
    private javax.swing.JLabel jLabelInvested;
    private javax.swing.JLabel jLabelLuzPrincipal;
    private javax.swing.JLabel jLabelNumPos;
    private javax.swing.JLabel jLabelPositionNum;
    private javax.swing.JLabel jLabelSemaforo;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel3;
    public static javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JLabel l1;
    public static javax.swing.JLabel l2;
    public static javax.swing.JLabel l3;
    private javax.swing.JScrollPane secondWeeksContainer;
    private javax.swing.JScrollPane thirdWeeksContainer;
    private javax.swing.JScrollPane tickerContainer;
    // End of variables declaration//GEN-END:variables

    /*oculto las consulta de indices*/
}

class CustomRendererCell extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 6703872492730589499L;

    public Component getTableCellRendererComponentCell(JTable table, Color color, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        cellComponent.setBackground(color);
        cellComponent.setForeground(color);

        return cellComponent;
    }
}
