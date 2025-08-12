package utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

public class Config {

    private static final String ENV = System.getenv("ENV");

    private static final Dotenv dotenv = Dotenv.configure().filename(String.format(".env.%s", ENV)).load();

    public static String getBaseUrl() {
        return dotenv.get("BASE_URL");
    }

    public static String getBrowser() {
        return dotenv.get("BROWSER");
    };

    public static String getUrlLite() {
        return dotenv.get("URL_SQL_LITE");
    }

    public static String getEnv(){
        return ENV;
    };

    public static String getAuth(){
        return  "src/main/java/auth/auth-user.json";
    };

    public static Integer getTimeout() {
        return Integer.parseInt(dotenv.get("TIMEOUT"));
    }

    public static Boolean getHeadless() {
        return Objects.equals(dotenv.get("HEADLESS"), "true");
    }
}
