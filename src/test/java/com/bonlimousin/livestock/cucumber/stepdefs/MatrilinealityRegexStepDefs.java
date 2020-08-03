package com.bonlimousin.livestock.cucumber.stepdefs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.bonlimousin.livestock.asyncmessaging.BovineEntityChangeHelper;
import com.bonlimousin.livestock.domain.MatrilinealityEntity;
import com.bonlimousin.livestock.repository.MatrilinealityRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MatrilinealityRegexStepDefs extends StepDefs {

	private List<MatrilinealityEntity> matris;
	private List<MatrilinealityEntity> matchedMatris;
	
	@Autowired
	private MatrilinealityRepository matrilinealityRepository;
	
	@Given("matrilinealities are setup")
	public void a_few_matrilinealities_are_setup() {
		matris = matrilinealityRepository.findAll();		
	}

	@When("I test the cow name {string}")
	public void i_test_the_cow_name(String string) {
		matchedMatris = BovineEntityChangeHelper.matchNameWithMatrilineality(string, matris);
	}

	@Then("I should get matrilineality name {string}")
	public void i_should_get_matrilineality_name(String string) {
		if(StringUtils.trimToNull(string) == null) {
			Assert.assertTrue(matchedMatris.isEmpty());
		} else {
			Assert.assertTrue(!matchedMatris.isEmpty());
			Assert.assertEquals(1, matchedMatris.size());
			Assert.assertEquals(string, matchedMatris.get(0).getFamilyname());
		}		
	}
}
