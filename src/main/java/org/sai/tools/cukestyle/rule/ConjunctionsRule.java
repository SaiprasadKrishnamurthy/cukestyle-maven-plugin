package org.sai.tools.cukestyle.rule;

import org.sai.tools.cukestyle.model.ChecksRequest;
import org.sai.tools.cukestyle.model.ResultType;
import org.sai.tools.cukestyle.model.RuleConfig;
import org.sai.tools.cukestyle.model.RuleResult;
import org.sai.tools.cukestyle.model.gherkin.Element;
import org.sai.tools.cukestyle.model.gherkin.Step;
import sun.security.pkcs11.wrapper.Functions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Created by sai on 04/07/2015.
 */
public class ConjunctionsRule extends DefaultRule {

    @Override
    public String id() {
        return "CONJUNCTIONS";
    }

    @Override
    public List<RuleResult> evaluate(final ChecksRequest request) {
        RuleConfig ruleConfig = configRepository.getRuleConfig(this);

        Predicate<Step> andStepsOnly = step -> step.getKeyword().trim().equals("And");

        List<List<RuleResult>> results = request.getFeatureFilesToJsonMapping().entrySet().stream()
                .map(entry -> {
                    List<Element> allElements = Arrays.stream(entry.getValue())
                            .flatMap(featureFileFragment -> featureFileFragment.getElements().stream())
                            .collect(Collectors.toList());

                    Map<String, List<Long>> conjunctiveStepsCountPerElement = allElements.stream()
                            .collect(
                                    groupingBy((Element e) -> e.getName()
                                            , mapping((Element e) -> e.getSteps().stream().filter(andStepsOnly).count(), toList())));


                    Map<String, List<Long>> stepsInError = conjunctiveStepsCountPerElement.entrySet().stream()
                            .filter(conjunctiveEntry -> conjunctiveEntry.getValue().stream()
                                    .filter(size -> size < ruleConfig.getMinThreshold() || size > ruleConfig.getMaxThreshold()).count() > 0)
                            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

                    return stepsInError.entrySet().stream().map(error ->
                                    new RuleResult(id(), ResultType.ERROR, ruleConfig.getRuleSeverity(), Optional.of("Conjunctions check failed | File: " + entry.getKey() + " | " + " Element: " + error.getKey()))
                    ).collect(Collectors.toList());
                }).collect(Collectors.toList());
        return results.stream().flatMap(result -> result.stream()).collect(Collectors.toList());
    }
}
