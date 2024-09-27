package com.nexfly.ai.common.model;

/**
 * 模型构建器
 */
public interface ModelFactory {

    /**
     * 创建大模型
     * 获取大模型实列工厂规则：provider_model表中 provider_name + "-" + model_type
     * @param createModel
     * @return
     * @throws Exception
     */
    Object getInstance(CreateModel createModel) throws Exception;

}
