package org.mule.tools.maven;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

/**
 * Deploys Mule Domain artifact to Mule instances
 */
@Mojo(name = "deploy", defaultPhase = LifecyclePhase.DEPLOY, requiresDependencyCollection = ResolutionScope.COMPILE)
public class DeployDomainMojo extends AbstractMuleDomainMojo {

    public void execute() throws MojoExecutionException {
        File domainFile = this.getDomain();
        getLog().info("Deploying domain: " + domainFile);
        getLog().info("Final domain name: " + getDomainName());
        getLog().info("Agent URLs are: " + getUrls());

        String[] urlsList = getUrls().split(",");
        for (String nextAgent :  urlsList) {
            try {
                URL nextAgentURL = new URL(nextAgent);
                URL postDomainURL = new URL(nextAgentURL, "mule/domains/" + getDomainName());

                getLog().info("Deploying to: " + postDomainURL);

                Client client = Client.create();

                WebResource webResource = client
                        .resource(postDomainURL.toString());

                ClientResponse response = webResource.type("application/octet-stream")
                        .put(ClientResponse.class, new FileInputStream(domainFile));

                if (response.getStatus() >= 299) {
                    throw new RuntimeException("HTTP error : " + response.getStatus() + " : " + response.getStatusInfo() + " : " + response.getEntity(String.class));
                }
            } catch (Exception e) {
                throw new MojoExecutionException("Domain deploymend failed", e);
            }
        }
    }

}
