  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kobytest;
/*
 import com.ib.controller.ApiConnection.ILogger;
 import com.ib.controller.ApiController;
 import com.ib.controller.ApiController.IBulletinHandler;
 import com.ib.controller.ApiController.IConnectionHandler;
 import com.ib.controller.ApiController.ITimeHandler;
 import com.ib.controller.Formats;
 import com.ib.controller.Types.NewsType;
 import java.util.logging.Logger;
 */

import com.ib.client.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import static kobytest.KobyTest.miPortfolioUpdates;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;
import semaforo.Controller;
import semaforo.Curl;
import semaforo.DDBB;
import semaforo.Groups;
import semaforo.HistoricalDataReq;
import semaforo.Semaforo;
import static semaforo.Semaforo.countBear;
import static semaforo.Semaforo.countBull;
import static semaforo.Semaforo.countCfd;
import static semaforo.Semaforo.jPanel3;
import static semaforo.Semaforo.panel;
import semaforo.Settings;


/**
 *
 * @author carlos
 */
/*
 TODO:
    
 -VAL: tornar valor, callback / o error    
 *HIST: Traure la fecha de la semana
 -HIST: Calcular max,min per semana i tornar per callback
        
 -FACTORIZAR
 -> callbacks on recibir la info / or threads
 -DATABASE???



 *PROBLEMA FESTIVOS: viernes 3rd abril --> ESPERAR FINISHED
 -Enviar semana 0.
 -Demanar per dies
 -o 1 semana mes de la que toca (offset per a semana actual)


 */
public abstract class KobyTest {
    public static String posiciones = "";
    public static HistoricalDataReq[] peticionesHistorical = new HistoricalDataReq[100];
    public static boolean newTicker = false; 
    public static String strSemana, strWeek;
    public static int id_ant=-1, count = -1;
    static String symbol_current = "";
    public static Boolean isDebugConsoleMode = false;
    
    public static EClientSocket connection;
    static EWrapper ewrapper;

    static String[] valores = {}; //interna
    public static int total_weeks_ago = 0;

    static HashMap<Integer, String> valores_tabla = new HashMap<Integer, String>();

    static HashMap<Integer, String> existen_valores_tabla = new HashMap<Integer, String>();

    // [Valor][Semana] = Array(valores
    static HashMap<String, HashMap<Integer, HashMap<String, Double>>> historico_valores = new HashMap<String, HashMap<Integer, HashMap<String, Double>>>();
    static HashMap<String, HashMap<Integer, HashMap<String, Double>>> historico_valores_cumulativo = new HashMap<String, HashMap<Integer, HashMap<String, Double>>>();

    static ListenerComprobarSimbolo listenerExiste = null;
    static ListenerConnectionConsole listener = null;
    
    public static String ticP = "";
    public static double strikeP = 0.0;
    public static int positionP = 0;
    public static int segundaPasada = 0;
    public static boolean graficopaint =false;
    public static boolean graficopaintP =false;
    
    /*new ListenerConnectionConsole() {

     @Override
     public void callback_historico(String simbolo, int weeks_ago, double high, double low, double open, double close) {
     //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     @Override
     public void callback_error(int Code, String error, String simbolo) {
     //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     @Override
     public void callback_historico_cumulativo(HashMap<String, HashMap<Integer, HashMap<String, Double>>> historico_valores_cumulativo) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     @Override
     public void callback_valor(String simbolo, int tipo, double precio) {
     //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     @Override
     public void callback_historico_cumulativo(String Valor, HashMap<Integer, HashMap<String, Double>> historico_valores_cumulativo) {
     // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }
     };
     */
    /**
     * @param args the command line arguments
     */
    
    
   
//######################################     
//######################################        
//######################################    
//######################################         
//######################################    
//######################################     
//######################################  
//######################################      
//######################################   
//######################################  
//######################################  
//######################################
//######################################     
//######################################     
            
//############ POSITION ################   
    
    public static void posiciones() {
        semaforo.LogFile.logFile("#### INVOCANDO posiciones");

        if (semaforo.Semaforo.isDebugMode) System.out.println("############################### INVOCANDO posiciones ###############################");

        try {
            if (Controller.conectado) {
                connection.reqPositions();
            }
        } catch (Exception e) {
            
            semaforo.LogFile.logFile("#### ERROR posiciones");
            
            System.out.println("############################### Error posiciones ###############################");
        }
    }

    public static void miPortfolioUpdates() {
        
        semaforo.LogFile.logFile("#### INVOCANDO miPortfolioUpdates");
        
        if (semaforo.Semaforo.isDebugMode) System.out.println("############################### INVOCANDO buscando miPortfolioUpdates ###############################");

        try {
            if (Controller.conectado) {
                connection.reqAccountUpdates(true, "0");
            }
        } catch (Exception e) {
            
            semaforo.LogFile.logFile("#### ERROR miPortfolioUpdates");
            
            System.out.println("############################### Error miPortfolioUpdates ###############################");
        }
    }

    
    
    static String[] valores_init = {};//{"KO", "IBM"};

    public static HashMap<String, HashMap<Integer, HashMap<String, Double>>> getHistorico_valores_cumulativo() {
        return historico_valores_cumulativo;
    }

    public static void setHistorico_valores_cumulativo(HashMap<String, HashMap<Integer, HashMap<String, Double>>> historico_valores_cumulativo) {
        KobyTest.historico_valores_cumulativo = historico_valores_cumulativo;
    }

    public static void init(int _total_weeks_ago, String[] _valores, ListenerConnectionConsole _listiner) {
        KobyTest.listener = _listiner;
        KobyTest.total_weeks_ago = _total_weeks_ago;
        KobyTest.valores_init = _valores;
        // total_weeks_ago++; // porque queremos desde el dia de hoy: esta es la semana 0 
        conectar();
    }

    public static void simbolo_existe_wrapper() {

    }

