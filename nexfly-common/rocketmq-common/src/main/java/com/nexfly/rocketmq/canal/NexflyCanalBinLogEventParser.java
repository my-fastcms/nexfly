package com.nexfly.rocketmq.canal;

import cn.throwx.canal.gule.common.BinLogEventType;
import cn.throwx.canal.gule.common.OperationType;
import cn.throwx.canal.gule.model.CanalBinLogEvent;
import cn.throwx.canal.gule.model.CanalBinLogResult;
import cn.throwx.canal.gule.support.parser.BaseCommonEntryFunction;
import cn.throwx.canal.gule.support.parser.BasePrimaryKeyTupleFunction;
import cn.throwx.canal.gule.support.parser.CanalBinLogEventParser;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Author wangjun
 * @Date 2024/10/13
 **/
public class NexflyCanalBinLogEventParser implements CanalBinLogEventParser {

    private static final Logger log = LoggerFactory.getLogger(NexflyCanalBinLogEventParser.class);

    @Override
    public <T> List<CanalBinLogResult<T>> parse(CanalBinLogEvent event, Class<T> aClass, BasePrimaryKeyTupleFunction primaryKeyFunction, BaseCommonEntryFunction<T> commonEntryFunction) {
        BinLogEventType eventType = BinLogEventType.fromType(event.getType());
        if (BinLogEventType.UNKNOWN != eventType && BinLogEventType.QUERY != eventType) {
            if (Boolean.TRUE.equals(event.getIsDdl())) {
                CanalBinLogResult<T> entry = new CanalBinLogResult();
                entry.setOperationType(OperationType.DDL);
                entry.setBinLogEventType(eventType);
                entry.setDatabaseName(event.getDatabase());
                entry.setTableName(event.getTable());
                entry.setSql(event.getSql());
                return Collections.singletonList(entry);
            } else {
                Optional.ofNullable(event.getPkNames()).filter((x) -> x.size() == 1).orElseThrow(() -> new IllegalArgumentException("DML类型binlog事件主键列数量不为1"));
                String primaryKeyName = event.getPkNames().get(0);
                List<CanalBinLogResult<T>> entryList = new LinkedList();
                List<Map<String, String>> data = event.getData();
                List<Map<String, String>> old = event.getOld();
                int dataSize = null != data ? data.size() : 0;
                int oldSize = null != old ? old.size() : 0;
                if (dataSize > 0) {
                    for(int index = 0; index < dataSize; ++index) {
                        CanalBinLogResult<T> entry = new CanalBinLogResult();
                        entryList.add(entry);
                        entry.setSql(event.getSql());
                        entry.setOperationType(OperationType.DML);
                        entry.setBinLogEventType(eventType);
                        entry.setTableName(event.getTable());
                        entry.setDatabaseName(event.getDatabase());
                        Map<String, String> item = (Map)data.get(index);
                        entry.setAfterData(commonEntryFunction.apply(item));
                        Map<String, String> oldItem = null;
                        if (oldSize > 0 && index <= oldSize) {
                            oldItem = (Map)old.get(index);
                            entry.setBeforeData(commonEntryFunction.apply(oldItem));
                        }

                        entry.setPrimaryKey(primaryKeyFunction.apply(oldItem, item, primaryKeyName));
                    }
                }

                return entryList;
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("监听到不需要处理或者未知的binlog事件类型[{}],将忽略解析过程返回空列表,binlog事件:{}", eventType, JSON.toJSONString(event));
            }

            return Collections.emptyList();
        }
    }

}
