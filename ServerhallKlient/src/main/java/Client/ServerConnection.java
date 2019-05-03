package Client;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class ServerConnection {
    private static ClientConfig config;
    private static Client client;
    private static WebResource service;

    public ServerConnection() {
        this.config = new DefaultClientConfig();
        this.client = Client.create(config);
        this.service = client.resource(UriBuilder.fromUri("http://localhost:36913/Serverhall_Server/rest/reportservice").build());
    }
    
    public StatusReport getLatestReport(){
        StatusReport latestReport = service.path("/latest")
                .accept(MediaType.APPLICATION_XML)
                .get(StatusReport.class);
        return latestReport;
    }
    
    public List<StatusReport> getDailyReport(){
        StatusReport[] monthlyArray = service.path("/monthly")
            .accept(MediaType.APPLICATION_XML)
            .get(StatusReport[].class);
        List<StatusReport> monthlyList = Arrays.asList(monthlyArray);
        return monthlyList;
    }
    
    public MinMaxAvgTemp getAvgTemp(){
        MinMaxAvgTemp avgTemp = service.path("avgtemp")
                .accept(MediaType.APPLICATION_XML)
                .get(MinMaxAvgTemp.class);
        return avgTemp;
    }
    
    public void sendNewTemp(StatusReport newtempdata){
        ClientResponse response = service.path("settemp")
                .accept(MediaType.APPLICATION_XML).post(ClientResponse.class, newtempdata);
    }
    
    public void sendNewPrice(StatusReport newpricedata){
        ClientResponse response = service.path("setprice")
                .accept(MediaType.APPLICATION_XML).post(ClientResponse.class, newpricedata);
    }
}
