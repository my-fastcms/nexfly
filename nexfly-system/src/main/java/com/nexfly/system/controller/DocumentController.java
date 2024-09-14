package com.nexfly.system.controller;

import com.nexfly.ai.common.vectorstore.VectorStoreFactory;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.manager.ModelManager;
import com.nexfly.system.mapper.DatasetMapper;
import com.nexfly.system.mapper.DocumentMapper;
import com.nexfly.system.mapper.DocumentSegmentMapper;
import com.nexfly.system.mapper.ProviderModelMapper;
import com.nexfly.system.model.Dataset;
import com.nexfly.system.model.DocumentSegment;
import com.nexfly.system.model.ProviderModel;
import com.nexfly.system.service.DocumentService;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/document")
public class DocumentController {

    @Autowired
    private VectorStoreFactory vectorStoreFactory;

    @Autowired
    private ModelManager modelManager;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentMapper docmentMapper;

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private DocumentSegmentMapper documentSegmentMapper;

    @Autowired
    private ProviderModelMapper providerModelMapper;

    @PostMapping("upload")
    @ExceptionHandler(value = MultipartException.class)
    public Object upload(@RequestParam("orgId") Long orgId, @RequestParam("datasetId") Long datasetId, @RequestParam("file") MultipartFile file) throws Exception {

        if(file == null) {
            return RestResultUtils.failed("请选择上传文件");
        }

        Dataset dataset = datasetMapper.findById(datasetId);

        if (dataset == null) {
            return RestResultUtils.failed("dataset不存在");
        }

        List<Document> documents = new TikaDocumentReader(new InputStreamResource(file.getInputStream())).get();

        com.nexfly.system.model.Document doc = new com.nexfly.system.model.Document();
        doc.setName(file.getOriginalFilename());
        doc.setOrgId(orgId);
        doc.setDatasetId(dataset.getDatasetId());
        doc.setDataSource(1);
        docmentMapper.save(doc);

        var tokenTextSplitter = new TokenTextSplitter(400, 5, 200, 10000, true);
        List<Document> splitDocuments = tokenTextSplitter.apply(documents);

        for (Document document :splitDocuments) {
            DocumentSegment documentSegment = new DocumentSegment();
            documentSegment.setOrgId(orgId);
            documentSegment.setDatasetId(dataset.getDatasetId());
            documentSegment.setContentId(document.getId());
            documentSegment.setDocumentId(doc.getDocumentId());
            documentSegment.setContent(document.getContent());
            documentSegmentMapper.save(documentSegment);
        }

        ProviderModel providerModel = providerModelMapper.findById(dataset.getEmbedModelId());
        EmbeddingModel embeddingModel = modelManager.getEmbeddingModel(orgId, providerModel.getModelName());
        VectorStore vectorStore = vectorStoreFactory.getVectorStore(dataset.getVsIndexNodeId(), embeddingModel);
        vectorStore.add(splitDocuments);

        return RestResultUtils.success(splitDocuments);
    }

    @PostMapping("save")
    public Object save(@RequestBody DocumentService.DocumentParam documentParam) throws NexflyException {
        documentService.saveDocument(documentParam);
        return RestResultUtils.success();
    }

}
