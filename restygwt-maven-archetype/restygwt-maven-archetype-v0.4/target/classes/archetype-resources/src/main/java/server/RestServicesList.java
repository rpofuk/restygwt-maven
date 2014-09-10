#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;

import java.lang.reflect.Method;
import java.util.List;

public interface RestServicesList {

	public List<Method> getApiMethods(); 
}
