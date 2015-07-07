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
public class ExamplesTest {

    private ChecksRequest request;

    @BeforeClass
    public static void init() {
        System.setProperty("rules.file", System.getProperty("user.dir") + File.separator + "rules.json");
    }

    @Before
    public void setup() throws Exception {
        request = DataUtil.getChecksRequest("org.sai", "max.examples.feature_", "foo.feature.1", "foo.feature.2", "max.conjunctions.feature_");
    }

    @Test
    public void should_return_a_failure_results_max_examples() throws Exception {
        ExamplesRule rule = new ExamplesRule();
        List<RuleResult> results = rule.evaluate(request);
        assertThat(results.size(), is(1));
        assertThat(results.stream().filter(result -> result.getType() == ResultType.ERROR).count(), is(1L));

    }
}
