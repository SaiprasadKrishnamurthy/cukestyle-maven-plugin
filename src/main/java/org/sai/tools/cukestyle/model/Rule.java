package org.sai.tools.cukestyle.model;

import java.util.List;

/**
 * Created by sai on 02/07/2015.
 */
public interface Rule {
    String id();
    List<RuleResult> evaluate(ChecksRequest request);
}
