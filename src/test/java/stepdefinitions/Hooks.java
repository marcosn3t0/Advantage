package stepdefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import db.DatabaseHelper;
import fixture.TestContext;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import okhttp3.Response;
import pages.HomePage;
import serializers.CartResponse;
import serializers.ProductInCart;
import serializers.User;
import utils.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hooks {

    // Instância única do Playwright para toda a execução
    protected static Playwright playwright;

    // Página atual utilizada nos testes
    public Page page;

    // Objetos auxiliares de configuração e API
    BrowserConfig browserConfig;
    ApiClient apiClient;
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Executa uma única vez antes de todos os cenários (Cucumber lifecycle).
     */
    @BeforeAll
    public static void setupAll() {
        try {
            DatabaseHelper.createTableIfNotExists();
            playwright = Playwright.create();
            LogHelper.info("Playwright Criado");
            Allure.addAttachment("Setup", "Playwright iniciado corretamente");
        } catch (Exception e) {
            Allure.addAttachment("Setup Error", e.toString());
            LogHelper.error("Erro no setupAll: " + e.getMessage());
            e.printStackTrace();
            // Não lançar para permitir que Allure escreva resultado
        }
    }

    /**
     * Executa antes de cada cenário.
     */
    @Before
    public void setup(Scenario scenario) {
        try {
            browserConfig = new BrowserConfig();
            apiClient = new ApiClient();
            this.page = this.browserConfig.initBrowser(playwright);

            String startMsg = String.format("Iniciando Cenário: %s", scenario.getName());
            LogHelper.info(startMsg);
            Allure.addAttachment("Iniciando Cenário", startMsg);
        } catch (Exception e) {
            Allure.addAttachment("Erro no Setup de Cenário", e.toString());
            LogHelper.error("Erro no setup: " + e.getMessage());
        }
    }

    /**
     * Executa antes de cenários com a tag @Auth.
     */
    @Before("@Auth")
    public void setupAuth() {
        File loginFile = new File("src/main/java/data/login.json");

        try {
            HomePage homePage = new HomePage(BrowserConfig.getPage());
            homePage.homePage();

            Map<String, Object> jsonMap = mapper.readValue(loginFile, new TypeReference<Map<String, Object>>() {});
            List<User> users = mapper.convertValue(jsonMap.get(Config.getEnv()), new TypeReference<List<User>>() {});
            TestContext.setUserData("user_login", Util.getRandomElement(users));

            String sessionId = homePage.login(
                    TestContext.getUserData("user_login").getUsername(),
                    TestContext.getUserData("user_login").getPassword(),
                    TestContext.getUserData("user_login").getIdUser()
            );

            TestContext.setDataBrowserContext("sessionid", sessionId);

            Allure.addAttachment("Usuário Autenticado", mapper.writeValueAsString(TestContext.getUserData("user_login")));
        } catch (Exception e) {
            Allure.addAttachment("Erro de Autenticação", e.toString());
            LogHelper.error("Erro na autenticação: " + e.getMessage());
        }
    }

    @After("@Auth")
    public void AuthtearDown(Scenario scenario) {
        Map<String, String> headers = new HashMap<>();
        try {
            headers.put("Content-Type", "application/json");
            headers.put("authorization", TestContext.getUserData("user_login").getAuthorization());

            Response cartResponses = apiClient.sendRequest(
                    "GET",
                    String.format("%s/order/api/v1/carts/%s", Config.getBaseUrl(), TestContext.getUserData("user_login").getIdUser()),
                    null,
                    headers
            );

            String cartJson = cartResponses.body().string();
            Allure.addAttachment("Resposta do Carrinho de Compras", "application/json", cartJson);

            CartResponse cart = mapper.readValue(cartJson, CartResponse.class);

            if (!cart.getProductsInCart().isEmpty()) {
                for (ProductInCart productInCart : cart.getProductsInCart()) {
                    Response removeProdResponse = apiClient.sendRequest(
                            "DELETE",
                            String.format("%s/order/api/v1/carts/%s/product/%s/color/%s",
                                    Config.getBaseUrl(),
                                    TestContext.getUserData("user_login").getIdUser(),
                                    productInCart.getProductId(),
                                    productInCart.getColor().getCode()
                            ),
                            null,
                            headers
                    );
                    Allure.addAttachment(
                            String.format("Produto Removido: %s", productInCart.getProductId()),
                            removeProdResponse.body().string()
                    );
                }
            }
        } catch (Exception e) {
            Allure.addAttachment("Erro no AuthTearDown", e.toString());
            LogHelper.error("Erro no AuthTearDown: " + e.getMessage());
        }
    }

    /**
     * Executa após cada cenário.
     */
    @After
    public void tearDown(Scenario scenario) {
        String scenarioName = scenario.getName();
        String status = scenario.isFailed() ? "FAILED" : "PASSED";
        String error = scenario.isFailed() ? "Verifique os logs ou relatórios para mais informações" : null;

        try {
            DatabaseHelper.saveTestResult(scenarioName, status, error);

            if (scenario.isFailed() && page != null) {
                byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                Allure.addAttachment("Screenshot da Falha", "image/png", new ByteArrayInputStream(screenshot), "png");
            }
        } catch (Exception e) {
            Allure.addAttachment("Erro no TearDown", e.toString());
            LogHelper.error("Erro no TearDown: " + e.getMessage());
        } finally {
            try {
                if (page != null) {
                    page.close();
                }
            } catch (Exception closeEx) {
                Allure.addAttachment("Erro ao Fechar Página", closeEx.toString());
            }
            Allure.addAttachment("Status do Cenário", String.format("Cenário: %s | Status: %s", scenarioName, status));
            LogHelper.info(String.format("Cenário: %s executado!!!", scenarioName));
        }
    }

    /**
     * Executa uma única vez após todos os cenários.
     */
    @AfterAll
    public static void tearDownAll() {
        try {
            if (BrowserConfig.getContext() != null) {
                BrowserConfig.getContext().close();
            }
            if (playwright != null) {
                playwright.close();
            }
            Allure.addAttachment("Execução Finalizada", "Fim da execução");
        } catch (Exception e) {
            Allure.addAttachment("Erro no final da execução", e.toString());
            LogHelper.error("Erro no AfterAll: " + e.getMessage());
        }
    }
}
