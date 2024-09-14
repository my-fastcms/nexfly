package com.nexfly.ai.common.model;

/**
 * 模型构建器
 */
public interface ModelFactory {

    Object getInstance(CreateModel createModel) throws Exception;

}
