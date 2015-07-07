package org.sai.tools.cukestyle.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sai.tools.cukestyle.model.AppConfig;
import org.sai.tools.cukestyle.model.Rule;
import org.sai.tools.cukestyle.model.RuleConfig;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sai on 02/07/2015.
 */
public final class ConfigRepository {

    private static List<RuleConfig> configs;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            configs = Arrays.asList(mapper.readValue(new File(System.getProperty("rules.file")), RuleConfig[].class));
        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public RuleConfig getRuleConfig(final Rule rule) {
        return configs.stream().filter(config -> config.getRuleId().equals(rule.id())).findFirst().orElseThrow(() -> new RuntimeException("Cannot find any matching rule for: "+rule.id()));
    }

    public AppConfig getAppConfig() {
        AppConfig config = new AppConfig();
        config.setTestbasePackage("org.sai");
        return config;
    }

}
