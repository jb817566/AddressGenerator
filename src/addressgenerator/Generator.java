package addressgenerator;

import java.util.Vector;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Generator {

    private Vector<Address> list = null;
    private Connection conn = null;
    int num = 0;

    public Generator(int n, Vector<Address> v) {
        num = n;
        list = v;
    }

    @Override
    public String toString() {
        Address a = null;
        for (int i = 0; i < num; i++) {
            a = getAddress();
            if (a.getState().length() == 2) {
                list.add(a);
            }
        }

        return "";
    }

    public Address getAddress() {
        Document doc = null;
        conn = Jsoup.connect("http://www.realusaaddress.com/");
        try {
            conn.timeout(10000);
            conn.get();
            doc = conn.get();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Element el = doc.select("html body table tbody tr th table tbody tr th table tbody tr td p").get(2);
        String inner = el.toString();
        String[] arr = inner.split("(<br>)");
        String addr1 = arr[4];
        String city = arr[6].split(",")[0];
        String state = arr[6].trim().split(" ")[1].split("\\(")[0];

        Address p = new Address();

        p.setAddr1(addr1);
        p.setCity(city);
        p.setState(state);
        conn = null;
        return p;
    }

}
