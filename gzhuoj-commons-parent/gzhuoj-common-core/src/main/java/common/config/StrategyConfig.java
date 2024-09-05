package common.config;

import common.designpattern.strategy.AbstractStrategyChoose;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyConfig {
    @Bean
    public AbstractStrategyChoose abstractStrategyChoose(ApplicationContext applicationContext) {
        return new AbstractStrategyChoose(applicationContext);
    }
}