    public static void conectar() {
        final int clientId = 1;
        final int port = 4001;
        final String host = "127.0.0.1";

        //EReader ereader = new EReader();
        final EWrapper anyWrapper = new EWrapper() {

            @Override
            public void error(Exception e) {
                System.out.println("error(exception):" + e.toString());
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void error(String str) {
                System.out.println("error(string):" + str);
                //throw new UnsupportedOperationException("Not error yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void error(int id, int errorCode, String errorMsg) {
              //  System.out.println("id:" + id + ". errorcode:" + errorCode + ". msg: " + errorMsg);

                // throw new UnsupportedOperationException(errorMsg); //To change body of generated methods, choose Tools | Templates.
                wrapper_error(id, errorCode, errorMsg);
            }

            @Override
            public void connectionClosed() {
                //JOptionPane.showMessageDialog(null,"Tws cerro flujo de datos", "DATA NULL", JOptionPane.ERROR_MESSAGE);
                System.out.println("Connection closed");
                //   throw new UnsupportedOperationException("Not connectionClosed yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
                if (isDebugConsoleMode) System.out.println("Entra tickPrice");
                wrapper_valor(tickerId, field, price);

                if (isDebugConsoleMode) System.out.println("Returned tickPrice for :" + tickerId + ". field:" + field + ". Price" + price);
                //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void tickSize(int tickerId, int field, int size) {
                if (isDebugConsoleMode) System.out.println("Returned tickSize for :" + tickerId + ". field:" + field + ". size" + size);
                //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
                if (isDebugConsoleMode) System.out.println("Returned tickOptionComputation for :" + tickerId + ". "
                        + "field:" + field + ". "
                        + "impliedVol" + impliedVol + ". "
                        + "delta " + delta + ".  "
                        + "optPrice" + optPrice + ".  "
                        + "pvDividend" + pvDividend + ".  "
                        + "gamma" + gamma + ".  "
                        + "vega" + vega + ".  "
                        + "theta" + theta + ".  "
                        + "undPrice" + undPrice + ".  "
                );

//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void tickGeneric(int tickerId, int tickType, double value) {
                System.out.println("Returned tickGeneric for :" + tickerId + ". tickType:" + tickType + ". value" + value);
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void tickString(int tickerId, int tickType, String value) {
                System.out.println("Returned tickString for :" + tickerId + ". tickType:" + tickType + ". value" + value);
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry) {
                if (isDebugConsoleMode) System.out.println("Returned tickGeneric for :" + tickerId + ". "
                        + "basisPoints:" + basisPoints + ". "
                        + "formattedBasisPoints:" + formattedBasisPoints + ". "
                        + "impliedFuture:" + impliedFuture + ". "
                        + "holdDays:" + holdDays + ". "
                        + "futureExpiry:" + futureExpiry + ". "
                        + "dividendImpact:" + dividendImpact + ". "
                        + "dividendsToExpiry:" + dividendsToExpiry + ". "
                );
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void openOrderEnd() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
// ==================================================  
// ==================================================  
// ==================================================      
// ==================================================    
            
        Double netLiquidation = 0.0;
        Double excessLiquidation = 0.0;
       
            
            
            
            
            @Override
            public void updateAccountValue(String key, String value, String currency, String accountName) {
                if (semaforo.Semaforo.isDebugMode) System.out.println("TWSClientInterface:updateAccountValue: Key: "+key + " value:" + value + " currency:" + currency + " accountName:" +accountName );
                
                switch (key){
                    case 
                        "NetLiquidation" : netLiquidation = Double.parseDouble(value) ; 
                        break;
                    case    
                        "ExcessLiquidity" : excessLiquidation = Double.parseDouble(value) ;                  
                        break;                     
                }
                
//                if (netLiquidation <> 0 && excessLiquidation <> 0 ) {
                    Double temp = ( (netLiquidation - excessLiquidation) / netLiquidation) * 100;
                    Controller.getSettings().setPorcentCapital( temp );
//                    
//                } 
                       
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
// ==================================================  
// ==================================================  
// ==================================================      
// ==================================================    
            
// ==================================================  
// ==================================================  
// ==================================================      
// ==================================================    
            @Override
            public void updateAccountTime(String timeStamp) {
                if (semaforo.Semaforo.isDebugMode) System.out.println("TWSClientInterface:updateAccountTime: "+timeStamp);
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void accountDownloadEnd(String accountName) {
                if (semaforo.Semaforo.isDebugMode) System.out.println("TWSClientInterface:accountDownloadEnd: "+accountName);             
                //listener.guardaNumPos(78);
            }

            @Override
            public void nextValidId(int orderId) {
                System.out.println("Ya estamos coenctados con el orderId: " + orderId);

                try {
                    conexion_aceptada_wrapper();
                } catch (SQLException ex) {
                    Logger.getLogger(KobyTest.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(KobyTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void contractDetails(int reqId, ContractDetails contractDetails) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+contractDetails.m_summary.m_secType); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void bondContractDetails(int reqId, ContractDetails contractDetails) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void contractDetailsEnd(int reqId) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void execDetails(int reqId, Contract contract, Execution execution) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void execDetailsEnd(int reqId) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
                System.out.println("Bulletin news");
                //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void managedAccounts(String accountsList) {
                System.out.println("Cuentas: " + accountsList);

                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void receiveFA(int faDataType, String xml) {
                System.out.println("Received FA");
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
 
            @Override
            public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {
                if(count>=0){
                    if(!date.equals(strWeek)){
                        Calendar cal = Calendar.getInstance();
                        int current_day = cal.get(Calendar.DAY_OF_MONTH);
                        int current_month = cal.get(Calendar.MONTH) + 1;
                        int current_year = cal.get(Calendar.YEAR);
                        String fecha = current_year+"-"+current_month+"-"+current_day;
                        DDBB.deleteLowHighFecha(fecha);
                        DDBB.insertLowHigh(symbol_current, fecha, ""+low, ""+high);
                        System.out.println("-------------------------------------"+strWeek);
                        System.out.println("HISTORICAL DATA for order_id: " + reqId);
                        System.out.println("Historical Date: " + date);
                        System.out.println("Historical Open: " + open);
                        System.out.println("Historical high: " + high);
                        System.out.println("Historical low: " + low);
                        System.out.println("Historical close: " + close);
                        System.out.println("Historical volume: " + volume);
                        System.out.println("Historical count: " + count);
                        System.out.println("Historical WAP: " + WAP);
                        System.out.println("Historical hasGaps: " + hasGaps);
                        System.out.println("-------------------------------------");
                    }else{
                        ResultSet ticks = DDBB.Tickers();
                        try {
                            while(ticks.next()){
                            if(reqId==Settings.getTickerID(ticks.getString("name"))){
                                DDBB.updateHighHighTicker(ticks.getString("name"), high+"");
                                DDBB.updateLowLowTicker(ticks.getString("name"), low+"");
                                System.out.println(ticks.getString("name")+"-------------------------------------"+strWeek);
                                System.out.println("HISTORICAL DATA for order_id: " + reqId);
                                System.out.println("Historical Date: " + date);
                                System.out.println("Historical Open: " + open);
                                System.out.println("Historical high: " + high);
                                System.out.println("Historical low: " + low);
                                System.out.println("Historical close: " + close);
                                System.out.println("Historical volume: " + volume);
                                System.out.println("Historical count: " + count);
                                System.out.println("Historical WAP: " + WAP);
                                System.out.println("Historical hasGaps: " + hasGaps);
                                System.out.println("-------------------------------------");
                            }    
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(KobyTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                //wrapper_historico(reqId, date, high, low, open, close);
            }

            @Override
            public void scannerParameters(String xml) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void scannerDataEnd(int reqId) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void currentTime(long time) {
                System.out.println("Current time is: " + time);
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void fundamentalData(int reqId, String data) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void deltaNeutralValidation(int reqId, UnderComp underComp) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void tickSnapshotEnd(int reqId) {
                System.out.println("tickSnapshotEnd: " + reqId);
                wrapper_snapshot_ended(reqId);
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void marketDataType(int reqId, int marketDataType) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void commissionReport(CommissionReport commissionReport) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            

            
            
//######################################     
//######################################        
//######################################    
//######################################         
//######################################    
//######################################     
//######################################  
//######################################      
//######################################   
//######################################  
//######################################  
//######################################
//######################################     
//######################################     
            
//############ POSITION ################           
            
            
            int numPosiciones = 0;
            public int numPosicionesTotal = 0;
            public Map valorPosiciones = new HashMap();
            
            @Override
            public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
                if(contract.m_secType.equals("CFD")){
                    Semaforo.countCfd++;
                }
                
                   
                      if(ticP.isEmpty()){
                          ticP = contract.m_symbol;
                          positionP = position;
                          strikeP = contract.m_strike;
                      }else{
                          if(ticP.equals(contract.m_symbol)){
                             if(strikeP>contract.m_strike){
                                 if(positionP>0){
                                     //el mayor es positivo Bear  
                                     Semaforo.countBear++;
                                     System.out.println(">>>>>countBear>>>>>"+Semaforo.countBear);
                                 }else{
                                     //ll mayor es negativo Bull
                                     Semaforo.countBull++;
                                     System.out.println(">>>>>countBull>>>>>"+Semaforo.countBull);
                                 }
                             }else{
                                 if(position>0){
                                     //el mayor es positivo Bear  
                                     Semaforo.countBear++;
                                     System.out.println(">>>>>countBear>>>>>"+Semaforo.countBear);
                                 }else{
                                     //ll mayor es negativo Bull
                                     Semaforo.countBull++;
                                     System.out.println(">>>>>countBull>>>>>"+Semaforo.countBull);
                                 }
                             }
                          }else{
                              ticP = contract.m_symbol;
                              positionP = position;
                              strikeP = contract.m_strike;
                          }
                      }
                  
                
                
   
      
    
                System.out.println("TWSClientInterface:updatePortfolio: "+accountName +" " + contract.m_secType +" " + contract.m_symbol +" " +  position + " " + marketPrice + " " + marketValue + " " + averageCost + " " + unrealizedPNL + " " + realizedPNL);
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
               
                
                
            }
       
            @Override
            public void position(String account, Contract contract, int pos, double avgCost) {
                

                semaforo.LogFile.logFile("#### INVOCANDO position: " + contract.m_symbol + " y posicion: " + pos);
                double avg= (avgCost * pos)+0.2;
                ResultSet resGruposOld = DDBB.GruposTickersTicker(contract.m_symbol);
                try {
                    if(resGruposOld.next()) {
                        
                        DDBB.updateGroupTickerInvertido(contract.m_symbol, (int) avg);

                    }else{
                        DDBB.insertGroupsTickers("", contract.m_symbol, (int) avg);
                    }
                } catch (SQLException ex) {
                           Logger.getLogger(Groups.class.getName()).log(Level.SEVERE, null, ex);
                }
            
//                System.out.println("ESTE ES EL POS TRAIDO POR TWS: ACCION: " + contract.m_symbol + " y posicion: " + pos);
                                
                String msg = " ---- Position begin ----\n"
               + "account = " + account + "\n"
                + "conid = " + contract.m_conId + "\n"
                + "symbol = " + contract.m_symbol + "\n"
                + "secType = " + contract.m_secType + "\n"
                + "expiry = " + contract.m_expiry + "\n"
                + "strike = " + contract.m_strike + "\n"
                + "right = " + contract.m_right + "\n"
                + "multiplier = " + contract.m_multiplier + "\n"
                + "exchange = " + contract.m_exchange + "\n"
                + "primaryExch = " + contract.m_primaryExch + "\n"
                + "currency = " + contract.m_currency + "\n"
                + "localSymbol = " + contract.m_localSymbol + "\n"
                + "tradingClass = " + contract.m_tradingClass + "\n"
                + "position = " + Util.IntMaxString(pos) + "\n"
                + "averageCost = " + Util.DoubleMaxString(avgCost) + "\n"
                + " ---- Position end ----\n";
                
                System.out.println(msg);
                
                
                if( pos > 0 ) {                
                    numPosiciones++;                    
                    if (contract.m_secType.compareTo("CFD") == 0 ) valorPosiciones.put(contract.m_symbol, pos);                    
                }
                
        
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void positionEnd() {
  
                semaforo.LogFile.logFile("#### INVOCANDO positionEnd");
                segundaPasada++;
//                    
               connection.reqAccountUpdates(false, "U1523016");

                numPosicionesTotal = numPosiciones;
                if(segundaPasada>1 && !graficopaint){
                    Semaforo.cfd = false;
                    Semaforo.bull = false;
                    Semaforo.bear = false;
                   
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
                    Semaforo.SemaforoGrafico(chart);
                    
                    graficopaint=true;
                    Semaforo.countBear=0.0;
                    Semaforo.countBull=0.0;
                    Semaforo.countCfd=0.0;
                    System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::"+segundaPasada+Semaforo.countBear+Semaforo.countBull+Semaforo.countCfd);
                }else if(segundaPasada>=2){
                    Semaforo.cfd = false;
                    Semaforo.bull = false;
                    Semaforo.bear = false;
                    
                   
                    Semaforo.editGrafico();
                    Semaforo.countBear=0.0;
                    Semaforo.countBull=0.0;
                    Semaforo.countCfd=0.0;
//                    Semaforo.countBear=Semaforo.countBear+5;
                }

                
                 
                 
                System.out.println("+++++"+posiciones);
                if(semaforo.Semaforo.edit == 0){
                    
                    Semaforo.finalcountBear = Semaforo.countBear;
                    Semaforo.finalcountBull = Semaforo.countBull;
                    Semaforo.finalcountCfd = Semaforo.countCfd;
                    
                }
                semaforo.Semaforo.edit = 1;
                listener.guardaNumPos(numPosicionesTotal);
                listener.guardaPosiciones(valorPosiciones);
                valorPosiciones = new HashMap(); // %30
                numPosiciones =0;
                
                
                
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void accountSummary(int reqId, String account, String tag, String value, String currency) {
                System.out.println("account summary");
                //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void accountSummaryEnd(int reqId) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void verifyMessageAPI(String apiData) {
                System.out.println("verifyMessageAPI: " + apiData);
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void verifyCompleted(boolean isSuccessful, String errorText) {
                System.out.println("verifyCompleted: " + errorText);
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void displayGroupList(int reqId, String groups) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void displayGroupUpdated(int reqId, String contractInfo) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

       // System.out.println("Enter something here : ");

        new Thread() {

            @Override
            public void run() {
                // super.run(); //To change body of generated methods, choose Tools | Templates.

                connection = new EClientSocket(anyWrapper);
                //connection.eConnect(host, port, clientId);
                connection.eConnect(host, port, clientId);
                //esperar conexion
                while (true) {
                    System.out.println("Testing if there's connection");
                    boolean connected = connection.isConnected();
                    if (connected) {
                        System.out.println("Conectados ! :");
                        conexion_iniciada_wrapper();
                        break;
                    }

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(KobyTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }.start();

    }

    public static void conexion_iniciada_wrapper() {
        System.out.println("Conexion iniciada");
    }

    public static void conexion_aceptada_wrapper() throws SQLException, ParseException {
        //bucle conectados
        
        semaforo.LogFile.logFile("#### INVOCANDO conexion_aceptada_wrapper");
        System.out.println("conexion aceptada");
        connection.reqCurrentTime();
        initial_setup(total_weeks_ago, valores_init);
        listener.callback_conexion_aceptada();
    }

    public static void cancel_market_data_wrapper(String symbol) {
        int idSymbol = -1;
        Iterator iterator = valores_tabla.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            if (symbol.equalsIgnoreCase((String) entry.getValue())) {
                idSymbol = (int) entry.getKey();
            }
        }
        if (idSymbol > -1) {
            connection.cancelMktData(idSymbol);
        }
    }

    public static void simbolo_existe(String simbolo, ListenerComprobarSimbolo listener) {
        System.out.println("Requesting existe simbolo data for :" + simbolo);
        Contract contract = new Contract();
        //contract.m_comboLegs
        //contract.m_comboLegsDescrip
        //contract.m_conId = 11;
        contract.m_currency = "USD";
        contract.m_exchange = "SMART";
        //contract.m_expiry;
        contract.m_includeExpired = false;
        //contract.m_localSymbol = "KO";
        //contract.m_multiplier;
        //contract.m_primaryExch
        //contract.m_right;
        //contract.m_secId;
        //contract.m_secIdType;
        contract.m_secType = "STK"; // stock        
        contract.m_symbol = simbolo; // symbol
        //contract.m_tradingClass;
        String genericTicklist = "165";
        boolean snapshot = false;
        //List<TagValue> mktDataOptions = new List<TagValue>();
        List mktDataOptions = null;

        int size = existen_valores_tabla.size();
        int id_simbolo = size + 10000;
        existen_valores_tabla.put(id_simbolo, simbolo);
        connection.reqMktData(id_simbolo, contract, genericTicklist, snapshot, mktDataOptions);
        
        
        connection.reqAccountUpdates(false, "U1523016");
        

        if (listener != null) {
            listenerExiste = listener;
        }
    }

    public static void initial_setup(int weeks, String[] valores_init) throws SQLException, ParseException {

        for (int i = 0; i < valores_init.length; i++) {
            anyadir_simbolo(valores_init[i]);
        }

    }

    public static void anyadir_simbolo(String simbolo) throws SQLException, ParseException {
        int new_pos = valores_tabla.size();
        if (semaforo.Semaforo.isDebugMode) System.out.println("Insertando " + simbolo + " at" + new_pos);
        String[] old_valores = valores;
        valores = new String[valores.length + 1];
        int i = 0;
        for (i = 0; i < old_valores.length; i++) {
            valores[i] = old_valores[i];
        }
        valores[i] = simbolo;

        valores_tabla.put(new_pos, simbolo);
        historico_valores.put(simbolo, new HashMap<Integer, HashMap<String, Double>>());
        historico_valores_cumulativo.put(simbolo, new HashMap<Integer, HashMap<String, Double>>());

        pedir_historico(new_pos, simbolo, total_weeks_ago);
        pedir_precios(new_pos, simbolo);
        Curl.jsonDecode(Curl.preciosTicker16d(simbolo), simbolo);
    }

    public static void pedir_precios(int id_valor, String symbol) {
        System.out.println("Requesting top data for :" + id_valor);
        Contract contract = new Contract();
        //contract.m_comboLegs
        //contract.m_comboLegsDescrip
        //contract.m_conId = 11;
        contract.m_currency = "USD";
        contract.m_exchange = "SMART";
//        contract.m_expiry = "";
        contract.m_includeExpired = false;
        //contract.m_localSymbol = "KO";
        //contract.m_multiplier;
        //contract.m_primaryExch
        //contract.m_right;
        //contract.m_secId;
        //contract.m_secIdType;
        contract.m_secType = "STK"; // stock
        String simbolo = valores_tabla.get(id_valor);
        contract.m_symbol = simbolo; // symbol
        //contract.m_tradingClass;

        String genericTicklist = "165";
        boolean snapshot = false;
        //List<TagValue> mktDataOptions = new List<TagValue>();
        List mktDataOptions = null;
        
        //System.out.println("####################### EPAAAAAAAAAAAA  ################");
        connection.reqMktData(id_valor, contract, genericTicklist, snapshot, mktDataOptions);
        connection.reqAccountUpdates(false, "U1523016");
        //eClientSocket.reqPositions();

    }

    public static void pedir_historico(final int id_valor, String symbol, int weeks_ago) throws SQLException, ParseException {

        System.out.println("Requesting historic data for :" + id_valor);

        //0 weeks ago is this weekd
        Calendar cal = Calendar.getInstance();
        int current_week = cal.get(Calendar.WEEK_OF_YEAR);
        int current_day = cal.get(Calendar.DAY_OF_MONTH);
        int current_month = cal.get(Calendar.MONTH) + 1;
        int current_year = cal.get(Calendar.YEAR);
        
        System.out.println("Current day is " + current_day);
        System.out.println("Current week is " + current_week);

        String year = ""+current_year;
        if(current_month==1) current_month=12;
        String month = ""+current_month;
        String day = "01";

        for (int i = 0; i < weeks_ago; i++) {
            System.out.println("i = " + i + ". id_valor=" + id_valor + ". valorestabla[id_valor]" + valores_tabla.get(id_valor));
            historico_valores.get(valores_tabla.get(id_valor)).put(i, null); 
        }

        final Contract contract = new Contract();
        //contract.m_comboLegs
        //contract.m_comboLegsDescrip
        //contract.m_conId = 11;
        contract.m_currency = "USD";
        contract.m_exchange = "SMART";
        //contract.m_expiry;
        contract.m_includeExpired = false;
        //contract.m_localSymbol = "KO";
        //contract.m_multiplier;
//        contract.m_primaryExch = "NASDAQ";
//        contract.m_right = "CALL";
        //contract.m_secId;
        //contract.m_secIdType;
        contract.m_secType = "STK"; // stock
        String simbolo = valores_tabla.get(id_valor);
        contract.m_symbol = simbolo; // symbol
        //contract.m_tradingClass;
        //int id, Contract contract, String endDateTime, String durationStr, String barSizeSetting, String whatToShow, int useRTH, int formatDate, List<TagValue> chartOptions

        //TODO
        // -semana: buscar el fin de la semana. Calcular a partir de la semana // format yyyymmdd hh:mm:ss tmz, where the time zone is allowed (optionally) after a space at the end.
        Calendar cal_last_sunday = Calendar.getInstance();
//        cal_last_sunday.set(Calendar.WEEK_OF_YEAR, current_week);
        cal_last_sunday.set(Calendar.WEEK_OF_YEAR, current_week - 1);
        cal_last_sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//        cal_last_sunday.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        int last_sunday_day = cal_last_sunday.get(Calendar.DAY_OF_MONTH);
        int last_sunday_month = cal_last_sunday.get(Calendar.MONTH) + 1;
//        int last_sunday_month = cal_last_sunday.get(Calendar.MONTH);
        int last_sunday_year = cal_last_sunday.get(Calendar.YEAR);

        String last_sunday_year_str = String.valueOf(last_sunday_year);
        String last_sunday_month_str = String.valueOf(last_sunday_month);
        if (last_sunday_month < 10) {
            last_sunday_month_str = "0" + last_sunday_month_str;
        }
        String last_sunday_day_str = String.valueOf(last_sunday_day);
        if (last_sunday_day < 10) {
            last_sunday_day_str = "0" + last_sunday_day_str;
        }

        String semana = last_sunday_year_str + last_sunday_month_str + last_sunday_day_str + " 16:00:00 EST"; // de la primera semana

        String current_year_str = String.valueOf(current_year);
        String current_month_str = String.valueOf(current_month);
        if (current_month < 10) {
            current_month_str = "0" + current_month_str;
        }
        String current_day_str = String.valueOf(current_day);
        if (current_day < 10) {
            current_day_str = "0" + current_day_str;
        }
        
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        SimpleDateFormat d = new SimpleDateFormat("yyyyMMdd");
        Date date = c.getTime();
        Date dateW = c1.getTime();
        strSemana = d.format(date);
        strWeek = d.format(dateW);
        
    
                           
        Calendar calendar = Calendar.getInstance(Locale.forLanguageTag("en-US"));
        calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String hour = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":00";
        //semana = current_year_str + current_month_str + current_day_str + " "+hour+" America/New_York";//EST
        semana = strSemana + " 09:00:00 EST";
        final String semanaTimer =  semana;
        System.out.println("Requestion values for " + strSemana);
        final String duracion = 1 + " D"; // pedimos todas las semanas
        final String duracionTimer = 1 + " D"; // pedimos todas las semanas
        final String barSizeSetting = "1 day";
        final String whatToShow = "TRADES";
        final int useRTH = 1; //only data that falls within regular trading hours.
        final int formatDate = 1;//1 - dates applying to bars returned in the format:yyyymmdd{space}{space}hh:mm:dd       2 - dates are returned as a long integer specifying the number of seconds since 1/1/1970 GMT.
//        List chartOptions = null;
        final Vector chartOptions = new Vector();
        symbol_current= symbol;
        
        String fecha = current_year+"-"+current_month+"-"+current_day;
//        ResultSet exsiste = DDBB.lowHighFecha(fecha);
//        ResultSet exsisteTicker = DDBB.lowHighFechaName(fecha, symbol);d?
        count++;
        HistoricalDataReq historial = new HistoricalDataReq();
        peticionesHistorical[count].id = id_valor;
        peticionesHistorical[count].contract = contract;
        peticionesHistorical[count].endDateTime = semana;
        peticionesHistorical[count].durationStr = duracion;
        peticionesHistorical[count].barSizeSetting = barSizeSetting;
        peticionesHistorical[count].whatToShow = whatToShow;
        peticionesHistorical[count].useRTH = useRTH;
        peticionesHistorical[count].formatDate = formatDate;
        peticionesHistorical[count].chartOptions = chartOptions;
        peticionesHistorical[count].ticker = symbol;
        
       // peticionesHistorical[count] = historial;
        
        
        
        System.out.println("--------------------------------------------------------------------------------------"+historial.id+" "+symbol);
        
        
//       connection.reqHistoricalData(id_valor, contract, semanaTimer, duracion, barSizeSetting, whatToShow, useRTH, formatDate, chartOptions);
               
        
        
//        try {
//
//            Thread t = new Thread(new Runnable() {
//
//                @Override
//                public void run() { 
//                    int num = 1000;
//                        ResultSet dat = DDBB.TickersAll();
//                    try {
//                        Thread.sleep((int)(Math.random()*((16000*num)-1000+1)+1000));
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(KobyTest.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                        
//                    try {
//                        if(dat.next())num=dat.getInt("id");
//                    } catch (SQLException ex) {
//                        Logger.getLogger(KobyTest.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                       Timer timer = new Timer ((int)(Math.random()*((16000*num)-1000+1)+1000), new ActionListener () { 
//                        public void actionPerformed(ActionEvent e) 
//                        { 
//                            connection.reqHistoricalData(id_valor, contract, semanaTimer, duracionTimer, barSizeSetting, whatToShow, useRTH, formatDate, chartOptions);
//                         } 
//                        }); 
//                        timer.start();
//                   
//                }
//            });
//            t.start();
//
//        } catch (Exception ex) {
//            Logger.getLogger(Semaforo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        id_ant = id_valor;
        
        
//        connection.reqHistoricalData(id_valor, contract, semana, "300 S", "1 secs", whatToShow, useRTH, formatDate, chartOptions);
//        if(!exsiste.next()){ System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//            DDBB.deleteLowHigh();
//            connection.reqHistoricalData(id_valor, contract, semana, duracion, barSizeSetting, whatToShow, useRTH, formatDate, chartOptions);
//        }else if(newTicker){
//            System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
//            connection.reqHistoricalData(id_valor, contract, semana, duracion, barSizeSetting, whatToShow, useRTH, formatDate, chartOptions);
//            newTicker = false;
//        }else if(!exsisteTicker.next()){
//            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//            connection.reqHistoricalData(id_valor, contract, semana, duracion, barSizeSetting, whatToShow, useRTH, formatDate, chartOptions);
//            newTicker = false;
//        }
        
    }
    
    

    private final static int BID = 1;
    private final static int LAST = 4;
    private final static int LOW_13_WEEKS = 15;
    private final static int HIGH_13_WEEKS = 16;
    private final static int LOW_26_WEEKS = 17;
    private final static int HIGH_26_WEEKS = 18;
    private final static int LOW_52_WEEKS = 19;
    private final static int HIGH_52_WEEKS = 20;
    private final static int CLOSE =  9;
    
    private static boolean isMarketOpen = false;

    public static void wrapper_valor(int id_simbolo, int tipo, double precio) {
        if (semaforo.Semaforo.isDebugMode) System.out.println("wrapper_valor: " + id_simbolo);
        if (id_simbolo >= 10000) {
            // used for testing existence
            System.out.println("wrapper_valor + 10000");
            String simbolo_existe = (String) existen_valores_tabla.get(id_simbolo);
            if (listenerExiste != null) {
                listenerExiste.callback_simbolo_existe(simbolo_existe, true);
            }
            return;
        }

        String simbolo = (String) valores_tabla.get(id_simbolo);
        if (precio == -1.0) {
            // return;
        }

        String valor = (String) valores_tabla.get(id_simbolo);
        switch (tipo) {
//            case BID:
//                if (precio > 0) {
//                    isMarketOpen = true;
//                    listener.callback_valor(simbolo, tipo, precio);
//                }
//                break;
            case LAST:
                if (!isMarketOpen) {
                    listener.callback_valor(simbolo, tipo, precio);
                }
                break;
                
                
                
            case CLOSE:
                
                //if (!isMarketOpen) {
                    listener.callback_valor(simbolo, tipo, precio);
                //}
                break;                
                
            case LOW_13_WEEKS:
//                addLowHigh(id_simbolo, 13, "low", (double) Math.round(precio * 100) / 100);
                addLowHigh(id_simbolo, 13, "low", precio);
                listener.callback_historico_cumulativo(valor, historico_valores_cumulativo.get(valor));
                break;
            case HIGH_13_WEEKS:
//                addLowHigh(id_simbolo, 13, "high", (double) Math.round(precio * 100) / 100);
                addLowHigh(id_simbolo, 13, "high", precio);
                listener.callback_historico_cumulativo(valor, historico_valores_cumulativo.get(valor));
                break;
            case LOW_26_WEEKS:
//                addLowHigh(id_simbolo, 26, "low", (double) Math.round(precio * 100) / 100);
                addLowHigh(id_simbolo, 26, "low", precio);
                listener.callback_historico_cumulativo(valor, historico_valores_cumulativo.get(valor));
                break;
            case HIGH_26_WEEKS:
//                addLowHigh(id_simbolo, 26, "high", (double) Math.round(precio * 100) / 100);
                addLowHigh(id_simbolo, 26, "high", precio);
                listener.callback_historico_cumulativo(valor, historico_valores_cumulativo.get(valor));
                break;
            case LOW_52_WEEKS:
//                addLowHigh(id_simbolo, 52, "low", (double) Math.round(precio * 100) / 100);
                addLowHigh(id_simbolo, 52, "low", precio);
                listener.callback_historico_cumulativo(valor, historico_valores_cumulativo.get(valor));
                break;
            case HIGH_52_WEEKS:
//                addLowHigh(id_simbolo, 52, "high", (double) Math.round(precio * 100) / 100);
                addLowHigh(id_simbolo, 52, "high", precio);
                listener.callback_historico_cumulativo(valor, historico_valores_cumulativo.get(valor));
                break;
        } 
    }

    public static void addLowHigh(int idSymbol, int indexWeek, String type, double price) {
        String valor = (String) valores_tabla.get(idSymbol);
        if (historico_valores_cumulativo == null && historico_valores_cumulativo.get(valor) == null) {
            return;
        }
        if (historico_valores_cumulativo.get(valor).get(indexWeek) == null) {
            HashMap<String, Double> new_values = new HashMap<String, Double>();
            new_values.put(type, price);
            historico_valores_cumulativo.get(valor).put(indexWeek, new_values);
            return;
        }
        historico_valores_cumulativo.get(valor).get(indexWeek).put(type, price);
    }

    public static void enviar_datos_historicos(int id_valor) {
        String valor = (String) valores_tabla.get(id_valor);

        historico_valores.get(valor); // <int semana,<string,valor>>

        Iterator it = historico_valores.get(valor).entrySet().iterator();
        double highest_so_far = 0;
        double lowest_so_far = Double.POSITIVE_INFINITY;

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //HashMap.Entry pair = (HashMap.Entry) it.next();
            int weeks_ago = (int) pair.getKey();
            if (weeks_ago == total_weeks_ago + 1) {
                continue;
            }
            System.out.println(weeks_ago + " Weeks ago for " + valor);
            HashMap<String, Double> aux = (HashMap<String, Double>) historico_valores.get(valor).get(weeks_ago);
            if (aux == null) {
                continue;
            }
            Double high = aux.get("high");
            Double low = aux.get("low");
            if (high > highest_so_far) {
                highest_so_far = high;
            }
            if (low < lowest_so_far && low != 0) {
                lowest_so_far = low;
            }
            HashMap<String, Double> new_values = new HashMap<String, Double>();
            new_values.put("high", highest_so_far);
            new_values.put("low", lowest_so_far);
            historico_valores_cumulativo.get(valor).put(weeks_ago, new_values);
            System.out.println("Calling callback_historico(" + valor + "," + weeks_ago + "," + high + "," + low + ")");
            //callback_historico( valor,  weeks_ago,  high,  low);
        }
        System.out.println("Calling callback_historico cumulativo ----> " + valor + ". id_valor:" + id_valor);
        listener.callback_historico_cumulativo(valor, historico_valores_cumulativo.get(valor));

        //callback_historico(historico_valores.get(valor));
        //aqui tenemos todos los valores para el ticker indicado, pero los tenemos para todos?        
        enviar_todos_los_datos_historicos_cumulativos();

    }

    public static void enviar_todos_los_datos_historicos_cumulativos() {
        boolean completed = true;
        for (int k = 0; k < valores.length; k++) {
            //  TEST IF WE HAVE ALREADY FOR EVERYTHING   
            System.out.println("TEST IF WE HAVE IT ALREADY FOR EVERYTHING: for " + valores[k] + " - - (" + k + ")");

            // claro, per a total weeks si que esta!
            //int total_weeks = historico_valores_cumulativo.get(valores[k]).size();
            for (int j = 0; j <= total_weeks_ago; j++) {
                System.out.println("TEST IF WE HAVE ALREADY FOR EVERYTHING: for " + valores[k] + "(" + k + ") WEEK " + j);
                HashMap<String, Double> week_j = historico_valores_cumulativo.get(valores[k]).get(j);
                if (week_j == null || week_j.get("low") == null || week_j.get("high") == null) {
                    System.out.println("HEEEEEERe");
                    completed = false;
                    break;
                }
            }
            if (!completed) {
                break;
            }
        }
        if (completed) {
            System.out.println("COMPLETED! Enviando historico_valores_cumulativo");
            listener.callback_historico_cumulativo(historico_valores_cumulativo);
            System.out.println("AND DONE");
        }
    }

    public static void wrapper_snapshot_ended(int id_simbolo) {
        if (id_simbolo >= 10000) {
            // used for testing existence
            String simbolo_existe = (String) existen_valores_tabla.get(id_simbolo);
            if (listenerExiste != null) {
                listenerExiste.callback_simbolo_existe(simbolo_existe, true);
            }
            return;
        }
    }

    public static void wrapper_error(int id, int errorCode, String errorMsg) {
        String simbolo = "";
        if (id >= 10000) {
            simbolo = (String) existen_valores_tabla.get(id);
        } else {
            simbolo = (String) valores_tabla.get(id);
        }

        if (errorMsg.equals("Already connected.") == true) {
            conexion_iniciada_wrapper();
        } else if (errorCode == 200) {
            System.out.println("Symbol not found " + simbolo);
            //No security definition has been found for the request                    
            listener.callback_error(errorCode, errorMsg, simbolo);
            if (listenerExiste != null) {
                listenerExiste.callback_simbolo_existe(simbolo, false);
            }
        } else {
            System.out.println("Error not indentified:" + id + ". Errorcode: " + errorCode + ". errorMsg: " + errorMsg);
            //callback_error(errorCode, errorMsg, String simbolo);
        }
    }

    public static void wrapper_historico(int id_valor, String date, double high, double low, double open, double close) {
        System.out.println("Wrapper historico");

        if (date.indexOf("finished") != -1) {
            // we are finished
            // we should send the data now

            System.out.println("We are FINISHED. Let's send the data");
            enviar_datos_historicos(id_valor);

            return;
        }
        // date = 20141201 
        // 1. a que weeks_ago corresponde el ¿date?
        int weeks_ago = 1;
        System.out.println("Fecha => " + date);
        int y = Integer.parseInt(date.substring(0, 4));
        int m = Integer.parseInt(date.substring(4, 6));
        int d = Integer.parseInt(date.substring(6, 8));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, d);
        cal.set(Calendar.MONTH, m - 1);
        cal.set(Calendar.YEAR, y);
        System.out.println("The day returned: " + cal.getTime());
        int that_week = cal.get(Calendar.WEEK_OF_YEAR);

        Calendar cal_current = Calendar.getInstance();
        int current_week = cal_current.get(Calendar.WEEK_OF_YEAR);

        System.out.println("YEAR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + cal_current.get(Calendar.YEAR));
        System.out.println("THAT WEEK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + that_week);

        if (1 == that_week && cal_current.get(Calendar.YEAR) != y) {

            y = cal_current.get(Calendar.YEAR);
        }

        //if((current_week - that_week) < 0 )
        weeks_ago = current_week + 52 * (cal_current.get(Calendar.YEAR) - y) - that_week;
        //  else
        //      weeks_ago = current_week - that_week;
        System.out.println("Weeks ago => " + weeks_ago + "(" + current_week + "-" + that_week + ")");

        /*  if (weeks_ago < 0) {
         weeks_ago = current_week - (weeks_ago);
         weeks_ago--;
         }*/
        // historico_valores          HashMap<String,HashMap<Integer, HashMap<String,Double>>>
        System.out.println("is_valor -> " + id_valor);
        String valor = (String) valores_tabla.get(id_valor);
        System.out.println("simbolo -> " + valor);

        HashMap<String, Double> aux;// = historico_valores.get(valor).get(weeks_ago);
        //Test if exist. Override si se superan.

        if (historico_valores.get(valor).get(weeks_ago) == null) {
            System.out.println("1.1");
            aux = new HashMap<String, Double>();
            System.out.println("2");
            aux.put("high", high);
            aux.put("low", low);
            aux.put("values_collected", 1.);
            historico_valores.get(valor).put(weeks_ago, aux);
            historico_valores_cumulativo.get(valor).put(weeks_ago, null);
        } else {
            aux = historico_valores.get(valor).get(weeks_ago);
            // compare
            System.out.println("3");
            if (high != -1.0 && high > aux.get("high")) {
                aux.put("high", high);
            }
            if (low != -1.0 && low < aux.get("low")) {
                aux.put("low", low);
            }
            aux.put("values_collected", aux.get("values_collected") + 1);
            System.out.println("Carrying " + aux.get("values_collected"));
        }

        int i = (int) Math.floor(aux.get("values_collected"));
        System.out.println("Values collected so far:" + i);
       // devuelve todos los dias aunque no haya abierto??

        // Si queremos enviarlo cuando tenemos los 5 dias sin espera a todo.
        // Pero existe un problema: dias festivos: la semana no es de 5 dias
        /*
         if(i>=5){ //5 dias    
           
         // esperamos tambien a tener todos los dias?? total_weeks_ago?
         System.out.println("THERE YOU GO - "+valor +" FOR WEEKs ago "+weeks_ago);
         System.out.println("High: "+aux.get("high"));
         System.out.println("Low: "+aux.get("low"));
         System.out.println("Values collected: "+aux.get("values_collected"));
         // llamamos a callbak_historico solo si ya hemos preguntado por todos los dias        
         //callback_historico( valor,  weeks_ago,  high,  low, open,  close);
         }
         */
    }

    public interface ListenerComprobarSimbolo {

        public abstract void callback_simbolo_existe(String simbolo, boolean existe);/*ESTA*/

    }

    public interface ListenerConnectionConsole {
        
        public abstract void callback_valor(String simbolo, int tipo, double precio); /*ESTA*/


        //  public abstract void callback_simbolo_existe(String simbolo, boolean existe);/*ESTA*/
        public abstract void callback_historico(String simbolo, int weeks_ago, double high, double low, double open, double close);

        public abstract void callback_error(int Code, String error, String simbolo);

        public abstract void callback_historico_cumulativo(HashMap<String, HashMap<Integer, HashMap<String, Double>>> historico_valores_cumulativo);/*ESTA*/


        public abstract void callback_historico_cumulativo(String Valor, HashMap<Integer, HashMap<String, Double>> historico_valores_cumulativo);/*ESTA*/


        public void callback_conexion_aceptada();
        
        public void guardaNumPos(int numPos);

        public void guardaPorcentCapital(Double porcentCapital);
        
        public void guardaPosiciones(Map posiciones);
    }

}
