package org.kepennar.facemash.init;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingIterator;
import org.codehaus.jackson.map.ObjectMapper;
import org.kepennar.facemash.model.Element;
import org.kepennar.facemash.repository.ElementRepository;
import org.kepennar.facemash.solr.repository.SolrElementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;



@Named("elementsInitializer")
public class ElementsInitializer implements InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(ElementsInitializer.class);
	
	
    @Inject @Named("elementRepository")
    private ElementRepository elementRepository;

    @Inject @Named("solrElementRepository")
    private SolrElementRepository solrElementRepository;
    
    private @Value("${images.directory}") String imgDirectory;
    private @Value("${init.file}") String initfile;
    
    @Override
	public void afterPropertiesSet() {
    	
    	ObjectMapper mapper = new ObjectMapper();
	
		MappingIterator<Element> elementsIterator = null;
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(initfile);
    		JsonParser parser = mapper.getJsonFactory().createJsonParser(in);
    		elementsIterator = mapper.readValues(parser, Element.class);  
		} catch (IOException ioE) {
			LOGGER.error("Error deserializing init data (json)", ioE);
		}
    	
		elementRepository.deleteAll();
		solrElementRepository.deleteAll();
		
    	if (elementsIterator != null) {
    		while (elementsIterator.hasNext()) {
    			Element elem = elementsIterator.next();
    			String imgurl = elem.getImgUrl();
    			elem.setImgUrl(imgDirectory + imgurl);
    			
    			elementRepository.save(elem);
    			
    			solrElementRepository.save(elem);
    	
    		}
    	}
    }
}
