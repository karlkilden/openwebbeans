/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.webbeans.component.creation;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionTarget;

import org.apache.webbeans.component.AbstractInjectionTargetBean;
import org.apache.webbeans.component.ProducerFieldBean;
import org.apache.webbeans.component.ProducerMethodBean;
import org.apache.webbeans.config.DefinitionUtil;

/**
 * Abstract implementation of {@link InjectedTargetBeanCreator}.
 * 
 * @version $Rev$ $Date$
 *
 * @param <T> bean class type
 */
public abstract class AbstractInjectedTargetBeanCreator<T> extends AbstractBeanCreator<T> implements InjectedTargetBeanCreator<T>
{    
    /**Injection target instance. Null if not set by the ProcessInjectionTarget event*/
    private InjectionTarget<T> injectionTarget; 

    /**Set or not*/
    private boolean injectionTargetSet = false;
    
    /**
     * Creates a new instance.
     * 
     * @param bean bean instance
     */
    public AbstractInjectedTargetBeanCreator(AbstractInjectionTargetBean<T> bean)
    {
        super(bean, bean.getReturnType().getDeclaredAnnotations());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInjectionTargetSet()
    {
        return this.injectionTargetSet;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void defineDisposalMethods()
    {
        if(isDefaultMetaDataProvider())
        {
            DefinitionUtil.defineDisposalMethods(getBean());   
        }
        else
        {
            //TODO
        }
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defineInjectedFields()
    {
        if(isDefaultMetaDataProvider())
        {
            DefinitionUtil.defineInjectedFields(getBean());   
        }
        else
        {
            //TODO
        }
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defineInjectedMethods()
    {
        if(isDefaultMetaDataProvider())
        {
            DefinitionUtil.defineInjectedMethods(getBean());
        }
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defineObserverMethods()
    {   
        if(isDefaultMetaDataProvider())
        {
            DefinitionUtil.defineObserverMethods(getBean(), getBean().getReturnType());
        }
        else
        {
            //TODO
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ProducerFieldBean<?>> defineProducerFields()
    {
        if(isDefaultMetaDataProvider())
        {
            return DefinitionUtil.defineProduerFields(getBean());
        }
        else
        {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ProducerMethodBean<?>> defineProducerMethods()
    {
        if(isDefaultMetaDataProvider())
        {
            return DefinitionUtil.defineProducerMethods(getBean());
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Return type-safe bean instance.
     */
    public AbstractInjectionTargetBean<T> getBean()
    {
        return (AbstractInjectionTargetBean<T>)super.getBean();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InjectionTarget<T> getInjectedTarget()
    {
        return this.injectionTarget;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInjectedTarget(InjectionTarget<T> injectionTarget)
    {
        this.injectionTarget = injectionTarget;
        this.injectionTargetSet = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inject(T instance, CreationalContext<T> ctx)
    {
        this.injectionTarget.inject(instance, ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postConstruct(T instance)
    {
        this.injectionTarget.postConstruct(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preDestroy(T instance)
    {
        this.injectionTarget.preDestroy(instance);
    }

}