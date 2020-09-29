package sample.spring.webflux.xsuaa;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.autoconfiguration.XsuaaAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { XsuaaAutoConfiguration.class })
class ApplicationTest {

    @Autowired
    private XsuaaServiceConfiguration xsuaaServiceConfiguration;

	@Test
	public void contextLoads() {
        assertThat(xsuaaServiceConfiguration, is(notNullValue()));
	}

}