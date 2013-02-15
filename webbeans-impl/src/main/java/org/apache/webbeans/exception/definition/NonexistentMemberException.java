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
package org.apache.webbeans.exception.definition;

import org.apache.webbeans.exception.inject.DefinitionException;

public class NonexistentMemberException extends DefinitionException
{
    private static final long serialVersionUID = -7507805351707018006L;

    public NonexistentMemberException()
    {
        super();
    }

    public NonexistentMemberException(String message)
    {
        super(message);
    }

    public NonexistentMemberException(Throwable e)
    {
        super(e);
    }

    public NonexistentMemberException(String message, Throwable e)
    {
        super(message, e);
    }

}