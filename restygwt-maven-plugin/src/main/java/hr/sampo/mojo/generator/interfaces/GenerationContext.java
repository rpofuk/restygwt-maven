package hr.sampo.mojo.generator.interfaces;

import org.sonatype.plexus.build.incremental.BuildContext;

public interface GenerationContext {

	String getEncoding();
	
	BuildContext getBuildContext();
	
}
