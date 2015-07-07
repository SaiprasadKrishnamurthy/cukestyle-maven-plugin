package org.sai.tools.cukestyle.rule;

import org.sai.tools.cukestyle.common.DataUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sai.tools.cukestyle.model.ChecksRequest;
import org.sai.tools.cukestyle.model.ResultType;
import org.sai.tools.cukestyle.model.RuleResult;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by sai on 04/07/2015.
 */
public class ConjunctionsTest {

    private ChecksRequest request;

    @BeforeClass
    public static void init() {
        System.setProperty("rules.file", System.getProperty("user.dir") + File.separator + "rules.json");
    }

    @Before
    public void setup() throws Exception {
        request = DataUtil.getChecksRequest("org.sai", "max.conjunctions.feature_");
    }

    @Test
    public void should_return_a_failure_results_max_given_conjunction() throws Exception {
        ConjunctionsRule rule = new ConjunctionsRule();
        List<RuleResult> results = rule.evaluate(request);
        assertThat(results.size(), is(3));
        assertThat(results.stream().filter(result -> result.getType() == ResultType.ERROR).count(), is(3L));
    }
}
