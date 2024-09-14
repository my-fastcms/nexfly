package com.nexfly.gen.common;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

public class CustomModelPlugin extends PluginAdapter {

    private static final String BASE_MODEL_CLASS = "com.nexfly.common.model.BaseModel"; // 替换为你的基础模型类全限定名

    private static final String EXCLUDE_CREATE = "createAt";

    private static final String EXCLUDE_UPDATE = "updateAt";

    private static final String EXCLUDE_CREATE_BY = "createBy";

    private static final String EXCLUDE_UPDATE_By = "updateBy";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addBaseModelInheritance(topLevelClass);
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String fieldName = field.getName();
        if (StringUtility.stringHasValue(fieldName) && fieldName.equals(EXCLUDE_CREATE)) {
            return false; // 不生成该字段的属性
        }
        if (StringUtility.stringHasValue(fieldName) && fieldName.equals(EXCLUDE_UPDATE)) {
            return false; // 不生成该字段的属性
        }
        if (StringUtility.stringHasValue(fieldName) && fieldName.equals(EXCLUDE_CREATE_BY)) {
            return false; // 不生成该字段的属性
        }
        if (StringUtility.stringHasValue(fieldName) && fieldName.equals(EXCLUDE_UPDATE_By)) {
            return false; // 不生成该字段的属性
        }
        return true; // 其他情况下仍生成属性
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String propertyName = introspectedColumn.getJavaProperty();
        return !propertyName.equals(EXCLUDE_CREATE) && !propertyName.equals(EXCLUDE_UPDATE)
                && !propertyName.equals(EXCLUDE_CREATE_BY) && !propertyName.equals(EXCLUDE_UPDATE_By); // 不生成getter方法
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String propertyName = introspectedColumn.getJavaProperty();
        return !propertyName.equals(EXCLUDE_CREATE) && !propertyName.equals(EXCLUDE_UPDATE)
        && !propertyName.equals(EXCLUDE_CREATE_BY) && !propertyName.equals(EXCLUDE_UPDATE_By); // 不生成setter方法
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 禁止生成 Example 类
        return false;
    }

    private void addBaseModelInheritance(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType baseModelType = new FullyQualifiedJavaType(BASE_MODEL_CLASS);
        topLevelClass.addImportedType(baseModelType);
        topLevelClass.setSuperClass(baseModelType);
    }

}
