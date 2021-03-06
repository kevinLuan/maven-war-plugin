package org.apache.maven.plugin.war;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Create an exploded webapp in a specified directory.
 *
 * @version $Id: WarExplodedMojo.java 1355570 2012-06-29 21:40:37Z rfscholte $
 */
@Mojo( name = "exploded", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true,
       requiresDependencyResolution = ResolutionScope.RUNTIME )
public class WarExplodedMojo
    extends AbstractWarMojo
{
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        getLog().info( "Exploding webapp" );

        buildExplodedWebapp( getWebappDirectory() );
    }

}