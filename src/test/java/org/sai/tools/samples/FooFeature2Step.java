package org.sai.tools.samples;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by sai on 02/07/2015.
 */
public class FooFeature2Step {

    @Given("I have a calculator in hand")
    public void have_calc() {

    }

    @When("^I input two numbers (\\d+) and (\\d+)$")
    public void input_two_numbers(int x, int y) {

    }

    @And("^I perform add$")
    public void perform_add(int x, int y) {

    }

    @Then("^I see (\\d+) as the result$")
    public void result(int result) {

    }



}
