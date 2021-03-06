/**************************************************************************************** 
 Copyright © 2003-2012 hbasesoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.hbasesoft.framework.db.jpa;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.hbasesoft.framework.common.utils.PropertyHolder;
import com.hbasesoft.framework.db.core.config.DbParam;
import com.hbasesoft.framework.db.core.utils.DataSourceUtil;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2017年9月23日 <br>
 * @since V1.0<br>
 * @see com.hbasesoft.framework.test.db.jpa <br>
 */
@Configuration
@EnableJpaRepositories(value = "com.hbasesoft.**/*.repository",
    repositoryFactoryBeanClass = NativeJpaRepositoryFactoryBean.class)
public class JpaConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(applicationContext.getBean(EntityManagerFactory.class));
        txManager.setValidateExistingTransaction(true);
        return txManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        String dbType = StringUtils.upperCase(PropertyHolder.getProperty("master.db.type", "mysql"));
        vendorAdapter.setDatabase(Database.valueOf(dbType));
        vendorAdapter.setShowSql(PropertyHolder.getBooleanProperty("master.db.showSql", true));
        vendorAdapter.setGenerateDdl(PropertyHolder.getBooleanProperty("master.db.generateDdl", false));

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.hbasesoft.**/*.entity");
        factory.setDataSource(DataSourceUtil.registDataSource("master", new DbParam("master")));
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @param arg0
     * @throws BeansException <br>
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
