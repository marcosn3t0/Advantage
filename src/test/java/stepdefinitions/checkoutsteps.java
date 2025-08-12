package stepdefinitions;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fixture.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import pages.Cart;
import pages.HomePage;
import pages.OrderPayment;
import pages.ProductDetails;
import serializers.CardInfo;
import serializers.Products;
import utils.BrowserConfig;
import utils.Config;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class checkoutsteps {

    public HomePage homePage = new HomePage(BrowserConfig.getPage());
    public ProductDetails productDetails = new ProductDetails(BrowserConfig.getPage());
    public Cart cart = new Cart(BrowserConfig.getPage());
    private final TestContext context = new TestContext();
    public OrderPayment orderPayment = new OrderPayment(BrowserConfig.getPage());
    ObjectMapper mapper = new ObjectMapper();

    @Dado("que estou na home page")
    public void que_estou_na_home_page() {
        homePage.homePage();
    }

    @Dado("que adiciono lista de produtos ao carrinho de compras")
    public void que_adiciono_lista_de_produtos_ao_carrinho_de_compras(List<Products> products) {

        this.context.setProducts(products);

        for(Products product:this.context.getProducts()){
            homePage.searchProduct(product.getProduto());
            homePage.selectProduct(product.getProduto());
            productDetails.selectColor(product.getCor());
            productDetails.selectQuantity(product.getQuantidade());
            productDetails.addCart();
        };

    }

    @Quando("navego até o carrinho de compras")
    public void navego_ate_o_carrinho_de_compras() {
        cart.cart();
    }

    @Quando("edito o produto adicionado pelo carrinho de compras")
    public void edito_o_produto_adicionado_pelo_carrinho_de_compras(List<Products> products) {

        this.context.setProducts(products);

        for(Products product:this.context.getProducts()){
            cart.editProduct(product.getProduto());
            productDetails.editColor(product.getCor());
            productDetails.selectQuantity(product.getQuantidade());
            productDetails.addCart();
            cart.cart();
        };

    }

    @Entao("verifico que a lista de produtos foram adicionadas corretamente")
    public void verifico_que_a_lista_de_produtos_foram_adicionadas_corretamente() {
        cart.verifyProductsOnCart(this.context.getProducts());
    };

    @Entao("verifico que o valor total da compra está correto")
    public void verifico_que_o_valor_total_da_compra_está_correto() {
        cart.verifySumProducts(this.context.getProducts());
    };

    @Quando("removo produtos adicionados")
    public void removo_produtos_adicionados() {
        cart.removeAllProducts(
                this.context.getProducts()
        );
    }

    @Entao("verfico a mensagem {string}")
    public void verfico_a_mensagem(String msg) {
        cart.checkEmptyCardMessage(msg);
    }

    @Entao("confirmo o checkout dos produtos")
    public void confirmo_o_checkout_dos_produtos() {

        cart.checkout();

    }

    @Entao("confirmo os detalhes de entrega")
    public void confirmo_os_detalhes_de_entrega() {
        orderPayment.checkAddressDetails(
                TestContext.getUserData("user_login").getNome(),
                TestContext.getUserData("user_login").getShipping()
        );

        orderPayment.nextButton();
    }

    @Entao("confirmo os detalhes de pagamento")
    public void confirmo_os_detalhes_de_pagamento() {

        try{
            File cardFile = new File("src/main/java/data/card.json");
            Map<String, Object> jsonMap = mapper.readValue(cardFile, new TypeReference<Map<String, Object>>() {});
            List<CardInfo> cardInfo = mapper.convertValue(jsonMap.get(Config.getEnv()), new TypeReference<List<CardInfo>>() {});
            Optional<CardInfo> cardInfoContext = Optional.ofNullable(cardInfo.stream().filter(card -> card.getUsername().equals(TestContext.getUserData("user_login").getUsername())).findFirst().orElse(null));

            context.setCardInfo(
                    cardInfoContext.get()
            );

            orderPayment.checkMasterCreditSection(
                    cardInfoContext.get()
            );

        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        };
    };

    @Entao("clico em pagar agora")
    public void clico_em_pagar_agora() {
        orderPayment.payNowBtn();
    }

    @Entao("verifico o resumo da compra")
    public void verifico_o_resumo_da_compra() {
        orderPayment.checkSummary(
                TestContext.getUserData("user_login").getNome(),
                TestContext.getUserData("user_login").getShipping(),
                context.getCardInfo(),
                context.getProducts()
        );
    }


}
