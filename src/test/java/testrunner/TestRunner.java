package testrunner;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("stepdefinitions")
@ConfigurationParameter(
        key = Constants.FEATURES_PROPERTY_NAME,
        value = "src/test/resources/features"
)
@ConfigurationParameter(
        key = Constants.GLUE_PROPERTY_NAME,
        value = "stepdefinitions"
)
@ConfigurationParameter(
        key = Constants.EXECUTION_DRY_RUN_PROPERTY_NAME,
        value = "false"
)
@ConfigurationParameter(
        key = Constants.PLUGIN_PROPERTY_NAME,
        value = "pretty, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
public class TestRunner {
    static {
        // Permite CLI execução cucumber.filter.tags
        String cliTags = System.getProperty("cucumber.filter.tags");
        if (cliTags != null && !cliTags.trim().isEmpty()) {
            System.setProperty(Constants.FILTER_TAGS_PROPERTY_NAME, cliTags);
        } else {
            // Tag padrão se nenhuma é fornecida
            System.setProperty(Constants.FILTER_TAGS_PROPERTY_NAME, "@Checkout");
        }
    }
}
