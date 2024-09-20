package com.nexfly.ai.common.provider;

import java.util.List;
import java.util.Map;

/**
 * @Author wangjun
 * @Date 2024/8/23
 **/
public interface ProviderManager {

    /**
     * 获取系统支持配置的大模型提供商
     * @return
     */
    List<SystemProvider> getSystemProviderList();

    Map<String, SystemProvider> getSystemProviderMap();

}
