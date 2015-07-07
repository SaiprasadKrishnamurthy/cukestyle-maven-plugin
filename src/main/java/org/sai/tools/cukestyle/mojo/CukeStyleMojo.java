package org.sai.tools.cukestyle.mojo;

import com.bethecoder.ascii_table.ASCIITable;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.reflections.Reflections;
import org.sai.tools.cukestyle.common.DataUtil;
import org.sai.tools.cukestyle.model.ChecksRequest;
import org.sai.tools.cukestyle.model.RuleResult;
import org.sai.tools.cukestyle.rule.DefaultRule;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by sai on 05/07/2015.
 *
 * @goal check
 * @phase prepare-package
 */
public class CukeStyleMojo extends AbstractMojo {

    /**
     * Location of the rules file.
     *
     * @parameter alias="rulesFilePath"
     * @required
     */
    private String rulesFilePath;

    /**
     * Failure threshold for high.
     *
     * @parameter alias="highThreshold"
     */
    private int highThreshold;

    /**
     * Failure threshold for high.
     *
     * @parameter alias="mediumThreshold"
     */
    private int mediumThreshold;

    /**
     * Base package of all step defs.
     *
     * @parameter alias="baseStepDefsPackage"
     * @required
     */
    private String baseStepDefsPackage;

    /**
     * Base directory of all feature files.
     *
     * @parameter alias="baseFeatureFilesDir"
     * @required
     */
    private String baseFeatureFilesDir;

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            getLog().info("Going to check the feature files against the rules: " + new File(rulesFilePath).getAbsolutePath());

            List<String> allFeatureFiles = FileUtils.listFiles(new File(baseFeatureFilesDir), new String[]{"feature"}, true).stream().map(file -> file.getAbsolutePath()).collect(toList());

            List<String> testClasspath = project.getTestClasspathElements();

            List classpathUrls = testClasspath.stream().map(element -> {
                try {
                    return Optional.of(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return Optional.empty();
                }
            })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toList());

            List<URL> urls = (List<URL>) classpathUrls.stream().map(element -> (URL) element).collect(toList());

            final PluginDescriptor descriptor = (PluginDescriptor) getPluginContext().get("pluginDescriptor");
            ClassRealm realm = descriptor.getClassRealm();
            urls.forEach(realm::addURL);

            System.setProperty("rules.file", rulesFilePath);
            ChecksRequest request = DataUtil.getChecksRequest(baseStepDefsPackage, allFeatureFiles.toArray(new String[allFeatureFiles.size()]));

            Set<Class<? extends DefaultRule>> subTypes = new Reflections("org.sai").getSubTypesOf(DefaultRule.class);

            List<RuleResult> ruleResults = subTypes.parallelStream().flatMap(clazz -> {
                try {
                    return clazz.newInstance().evaluate(request).stream();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("Checks failed!");
            }).sorted((r1, r2) -> r1.getType().compareTo(r2.getType()))
                    .collect(toList());
            List<String> lines = new ArrayList<>();
            lines.add("RULE, SEVERITY, ERROR_MESSAGE");
            lines.addAll(ruleResults.stream().map(res -> res.getRuleId() + ", " + res.getRuleSeverityType() + ", " + res.getMessage().get()).collect(toList()));


            printTableToConsole(ruleResults);
            if (!ruleResults.isEmpty()) {
                FileUtils.writeLines(new File("target/cuckestyle_results.csv"), lines);
                getLog().info("Cukestyle Reports generated successfully in " + new File("target/cuckestyle_results.csv").getAbsolutePath());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private void printTableToConsole(final List<RuleResult> ruleResults) {
        if (!ruleResults.isEmpty()) {
            String[] header = {"INDEX", "RULE", "SEVERITY", "ERROR_MESSAGE"};
            String[][] data = new String[ruleResults.size()][4];

            IntStream.range(0, ruleResults.size()).forEach(index -> {
                data[index][0] = index + "";
                data[index][1] = ruleResults.get(index).getRuleId();
                data[index][2] = ruleResults.get(index).getRuleSeverityType().toString();
                data[index][3] = ruleResults.get(index).getMessage().get();
            });

            ASCIITable.getInstance().printTable(header, data);
        }
    }


    public String getRulesFilePath() {
        return rulesFilePath;
    }

    public void setRulesFilePath(String rulesFilePath) {
        this.rulesFilePath = rulesFilePath;
    }

    public int getHighThreshold() {
        return highThreshold;
    }

    public void setHighThreshold(int highThreshold) {
        this.highThreshold = highThreshold;
    }

    public int getMediumThreshold() {
        return mediumThreshold;
    }

    public void setMediumThreshold(int mediumThreshold) {
        this.mediumThreshold = mediumThreshold;
    }

    public String getBaseStepDefsPackage() {
        return baseStepDefsPackage;
    }

    public void setBaseStepDefsPackage(String baseStepDefsPackage) {
        this.baseStepDefsPackage = baseStepDefsPackage;
    }

    public String getBaseFeatureFilesDir() {
        return baseFeatureFilesDir;
    }

    public void setBaseFeatureFilesDir(String baseFeatureFilesDir) {
        this.baseFeatureFilesDir = baseFeatureFilesDir;
    }
}
