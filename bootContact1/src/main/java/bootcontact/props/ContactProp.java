package bootcontact.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "contact")
public class ContactProp {

    private int rowsOfPage;
    private String defaultTheme;
    private String defaultSyubetu;
    public int getRowsOfPage() {
        return rowsOfPage;
    }
    public void setRowsOfPage(int rowsOfPage) {
        this.rowsOfPage = rowsOfPage;
    }
    public String getDefaultTheme() {
        return defaultTheme;
    }
    public void setDefaultTheme(String defaultTheme) {
        this.defaultTheme = defaultTheme;
    }
    public String getDefaultSyubetu() {
        return defaultSyubetu;
    }
    public void setDefaultSyubetu(String defaultSyubetu) {
        this.defaultSyubetu = defaultSyubetu;
    }
}
