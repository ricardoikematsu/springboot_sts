package com.ikematsu.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ikematsu.configs.TestConfigs;
import com.ikematsu.integrationtests.dto.AccountCredentialsDTO;
import com.ikematsu.integrationtests.dto.TokenDTO;
import com.ikematsu.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

	private static TokenDTO tokenDTO;

	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsDTO user =
				new AccountCredentialsDTO("leandro", "admin123");

		tokenDTO = given()
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenDTO.class);

		Assertions.assertNotNull(tokenDTO.getAccessToken());
		Assertions.assertNotNull(tokenDTO.getRefreshToken());
	}

	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {

		var newTokenVO = given()
				.basePath("/auth/refresh")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("username", tokenDTO.getUsername())
				.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
				.when()
				.put("{username}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenDTO.class);

		Assertions.assertNotNull(newTokenVO.getAccessToken());
		Assertions.assertNotNull(newTokenVO.getRefreshToken());
	}
}
