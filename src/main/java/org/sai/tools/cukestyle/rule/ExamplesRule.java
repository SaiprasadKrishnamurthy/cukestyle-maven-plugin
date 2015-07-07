package org.sai.tools.cukestyle.rule;

import org.sai.tools.cukestyle.model.ChecksRequest;
import org.sai.tools.cukestyle.model.ResultType;
import org.sai.tools.cukestyle.model.RuleConfig;
import org.sai.tools.cukestyle.model.RuleResult;
import org.sai.tools.cukestyle.model.gherkin.Element;
import org.sai.tools.cukestyle.model.gherkin.Example;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by sai on 05/07/2015.
 */
public class ExamplesRule extends DefaultRule {

    @Override
    public String id() {
        return "EXAMPLES_LIMIT";
    }

    @Override
    public List<RuleResult> evaluate(final ChecksRequest request) {
        RuleConfig ruleConfig = configRepository.getRuleConfig(this);
        return request.getFeatureFilesToJsonMapping().entrySet().stream()
                .map(entry -> {
                    boolean error = Arrays.stream(entry.getValue())
                            .filter(featureFileFragment ->
                                    featureFileFragment.getElements().stream()
                                            .filter(element ->
                                                    element.getExamples().stream()
                                                            .filter(example ->
                                                                    example.getRows().size() < ruleConfig.getMinThreshold()
                                                                            || example.getRows().size() > ruleConfig.getMaxThreshold() - 1).count() > 0).count() > 0).count() > 0;
                    if (error) {
                        if (entry.getKey().contains(File.separator)) {
                            return new RuleResult(id(), ResultType.ERROR, ruleConfig.getRuleSeverity(), Optional.of(entry.getKey().substring(entry.getKey().lastIndexOf(File.separator)) + " (Max allowed: " + ruleConfig.getMaxThreshold() + ")"));
                        } else {
                            return new RuleResult(id(), ResultType.ERROR, ruleConfig.getRuleSeverity(), Optional.of(entry.getKey() + " (Max allowed: " + ruleConfig.getMaxThreshold() + ")"));
                        }
                    } else {
                        return new RuleResult(id(), ResultType.SUCCESS, ruleConfig.getRuleSeverity(), Optional.empty());
                    }

                })
                .filter(result -> result.getType() == ResultType.ERROR)
                .collect(toList());
    }
}
