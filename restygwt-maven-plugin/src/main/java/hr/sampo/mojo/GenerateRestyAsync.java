package hr.sampo.mojo;

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

import hr.sampo.mojo.generator.interfaces.ClientGenerator;
import hr.sampo.mojo.generator.interfaces.GenerationContext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.Scanner;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;

/**
 * Goal which generate Asyn REST interface.
 * 
 * @goal generateRestyAsync
 * @phase generate-sources
 * @requiresDependencyResolution compile
 * @threadSafe
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 * @version $Id$
 */
public class GenerateRestyAsync extends AbstractGwtMojo implements GenerationContext {

//	private static final String REMOTE_PATH_ANNOTATION = Path.class
//			.getCanonicalName();

	/**
	 * Pattern for GWT service interface
	 * 
	 * @parameter default-value="**\/*Service.java"
	 */
	private String servicePattern;

	/**
	 * Stop the build on error
	 * 
	 * @parameter default-value="true" property="maven.gwt.failOnError"
	 */
	private boolean failOnError;

	/**
	 * Pattern for GWT service interface
	 * 
	 * @parameter default-value="false" property="generateAsync.force"
	 */
	private boolean force;

	/**
	 * @parameter property="project.build.sourceEncoding"
	 */
	private String encoding;

	/** @component */	
	private BuildContext buildContext;
	
	/** @component */	
	private ClientGenerator clientGenerator;
	
	private JavaProjectBuilder JavaProjectBuilder;

	@Override
	public void execute() throws MojoExecutionException {
		if ("pom".equals(getProject().getPackaging())) {
			getLog().info("GWT generateAsync is skipped");
			return;
		}

		clientGenerator.setContext(this);
		
		if (encoding == null) {
			getLog().warn(
					"Encoding is not set, your build will be platform dependent");
			encoding = Charset.defaultCharset().name();
		}

		JavaProjectBuilder = createJavaProjectBuilder();
		
		@SuppressWarnings("unchecked")
		List<String> sourceRoots = getProject().getCompileSourceRoots();
		boolean generated = false;
		for (String sourceRoot : sourceRoots) {
			try {
				generated |= scanAndGenerateAsync(new File(sourceRoot));
			} catch (Throwable e) {
				getLog().error("Failed to generate Async interface", e);
				if (failOnError) {
					throw new MojoExecutionException(
							"Failed to generate Async interface", e);
				}
			}
		}
		if (generated) {
			getLog().debug("add compile source root " + getGenerateDirectory());
			addCompileSourceRoot(getGenerateDirectory());
		}
	}

	/**
	 * @param sourceRoot
	 *            the base directory to scan for RPC services
	 * @return true if some file have been generated
	 * @throws Exception
	 *             generation failure
	 */
	private boolean scanAndGenerateAsync(File sourceRoot) throws Exception {
		Scanner scanner = buildContext.newScanner(sourceRoot);
		scanner.setIncludes(new String[] { servicePattern });
		scanner.scan();
		String[] sources = scanner.getIncludedFiles();
		if (sources.length == 0) {
			return false;
		}
		boolean fileGenerated = false;
		for (String source : sources) {
			File sourceFile = new File(sourceRoot, source);
			File targetFile = getTargetFile(source);
			if (!force && buildContext.isUptodate(targetFile, sourceFile)) {
				getLog().debug(
						targetFile.getAbsolutePath()
								+ " is up to date. Generation skipped");
				// up to date, but still need to report generated-sources
				// directory as sourceRoot
				fileGenerated = true;
				continue;
			}

			String className = getTopLevelClassName(source);
			JavaClass clazz = JavaProjectBuilder.getClassByName(className);
			if (isEligibleForGeneration(clazz)) {
				getLog().debug(
						"Generating async interface for service " + className);
				targetFile.getParentFile().mkdirs();
				clientGenerator.generateAsync(clazz, targetFile);
				fileGenerated = true;
			}
		}
		return fileGenerated;
	}

	private File getTargetFile(String source) {
		String targetFileName = source.substring(0, source.length() - 5)
				+ "Async.java";
		File targetFile = new File(getGenerateDirectory(), targetFileName);
		return targetFile;
	}

	private boolean isEligibleForGeneration(JavaClass javaClass) {
		return true;
//		if (javaClass.getAnnotations().size() == 0) {
//			return false;
//		}
//		return javaClass.getAnnotations().get(0).getType().equals(
//				JavaProjectBuilder.getClassByName(REMOTE_PATH_ANNOTATION));
	}

	@SuppressWarnings("unchecked")
	private JavaProjectBuilder createJavaProjectBuilder() throws MojoExecutionException {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.setEncoding(encoding);
		builder.addClassLoader(getProjectClassLoader());
		for (String sourceRoot : (List<String>) getProject()
				.getCompileSourceRoots()) {
			builder.addSourceFolder(new File(sourceRoot));
		}
		return builder;
	}

	private String getTopLevelClassName(String sourceFile) {
		String className = sourceFile.substring(0, sourceFile.length() - 5); // strip
																				// ".java"
		return className.replace(File.separatorChar, '.');
	}

	private ClassLoader getProjectClassLoader() throws MojoExecutionException {
		Collection<File> classpath = getClasspath(Artifact.SCOPE_COMPILE);
		URL[] urls = new URL[classpath.size()];
		try {
			int i = 0;
			for (File classpathFile : classpath) {
				urls[i] = classpathFile.toURI().toURL();
				i++;
			}
		} catch (MalformedURLException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		return new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
	}

	@Override
	public String getEncoding() {
		if (encoding == null) {
			getLog().warn(
					"Encoding is not set, your build will be platform dependent");
			encoding = Charset.defaultCharset().name();
		}
		return encoding;
	}

	@Override
	public BuildContext getBuildContext() {
		return buildContext;
	}
}
