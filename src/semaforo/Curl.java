/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semaforo;
 
import java.io.*;
import java.net.*;
import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import kobytest.KobyTest;
import static kobytest.KobyTest.strSemana;
import static kobytest.KobyTest.strWeek;
import org.apache.commons.codec.binary.Base64;
 
public class Curl 
{
 
  public static String generateRandomChars(String candidateChars, int length) {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
        sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
    }

    return sb.toString();
}
    
  private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {

    SecretKey secretKey = null;
    byte[] keyBytes = keyString.getBytes();
    secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(secretKey);
    byte[] text = baseString.getBytes();
    return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
    
}
  
    public static String excutePost(String targetURL, String urlParameters){
    URL url;
    HttpURLConnection connection = null;  
    try {
     
      
      url = new URL(targetURL);
      connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
      connection.setRequestProperty("Content-Language", "es-ES");  
      
      //merchant_ref=Astonishing-Sale&transaction_type=authorize&method=token&amount=200&currency_code=USD&token_type=FDToken&type=visa&value=2537446225198291&cardholder_name=JohnSmith&exp_date=1030		
      connection.setUseCaches (false);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      //Send request
      DataOutputStream wr = new DataOutputStream (
                  connection.getOutputStream ());
      wr.writeBytes (urlParameters);
      wr.flush ();
      wr.close ();

      //Get Response	
      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      String line;
      StringBuffer response = new StringBuffer(); 
      while((line = rd.readLine()) != null) {
        response.append(line);
        response.append('\n');
      }
      rd.close();
      return response.toString();

    } catch (Exception e) {

      e.printStackTrace();
      e.getMessage();
      return null;

    } finally {
      if(connection != null) {
        connection.disconnect(); 
      }
    }
  }
    
  public static int initComponentSwing(){
      String json=excutePost("http://pkclick.com/pkapi/statusapp?alias=app1", "");
      if(json.indexOf("\"success\":true")>0){
          return 1;
      }else{
          return 0;
      }
  }
  public static void jsonDecode(String curl, String ticker){

      if(curl.indexOf("timestamp")>=0){
            
            char[] charArray = curl.toCharArray();
            DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            DecimalFormat df2 = new DecimalFormat("###.##");
            
            String fechas = "";
            boolean andar = true, copy = false;
            for(int i=curl.indexOf("timestamp");andar;i++){
                if(charArray[i-1]=='['){
                    copy = true;
                }
                if(copy){
                    fechas+=charArray[i];
                }
                if(charArray[i+1]==']'){
                    copy = false;
                    andar = false;
                }
            }
            String[] fechasArray = fechas.split(",");
            
            for(int i=0;i<=fechasArray.length-1;i++){
                fechasArray[i]+="000";
                long milliSeconds=Long.parseLong(fechasArray[i]);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(milliSeconds);
                fechasArray[i]=formatter.format(calendar.getTime());
                System.out.println(fechasArray[i]); 
            }
            
           
            String lows = "";
            andar = true;
            copy = false;
            for(int i=curl.indexOf("low");andar;i++){
                if(charArray[i-1]=='['){
                    copy = true;
                }
                if(copy){
                    lows+=charArray[i];
                }
                if(charArray[i+1]==']'){
                    copy = false;
                    andar = false;
                }
            }
            String[] lowArray = lows.split(",");
            for(int i=0;i<=lowArray.length-1;i++){              
                lowArray[i]=String.valueOf(df2.format(Double.parseDouble(lowArray[i])));
                //System.out.println(lows+lowArray[i]+""+i);
            }
            
            
  
            String highs = "";
            andar = true;
            copy = false;
            for(int i=curl.indexOf("high");andar;i++){
                if(charArray[i-1]=='['){
                    copy = true;
                }
                if(copy){
                    highs+=charArray[i];
                }
                if(charArray[i+1]==']'){
                    copy = false;
                    andar = false;
                }
            }
            String[] highArray =highs.split(",");
            for(int i=0;i<=highArray.length-1;i++){
                highArray[i]=String.valueOf(df2.format(Double.parseDouble(highArray[i])));
                //System.out.println(highs+":"+highArray[i]+""+i); 
            }
            if(fechasArray.length==1){
                for(int i=0;i<=fechasArray.length-1;i++){
                    historicalData(ticker,lowArray[i], highArray[i], fechasArray[i], true);
                }
            }else{
                for(int i=0;i<=fechasArray.length-1;i++){
                    historicalData(ticker,lowArray[i], highArray[i], fechasArray[i], false);
                }
            }
            
      }else{
                System.out.println("No se ha podido consultar historico de precios."); 
      }
  }
  
  
  public static void historicalData(String symbol, String low, String high, String date, boolean currentDay) {
      
                    Calendar c1 = Calendar.getInstance();
                    SimpleDateFormat d = new SimpleDateFormat("yyyyMMdd");
                    Date dateW = c1.getTime();
                    strWeek = d.format(dateW);
                    if(!currentDay){
                            if(!date.equals(strWeek)){
                                Calendar cal = Calendar.getInstance();
                                int current_day = cal.get(Calendar.DAY_OF_MONTH);
                                int current_month = cal.get(Calendar.MONTH) + 1;
                                int current_year = cal.get(Calendar.YEAR);
                                String fecha = current_year+"-"+current_month+"-"+current_day;               
                                DDBB.deleteLowHighFecha(fecha);
                                DDBB.insertLowHigh(symbol, fecha, ""+low, ""+high);

                            }else{
                                DDBB.updateHighHighTicker(symbol, high+"");
                                DDBB.updateLowLowTicker(symbol, low+"");
                                System.out.println(symbol+"-------------------------------------"+strWeek);
            //                                System.out.println("HISTORICAL DATA for order_id: " + reqId);
                                System.out.println("Historical Date: " + date);
            //                                System.out.println("Historical Open: " + open);
                                System.out.println("Historical high: " + high);
                                System.out.println("Historical low: " + low);
            //                                System.out.println("Historical close: " + close);
            //                                System.out.println("Historical volume: " + volume);
            //                                System.out.println("Historical count: " + count);
            //                                System.out.println("Historical WAP: " + WAP);
            //                                System.out.println("Historical hasGaps: " + hasGaps);
                                System.out.println("-------------------------------------");
                            }
                    }else{
                                DDBB.updateHighHighTicker(symbol, high+"");
                                DDBB.updateLowLowTicker(symbol, low+"");
                                System.out.println(symbol+"-------------------------------------"+strWeek);
            //                                System.out.println("HISTORICAL DATA for order_id: " + reqId);
                                System.out.println("Historical Date: " + date);
            //                                System.out.println("Historical Open: " + open);
                                System.out.println("Historical high: " + high);
                                System.out.println("Historical low: " + low);
            //                                System.out.println("Historical close: " + close);
            //                                System.out.println("Historical volume: " + volume);
            //                                System.out.println("Historical count: " + count);
            //                                System.out.println("Historical WAP: " + WAP);
            //                                System.out.println("Historical hasGaps: " + hasGaps);
                                System.out.println("-------------------------------------");
                    }
                        
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                //wrapper_historico(reqId, date, high, low, open, close);
            }
  
  
  public static String preciosTicker(String ticker){
      String json=excutePost("https://query2.finance.yahoo.com/v8/finance/chart/"+ticker+"?lang=en-US&region=US&interval=1d&range=1d&corsDomain=finance.yahoo.com", "");
      return json;
  }
  
  public static String preciosTicker16d(String ticker){
      String json=excutePost("https://query2.finance.yahoo.com/v8/finance/chart/"+ticker+"?lang=en-US&region=US&interval=1d&range=16d&corsDomain=finance.yahoo.com", "");
      return json;
  }
  
  
    
//  public static void main(String args[]) {
//      int i = initComponentSwing();
//      if (i > 0)
//      System.out.println("true");
//  } 

  
}