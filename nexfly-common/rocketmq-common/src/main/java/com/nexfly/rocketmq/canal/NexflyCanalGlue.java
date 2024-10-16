package com.nexfly.rocketmq.canal;

import cn.throwx.canal.gule.CanalGlue;
import cn.throwx.canal.gule.model.CanalBinLogEvent;
import cn.throwx.canal.gule.model.ModelTable;
import cn.throwx.canal.gule.support.adapter.SourceAdapterFacade;
import cn.throwx.canal.gule.support.processor.BaseCanalBinlogEventProcessor;
import cn.throwx.canal.gule.support.processor.CanalBinlogEventProcessorFactory;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/10/13
 **/
public class NexflyCanalGlue implements CanalGlue {

    private final CanalBinlogEventProcessorFactory canalBinlogEventProcessorFactory;

    public NexflyCanalGlue(CanalBinlogEventProcessorFactory canalBinlogEventProcessorFactory) {
        this.canalBinlogEventProcessorFactory = canalBinlogEventProcessorFactory;
    }

    @Override
    public void process(String content) {
        CanalBinLogEvent event = SourceAdapterFacade.X.adapt(CanalBinLogEvent.class, content);
        ModelTable modelTable = ModelTable.of(event.getDatabase(), event.getTable());
        List<BaseCanalBinlogEventProcessor<?>> baseCanalBinlogEventProcessors = this.canalBinlogEventProcessorFactory.get(modelTable);
        if (baseCanalBinlogEventProcessors.isEmpty()) {
            return;
        }
        baseCanalBinlogEventProcessors.forEach((processor) -> processor.process(event));
    }

}
