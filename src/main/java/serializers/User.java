package serializers;

public class User {
    private String username;
    private String password;
    private String nome;
    private Boolean valid;
    private Integer idUser;
    private String authorization;
    private ShippingDetails shipping;

    public String getNome() {
        return nome;
    };

    public void setNome(String nome) {
        this.nome = nome;
    };

    public String getUsername() {
        return username;
    };

    public Integer getIdUser() {
        return idUser;
    };

    public String getAuthorization() {
        return authorization;
    }

    public void setDataContextname(String username) {
        this.username = username;
    };

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getPassword() {
        return password;
    };

    public void setPassword(String password) {
        this.password = password;
    };

    public Boolean getValid() {
        return valid;
    };

    public void setValid(Boolean valid) {
        this.valid = valid;
    };

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public ShippingDetails getShipping() {
        return shipping;
    }

    public void setShipping(ShippingDetails shipping) {
        this.shipping = shipping;
    }

}
