package hr.sampo.mojo.generator.interfaces;

import java.io.File;
import java.io.IOException;

import com.thoughtworks.qdox.model.JavaClass;

public interface ClientGenerator {

	/**
	 * @param clazz
	 *            the RPC service java class
	 * @param targetFile
	 *            RemoteAsync file to generate
	 * @throws Exception
	 *             generation failure
	 */
	void generateAsync(JavaClass clazz, File targetFile)
			throws IOException;
	
	void setContext(GenerationContext context);
}
