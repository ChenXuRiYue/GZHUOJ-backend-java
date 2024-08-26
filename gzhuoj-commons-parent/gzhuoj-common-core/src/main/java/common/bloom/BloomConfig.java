package common.bloom;

import cn.hutool.bloomfilter.BloomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloomConfig {
    @Bean
    public Bloom bloom() {
        return new Bloom();
    }
}
