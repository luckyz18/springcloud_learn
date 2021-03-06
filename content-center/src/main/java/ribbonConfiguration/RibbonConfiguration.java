package ribbonConfiguration;

import com.itmuch.contentcenter.configuration.NacosFinalRule;
import com.itmuch.contentcenter.configuration.NacosSameClusterWeightedRule;
import com.itmuch.contentcenter.configuration.NacosWeightedRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.RandomRule;
import com.sun.deploy.pings.Pings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfiguration {
    @Bean
    public IRule ribbonRule(){
        //return new RandomRule();
        //return new NacosWeightedRule();
        //return new NacosSameClusterWeightedRule();
        return new NacosFinalRule();
    }

    /*@Bean
    public IPing ping(){
        return new PingUrl();
    }*/

}
