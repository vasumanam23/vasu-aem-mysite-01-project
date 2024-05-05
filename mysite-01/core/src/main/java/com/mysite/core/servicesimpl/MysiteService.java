package com.mysite.core.servicesimpl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class MysiteService {
    public static final Logger LOG=LoggerFactory.getLogger(MysiteService.class);

    @Activate
    public void activate(){
        LOG.info("Component is activated");
    }
    @Deactivate
    public void deactivate(){
        LOG.info("Component is deactivated");
    }

    @Modified
    public void modified(){
        LOG.info("Component is modified");
    }

}
