package fixture;

import serializers.CardInfo;
import serializers.Products;
import serializers.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestContext {
    private List<Products> products = new ArrayList<>();
    private static final Map<String,User> userContext = new HashMap<>();
    private static final Map<String,String> dataBrowserContext = new HashMap<>();
    private CardInfo cardInfo;

    public CardInfo getCardInfo(){
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo){
        this.cardInfo = cardInfo;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public List<Products> getProducts() {
        return products;
    }

    public static void setUserData(String userType,User User) {
        userContext.put(userType,User);
    };

    public static void setDataBrowserContext(String userType,String Browser) {
        dataBrowserContext.put(userType,Browser);
    };

    public static String getDataBrowserContext(String userType) {
        return dataBrowserContext.get(userType);
    }

    public static User getUserData(String userType) {
        return userContext.get(userType);
    }
}
