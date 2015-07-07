package org.sai.tools.cukestyle.model;

/**
 * Created by sai on 02/07/2015.
 */

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "ruleId",
        "ruleDescription",
        "ruleSeverity",
        "minThreshold",
        "maxThreshold"
})
public class RuleConfig {

    @JsonProperty("ruleId")
    private String ruleId;
    @JsonProperty("ruleDescription")
    private String ruleDescription;
    @JsonProperty("ruleSeverity")
    private RuleSeverityType ruleSeverity;
    @JsonProperty("minThreshold")
    private Integer minThreshold;
    @JsonProperty("maxThreshold")
    private Integer maxThreshold;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The ruleId
     */
    @JsonProperty("ruleId")
    public String getRuleId() {
        return ruleId;
    }

    /**
     * @param ruleId The ruleId
     */
    @JsonProperty("ruleId")
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * @return The ruleDescription
     */
    @JsonProperty("ruleDescription")
    public String getRuleDescription() {
        return ruleDescription;
    }

    /**
     * @param ruleDescription The ruleDescription
     */
    @JsonProperty("ruleDescription")
    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    /**
     * @return The ruleSeverity
     */
    @JsonProperty("ruleSeverity")
    public RuleSeverityType getRuleSeverity() {
        return ruleSeverity;
    }

    /**
     * @param ruleSeverity The ruleSeverity
     */
    @JsonProperty("ruleSeverity")
    public void setRuleSeverity(RuleSeverityType ruleSeverity) {
        this.ruleSeverity = ruleSeverity;
    }

    /**
     * @return The minThreshold
     */
    @JsonProperty("minThreshold")
    public Integer getMinThreshold() {
        return minThreshold;
    }

    /**
     * @param minThreshold The minThreshold
     */
    @JsonProperty("minThreshold")
    public void setMinThreshold(Integer minThreshold) {
        this.minThreshold = minThreshold;
    }

    /**
     * @return The maxThreshold
     */
    @JsonProperty("maxThreshold")
    public Integer getMaxThreshold() {
        return maxThreshold;
    }

    /**
     * @param maxThreshold The maxThreshold
     */
    @JsonProperty("maxThreshold")
    public void setMaxThreshold(Integer maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}