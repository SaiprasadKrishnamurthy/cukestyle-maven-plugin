package org.sai.tools.cukestyle.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.JSONFormatter;
import gherkin.parser.Parser;
import gherkin.util.FixJava;
import org.sai.tools.cukestyle.model.ChecksRequest;
import org.sai.tools.cukestyle.model.gherkin.FeatureFileFragment;
import org.sai.tools.cukestyle.rule.UnusedStepDefsRule;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sai on 04/07/2015.
 */
public final class DataUtil {

    public static ChecksRequest getChecksRequest(final String baseStepsPackage, final String... featureFiles) throws Exception {
        ChecksRequest request = new ChecksRequest();
        String testBasePackage = baseStepsPackage;
        ImmutableSet<ClassPath.ClassInfo> ci = ClassPath.from(UnusedStepDefsRule.class.getClassLoader()).getAllClasses();
        List<Class<?>> tests = ci.stream()
                .filter(classInfo -> classInfo.getPackageName().contains(testBasePackage))
                .filter(classInfo -> Arrays.stream(classInfo.load().getMethods())
                        .filter(method -> method.isAnnotationPresent(Given.class)
                                || method.isAnnotationPresent(When.class)
                                || method.isAnnotationPresent(Then.class)).count() > 0)
                .map(classInfo -> classInfo.load())
                .collect(Collectors.toList());

        request.setAllStepDefinitions(tests);

        Arrays.stream(featureFiles).forEach(featureFile -> {
            try {
                addFeatureFile(request, featureFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return request;
    }

    private static void addFeatureFile(ChecksRequest request, String featureFileName) throws java.io.IOException {
        InputStreamReader in = null;
        if (new File(featureFileName).exists()) {
            in = new InputStreamReader(new FileInputStream(featureFileName), "UTF-8");
        } else {
            in = new InputStreamReader(DataUtil.class.getClassLoader().getResourceAsStream(featureFileName), "UTF-8");
        }
        String gherkin = FixJava.readReader(in);
        StringBuilder json = new StringBuilder();
        JSONFormatter formatter = new JSONFormatter(json);
        Parser parser = new Parser(formatter);
        parser.parse(gherkin, "", 0);
        formatter.done();
        formatter.close();

        ObjectMapper mapper = new ObjectMapper();
        request.getFeatureFilesToJsonMapping().put(featureFileName, mapper.readValue(json.toString(), FeatureFileFragment[].class));
    }
}
