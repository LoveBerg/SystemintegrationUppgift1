package Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class UI {
    ServerConnection server = new ServerConnection();
    Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args){
        System.out.println("WELCOME 2 SERVERHALL MONITOR SYSTEM");
        UI ui = new UI();
    }
    public UI(){
        mainMenu();

    }
    public void mainMenu(){
        while(true){
            System.out.println("choose a service:");
            System.out.println("1. Access latest report \n "
                + "2. Access daily report \n "
                + "3. Configure electrical pricing \n"
                + "4. Configure target temperautre");
            int userIn = scanner.nextInt();
            if(userIn == 1){ latestReportMenu(); }
            if(userIn == 2){ dailyReportMenu(); }
            if(userIn == 3){ configurePrice(); }
            if(userIn == 4){ configureTemperature(); }
        
        }
    }
    
    public void latestReportMenu(){
        int showSubmenu = 1;
        StatusReport latestReport = server.getLatestReport();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dateString = dateFormat.format(latestReport.getTimestamp());
       
        while(showSubmenu == 1){
            System.out.println("choose a service:");
            System.out.println("1. Read latest temperature \n "
                + "2. Read latest electrical consumption \n "
                + "3. Read full latest report \n"
                + "4. Exit to main menu");
            int userIn = scanner.nextInt();
            if(userIn == 1){ 
                System.out.print("\033[H\033[2J");
                System.out.println(String.format("LATEST TEMPERATURE READING: %d", latestReport.getTemperature()));
             }
            if(userIn ==2){
                System.out.print("\033[H\033[2J");
                System.out.println(String.format("LATEST CONSUMPTION READING : %d", latestReport.getConsumption()));
         }
            if(userIn ==3){
                System.out.print("\033[H\033[2J");
                System.out.println(String.format("SERVERHALL DESCRIPTION: %s", latestReport.getServerhall()));
                System.out.println(String.format("TIMESTAMP: %s", dateString));
                System.out.println(String.format("CURRENT ELECTRICAL PRICE: %f", latestReport.getPrice()));
                System.out.println(String.format("LATEST TEMPERATURE READING: %d", latestReport.getTemperature()));
                System.out.println(String.format("LATEST CONSUMPTION READING : %d", latestReport.getConsumption()));
        }
            if(userIn ==4){ showSubmenu = 0; }
        
        }
    }
    public void dailyReportMenu(){
        int showSubmenu = 1;
        List<StatusReport> dailyReport = server.getDailyReport();
        while(showSubmenu == 1){
          System.out.println("choose a service: \n "
          + "1. Read daily temperature report \n "
          + "2. Read daily electrical consumption report \n"
          + "3. Exit to main menu");
          int userIn = scanner.nextInt();

          if(userIn == 1){
            for(StatusReport report : dailyReport){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String dateString = dateFormat.format(report.getTimestamp());
                System.out.println(String.format("%s  temp: %d", dateString, report.getTemperature()));
            }
            MinMaxAvgTemp tempdata = server.getAvgTemp();
            System.out.println(String.format("daily average: %f daily max: %f daily min: %f", tempdata.getAvgTemp(), tempdata.getMaxTemp(), tempdata.getMinTemp()));     
          }
          
          if(userIn == 2){
              for(StatusReport report : dailyReport){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String dateString = dateFormat.format(report.getTimestamp());
                System.out.println(String.format("%s  consumption: %d", dateString, report.getConsumption()));
              }
              DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
              StatusReport consumer = getMaxConsumption(dailyReport);
              System.out.println(String.format("Daily max: %d %s", consumer.getConsumption(), dateFormat.format(consumer.getTimestamp())));
              consumer = getMinConsumption(dailyReport);
              System.out.println(String.format("Daily min: %d %s", consumer.getConsumption(), dateFormat.format(consumer.getTimestamp())));
              System.out.println(String.format("Daily average: %d", getAvgConsumption(dailyReport)));
          }
          if(userIn ==3){ showSubmenu = 0; }
    }
    }
    
    public void configurePrice(){
        System.out.println("Enter datacenter id to configure the pricing for:");
        int targetdc = scanner.nextInt();
        System.out.println("Enter new pricing:");
        float newPrice = scanner.nextFloat();
        StatusReport newpricingdata = new StatusReport();
        newpricingdata.setPrice(newPrice);
        newpricingdata.setId(targetdc);
        server.sendNewPrice(newpricingdata);
        System.out.println(String.format("NEW PRICING: %f SET FOR DATACENTER: %d", newPrice, targetdc));

    }
    public void configureTemperature(){
        System.out.println("Enter datacenter id to configure the target temperature for:");
        int targetdc = scanner.nextInt();
        System.out.println("Enter new target temperature:");
        float targettemp = scanner.nextFloat();
        StatusReport newtempdata = new StatusReport();
        newtempdata.setPrice(targettemp);
        newtempdata.setId(targetdc);
        server.sendNewTemp(newtempdata);
        System.out.println(String.format("NEW TARGET TEMPERATURE: %f SET FOR DATACENTER: %d", targettemp, targetdc));
    }
    
    public StatusReport getMaxConsumption(List<StatusReport> dailyReport){
        StatusReport maxconsumer = new StatusReport();
        maxconsumer.setConsumption(0);
        for(StatusReport report : dailyReport){
            if (report.getConsumption() > maxconsumer.getConsumption()){
                maxconsumer = report;
            }
        }
        return maxconsumer;
    }
        public StatusReport getMinConsumption(List<StatusReport> dailyReport){
        StatusReport minconsumer = new StatusReport();
        minconsumer.setConsumption(9999999);
        for(StatusReport report : dailyReport){
            if (report.getConsumption() < minconsumer.getConsumption()){
                minconsumer = report;
            }
        }
        return minconsumer;
        
    }
        
        public int getAvgConsumption(List<StatusReport> dailyReport){
            int totalconsumption = 0;
            int count = 0;
            for(StatusReport report : dailyReport){
                totalconsumption = totalconsumption + report.getConsumption();
                count++;
            }
            return totalconsumption/count;
        }
  
}
