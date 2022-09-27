package com.wsi.proshipperformance.impl;

import com.wsi.proshipperformance.api.DataRandomizerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DataRandomizerServiceImpl implements DataRandomizerService {

    public DataRandomizerServiceImpl(){}

    @Override
    public String randomizeElementData(String originalXML, List<String> tagsToRandomize) {
        return null;
    }
}
