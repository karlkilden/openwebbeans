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
package org.apache.webbeans.test.unittests.xml;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Set;

import junit.framework.Assert;

import org.apache.webbeans.test.TestContext;
import org.apache.webbeans.xml.WebBeansXMLConfigurator;
import org.apache.webbeans.xml.XMLAnnotationTypeManager;
import org.junit.Test;

public class XMLAnnotTypeTest extends TestContext
{
    public XMLAnnotTypeTest()
    {
        super(XMLAnnotTypeTest.class.getName());
    }

    public void init()
    {
        // TODO is this intentionally that we do not call super.init() ?
    }

    @Test
    public void testBindingAnnotation()
    {
        InputStream stream = XMLFieldTest.class.getClassLoader().getResourceAsStream("org/apache/webbeans/test/xml/bindingTypeAnnot.xml");
        Assert.assertNotNull(stream);
        
        WebBeansXMLConfigurator configurator = new WebBeansXMLConfigurator();
        configurator.configureOwbSpecific(stream, "bindingTypeAnnot.xml");

        Set<Class<? extends Annotation>> aanns = XMLAnnotationTypeManager.getInstance().getBindingTypes();

        Assert.assertEquals(2, aanns.size());
    }

    @Test
    public void testInterceptorBindingAnnotation()
    {
        //X TODO
    }

    @Test
    public void testStereotypeAnnotation()
    {
        //X TODO
    }
}