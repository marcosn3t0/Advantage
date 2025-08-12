package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;

import java.io.File;

public class BrowserConfig {

    public static Page page;
    public Browser browser;
    public static BrowserContext context;

    public static ThreadLocal<Page> threadLocalDriver = new ThreadLocal<>();
    public static ThreadLocal<BrowserContext> threadLocalContext = new ThreadLocal<>();

    public Page initBrowser(Playwright playwright) {

        try{
//            ObjectMapper mapper = new ObjectMapper();
//            File file = new File(Config.getAuth());

            switch (Config.getBrowser()) {
                case "firefox":
                    this.browser = playwright.firefox().launch(
                            new BrowserType.LaunchOptions().setHeadless(Config.getHeadless())
                    );
                    break;
                case "chrome":
                    this.browser = playwright.chromium().launch(
                            new BrowserType.LaunchOptions().setHeadless(Config.getHeadless())
                    );
                    break;
                case "webkit":
                    this.browser = playwright.webkit().launch(
                            new BrowserType.LaunchOptions().setHeadless(Config.getHeadless())
                    );
                    break;
                default:
                    throw new RuntimeException("this.browserNot Found" + Config.getBrowser());
            };

            context = this.browser.newContext(
                    new Browser.NewContextOptions().setBaseURL(Config.getBaseUrl())
            );
            context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(false));
            context.setDefaultTimeout(Config.getTimeout());

// Lógica de carregamento de cookies comentada — o site alvo não possui persistência real de cookies.
// Por isso, o armazenamento e reutilização de cookies não estão implementados neste teste.
//
// O código abaixo, se ativado, faria o seguinte:
// 1. Verifica se o arquivo local de cookies existe.
// 2. Carrega os cookies do arquivo (formato JSON).
// 3. Converte para uma lista de cookies do Playwright.
// 4. Define uma nova data de expiração (1 ano a partir de agora) para cada cookie.
// 5. Adiciona os cookies ao contexto atual do navegador.
//
// Isso ainda não está implementado.
//            if(file.exists()) {
//                List<Cookie> cookies = new ArrayList<>();
//                Map<String, Object> jsonMap = mapper.readValue(file, new TypeReference<>() {});
//
//                List<Cookies> jsonCookies = mapper.convertValue(jsonMap.get("cookies"), new TypeReference<List<Cookies>>() {});
//                long now = System.currentTimeMillis() / 1000;
//
//                long oneYear = 365L * 24 * 60 * 60;
//
//                double expiresAt = now + oneYear;
//                for(Cookies cookieSon:  jsonCookies) {
//                    System.out.println("Add New Cookies: " + cookieSon.name + "Domain: " + cookieSon.domain);
//                    Cookie cookie = new Cookie(
//                            cookieSon.name,
//                            cookieSon.value
//                    ).setDomain(cookieSon.domain)
//                            .setExpires(expiresAt)
//                            .setHttpOnly(cookieSon.httpOnly)
//                            .setSecure(cookieSon.secure)
//                            .setSameSite(SameSiteAttribute.valueOf(cookieSon.sameSite.toUpperCase()))
//                            .setPath(cookieSon.path);
//                    cookies.add(cookie);
//                }
//
//                context.addCookies(cookies);
//
//            };

            page = context.newPage();
            threadLocalDriver.set(page);
            threadLocalContext.set(context);

            return page;

        }catch (Exception e){
            System.err.println("Erro ao inicializar BrowserConfig");
            e.printStackTrace();
            return null;
        }
    };

    public static synchronized Page getPage() {
        return threadLocalDriver.get();
    }

    public static synchronized BrowserContext getContext() {
        return threadLocalContext.get();
    }
}
