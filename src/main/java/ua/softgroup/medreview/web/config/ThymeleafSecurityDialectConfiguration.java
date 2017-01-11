package ua.softgroup.medreview.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Configuration
@ConditionalOnClass({SpringSecurityDialect.class})
public class ThymeleafSecurityDialectConfiguration {

    protected ThymeleafSecurityDialectConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

}
