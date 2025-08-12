package serializers;

public class Cookies {

    public String name;
    public String value;
    public String domain;
    public String path;
    public double expires;  // Playwright uses `double` for expiry timestamp
    public boolean httpOnly;
    public boolean secure;
    public String sameSite;

}
