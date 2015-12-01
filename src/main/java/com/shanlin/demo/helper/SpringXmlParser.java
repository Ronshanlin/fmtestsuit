package com.shanlin.demo.helper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class SpringXmlParser{
    private static SpringXmlParser parser = null;
    private XmlBeanFactory factory;
    
    private SpringXmlParser(String path){
        Resource resource = new FileSystemResource(path);
        this.factory = new XmlBeanFactory(resource);
    }
    
    public static SpringXmlParser getInstance(String path){
        if (parser==null) {
            parser = new SpringXmlParser(path);
        }
        
        return parser;
    }
    
    public <T> T getBean(Class<T> clazz){
        return factory.getBean(clazz);
    }

    /**
     * 〈一句话功能简述〉<br> 
     *
     * @author 13073457
     */
    private class XmlBeanFactory extends DefaultListableBeanFactory {

        private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);


        public XmlBeanFactory(Resource resource) throws BeansException {
            this(resource, null);
        }
        
        public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory) throws BeansException {
            super(parentBeanFactory);
            this.reader.loadBeanDefinitions(resource);
        }
    }
}
