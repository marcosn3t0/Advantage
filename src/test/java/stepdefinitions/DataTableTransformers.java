package stepdefinitions;

import io.cucumber.java.DataTableType;
import serializers.Products;

import java.util.Map;

public class DataTableTransformers {

    @DataTableType
    public Products defineProduct(Map<String, String> entry) {
        Products product = new Products();
        product.setId(Integer.valueOf(entry.get("id")));
        product.setProduto(entry.get("produto"));
        product.setQuantidade(Integer.parseInt(entry.get("quantidade")));
        product.setCor(entry.get("cor"));
        product.setColorCode(entry.get("codeColor"));
        product.setValor(Double.parseDouble(entry.get("valor")));
        return product;
    }
}
