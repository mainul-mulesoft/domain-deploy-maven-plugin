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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

public abstract class AbstractMuleDomainMojo extends AbstractMojo {

    // Final artifact name?
    @Parameter(defaultValue = "${artifactId}")
    private String domainName;

    @Parameter( defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    @Parameter
    private String urls;

    private File domain;

    protected File getDomain() {
        if (domain == null) {
            String type = project.getArtifact().getType();
            if (!"mule-domain".equals(type)) {
                throw new IllegalArgumentException("Project packaging should be mule-domain but is: " + type);
            }
            if (project.getAttachedArtifacts().isEmpty()) {
                throw new IllegalArgumentException("No project artifact was found.");
            }
            domain = ((Artifact) this.project.getAttachedArtifacts().get(0)).getFile();
        }

        return domain;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
