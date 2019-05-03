/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StatusReport")
public class StatusReport {
    String serverhall;
    int temperature;
    int consumption;
    Float price;
    Date timestamp;
    int id;
    
  @XmlElement
    public String getServerhall() {
        return serverhall;
    }

    @XmlElement
    public int getTemperature() {
        return temperature;
    }

    @XmlElement
    public int getConsumption() {
        return consumption;
    }

    @XmlElement
    public Float getPrice() {
        return price;
    }

    @XmlElement
    public Date getTimestamp() {
        return timestamp;
    }
    @XmlElement
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setServerhall(String serverhall) {
        this.serverhall = serverhall;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}