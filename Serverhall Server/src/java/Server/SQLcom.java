package Server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SQLcom {
     Properties p = new Properties();
     Connection con;

     public SQLcom() throws SQLException, FileNotFoundException, ClassNotFoundException, IOException{
       p.load(new FileInputStream("C:\\Users\\loveo\\Documents\\NetBeansProjects\\Serverhall Server\\src\\java\\Server\\settings.properties"));
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("user"), p.getProperty("password"));     
        //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/datacenters?serverTimezone=UTC&useSSL=false", "root", "password123");     

     }
    public StatusReport getLatestReport() throws SQLException{
        Statement stmt = con.createStatement();
        StatusReport latestReport = new StatusReport();

        ResultSet rs = stmt.executeQuery("SELECT description, temperature, electricityPrice_sek_kwh, electricityconsumption_kw, time, timestamp.id FROM datacenters "+
        "JOIN electricityprice ON electricityprice.datacenterId = datacenters.Id "+
        "JOIN temperature ON temperature.datacenterId = datacenters.Id "+
        "JOIN electricityconsumption ON electricityconsumption.datacenterId = datacenters.Id " +
        "JOIN timestamp ON timestamp.temperatureId = temperature.Id AND timestamp.electricityConsumptionId = electricityconsumption.Id " +
        "ORDER BY timestamp.id DESC LIMIT 1;");
        while(rs.next()){
            latestReport.setServerhall(rs.getString("description"));
            latestReport.setTemperature(rs.getInt("temperature"));
            latestReport.setPrice(rs.getFloat("electricityPrice_sek_kwh"));
            latestReport.setConsumption(rs.getInt("electricityconsumption_kw"));
            latestReport.setTimestamp(rs.getDate("time"));
            latestReport.setId(rs.getInt("id"));
        }
        return latestReport;
    }
    
    public List<StatusReport> getMonthlyReport() throws SQLException{
        Statement stmt = con.createStatement();
        StatusReport report = new StatusReport();
        List<StatusReport> reportList = new ArrayList<StatusReport>();

        ResultSet rs = stmt.executeQuery("SELECT description, temperature, electricityPrice_sek_kwh, electricityconsumption_kw, time, timestamp.id FROM datacenters\n" +
        "JOIN electricityprice ON electricityprice.datacenterId = datacenters.Id\n" +
        "JOIN temperature ON temperature.datacenterId = datacenters.Id\n" +
        "JOIN electricityconsumption ON electricityconsumption.datacenterId = datacenters.Id\n" +
        "JOIN timestamp ON timestamp.temperatureId = temperature.Id AND timestamp.electricityConsumptionId = electricityconsumption.Id\n" +
        "ORDER BY timestamp.id DESC LIMIT 24;");
        while(rs.next()){
            report = new StatusReport();
            report.setServerhall(rs.getString("description"));
            report.setTemperature(rs.getInt("temperature"));
            report.setPrice(rs.getFloat("electricityPrice_sek_kwh"));
            report.setConsumption(rs.getInt("electricityconsumption_kw"));
            Timestamp timestamp = rs.getTimestamp("time");
            report.setTimestamp(new java.util.Date(timestamp.getTime()));            
            report.setId(rs.getInt("id"));
            reportList.add(report);
        }
        return reportList;
    }

    public MinMaxAvgTemp getAvgTemp() throws SQLException{
        Statement stmt = con.createStatement();
        MinMaxAvgTemp avgTemp = new MinMaxAvgTemp();
        ResultSet rs = stmt.executeQuery("SELECT (avg(temperature)) as medel, (max(temperature)) as max, (min(temperature)) as min FROM temperature");
        while(rs.next()){
            avgTemp.setAvgTemp(rs.getFloat("medel"));
            avgTemp.setMaxTemp(rs.getFloat("max"));
            avgTemp.setMinTemp(rs.getFloat("min"));
        }
        return avgTemp;
    }
    
    public void setNewTemp(float newtemp, int targetdc) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("UPDATE `datacenters`.`datacenters` SET `targettemperature` = ? WHERE (`id` = ?);");
        stmt.setFloat(1, newtemp);
        stmt.setInt(2, targetdc);
        int a = stmt.executeUpdate(); 
    }
    
    public void setNewPrice(float newprice, int targetdc) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("UPDATE `datacenters`.`electricityprice` SET `electricityPrice_sek_kwh` = ? WHERE (`datacenterId` = ?);");
        stmt.setFloat(1, newprice);
        stmt.setInt(2, targetdc);
        int a = stmt.executeUpdate();
    }
    }

