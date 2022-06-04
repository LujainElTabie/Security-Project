import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Visit implements Serializable {

    String readings ;
    String reason ;

    public Visit(String readings, String reason) {
        this.readings = readings;
        this.reason = reason;
//        this.prescription = new ArrayList<Objects>();
    }

    @Override
    public String toString() {
        return "Visit{" +
                "readings='" + readings + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    ArrayList<Objects> prescription;
}
