package serializers;

public class Products {
    private Integer id;
    private String produto;
    private Integer quantidade;
    private String cor;
    private Double valor;
    private String codeColor;

    public Products() {}

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColorCode() {
        return codeColor;
    }

    public void setColorCode(String colorCode) {
        this.codeColor = colorCode;
    }
}
