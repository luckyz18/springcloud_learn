package com.itmuch.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.itmuch.contentcenter.Util.ExtendBalancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Ribbon 扩展，同集群优先调用
 */
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object o) {
        try {
            //拿到配置文件中的集群的名称 BJ
            String clusterName = nacosDiscoveryProperties.getClusterName();
            //想要调用的服务名称
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            String name = loadBalancer.getName();
            //拿到服务发现相关的API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            //业务流程  1. 找到指定服务的所有实例 A
            List<Instance> instances = namingService.selectInstances(name, true);
            //2. 过滤出相同集群下的所有实例 B
            List<Instance> sameClusterInstances = instances.stream()
                    .filter(instance -> Objects.equals(instance.getClusterName(), clusterName))
                    .collect(Collectors.toList());
            //3. B如果为空，则用A
            List<Instance> instancesToChosen = new ArrayList();
            if (CollectionUtils.isEmpty(sameClusterInstances)) {
                log.info("发生跨集群的调用，name = {},cluterName ={},instance={}",
                        name,
                        clusterName,
                        instances
                );
                instancesToChosen = instances;
            }else {
                instancesToChosen = sameClusterInstances;
            }
            //4. 调用 基于权重的负载均衡算法
            Instance instance = ExtendBalancer.getHostByRandomWeight2(instancesToChosen);
            log.info("选择的实例是 ：port={},instance={} ",instance.getPort(),instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("发生异常了");
            return null;
        }
    }
}

/*//用别人的类中的方法但是是  protect ,自己写一个包装一下
class ExtendBalancer extends Balancer{
    public static Instance getHostByRandomWeight2(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}*/
