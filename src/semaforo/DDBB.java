 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semaforo;

import java.util.Random;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static kobytest.KobyTest.isDebugConsoleMode;

/**
 *
 * @author fernando
 */
public class DDBB {

    public static String RANGO_1 = "rango_1";
    public static String RANGO_2 = "rango_2";
    public static String RANGO_3 = "rango_3";
    public static String RATIO_REFRESCO = "ratio_refresco";
    public static String VALUE = "value";

    private static Connection conn = null;

    private final static String GET_RANGO_1 = "select * from PREFERENCIAS where name='" + RANGO_1 + "'";
    private final static String GET_RANGO_2 = "select * from PREFERENCIAS where name='" + RANGO_2 + "'";
    private final static String GET_RANGO_3 = "select * from PREFERENCIAS where name='" + RANGO_3 + "'";
    private final static String GET_REFRESH_TIME = "select * from PREFERENCIAS where name='" + RATIO_REFRESCO + "'";

    private final static String url = "jdbc:h2:./ticker_database";
    public static boolean loadData = true;

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void getConection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, "sa", "sa");
            }
        } catch (SQLException ex) {
            //closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void closeConection() {

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ResultSet query(String query) {
        try {

            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException ex) {
            // // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static void createrDB() {
        
         final String CREATE_TABLE_COUNT = "CREATE TABLE `count` (\n"
                + "  `id` INT NOT NULL); ";
         
         final String CREATE_TABLE_TICKERS_LOW13 = "CREATE TABLE `tickers_low13` (\n"
                + "  `id` INT NOT NULL AUTO_INCREMENT,\n"
                + "  `name_t` VARCHAR(15) NOT NULL,"
                + "  `tabla` VARCHAR(15) NOT NULL,"
                + "  `encendido` INT,"
                + "  `fecha_low13` VARCHAR(20) NOT NULL,\n"
                + "PRIMARY KEY (`id`)); ";
        
        final String CREATE_TABLE_PREFERENCE = "CREATE TABLE `preferencias` ( "
                + " `id` INT NOT NULL AUTO_INCREMENT,"
                + " `name` VARCHAR(50) NULL,  "
                + "`value` VARCHAR(50) NULL,"
                + "PRIMARY KEY (`id`));"; 

        final String CREATE_TABLE_TICKERS = "CREATE TABLE `tickers` (\n"
                + "  `id` INT NOT NULL AUTO_INCREMENT,\n"
                + "  `name` VARCHAR(15) NOT NULL,"
                + "  `capital` INT,"
                + "  `hedge` INT,"
                + "  `low_13` VARCHAR,"
                + "  `high_13` VARCHAR,"
                + "PRIMARY KEY (`id`)); "
                + "CREATE UNIQUE INDEX `simbolo` ON `tickers` (`name`)";

        final String CREATE_TABLE_COMPRAS = "CREATE TABLE `compras` (\n"
                + "  `id` INT NOT NULL AUTO_INCREMENT,\n"
                + "  `id_ticker` VARCHAR(20) NOT NULL,\n"
                + "  `compradas` VARCHAR(20) NOT NULL,"
                + "  `compro` INT(1) NOT NULL,"
                + "  `fecha` VARCHAR(20) NOT NULL);";
        
        final String CREATE_TABLE_PARPADEO = "CREATE TABLE `parpadeo` (\n"
                + "  `id` INT NOT NULL AUTO_INCREMENT,\n"
                + "  `id_ticker` VARCHAR(20) NOT NULL,\n"
                + "  `contador` INT(1) NOT NULL)";
        
        final String CREATE_TABLE_GROUP = "CREATE TABLE `groups` (\n"
                + "  `id` INT NOT NULL AUTO_INCREMENT,\n"
                + "  `nombre` VARCHAR(20) NOT NULL,\n"
                + "  `maximo_a_invertir` INT(11) NOT NULL)";
        
        final String CREATE_TABLE_GROUP_TICKERS = "CREATE TABLE `groups_tickers` (\n"
                + "  `id` INT NOT NULL AUTO_INCREMENT,\n"
                + "  `group_name` VARCHAR(20) NOT NULL,\n"
                + "  `ticker_name` VARCHAR(20) NOT NULL,\n"
                + "  `invertido` INT(11) NOT NULL)";
        
        final String CREATE_TABLE_LOW_HIGH = "CREATE TABLE `low_high` (\n"
                + "  `id` INT NOT NULL AUTO_INCREMENT,\n"
                + "  `precio_ticker` VARCHAR(20) NOT NULL,\n"
                + "  `nombre_ticker` VARCHAR(20) NOT NULL,\n"
                + "  `fecha_low_high` VARCHAR(20) NOT NULL,\n"
                + "  `high_high` VARCHAR(20) NOT NULL,\n"
                + "  `low_low` VARCHAR(20) NOT NULL,\n"
                + "  `low_dia` VARCHAR(20) NOT NULL,\n"
                + "  `high_dia` VARCHAR(20) NOT NULL)";
        
        final String CREATE_TABLE_ITERACION = "CREATE TABLE `iteracion` (\n"
                + "  `id` INT NOT NULL AUTO_INCREMENT,\n"
                + "  `name_iteracion` VARCHAR(20) NOT NULL,\n"
                + "  `l_h` VARCHAR(20) NOT NULL,\n"
                + "  `iteracion` INT(11) NOT NULL,\n"
                + "  `inicio` INT(11) NOT NULL,\n"
                + "  `longitud` INT(11) NOT NULL,\n"
                + "  `posicion_vector` INT(11) NOT NULL)";

        final String CREATE_COLUMN_CAPITAL = "ALTER TABLE `tickers` ADD `capital` INT";
        Calendar cal2 = Calendar.getInstance();
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaHoy = format2.format(cal2.getTime());
        final String INSERT_DEFAULT_PREFERENCES_RANGO_1 = "insert INTO preferencias VALUES(null,'" + RANGO_1 + "', '13')";
        final String INSERT_DEFAULT_PREFERENCES_RANGO_2 = "insert INTO preferencias VALUES(null,'" + RANGO_2 + "', '26')";
        final String INSERT_DEFAULT_PREFERENCES_RANGO_3 = "insert INTO preferencias VALUES(null,'" + RANGO_3 + "', '52')";
        final String INSERT_DEFAULT_PREFERENCES_RATIO = "insert INTO preferencias VALUES(null,'" + RATIO_REFRESCO + "',2000)";
//        final String w = "insert INTO compras VALUES(NULL,'GILD','4',1,'"+fechaHoy+"')";
//        final String w2 = "insert INTO compras VALUES(NULL,'GOOG','2',1,'"+fechaHoy+"')";
//        final String w3 = "insert INTO compras VALUES(NULL,'IBM','-1',1,'"+fechaHoy+"')";
//        final String w4 = "insert INTO compras VALUES(NULL,'ADM','2',1,'"+fechaHoy+"')";

        final String grupo1 = "insert INTO groups VALUES(NULL,'grupo1', 0)";
        final String grupo2 = "insert INTO groups VALUES(NULL,'grupo2', 0)";
        final String grupo3 = "insert INTO groups VALUES(NULL,'Grupo3', 0)";
        final String grupo4 = "insert INTO groups VALUES(NULL,'Grupo4', 0)";
        final String grupo5 = "insert INTO groups VALUES(NULL,'Grupo5', 0)";
        final String grupo6 = "insert INTO groups VALUES(NULL,'Grupo6', 0)";
        final String COUNT = "insert INTO COUNT VALUES(0)";
        
//        final String grupo1 = "insert INTO groups VALUES(NULL,'grupo1','gild',1000,100)";
//        final String grupo2 = "insert INTO groups VALUES(NULL,'grupo2','gild',1000,100)";
//        final String grupo3 = "insert INTO groups VALUES(NULL,'Grupo3','gild',1000,100)";
//        final String grupo4 = "insert INTO groups VALUES(NULL,'Grupo4','gild',1000,100)";
//        final String grupo5 = "insert INTO groups VALUES(NULL,'Grupo5','gild',1000,100)";
//        final String grupo6 = "insert INTO groups VALUES(NULL,'Grupo6','gild',1000,100)";
        
        Statement stmt;
        try {
            stmt = conn.createStatement();

            // Creation of the tables
            stmt.executeUpdate(CREATE_TABLE_TICKERS);
            stmt.executeUpdate(CREATE_TABLE_PREFERENCE);
            stmt.executeUpdate(CREATE_TABLE_COMPRAS);
            stmt.executeUpdate(CREATE_TABLE_PARPADEO);
            stmt.executeUpdate(CREATE_TABLE_GROUP);
            stmt.executeUpdate(CREATE_TABLE_GROUP_TICKERS);
            stmt.executeUpdate(CREATE_TABLE_LOW_HIGH);
            stmt.executeUpdate(CREATE_TABLE_ITERACION);
            stmt.executeUpdate(CREATE_TABLE_TICKERS_LOW13);
            stmt.executeUpdate(CREATE_TABLE_COUNT);
            // TICKERS
            stmt.executeUpdate(INSERT_DEFAULT_PREFERENCES_RANGO_1);
            stmt.executeUpdate(INSERT_DEFAULT_PREFERENCES_RANGO_2);
            stmt.executeUpdate(INSERT_DEFAULT_PREFERENCES_RANGO_3);
            stmt.executeUpdate(INSERT_DEFAULT_PREFERENCES_RATIO);
            stmt.executeUpdate(grupo1);
            stmt.executeUpdate(grupo2);
            stmt.executeUpdate(grupo3);
            stmt.executeUpdate(grupo4);
            stmt.executeUpdate(grupo5);
            stmt.executeUpdate(grupo6);

        } catch (SQLException ex) {

            if (semaforo.Semaforo.isDebugMode) {
                //System.out.println("################################################    ENTRANDO EN ERROR DDBB " + ex.getErrorCode());
            }
            if (ex.getErrorCode() != 42101) {
               // closeConection();
            }
        }

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(CREATE_COLUMN_CAPITAL);
        } catch (SQLException ex) {

            if (semaforo.Semaforo.isDebugMode) {
                //System.out.println("################################################    ENTRANDO EN ERROR COLUMNA " + ex.getErrorCode());
            }
            if (ex.getErrorCode() != 42101) { 
               // closeConection();
            }
        }

    }

    public static void setup(final Settings settings) {

        getConection();
        createrDB();

        try {

            getConection();
            ResultSet res1 = query(GET_RANGO_1);
            res1.next();
            settings.setVaribable(RANGO_1, Integer.parseInt(res1.getString(VALUE)));

            getConection();
            ResultSet res2 = query(GET_RANGO_2);
            res2.next();
            settings.setVaribable(RANGO_2, Integer.parseInt(res2.getString(VALUE)));

            getConection();
            ResultSet res3 = query(GET_RANGO_3);
            res3.next();
            settings.setVaribable(RANGO_3, Integer.parseInt(res3.getString(VALUE)));

            getConection();
            ResultSet res4 = query(GET_REFRESH_TIME);
            res4.next();
            settings.setVaribable(RATIO_REFRESCO, Integer.parseInt(res4.getString(VALUE)));
            // // closeConection();
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        }

        loadData = true;
        Thread thread = new Thread() {
            public void run() {
                ResultSet resTicekrs = DDBB.Tickers();
                try {
                    while (resTicekrs.next()) {

                        String nameTicker = resTicekrs.getString("name");

                        synchronized (Controller.countLock) {
                            Controller.finish = false;
                        }

                        while (!Controller.conectado) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        settings.addTicker(nameTicker);

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    loadData = false;
                }

               // closeConection();
            }
        };
        thread.start();

    }

    public static void deleteTicker(String ticker) {
        getConection();

        String deleteTicker = "DELETE FROM TICKERS WHERE name = '" + ticker + "'";
        String deleteCompras = "DELETE FROM COMPRAS WHERE id_ticker = '" + ticker + "'";
        String deleteGrupos = "DELETE FROM GROUPS_TICKERS WHERE ticker_name = '" + ticker + "'";

        try {
            conn.createStatement().executeUpdate(deleteTicker);
            conn.createStatement().executeUpdate(deleteCompras);
            conn.createStatement().executeUpdate(deleteGrupos);
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           // closeConection();
        }

    }

    public static void deleteCompras(String ticker) {
        getConection();

        String deleteCompras = "DELETE FROM COMPRAS WHERE id_ticker = '" + ticker + "'";

        try {
            conn.createStatement().executeUpdate(deleteCompras);
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           // closeConection();
        }

    }
    
     public static void deleteIteracion(String ticker, String l_h) {
        getConection();

        String deleteCompras = "DELETE FROM ITERACION WHERE name_iteracion = '" + ticker + "' and l_h='"+l_h+"'";

        try {
            conn.createStatement().executeUpdate(deleteCompras);
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           // closeConection();
        }

    }
    
     public static void deleteGroupTicker(String ticker) {
        getConection();

        String deleteCompras = "DELETE FROM GROUPS_TICKERS WHERE ticker_name = '" + ticker + "'";

        try {
            conn.createStatement().executeUpdate(deleteCompras);
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           // closeConection();
        }

    }
    public static void deleteComprasFecha(String fecha) {
        getConection();

        String deleteCompras = "DELETE FROM COMPRAS WHERE fecha != '" + fecha + "'";

        try {
            conn.createStatement().executeUpdate(deleteCompras);
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public static void insert(String sql) {
        update(sql);
    }

    public static void update(String sql) {
        try {

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateTicker(String name, String new_name) {
        final String UPDATE_PREFERENCE = "UPDATE tickers SET name='" + new_name + "' WHERE name='" + name + "'";
        getConection();
        update(UPDATE_PREFERENCE);
       // closeConection();
    }
    public static void updateParpadeo(String id_ticker, int parpadeo, boolean descontar) {
        String UPDATE_PREFERENCE=null;
        if(descontar)
        UPDATE_PREFERENCE = "UPDATE parpadeo SET contador=contador-1 WHERE id_ticker='" + id_ticker + "'";
        else 
        UPDATE_PREFERENCE = "UPDATE parpadeo SET contador='" + parpadeo + "' WHERE id_ticker='" + id_ticker + "'";
        getConection();
        update(UPDATE_PREFERENCE);
       // closeConection();
    }
    public static void deleteParpadeo(String id_ticker) {
        getConection();

        String deleteCompras = "DELETE FROM parpadeo WHERE id_ticker = '" + id_ticker + "'";

        try {
            conn.createStatement().executeUpdate(deleteCompras);
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           // closeConection();
        }

    }
     public static void deleteLowHigh() {
        getConection();

        String deleteCompras = "DELETE FROM low_high WHERE id >= 0";
        try {
            conn.createStatement().executeUpdate(deleteCompras);
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           // closeConection();
        }

    }
     
      public static void deleteLowHighFecha(String fecha) {
        getConection();

        String deleteCompras = "DELETE FROM low_high WHERE fecha_low_high!='"+fecha+"'";
        try {
            conn.createStatement().executeUpdate(deleteCompras);
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           // closeConection();
        }

    }
    
    public static ResultSet buscarParpadeo(String id_ticker) {
        String sql = "select * from parpadeo where id_ticker='"+id_ticker+"' order by id desc";
        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
            //closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
     public static void insertParpadeo(String id_ticker, int parpadeo) {
        String INSERT_TICKER = "insert INTO parpadeo VALUES(null, '" + id_ticker + "',"+parpadeo+");";
        getConection();
        insert(INSERT_TICKER);
       // closeConection();
    }
     public static void insertLowHigh(String nombre_ticker, String fecha_low_high, String low_dia, String high_dia) {
         
        String sql = "select * from low_high where nombre_ticker='"+nombre_ticker+"' and low_low != '' and high_high!='' order by id desc limit 1";
        String low_low = "999999999.00";
        String high_high = "0.00";
        try {
            getConection();
            ResultSet result =  conn.createStatement().executeQuery(sql);
            if(result.next()){
                low_low = result.getString("low_low");
                high_high = result.getString("high_high");      
            }
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        String INSERT_TICKER = "insert INTO low_high VALUES(null, '', '" + nombre_ticker + "', '"+fecha_low_high+"', '"+high_high+"', '"+low_low+"', '"+low_dia+"', '"+high_dia+"');";
         //System.out.println("->"+INSERT_TICKER);
        getConection();
        insert(INSERT_TICKER);
       // closeConection();
    }
     
     
//$$$    
    public static void updateTickerCapital(String name, int capital) {
        final String UPDATE_TICKER_CAPITAL = "UPDATE tickers SET capital=" + capital + " WHERE name='" + name + "'";
        getConection();
        update(UPDATE_TICKER_CAPITAL);
       // closeConection();

        if (semaforo.Semaforo.isDebugMode) {
            //System.out.println("################################################################");
        }
        if (semaforo.Semaforo.isDebugMode) {
            //System.out.println("################################################################");
        }
        if (semaforo.Semaforo.isDebugMode) {
            //System.out.println("################################ updateTickerCapital: Name: " + name + " Capital: " + capital);
        }
    }

    public static int requestTickerCapital(String name) {
        int capital = 0;
        final String SELECT_TICKER = "SELECT capital from TICKERS WHERE name='" + name + "'";

        try {
            getConection();
            ResultSet x = conn.createStatement().executeQuery(SELECT_TICKER);
            if (x.next()) {
                capital = x.getInt("capital");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           // closeConection();
        }
        //System.out.println("################################################################");
        //System.out.println("################################################################");
        //System.out.println("################################ requestTickerCapital: " + capital);

        return capital;
    }

    public static void updatePreference(String name, String value) {
        final String UPDATE_TICKER = "UPDATE PREFERENCIAS SET value='" + value + "' WHERE name='" + name + "'";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }

    public static void updateCompras(String ticker, int value) {
        final String UPDATE_TICKER = "UPDATE compras SET compro=" + value + " WHERE id_ticker='" + ticker + "'";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static void updatePriceTicker(String ticker, String value) {
        final String UPDATE_TICKER = "UPDATE low_high SET precio_ticker='" + value + "' WHERE nombre_ticker='" + ticker + "'";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static void updateLowLowTicker(String ticker, String value) {
        final String UPDATE_TICKER = "UPDATE low_high SET low_low='" + value + "' WHERE nombre_ticker='" + ticker + "'";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static void updateHighHighTicker(String ticker, String value) {
        final String UPDATE_TICKER = "UPDATE low_high SET high_high='" + value + "' WHERE nombre_ticker='" + ticker + "'";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static void updateTicker(String ticker, int value) {
        final String UPDATE_TICKER = "UPDATE tickers SET hedge='" + value + "' WHERE name='" + ticker + "'";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static void updateGroup(String nombre_old, String nombre_new, int maximo_a_invertir) {
        final String UPDATE_TICKER = "UPDATE groups SET nombre='" + nombre_new + "', maximo_a_invertir = "+maximo_a_invertir+" WHERE nombre='" + nombre_old + "';";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static void updateGroupTickerInvertido(String name_ticker, int invertido) {
        final String UPDATE_TICKER = "UPDATE groups_tickers SET invertido=" + invertido + " WHERE ticker_name='" + name_ticker + "';";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static void updateGroupTickerDelete(String name_ticker) {
        final String UPDATE_TICKER = "UPDATE groups_tickers SET group_name='' WHERE ticker_name='" + name_ticker + "';";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static void updateGroupTickerDelete(String name_ticker, String name_group) {
        final String UPDATE_TICKER = "UPDATE groups_tickers SET group_name='"+name_group+"' WHERE ticker_name='" + name_ticker + "';";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static void updateGroupTicker(String name_group_old, String name_group_new) {
        final String UPDATE_TICKER = "UPDATE groups_tickers SET group_name='"+name_group_new+"' WHERE group_name='" + name_group_old + "';";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static Map<String, ElementoCapitalDB> queryCapital() {
        String SELECT_CAPITAL = "select * from tickers;";
        Map<String, ElementoCapitalDB> valoresTickerCapital = new HashMap<>();
        ElementoCapitalDB elemCapitalDB = new ElementoCapitalDB();

        try {
            getConection();
            ResultSet result = conn.createStatement().executeQuery(SELECT_CAPITAL);
           // if (result.first()) {
            //      valoresTickerCapital.put(result.getString("name"), result.getInt("capital"));
            while (result.next()) {
                elemCapitalDB = new ElementoCapitalDB();
                elemCapitalDB.setCapital(result.getInt("capital"));
                if (semaforo.Semaforo.isDebugMode) {
                    //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ " + result.getString("name") + " " + elemCapitalDB.getCapital());
                }
                elemCapitalDB.setIsChequeado(false);
                valoresTickerCapital.put(result.getString("name"), elemCapitalDB);
            }
           // }

            return valoresTickerCapital;

        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void insertTicker(String name) {
        String INSERT_TICKER = "insert INTO tickers VALUES(null, '" + name + "',0,3,'', '');";
        getConection();
        insert(INSERT_TICKER);
       // closeConection();
    }
    
    public static void insertTickerLow13(String name_t, int encedido, String fecha_low13, String tabla) {
        String INSERT_TICKER = "insert INTO tickers_low13 VALUES(null, '" + name_t + "', '" + tabla + "', "+encedido+", '"+fecha_low13+"');";
        getConection();
        insert(INSERT_TICKER);
       // closeConection();
    }
    public static void updateTickerLow13(String ticker, int encendido) {
        final String UPDATE_TICKER = "UPDATE tickers_low13 SET encendido="+encendido+" WHERE name_t='" + ticker + "';";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    public static ResultSet TickersLow13(String name_t, String fecha, String tabla) {
        String sql = "select * from tickers_low13 where name_t='"+name_t+"' and fecha_low13='"+fecha+"' and tabla = '"+tabla+"' order by id desc";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
     public static void deleteLow13(String name_t, String fecha, String tabla) {
        getConection();

        String deleteCompras = "DELETE FROM tickers_low13 WHERE name_t='"+name_t+"' and fecha_low13!='"+fecha+"' and tabla = '"+tabla+"'";
        try {
            conn.createStatement().executeUpdate(deleteCompras);
        } catch (SQLException ex) {
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           // closeConection();
        }

    }
     public static void updateTicker(String ticker, String low_13, String high_13) {
        final String UPDATE_TICKER = "UPDATE tickers SET low_13='"+low_13+"',high_13='"+high_13+"'  WHERE name='" + ticker + "';";
        getConection();
        update(UPDATE_TICKER);
       // closeConection();
    }
    
    public static void insertIteracion(String name, int iteracion, int inicio, int longitud, int posicionVector, String l_h) {
        String INSERT_TICKER = "insert INTO iteracion VALUES(null, '" + name + "', '" + l_h + "',"+iteracion+","+inicio+","+longitud+","+posicionVector+");";
        getConection();
        insert(INSERT_TICKER);
       // closeConection();
    }

    public static void insertCompras(String id_ticker, String compradas, String fecha, int compro) {
        String INSERT_COMPRAS = "insert INTO compras VALUES(NULL, '" + id_ticker + "', '" + compradas + "', " + compro + ", '" + fecha + "');";
        //JOptionPane.showMessageDialog(null, INSERT_COMPRAS+"");
        getConection();
        insert(INSERT_COMPRAS);
       // closeConection();
    }
    public static void insertGroupsTickers(String group, String ticker, int invertido) {
        String INSERT_COMPRAS = "insert INTO groups_tickers VALUES(NULL, '" + group + "', '" + ticker + "', " + invertido + ");";
        //JOptionPane.showMessageDialog(null, INSERT_COMPRAS+"");
        getConection();
        insert(INSERT_COMPRAS);
       // closeConection();
    }
    public static ResultSet Tickers() {
        String sql = "select name from tickers order by id asc";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static ResultSet TickersAll() {
        String sql = "select * from tickers order by id desc";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static ResultSet Iteraciones(int inicio, int longitud, String order, String l_h) {
        String sql = "select * from iteracion where  l_h='"+l_h+"' order by inicio desc, iteracion desc";
//        System.out.println(""+sql);
        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static ResultSet GruposTickers(String group) {
        String sql = "select * from groups_tickers where group_name = '"+group+"' order by id asc";
  
        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static ResultSet lowHigh(String ticker) {
        String sql = "select * from low_high where nombre_ticker = '"+ticker+"' order by id asc limit 15";
  
        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static ResultSet lowHighFecha(String fecha) {
        String sql = "select * from low_high where fecha_low_high = '"+fecha+"' order by id desc limit 1";
  
        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static ResultSet lowHighFechaName(String fecha, String ticker) {
        String sql = "select * from low_high where fecha_low_high = '"+fecha+"' and nombre_ticker = '"+ticker+"' order by id desc limit 1";
  
        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static ResultSet GruposTickersSumarInvertido(String group) {
        String sql = "select SUM(invertido) from groups_tickers where group_name = '"+group+"'";
  
        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    
    public static ResultSet GruposTickersTicker(String ticker) {
        String sql = "select * from groups_tickers where ticker_name= '"+ticker+"' order by id asc";
  
        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
            //closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static ResultSet GruposTickers() { 
        String sql = "select * from groups_tickers order by id asc";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static ResultSet Grupos() {
        String sql = "select * from groups;";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static ResultSet Grupos(String nombre) {
        String sql = "select * from groups where nombre='"+nombre+"';";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static ResultSet BuscarComprasFecha(String ticker, String fecha) {
        String sql = "select * from compras where fecha='" + fecha + "' and id_ticker='" + ticker + "'";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static ResultSet BuscarTickers() {
        String sql = "select name, id, low_13, high_13 from tickers ORDER BY id asc";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static ResultSet BuscarTickers(String name) {
        String sql = "select * from tickers where name='"+name+"'";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static ResultSet BuscarCompras(String ticker) {
        String sql = "select * from compras where id_ticker='" + ticker + "'";

        try {
            getConection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
           // closeConection();
            Logger.getLogger(DDBB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static float getTickerValue(String nameTicker, float ini, float fin) {
        Random r = new Random(System.nanoTime());

        return (r.nextFloat() * (fin - ini)) + ini;
    }

    public static float getTickerHistory(String nameTicker, int weeksAgo) {
        Random r = new Random(System.nanoTime());

        return (float) ((r.nextInt(weeksAgo) * 1.0f) + r.nextFloat() * (r.nextInt(weeksAgo) * 1.0f));
    }

    
}
