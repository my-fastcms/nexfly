package com.nexfly.ai.common.provider;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author wangjun
 * @Date 2024/8/23
 **/
@Service
public class DefaultProviderManager implements ProviderManager, InitializingBean {

    List<SystemProvider> providerList = Collections.synchronizedList(new ArrayList<>());

    List<SystemProviderModel> providerModelList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public List<SystemProvider> getSystemProviderList() {
        return providerList;
    }

    @Override
    public List<SystemProvider> getSystemAvailableProviderList() {
        return getSystemProviderList().stream().filter(pm -> pm.getStatus().equals("1")).toList();
    }

    @Override
    public Map<String, SystemProvider> getSystemProviderMap() {
        return providerList.stream().collect(Collectors.toMap(SystemProvider::getProvider, SystemProvider -> SystemProvider));
    }

    @Override
    public Map<String, SystemProvider> getSystemAvailableProviderMap() {
        return getSystemAvailableProviderList().stream().collect(Collectors.toMap(SystemProvider::getProvider, SystemProvider -> SystemProvider));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initSystemProvider();
    }

    void initSystemProvider() {
        try {
            // 加载provider
            Resource[] providerResources = new PathMatchingResourcePatternResolver().getResources("provider/*.yaml");
            for (Resource res: providerResources) {
                providerList.add(new Yaml().loadAs(res.getInputStream(), SystemProvider.class));
            }

            // 加载model
            Resource[] modelResources = new PathMatchingResourcePatternResolver().getResources("model/**/*.yaml");
            for (Resource res: modelResources) {
                String filename = res.getFilename();
                assert filename != null;
                if (filename.contains("tts")) {
                    SystemProviderModel systemProviderModel = new Yaml().loadAs(res.getInputStream(), TtsSystemProviderModel.class);
                    providerModelList.add(systemProviderModel);
                } else if (filename.contains("embed")) {
                    SystemProviderModel systemProviderModel = new Yaml().loadAs(res.getInputStream(), EmbedSystemProviderModel.class);
                    providerModelList.add(systemProviderModel);
                } else {
                    SystemProviderModel systemProviderModel = new Yaml().loadAs(res.getInputStream(), LlmSystemProviderModel.class);
                    providerModelList.add(systemProviderModel);
                }
            }

            Map<String,List<SystemProviderModel>> modleMap = providerModelList.stream().collect(Collectors.groupingBy(SystemProviderModel::getProvider));

            for (SystemProvider systemProvider : providerList) {
                systemProvider.setProviderModelList(modleMap.get(systemProvider.getProvider()));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
