package addressgenerator;

import java.util.ArrayList;
import java.util.List;

public class Address {

    private String addr1, city, state = "";

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public List<String> csvOut(){
        List<String> str = new ArrayList<String>();
        
        str.add(addr1);
        str.add(city);
        str.add(state);
        return str;
    }
    
}
