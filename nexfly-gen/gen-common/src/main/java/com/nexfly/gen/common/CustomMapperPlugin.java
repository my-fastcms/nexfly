package com.nexfly.gen.common;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.Set;

public class CustomMapperPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        // 在这里可以进行插件的验证逻辑
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {

        // 生成 Mapper 接口方法
        interfaze.getMethods().clear();

        // 设置接口的可见性为 public
        interfaze.setVisibility(JavaVisibility.PUBLIC);

        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));

        String domainObjectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();

        // 生成 getById 方法声明
        Method getByIdMethod = new Method("findById");
        getByIdMethod.setVisibility(JavaVisibility.PUBLIC);
        getByIdMethod.setReturnType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));

        // 遍历主键列
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            getByIdMethod.addParameter(new Parameter(column.getFullyQualifiedJavaType(), column.getJavaProperty(), "@Param(\"" + column.getJavaProperty() + "\")"));
        }

        interfaze.addMethod(getByIdMethod);

        // 生成 save 方法声明
        Method saveMethod = new Method("save");
        saveMethod.setVisibility(JavaVisibility.PUBLIC);
        saveMethod.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), domainObjectName, "@Param(\""+StringUtil.firstLetterToLower(domainObjectName)+"\")"));
        interfaze.addMethod(saveMethod);

        // 生成 update 方法声明
        Method updateMethod = new Method("update");
        updateMethod.setVisibility(JavaVisibility.PUBLIC);
        updateMethod.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), domainObjectName, "@Param(\""+StringUtil.firstLetterToLower(domainObjectName)+"\")"));
        interfaze.addMethod(updateMethod);

        // 生成 delete 方法声明
//        Method deleteMethod = new Method("delete");
//        deleteMethod.setVisibility(JavaVisibility.PUBLIC);

        // 遍历主键列
//        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
//            deleteMethod.addParameter(new Parameter(column.getFullyQualifiedJavaType(), column.getJavaProperty(), "@Param(\"" + column.getJavaProperty() + "\")"));
//        }

//        interfaze.addMethod(deleteMethod);

        interfaze.getMethods().forEach(method -> method.setAbstract(true));

        removeImport(interfaze);

        return true;
    }

    private void removeImport(CompilationUnit compilationUnit) {
        Set<FullyQualifiedJavaType> importedTypes = compilationUnit.getImportedTypes();
        importedTypes.removeIf(importedType -> importedType.getShortName().contains("Example"));
        importedTypes.removeIf(importedType -> importedType.getFullyQualifiedName().equals("java.util.List"));
    }

}
