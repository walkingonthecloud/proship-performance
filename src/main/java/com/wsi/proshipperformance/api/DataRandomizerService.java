package com.wsi.proshipperformance.api;

import java.util.List;

public interface DataRandomizerService {

    String randomizeElementData(String originalXML, List<String> tagsToRandomize);

}
