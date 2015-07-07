package org.sai.tools.cukestyle.rule;

import cucumber.api.java.en.*;
import org.sai.tools.cukestyle.model.*;
import org.sai.tools.cukestyle.model.gherkin.Element;
import org.sai.tools.cukestyle.model.gherkin.FeatureFileFragment;
import org.sai.tools.cukestyle.model.gherkin.Step;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sai on 02/07/2015.
 */
public class UnusedStepDefsRule extends DefaultRule {
    public String id() {
        return "UNUSED_STEP_DEFS";
    }

    public List<RuleResult> evaluate(final ChecksRequest request) {
        try {
            RuleConfig config = configRepository.getRuleConfig(this);

            List<Method> methods = request.getAllStepDefinitions().stream()
                    .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()))
                    .filter(method -> method.isAnnotationPresent(Given.class)
                            || method.isAnnotationPresent(When.class)
                            || method.isAnnotationPresent(Then.class)
                            || method.isAnnotationPresent(And.class)
                            || method.isAnnotationPresent(But.class))
                    .collect(Collectors.toList());

            List<RuleResult> ruleResults = methods.stream().flatMap(method -> unusedStepsFunction(method, config, request).stream()).collect(Collectors.toList());
            if (ruleResults.size() > config.getMaxThreshold()) {
                return ruleResults;
            }
            return Collections.emptyList();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private List<RuleResult> unusedStepsFunction(final Method method, final RuleConfig ruleConfig, final ChecksRequest request) {

        List<String> allGivenDefsFromFeatureFiles = stepsFromFeatureFiles(request, "Given");
        List<String> allWhenDefsFromFeatureFiles = stepsFromFeatureFiles(request, "When");
        List<String> allThenDefsFromFeatureFiles = stepsFromFeatureFiles(request, "Then");
        List<String> allAndDefsFromFeatureFiles = stepsFromFeatureFiles(request, "And");
        List<String> allButDefsFromFeatureFiles = stepsFromFeatureFiles(request, "But");

        // O(n^2) comparison.
        return Arrays.stream(method.getDeclaredAnnotations())
                .map(annotation -> {
                    String description = "";
                    List<String> defsFromFeatureFile = null;
                    if (annotation instanceof Given) {
                        description = ((Given) annotation).value();
                        defsFromFeatureFile = allGivenDefsFromFeatureFiles;
                    } else if (annotation instanceof Then) {
                        description = ((Then) annotation).value();
                        defsFromFeatureFile = allThenDefsFromFeatureFiles;
                    } else if (annotation instanceof But) {
                        description = ((But) annotation).value();
                        defsFromFeatureFile = allButDefsFromFeatureFiles;
                    } else if (annotation instanceof And) {
                        description = ((And) annotation).value();
                        defsFromFeatureFile = allAndDefsFromFeatureFiles;
                    } else {
                        description = ((When) annotation).value();
                        defsFromFeatureFile = allWhenDefsFromFeatureFiles;
                    }
                    final String stepFromJava = description;
                    long matched = defsFromFeatureFile.stream()
                            .filter(stepFromFeatureFile -> stepFromFeatureFile.matches(stepFromJava))
                            .count();

                    // invert the logic
                    if (matched > 0) {
                        return new RuleResult(id(), ResultType.SUCCESS, ruleConfig.getRuleSeverity(), Optional.empty());
                    }
                    return new RuleResult(id(), ResultType.ERROR, ruleConfig.getRuleSeverity(), Optional.of(method.getDeclaringClass().getName() + " :: " + method.getName()+"(...)"));
                })
                .filter(result -> result.getType() == ResultType.ERROR)
                .collect(Collectors.toList());

    }

    private List<String> stepsFromFeatureFiles(final ChecksRequest request, final String stepType) {
        List<String> allSteps = new ArrayList();
        List<String> nonConjunctiveKeywords = new ArrayList<>();
        for (Map.Entry<String, FeatureFileFragment[]> fragments : request.getFeatureFilesToJsonMapping().entrySet()) {
            for (FeatureFileFragment featureFileFragment : fragments.getValue()) {
                for (Element element : featureFileFragment.getElements()) {
                    boolean isGiven = false;
                    boolean isWhen = false;
                    boolean isThen = false;
                    boolean isAnd = false;
                    boolean isBut = false;

                    for (Step step : element.getSteps()) {

                        isAnd = step.getKeyword().trim().equals("And");
                        isBut = step.getKeyword().trim().equals("But");

                        isGiven = step.getKeyword().trim().equals("Given");
                        isWhen = step.getKeyword().trim().equals("When");
                        isThen = step.getKeyword().trim().equals("Then");
                        if (isGiven || isWhen || isThen) nonConjunctiveKeywords.add(step.getKeyword().trim());

                        if ((isAnd || isBut) && !nonConjunctiveKeywords.isEmpty() && nonConjunctiveKeywords.get(nonConjunctiveKeywords.size() - 1).equals(stepType)) {
                            allSteps.add(step.getName().trim());
                        }
                        if (stepType.equals(step.getKeyword().trim())) {
                            allSteps.add(step.getName().trim());
                        }
                    }
                }
            }
        }
        return allSteps;
        /*return request.getFeatureFilesToJsonMapping().entrySet().stream()
                .flatMap(entry -> {
                    List<String> stepDescription = Arrays.stream(entry.getValue())
                            .flatMap(featureFileFragment ->
                                    featureFileFragment.getElements().stream()
                                            .flatMap(element ->
                                                    element.getSteps().stream()
                                                            .filter((Step step) ->
                                                                            step.getKeyword().trim().equals(stepType)
                                                            )))
                            .map(step -> step.getName())
                            .collect(Collectors.toList());
                    return stepDescription.stream();
                }).collect(Collectors.toList());*/
    }
}
