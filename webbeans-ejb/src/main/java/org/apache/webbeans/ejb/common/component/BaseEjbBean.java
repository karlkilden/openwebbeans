/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.ejb.common.component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.SessionBeanType;

import org.apache.webbeans.component.AbstractInjectionTargetBean;
import org.apache.webbeans.component.EnterpriseBeanMarker;
import org.apache.webbeans.component.WebBeansType;
import org.apache.webbeans.util.ClassUtil;

/**
 * Defines bean contract for the session beans.
 * 
 * @version $Rev$ $Date$
 */
public abstract class BaseEjbBean<T> extends AbstractInjectionTargetBean<T> implements EnterpriseBeanMarker
{
    /**Session bean type*/
    protected SessionBeanType ejbType;
    
    /**Injected reference local interface type*/
    protected Class<?> iface = null;
    
    /** Map of proxy instances to the dependent SFSB they've acquired but not yet removed */
    private Map<Object, Object> dependentSFSBToBeRemoved = new ConcurrentHashMap<Object, Object>();

    /**
     * Creates a new instance of the session bean.
     * @param ejbClassType ebj class type
     */
    public BaseEjbBean(Class<T> ejbClassType)
    {
        super(WebBeansType.ENTERPRISE,ejbClassType);

        //Setting inherited meta data instance
        setInheritedMetaData();
    }

    /**
     * Sets local interface type.
     * @param iface local interface type
     */
    public void setIface(Class<?> iface)
    {
        this.iface = iface;
    }
    
    public Class<?> getIface()
    {
        return this.iface;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void injectFields(T instance, CreationalContext<T> creationalContext)
    {
        //No-operations
    }
    
    /* (non-Javadoc)
     * @see org.apache.webbeans.component.AbstractBean#isPassivationCapable()
     */
    @Override
    public boolean isPassivationCapable()
    {
        if(getEjbType().equals(SessionBeanType.STATEFUL))
        {
            return true;
        }
        
        return false;
    }

    /**
     * Inject session bean injected fields. It is called from
     * interceptor.
     * @param instance bean instance
     * @param creationalContext creational context instance
     */
    @SuppressWarnings("unchecked")
    public void injectFieldInInterceptor(Object instance, CreationalContext<?> creationalContext)
    {
        super.injectFields((T)instance, (CreationalContext<T>)creationalContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected T createComponentInstance(CreationalContext<T> creationalContext)
    {
        return getInstance(creationalContext);
    }
    
    /**
     * Sublclasses must return instance.
     * @param creationalContext creational context
     * @return instance
     */
    protected abstract T getInstance(CreationalContext<T> creationalContext);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroyComponentInstance(T instance, CreationalContext<T> creational)
    {
        if ((this.getScope() == Dependent.class) && (getEjbType().equals(SessionBeanType.STATEFUL)))
        {
            try
            {
                Object ejbInstance = getDependentSFSBForProxy(instance);
                if (ejbInstance != null)
                {
                    List<Method> methods = getRemoveMethods();
                    if (methods.size() > 0)
                    {
                        // FIXME: This needs to call an API from the EJB
                        // container to remove the EJB instance directly, not
                        // via a remove method
                        // For now, just call 1 remove method directly on the
                        // EJB
                        ClassUtil.callInstanceMethod(methods.get(0), ejbInstance, ClassUtil.OBJECT_EMPTY);
                    }
                }
            }
            finally
            {
                removeDependentSFSB(instance);
            }
        }
    }

    /**
     * Sets session bean type.
     * @param type session bean type
     */
    public void setEjbType(SessionBeanType type)
    {
        this.ejbType = type;
        
    }
    
    /**
     * Subclasses can override this.
     * @return remove methods
     */
    public List<Method> getRemoveMethods()
    {
        return null;
    }
    
    /**
     * Subclasses must override this to return local interfaces.
     * @return local business interfaces.
     */
    public List<Class<?>> getBusinessLocalInterfaces()
    {
        return null;
    }
    
    /**
     * Subclasses must override this to return ejb name
     * @return ejb name
     */    
    public String getEjbName()
    {
        return null;
    }
    
    /**
     * Gets ejb session type.
     * @return type of the ejb
     */
    public SessionBeanType getEjbType()
    {
        return this.ejbType;
    }
    
    /**
     * Keep track of which proxies have gotten EJB objects out of a context
     * @param dependentSFSB The dependent SFSB acquired from the EJB container
     * @param proxy The OWB proxy instance whose method handler acquired the dependnet SFSB
     */
    public void addDependentSFSB(Object dependentSFSB, Object proxy) 
    { 
        dependentSFSBToBeRemoved.put(proxy, dependentSFSB);
    }
    
    /**
     * Call after observing an @Remove method on an EJB instance
     * @param proxy the proxy instance the dependent SFSB is associated with
     */
    public void removeDependentSFSB(Object proxy) 
    { 
        dependentSFSBToBeRemoved.remove(proxy);
    }
    
    /**
     * 
     * @param proxy an instance of our own proxy
     * @return the underlying EJB instance associated with the proxy
     */
    public Object getDependentSFSBForProxy(Object proxy) 
    { 
        return dependentSFSBToBeRemoved.get(proxy);
    }
    


}