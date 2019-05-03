
package Server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Path("/reportservice")


public class ReportService {
    static SQLcom sql;

    
    @GET
    @Path("/latest")
    @Produces(MediaType.APPLICATION_XML)
    public StatusReport getLatestReport() throws SQLException, ClassNotFoundException, IOException{
        
        sql = new SQLcom();
        StatusReport latestreport = new StatusReport();
        
        latestreport = sql.getLatestReport();
        return latestreport;
       
    }
    
    @GET
    @Path("/monthly")
    @Produces(MediaType.APPLICATION_XML)
    public List<StatusReport> getMonthlyReport() throws SQLException, ClassNotFoundException, IOException{
        sql = new SQLcom();
        List<StatusReport> reportList = new ArrayList<StatusReport>();
        reportList = sql.getMonthlyReport();
        return reportList;
        
    }  
       
    @GET
    @Path("/avgtemp")
    @Produces(MediaType.APPLICATION_XML)
    public MinMaxAvgTemp getAvgTemp() throws SQLException, ClassNotFoundException, IOException{
        sql = new SQLcom();
        MinMaxAvgTemp avgTemp = new MinMaxAvgTemp();
        avgTemp = sql.getAvgTemp();
        return avgTemp;
        
    }  
    
    @POST
    @Path("/settemp")
    public void setTemperature(StatusReport fromclient) throws SQLException, ClassNotFoundException, IOException{
        sql = new SQLcom();
        sql.setNewTemp(fromclient.getPrice(), fromclient.getId());
        
    }
    
    @POST
    @Path("/setprice")
    public void setPrice(StatusReport fromclient) throws SQLException, ClassNotFoundException, IOException{
        sql = new SQLcom();
        sql.setNewPrice(fromclient.getPrice(), fromclient.getId());
        
    }

}
