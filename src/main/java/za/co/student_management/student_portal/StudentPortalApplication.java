package za.co.student_management.student_portal;

import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class StudentPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentPortalApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean<Servlet> customServlet() {
		return new ServletRegistrationBean<>(new HttpServlet() {
			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
				resp.getWriter().write("Hello from Custom Servlet!");
			}
		}, "/custom-servlet");
	}
}
