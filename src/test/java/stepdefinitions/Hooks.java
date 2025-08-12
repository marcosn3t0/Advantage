package stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import db.DatabaseHelper;
import fixture.TestContext;
import io.qameta.allure.Allure;
import okhttp3.Response;
import org.junit.jupiter.api.TestInstance;
import pages.HomePage;
import serializers.CartResponse;
import serializers.ProductInCart;
import serializers.User;
import utils.*;
import com.microsoft.playwright.*;
import io.cucumber.java.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Hooks {

    // Instância única do Playwright para toda a execução
    protected static Playwright playwright;

    // Página atual utilizada nos testes
    public Page page;

    // Objetos auxiliares de configuração e API
    BrowserConfig browserConfig;
    ApiClient apiClient;
    ObjectMapper mapper = new ObjectMapper();
    User user;

    /**
     * Executa uma única vez antes de todos os cenários.
     * Cria tabelas necessárias no banco e inicializa o Playwright.
     */
    @BeforeAll
    public static void setupAll() {
        try {
            DatabaseHelper.createTableIfNotExists();
            playwright = Playwright.create();
            LogHelper.info("Playwright Criado");
            Allure.addAttachment("Setup", "Playwright iniciado corretamente");
        } catch (Exception e) {
            Allure.addAttachment("Setup Error", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Executa antes de cada cenário.
     * Inicializa o navegador e configura objetos de apoio.
     */
    @Before
    public void setup(Scenario scenario) {
        browserConfig = new BrowserConfig();
        apiClient = new ApiClient();
        this.page = this.browserConfig.initBrowser(playwright);

        String startMsg = String.format("Iniciando Cenário: %s", scenario.getName());
        LogHelper.info(startMsg);
        Allure.addAttachment("Iniciando Cenário", startMsg);
    }

    /**
     * Executa antes de cenários com a tag @Auth.
     * Realiza login e armazena informações do usuário autenticado no contexto do teste.
     */
    @Before("@Auth")
    public void setupAuth() {
        File loginFile = new File("src/main/java/data/login.json");

        try {
            HomePage homePage = new HomePage(BrowserConfig.getPage());
            homePage.homePage();

            // Carrega usuários do arquivo JSON de login
            Map<String, Object> jsonMap = mapper.readValue(loginFile, new TypeReference<Map<String, Object>>() {});
            List<User> users = mapper.convertValue(jsonMap.get(Config.getEnv()), new TypeReference<List<User>>() {});
            TestContext.setUserData("user_login", Util.getRandomElement(users));

            // Realiza login e obtém a sessão
            String sessionId = homePage.login(
                    TestContext.getUserData("user_login").getUsername(),
                    TestContext.getUserData("user_login").getPassword(),
                    TestContext.getUserData("user_login").getIdUser()
            );

            // Armazena o ID da sessão no contexto
            TestContext.setDataBrowserContext("sessionid", sessionId);

            // Anexa informações do usuário ao relatório
            Allure.addAttachment("Usuário Autenticado", mapper.writeValueAsString(TestContext.getUserData("user_login")));
        } catch (Exception e) {
            Allure.addAttachment("Erro de Autenticação", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Executa após cada cenário.
     * Registra resultado no banco, limpa carrinho do usuário e captura evidências.
     */
    @After
    public void tearDown(Scenario scenario) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("authorization", TestContext.getUserData("user_login").getAuthorization());

        String scenarioName = scenario.getName();
        String status = scenario.isFailed() ? "FAILED" : "PASSED";
        String error = scenario.isFailed() ? "Verifique os logs ou relatórios para mais informações" : null;

        try {
            // Salva resultado do cenário no banco de dados
            DatabaseHelper.saveTestResult(scenarioName, status, error);

            // Obtém conteúdo do carrinho de compras
            Response cartResponses = apiClient.sendRequest(
                    "GET",
                    String.format("%s/order/api/v1/carts/%s", Config.getBaseUrl(), TestContext.getUserData("user_login").getIdUser()),
                    null,
                    headers
            );

            String cartJson = cartResponses.body().string();
            Allure.addAttachment("Resposta do Carrinho de Compras", "application/json", cartJson);

            CartResponse cart = mapper.readValue(cartJson, CartResponse.class);

            // Remove produtos restantes do carrinho
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

            // Captura screenshot em caso de falha
            if (scenario.isFailed() && this.page != null) {
                byte[] screenshot = this.page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                Allure.addAttachment("Screenshot da Falha", "image/png", new ByteArrayInputStream(screenshot), "png");
            }

            // Fecha a página
            if (page != null) {
                this.page.close();
            }

        } catch (Exception e) {
            Allure.addAttachment("Erro no TearDown", e.getMessage());
            LogHelper.error("Error: " + e.getMessage());
            e.printStackTrace();
        }

        Allure.addAttachment("Status do Cenário", String.format("Cenário: %s | Status: %s", scenarioName, status));
        LogHelper.info(String.format("Cenário: %s executado!!!", scenarioName));
    }

    /**
     * Executa uma única vez após todos os cenários.
     * Encerra contexto do navegador e finaliza o Playwright.
     */
    @AfterAll
    public static void tearDownAll() {
        try {
            BrowserConfig.getContext().close();
            if (playwright != null) {
                playwright.close();
            }
            Allure.addAttachment("Execução Finalizada", "Fim da execução");
        } catch (Exception e) {
            Allure.addAttachment("Erro no final da execução", e.getMessage());
            e.printStackTrace();
        }
    }
}
