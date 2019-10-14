package com.itmuch.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonConfiguration.RibbonConfiguration;

//configuration 这个包 必须建在 启动类所在的包以外 ！！！
//@Configuration
//@RibbonClient(name = "user-center",configuration = RibbonConfiguration.class)
//实现全局配置
@Configuration
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
public class UserCenterConfiguration {

}
