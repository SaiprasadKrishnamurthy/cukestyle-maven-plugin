package org.sai.tools.cukestyle.rule;

import org.sai.tools.cukestyle.model.Rule;
import org.sai.tools.cukestyle.repository.ConfigRepository;

/**
 * Created by sai on 03/07/2015.
 */
public abstract class DefaultRule implements Rule {
    protected ConfigRepository configRepository = new ConfigRepository();
}
