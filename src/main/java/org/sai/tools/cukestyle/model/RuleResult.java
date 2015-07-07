package org.sai.tools.cukestyle.model;

import java.util.Optional;

/**
 * Created by sai on 02/07/2015.
 */
public final class RuleResult {

    private final String ruleId;
    private final ResultType type;
    private final RuleSeverityType ruleSeverityType;
    private final Optional<String> message;


    public RuleResult(final String ruleId, final ResultType type, final RuleSeverityType ruleSeverityType, final Optional<String> message) {
        this.ruleId = ruleId;
        this.type = type;
        this.ruleSeverityType = ruleSeverityType;
        this.message = message;
    }

    public String getRuleId() {
        return ruleId;
    }

    public ResultType getType() {
        return type;
    }

    public Optional<String> getMessage() {
        return message;
    }

    public RuleSeverityType getRuleSeverityType() {
        return ruleSeverityType;
    }

    @Override
    public String toString() {
        return "RuleResult{" +
                "ruleId='" + ruleId + '\'' +
                ", type=" + type +
                ", ruleSeverityType=" + ruleSeverityType +
                ", message=" + message +
                '}';
    }
}
