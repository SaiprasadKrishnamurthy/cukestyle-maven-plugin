package org.sai.tools.cukestyle.rule;

import org.sai.tools.cukestyle.common.DataUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sai.tools.cukestyle.model.ChecksRequest;
import org.sai.tools.cukestyle.model.ResultType;
import org.sai.tools.cukestyle.model.RuleResult;
import org.sai.tools.cukestyle.model.RuleSeverityType;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by sai on 02/07/2015.
 */
public class UnusedStepDefsRuleTest {

    private ChecksRequest request;

    @BeforeClass
    public static void init() {
        System.setProperty("rules.file", System.getProperty("user.dir") + File.separator + "rules.json");
    }

    @Before
    public void setup() throws Exception {
        request = DataUtil.getChecksRequest("org.sai", "foo.feature.1", "foo.feature.2");
    }

    @Test
    public void should_return_a_failure_results_for_unused_step() throws Exception {
        UnusedStepDefsRule r = new UnusedStepDefsRule();
        List<RuleResult> results = r.evaluate(request);
        assertThat(results.size(), is(2));
        assertThat(results.stream().allMatch(result -> result.getType() == ResultType.ERROR), is(true));
        assertThat(results.stream().allMatch(result -> result.getRuleId() == "UNUSED_STEP_DEFS"), is(true));
        assertThat(results.stream().allMatch(result -> result.getRuleSeverityType() == RuleSeverityType.HIGH), is(true));
    }
}
