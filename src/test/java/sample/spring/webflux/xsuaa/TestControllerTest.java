package sample.spring.webflux.xsuaa;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.test.JwtGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;

import static org.hamcrest.CoreMatchers.containsString;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = TestController.class)
@AutoConfigureWebTestClient(timeout = "2500000")
@Import(SecurityConfiguration.class)
class TestControllerTest {

	@Autowired
	private WebTestClient webClient;

	@Autowired
	private XsuaaServiceConfiguration xsuaaServiceConfiguration;

	@Test
	void unauthorizedRequest() {
		JwtGenerator jwtGenerator = new JwtGenerator("WrongClientId");

		webClient.method(HttpMethod.GET).uri("/v1/sayHello").contentType(MediaType.APPLICATION_JSON_UTF8)
				.header(HttpHeaders.AUTHORIZATION, jwtGenerator.getTokenForAuthorizationHeader()).exchange()
				.expectStatus().isUnauthorized();
	}

	@Test
	void authorizedRequest() {
		JwtGenerator jwtGenerator = new JwtGenerator().addScopes(getGlobalScope("Read"));

		webClient.method(HttpMethod.GET).uri("/v1/sayHello").contentType(MediaType.APPLICATION_JSON_UTF8)
				.header(HttpHeaders.AUTHORIZATION, jwtGenerator.getTokenForAuthorizationHeader()).exchange()
				.expectStatus().is2xxSuccessful().expectBody(String.class).value(containsString(",\"scope\":[\"xsapplication!t895.Read\"],"));
	}

	private String getGlobalScope(String localScope) {
		Assert.hasText(xsuaaServiceConfiguration.getAppId(), "make sure that xsuaa.xsappname is configured properly.");
		return xsuaaServiceConfiguration.getAppId() + "." + localScope;
	}

}
