# CukeStyle Maven Plugin #

This is a simple extensible cucumber feature files rules checker maven plugin. 
This currently only supports Cucumber (Java) as a Gherkin implementation.
The rules are defined in a json file which is fed into the plugin for checking.
More rules can be easily added in the future without modifying the existing ones.

## A sample rules json file would typically look like this. ##

```javascript
[
  {
    "ruleId": "UNUSED_STEP_DEFS",
    "ruleDescription": "Number of unused Java step definitions",
    "ruleSeverity": "HIGH",
    "maxThreshold": 0
  },
  {
    "ruleId": "CONJUNCTIONS",
    "ruleDescription": "Number of conjunctions 'and' in a a step",
    "ruleSeverity": "HIGH",
    "minThreshold": 0,
    "maxThreshold": 5
  },
  {
    "ruleId": "EXAMPLES_LIMIT",
    "ruleDescription": "Number of examples in a data table",
    "ruleSeverity": "HIGH",
    "minThreshold": 0,
    "maxThreshold": 10
  }
]
```

The basic usage is to hook this plugin during the prepare-package phase (before the Functional Tests) are run and get a report of the issues (if any).

### To hook this plugin into the pom of the functional tests, the following must be done. ###
   
   
      <plugin>
                <groupId>org.sai.tools</groupId>
                <artifactId>cuke-style-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <configuration>
                    <rulesFilePath>rules.json</rulesFilePath>
                    <baseStepDefsPackage>features.steps</baseStepDefsPackage>
                    <baseFeatureFilesDir>target/test-classes</baseFeatureFilesDir>
                </configuration>
                <executions>
                    <execution>
                        <id>check</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>check</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            
All the configuration parameters are self explanatory.

## An example table is output on the command line as a result of the checks 
```
[INFO] --- cuke-style-maven-plugin:1.0-SNAPSHOT:check (check) @ ui-functional-tests ---
[INFO] Going to check the feature files against the rules: /Users/sai/ui-functional-tests/ui-functional-tests/rules.json
+------------------+----------+--------------------------------------------------------------------------------------------------------+
|       RULE       | SEVERITY |                                              ERROR_MESSAGE                                             |
+------------------+----------+--------------------------------------------------------------------------------------------------------+
|   EXAMPLES_LIMIT |     HIGH |                                                                      /footer.feature (Max allowed: 10) |
|   EXAMPLES_LIMIT |     HIGH |                                                                     /meganav.feature (Max allowed: 10) |
| UNUSED_STEP_DEFS |     HIGH |                   features.steps.CommonPageSteps::link_for_respective_address_should_be_displayed(...) |
| UNUSED_STEP_DEFS |     HIGH |                                         features.steps.CommonPageSteps::links_should_be_displayed(...) |
| UNUSED_STEP_DEFS |     HIGH |            features.steps.ProductDetailsLegacyPageSteps::i_add_a_product_to_the_localised_trolley(...) |
| UNUSED_STEP_DEFS |     HIGH | features.steps.ProductDetailsLegacyPageSteps::i_add_a_product_to_the_localised_OutOfStock_trolley(...) |
+------------------+----------+--------------------------------------------------------------------------------------------------------+

[INFO] Cukestyle Reports generated successfully in /Users/sai/ui-functional-tests/ui-functional-tests/target/cuckestyle_results.csv
```

## For Rule Implementors ##
Every rule must have an unique ID defined in the "ruleId" field of the JSON. Once you have chosen the ID, 
you will just need to implement a new rule just by extending the DefaultRule.

All the wiring of the rules would happen automatically.