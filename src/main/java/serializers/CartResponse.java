package serializers;

import java.util.List;

public class CartResponse {
    private long userId;
    private List<ProductInCart> productsInCart;
    private String sessionId;

    // Getters and Setters
    public long getUserId() {
        return userId;
    }

    public void setDataContextId(long userId) {
        this.userId = userId;
    }

    public List<ProductInCart> getProductsInCart() {
        return productsInCart;
    }

    public void setProductsInCart(List<ProductInCart> productsInCart) {
        this.productsInCart = productsInCart;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
