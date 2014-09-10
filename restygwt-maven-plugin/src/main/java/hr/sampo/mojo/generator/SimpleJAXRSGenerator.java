package hr.sampo.mojo.generator;

import hr.sampo.mojo.generator.interfaces.ClientGenerator;
import hr.sampo.mojo.generator.interfaces.GenerationContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Util to consolidate classpath manipulation stuff in one place.
 * 
 * @author ccollins
 * @version $Id$
 * @plexus.component role="hr.sampo.mojo.generator.interfaces.ClientGenerator"
 * 
 */
public class SimpleJAXRSGenerator implements ClientGenerator {

	private GenerationContext context;
	
	@Override
	public void setContext(GenerationContext context) {
		this.context = context;
	}
	
	@Override
	public void generateAsync(JavaClass clazz, File targetFile)
			throws IOException {

		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(context.getBuildContext()
						.newFileOutputStream(targetFile), context.getEncoding())));

//		boolean hasRemoteServiceRelativePath = hasRemoteServiceRelativePath(clazz);

		String className = clazz.getName();
		if (clazz.getPackage() != null) {
			writer.println("package " + clazz.getPackageName() + ";");
			writer.println();
		}
		writer.println("import com.google.gwt.core.client.GWT;");
		writer.println("import com.google.gwt.user.client.rpc.AsyncCallback;");

//		if (!hasRemoteServiceRelativePath) {
//			writer.println("import com.google.gwt.user.client.rpc.ServiceDefTarget;");
//		}

		writer.println();
		writer.println("public interface " + className + "Async");
		writer.println("{");

//		List<JavaMethod> methods = clazz.getMethods(true);
//		for (JavaMethod method : methods) {
//			
//		}

		writer.println();
		writer.println("    /**");
		writer.println("     * Utility class to get the RPC Async interface from client-side code");
		writer.println("     */");
		writer.println("    public static final class Util ");
		writer.println("    { ");
		writer.println("        private static " + className
				+ "Async instance;");
		writer.println();
		writer.println("        public static final " + className
				+ "Async getInstance()");
		writer.println("        {");
		writer.println("            if ( instance == null )");
		writer.println("            {");
		writer.println("                instance = (" + className
				+ "Async) GWT.create( " + className + ".class );");
//		if (!hasRemoteServiceRelativePath) {
//			String uri = MessageFormat.format(rpcPattern, className);
//			writer.println("                ServiceDefTarget target = (ServiceDefTarget) instance;");
//			writer.println("                target.setServiceEntryPoint( GWT.getModuleBaseURL() + \""
//					+ uri + "\" );");
//		}
		writer.println("            }");
		writer.println("            return instance;");
		writer.println("        }");
		writer.println("");
		writer.println("        private Util()");
		writer.println("        {");
		writer.println("            // Utility class should not be instanciated");
		writer.println("        }");
		writer.println("    }");

		writer.println("}");
		writer.close();

	}

}
