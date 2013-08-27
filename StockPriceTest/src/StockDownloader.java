import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.*;
import java.net.*;
import java.io.FileWriter;

public class StockDownloader extends JPanel implements ActionListener {
    
    private static int lineCount = 0;
    public static String[] stocks = new String[0]; 
    public static int month, day, year;
    public String fullTextArea = null; 
    public static final String directory = System.getProperty("user.home")+"\\"+"Desktop";
    
    static JTextArea textArea;
    static JButton getPrices;
    static JTextField MonthTextField;
    static JTextField DayTextField;
    static JTextField YearTextField;
    
    private static void getHistoricalPrice(String[] prices){
        
        //Loop through stock array to get prices for each stock
        for (int i=0; i<stocks.length; i++){
            
        try
        {
            
            URL yahoofinance = new URL("http://ichart.yahoo.com/table.csv?s="+stocks[i]+"&a="+month+"&b="+day+"&c="+year+"&d="+month+"&e="+day+"&f="+year);
            URLConnection yc = yahoofinance.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            //While loop to add stock prices to stock array
            while ((inputLine = in.readLine()) != null)
            {
                prices[i] = inputLine;                
            }
            in.close();            
            
        } catch (IOException ex)
        {
            System.out.println("Oops bad things happened");
        }
        }
        //Call generateCsvFile method and pass the directory to save the file and the prices array
        generateCsvFile(directory, prices); 
    }
    
    private static void generateCsvFile(String sFileName, String[] prices)
    {
     try
     {
         FileWriter writer = new FileWriter(sFileName);
         
         //Writing Headers
         writer.append("Ticker");
         writer.append(",");
         writer.append("Date");
         writer.append(",");
         writer.append("Open");
         writer.append(",");
         writer.append("High");
         writer.append(",");
         writer.append("Low");
         writer.append(",");
         writer.append("Close");
         writer.append(",");
         writer.append("Volume");
         writer.append(",");
         writer.append("Adj Close");
         writer.append("\n");
         
         //Loop through the stock and price and add to .csv file
         for (int i=0; i<stocks.length;i++){
         
         writer.append(stocks[i]);
         writer.append(",");
         writer.append(prices[i]);
         writer.append("\n");
         
         }
  
         writer.flush();
         writer.close();
     }
     catch(IOException e)
     {
          e.printStackTrace();
     }     
    }
     
    public StockDownloader(){
        
        //Create button, text area and text fields
        textArea = new JTextArea(7,20);
        MonthTextField = new JTextField(3);
        DayTextField = new JTextField(3);
        YearTextField = new JTextField(5);
        getPrices = new JButton("Get Prices");
        
        //Create window for user interface
        JFrame window = new JFrame("Stock Price Downloader");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300,270);
        
        //Create action listener for button
        getPrices.addActionListener(this);
        
        //Create user interface
        JPanel pane = new JPanel();
        JLabel stockLabel = new JLabel("Enter Stocks");
        JLabel month = new JLabel("Month");
        JLabel day = new JLabel("Day");
        JLabel year = new JLabel("Year");
        JScrollPane scroll = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        //Add Labels and fields to user interface
        pane.add(month);
        pane.add(MonthTextField);
        pane.add(day);
        pane.add(DayTextField);
        pane.add(year);
        pane.add(YearTextField);
        pane.add(stockLabel);
        pane.add(scroll);
        pane.add(getPrices);
        
        window.add(pane);
        window.setVisible(true);       
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == getPrices){       
            
            //Get month, date and year that is entered
            month = (Integer.parseInt(MonthTextField.getText()))-1;
            day = Integer.parseInt(DayTextField.getText());
            year = Integer.parseInt(YearTextField.getText());
            
            //Get the list of stocks entered
            fullTextArea = textArea.getText();
            stocks = fullTextArea.split("\n");
            lineCount = textArea.getLineCount();
            
            //Create array the same size as number of stocks
            String[] prices = new String[lineCount];
            
            //Call getHistoricalPrice method and pass prices array
            getHistoricalPrice(prices);
            
        }       
    }
    
    private static void setLookAndFeel(){
        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception exc){
            
        }
    }
    
    public static void main(String[] args){
        
        setLookAndFeel();
        StockDownloader frame = new StockDownloader();         
        
    }
}
