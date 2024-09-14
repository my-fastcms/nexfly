package com.nexfly.gen.common;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class CustomXmlMapperPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        // 这里可以添加一些自定义的验证逻辑
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        // 清除默认生成的内容
        document.getRootElement().getElements().clear();

        // 添加命名空间
        String namespace = introspectedTable.getMyBatis3JavaMapperType();
        XmlElement rootElement = new XmlElement("mapper");
        rootElement.addAttribute(new Attribute("namespace", namespace));

        // 添加 resultMap 元素
        rootElement.addElement(buildBaseResultMapElement(introspectedTable));

        // 添加 Vo_Column_List sql 元素
        rootElement.addElement(voColumnListElement(introspectedTable));

        // 添加 list select 元素
//        rootElement.addElement(listSelectElement(introspectedTable));

        // 添加 save insert 元素
        rootElement.addElement(saveInsertElement(introspectedTable));

        // 添加 update update 元素
        rootElement.addElement(updateUpdateElement(introspectedTable));

        // 添加 delete delete 元素
//        rootElement.addElement(deleteDeleteElement(introspectedTable));

        // 添加 findById select 元素
        rootElement.addElement(buildFindByIdElement(introspectedTable));

        // 将根元素添加到文档中
        document.setRootElement(rootElement);

        // 返回 true 表示插件执行成功
        return true;
    }

    private XmlElement voColumnListElement(IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("`").append(columns.get(i).getActualColumnName()).append("`");
        }

        XmlElement sqlElement = new XmlElement("sql");
        sqlElement.addAttribute(new Attribute("id", "Vo_Column_List"));
        sqlElement.addElement(new TextElement(sb.toString()));
        return sqlElement;
    }

    private XmlElement listSelectElement(IntrospectedTable introspectedTable) {
        XmlElement selectElement = new XmlElement("select");
        selectElement.addAttribute(new Attribute("id", "list"));
        selectElement.addAttribute(new Attribute("resultType", introspectedTable.getBaseRecordType()));
        selectElement.addElement(new TextElement("select <include refid=\"Vo_Column_List\"/> from " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " order by " + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName() + " desc"));
        return selectElement;
    }

    private XmlElement saveInsertElement(IntrospectedTable introspectedTable) {
        String baseRecordType = StringUtil.firstLetterToLower(introspectedTable.getFullyQualifiedTable().getDomainObjectName());

        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(introspectedTable.getFullyQualifiedTableNameAtRuntime()).append(" (");
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        columns.removeIf(item -> item.getActualColumnName().equals("create_at") || item.getActualColumnName().equals("update_at"));
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("`").append(columns.get(i).getActualColumnName()).append("`");
        }
        sb.append(") values (");
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("#{").append(baseRecordType).append(".").append(columns.get(i).getJavaProperty()).append("}");
        }
        sb.append(")");

        XmlElement insertElement = new XmlElement("insert");
        insertElement.addAttribute(new Attribute("id", "save"));
        insertElement.addElement(new TextElement(sb.toString()));
        return insertElement;
    }

    private XmlElement updateUpdateElement(IntrospectedTable introspectedTable) {
        String baseRecordType = StringUtil.firstLetterToLower(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        XmlElement updateElement = new XmlElement("update");
        updateElement.addAttribute(new Attribute("id", "update"));

        // 添加 update 语句
        StringBuilder updateClause = new StringBuilder();
        updateClause.append("update ");
        updateClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        TextElement updateText = new TextElement(updateClause.toString());
        updateElement.addElement(updateText);

        // 添加 set 子句
        XmlElement setElement = new XmlElement("set");
        updateElement.addElement(setElement);

        // 添加 if 条件
        for (IntrospectedColumn column : introspectedTable.getBaseColumns()) {
            StringBuilder columnClause = new StringBuilder();
            columnClause.append(column.getActualColumnName()).append(" = ");
            columnClause.append("#{").append(baseRecordType).append(".").append(column.getJavaProperty());
            columnClause.append(",jdbcType=").append(column.getJdbcTypeName()).append("},");
            XmlElement ifElement = new XmlElement("if");
            ifElement.addAttribute(new Attribute("test", baseRecordType + "." + column.getJavaProperty() + " != null"));
            TextElement columnText = new TextElement(columnClause.toString());
            ifElement.addElement(columnText);
            setElement.addElement(ifElement);
        }

        // 添加 where 子句
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("where ");
        IntrospectedColumn pkColumn = introspectedTable.getPrimaryKeyColumns().get(0);
        whereClause.append(pkColumn.getActualColumnName()).append(" = ");
        whereClause.append("#{").append(baseRecordType).append(".").append(pkColumn.getJavaProperty());
        whereClause.append(",jdbcType=").append(pkColumn.getJdbcTypeName()).append("}");
        TextElement whereText = new TextElement(whereClause.toString());
        updateElement.addElement(whereText);

        // 在 update 元素的结尾处添加一个空格
        updateElement.addElement(new TextElement(" "));

        return updateElement;
    }

    private XmlElement deleteDeleteElement(IntrospectedTable introspectedTable) {
        String idName = CaseConverter.underscoreToCamel(introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName());
        XmlElement deleteElement = new XmlElement("delete");
        deleteElement.addAttribute(new Attribute("id", "delete"));
        deleteElement.addElement(new TextElement("delete from " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " where " + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName() + " = #{"+idName+"}"));
        return deleteElement;
    }

    private XmlElement buildFindByIdElement(IntrospectedTable introspectedTable) {

        String idName = CaseConverter.underscoreToCamel(introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName());

        XmlElement selectElement = new XmlElement("select");
        selectElement.addAttribute(new Attribute("id", "findById"));
        selectElement.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
        selectElement.addElement(new TextElement("select <include refid=\"Vo_Column_List\"/> from " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " where " + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName() + " = #{"+idName+"}"));
        return selectElement;
    }

    private XmlElement buildBaseResultMapElement(IntrospectedTable introspectedTable) {
        XmlElement resultMap = new XmlElement("resultMap");
        resultMap.addAttribute(new Attribute("id", introspectedTable.getBaseResultMapId()));
        resultMap.addAttribute(new Attribute("type", introspectedTable.getBaseRecordType()));

        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            XmlElement idElement = new XmlElement("id");
            idElement.addAttribute(new Attribute("column", column.getActualColumnName()));
            idElement.addAttribute(new Attribute("property", column.getJavaProperty()));
            resultMap.addElement(idElement);
        }

        for (IntrospectedColumn column : introspectedTable.getBaseColumns()) {
            XmlElement resultElement = new XmlElement("result");
            resultElement.addAttribute(new Attribute("column", column.getActualColumnName()));
            resultElement.addAttribute(new Attribute("property", column.getJavaProperty()));
            resultMap.addElement(resultElement);
        }

        return resultMap;
    }

    public class CaseConverter {
        public static String underscoreToCamel(String input) {
            if (input == null || input.isEmpty()) {
                return input;
            }
            StringBuilder builder = new StringBuilder();
            boolean nextUpperCase = false;
            for (char c : input.toCharArray()) {
                if (c == '_') {
                    nextUpperCase = true;
                } else {
                    if (nextUpperCase) {
                        builder.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        builder.append(Character.toLowerCase(c));
                    }
                }
            }
            return builder.toString();
        }

    }
}