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
package org.apache.webbeans.newtests.decorators.tests;

import org.apache.webbeans.newtests.AbstractUnitTest;
import org.apache.webbeans.newtests.decorators.multiple.IOutputProvider;
import org.apache.webbeans.newtests.decorators.multiple.MultipleCallDecorator;
import org.apache.webbeans.newtests.decorators.multiple.OutputProvider;
import org.apache.webbeans.newtests.decorators.multiple.RequestStringBuilder;
import org.junit.Ignore;
import org.junit.Test;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class MultipleCallDecoratorTest extends AbstractUnitTest
{
    public static final String PACKAGE_NAME = MultipleDecoratorStackTests.class.getPackage().getName();

    @Test
    @Ignore("Decorator can't call twice the delegate")
    public void testDecoratorStackWithAbstractAtEnd()
    {
        Collection<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(MultipleCallDecorator.class);
        classes.add(IOutputProvider.class);
        classes.add(OutputProvider.class);
        classes.add(RequestStringBuilder.class);

        Collection<String> xmls = new ArrayList<String>();
        xmls.add(getXmlPath(PACKAGE_NAME, "MultipleCallDecoratorTest"));

        startContainer(classes, xmls);

        Bean<?> bean = getBeanManager().getBeans(OutputProvider.class, new AnnotationLiteral<Default>()
        {
        }).iterator().next();

        IOutputProvider instance = (IOutputProvider) getBeanManager().getReference(bean, IOutputProvider.class, getBeanManager().createCreationalContext(bean));
        assertEquals("delegate/trace", instance.trace()); // StackOverFlow
    }
}