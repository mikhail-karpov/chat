package com.mikhailkarpov.apigateway;

import com.mikhailkarpov.apigateway.config.SecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(SecurityTestConfig.class)
class ApiGatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
