package com.nexfly.rocketmq.canal;

import cn.throwx.canal.gule.CanalGlue;
import cn.throwx.canal.gule.support.parser.*;
import cn.throwx.canal.gule.support.parser.converter.CanalFieldConverterFactory;
import cn.throwx.canal.gule.support.parser.converter.InMemoryCanalFieldConverterFactory;
import cn.throwx.canal.gule.support.processor.BaseCanalBinlogEventProcessor;
import cn.throwx.canal.gule.support.processor.CanalBinlogEventProcessorFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

/**
 * @Author wangjun
 * @Date 2024/10/13
 **/
@Configuration
public class CanalGlueAutoConfiguration implements SmartInitializingSingleton, BeanFactoryAware {

    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    @Bean
    @ConditionalOnMissingBean
    public CanalBinlogEventProcessorFactory canalBinlogEventProcessorFactory() {
        return NexflyCanalBinlogEventProcessorFactory.of();
    }

    @Bean
    @ConditionalOnMissingBean
    public ModelTableMetadataManager modelTableMetadataManager(CanalFieldConverterFactory canalFieldConverterFactory) {
        return InMemoryModelTableMetadataManager.of(canalFieldConverterFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public CanalFieldConverterFactory canalFieldConverterFactory() {
        return InMemoryCanalFieldConverterFactory.of();
    }

    @Bean
    @ConditionalOnMissingBean
    public CanalBinLogEventParser canalBinLogEventParser() {
        return new NexflyCanalBinLogEventParser();
    }

    @Bean
    @ConditionalOnMissingBean
    public ParseResultInterceptorManager parseResultInterceptorManager(ModelTableMetadataManager modelTableMetadataManager) {
        return InMemoryParseResultInterceptorManager.of(modelTableMetadataManager);
    }

    @Bean
    @Primary
    public CanalGlue canalGlue(CanalBinlogEventProcessorFactory canalBinlogEventProcessorFactory) {
        return new NexflyCanalGlue(canalBinlogEventProcessorFactory);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.configurableListableBeanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void afterSingletonsInstantiated() {
        ParseResultInterceptorManager parseResultInterceptorManager
                = configurableListableBeanFactory.getBean(ParseResultInterceptorManager.class);
        ModelTableMetadataManager modelTableMetadataManager
                = configurableListableBeanFactory.getBean(ModelTableMetadataManager.class);
        CanalBinlogEventProcessorFactory canalBinlogEventProcessorFactory
                = configurableListableBeanFactory.getBean(CanalBinlogEventProcessorFactory.class);
        CanalBinLogEventParser canalBinLogEventParser
                = configurableListableBeanFactory.getBean(CanalBinLogEventParser.class);
        Map<String, BaseParseResultInterceptor> interceptors
                = configurableListableBeanFactory.getBeansOfType(BaseParseResultInterceptor.class);
        interceptors.forEach((k, interceptor) -> parseResultInterceptorManager.registerParseResultInterceptor(interceptor));
        Map<String, BaseCanalBinlogEventProcessor> processors
                = configurableListableBeanFactory.getBeansOfType(BaseCanalBinlogEventProcessor.class);
        processors.forEach((k, processor) -> processor.init(canalBinLogEventParser, modelTableMetadataManager,
                canalBinlogEventProcessorFactory, parseResultInterceptorManager));
    }

}
