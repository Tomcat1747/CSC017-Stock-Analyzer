// Import the java.io Library
import java.io.*;
// Import the java.math Library
import java.math.*;
// Import the java.util Library
import java.util.*;
// Import the yahoofiance Library
import yahoofinance.*;

public class myStock
{
    // This is the Nested Class Provided for You to Store the Information
    // Associated with a Stock Symbol
    private static class stockInfo
    {
        // Variable for the Name of the Stock
        private String name;
        // Variable for the Value of the Stock
        private BigDecimal price;
        // Sets the Name of a Stock and it's Value
        public stockInfo(String nameIn, BigDecimal priceIn)
        {
            // Set the Name of the Stock to be nameIn
            name = nameIn;
            // Set the Value of the Stock to be priceIn
            price = priceIn;
        }
        // Returns the Information About the Stock as a String
        public String toString()
        {
            // Create an Empty String
            StringBuilder stockInfoString = new StringBuilder("");
            // Append the Stock Info to the String
            stockInfoString.append(name + " " + price.toString());
            // Return the String 
            return stockInfoString.toString();
        }
    }
    // Comparator for TreeSet of the Stocks
    class myComparator implements Comparator<Map.Entry<String, stockInfo>>
    {
        // Compares the Values of stockA and stockB
        public int compare(Map.Entry<String, stockInfo> stockA, Map.Entry<String, stockInfo> stockB)
        {
            // Find the Value of stockA
            BigDecimal priceA = stockA.getValue().price;
            // Find the Value of stockB
            BigDecimal priceB = stockB.getValue().price;
            // Return the Result of Comparision Between the Two Stocks
            return priceB.compareTo(priceA);
        }
    }
    // Initialize a HashMap for the Stocks
    HashMap<String, stockInfo> stockDataHash;
    // Initialize a TreeSet for the Stocks
    TreeSet<Map.Entry<String, stockInfo>> stockDataTree;
    // Initializes the Databases to Hold the Stock Information
    public myStock()
    {
        // Create a HashMap for the Stock
        stockDataHash = new HashMap<>();
        // Create a TreeSet for the Stock
        stockDataTree = new TreeSet<Map.Entry<String, stockInfo>>(new myComparator());
    }
    // Update or Insert Data into the Databases
    public void insertOrUpdate(String symbol, stockInfo newData)
    {
        // Check to See if the the HashMap has the Stock Already in it
        if(stockDataHash.containsKey(symbol))
        {
            // Set a Variable to Hold the Old Stock Information
            stockInfo oldData = stockDataHash.get(symbol);
            // Replace the Data with the New Data in the HashMap
            stockDataHash.replace(symbol, oldData, newData);
            // Create New Data for the TreeSet
            AbstractMap.SimpleEntry<String, stockInfo> data = new AbstractMap.SimpleEntry<>(symbol, newData);
            // Add the Data to the TreeSet
            stockDataTree.add(data);
        }
        // Otherwise
        else
        {
            // Add the New Information into the HashMap
            stockDataHash.put(symbol, newData);
            // Create New Data for the TreeSet
            AbstractMap.SimpleEntry<String, stockInfo> data = new AbstractMap.SimpleEntry<>(symbol, newData);
            // Add the Data to the TreeSet
            stockDataTree.add(data);
        }
    }
    // Returns the Information About the Given Stock
    public stockInfo get(String symbol)
    {
        // Returns the Stock's Infomation
        return stockDataHash.get(symbol);
    }
    // List the Top k Stocks
    public List<Map.Entry<String, stockInfo>> top(int k)
    {
        // Create a List to Hold the Top k Stocks
        List<Map.Entry<String, stockInfo>> topKStock = new ArrayList<>();
        // Create an Iterator
        Iterator<Map.Entry<String, stockInfo>> value = stockDataTree.iterator();
        // Set the Iterator Value to 0
        int itr = 0;
        // Go Through the Values Until You Reach k
        while(value.hasNext() && itr < k)
        {
            // Add the Value to the Top k Stocks
            topKStock.add(value.next());
            // Increase the itr Value
            itr++;
        }
        // Return the Top k Stocks
        return topKStock;
    }
    public static void main(String[] args) throws IOException {   	
    	
    	
        // test the database creation based on the input file
    	
        myStock techStock = new myStock();
    	
        BufferedReader reader;
		
        try {
			
            reader = new BufferedReader(new FileReader("./US-Tech-Symbols.txt"));
			
            String line = reader.readLine();
			
            while (line != null) {
				
                String[] var = line.split(":");
				
				
                // YahooFinance API is used and make sure the lib file is included in the project build path
				
                Stock stock = YahooFinance.get(var[0]);
				
				
                // test the insertOrUpdate operation
				
                techStock.insertOrUpdate(var[0], new stockInfo(var[1], stock.getQuote().getPrice())); 
				
                line = reader.readLine();
			
            }
			
            reader.close();
		
        } 
        catch (IOException e) {
			
            e.printStackTrace();
		
        }
		
        int i = 1;
		
        System.out.println("===========Top 10 stocks===========");
		
		
        // test the top operation
		
        for (Map.Entry<String, stockInfo> element : techStock.top(10)) {
		    
            System.out.println("[" + i + "]" +element.getKey() + " " + element.getValue());
		    
            i++;
		
        }
		
		
        // test the get operation
		
        System.out.println("===========Stock info retrieval===========");
    	
        System.out.println("VMW" + " " + techStock.get("VMW"));
    	
        System.out.println("CHL" + " " + techStock.get("CHL"));
    
    }


}