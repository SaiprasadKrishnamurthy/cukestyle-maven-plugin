package org.sai.tools.cukestyle.model;

import org.sai.tools.cukestyle.model.gherkin.FeatureFileFragment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sai on 03/07/2015.
 */
public class ChecksRequest {
    private List<Class<?>> allStepDefinitions;
    private Map<String, FeatureFileFragment[]> featureFilesToJsonMapping = new ConcurrentHashMap<>();

    public List<Class<?>> getAllStepDefinitions() {
        return allStepDefinitions;
    }

    public void setAllStepDefinitions(List<Class<?>> allStepDefinitions) {
        this.allStepDefinitions = allStepDefinitions;
    }

    public Map<String, FeatureFileFragment[]> getFeatureFilesToJsonMapping() {
        return featureFilesToJsonMapping;
    }

    public void setFeatureFilesToJsonMapping(Map<String, FeatureFileFragment[]> featureFilesToJsonMapping) {
        this.featureFilesToJsonMapping = featureFilesToJsonMapping;
    }
}
