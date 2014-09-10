#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Application;

import ${package}.server.services.GreetingService;

public class RestResoureces extends Application implements RestServicesList {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		classes.add(GreetingService.class);

		classes = filterInvalid(classes);
		classes.add(JacksonConfigurator.class);
		return classes;
	}

	private Set<Class<?>> filterInvalid(Set<Class<?>> list) {
		return list;
	}

	@Override
	public List<Method> getApiMethods() {
		List<Method> methods = new ArrayList<Method>();

		for (Class<?> clazz : getClasses()) {
			for (Method method : clazz.getDeclaredMethods()) {
				if (isServiceMethod(method)) {
					methods.add(method);
				}
			}
			for (Method method : clazz.getSuperclass().getDeclaredMethods()) {
				if (isServiceMethod(method)) {
					methods.add(method);
				}
			}
		}
		return methods;
	}

	private boolean isServiceMethod(Method method) {
		if (method.getAnnotation(GET.class) != null) {
			return true;
		} else if (method.getAnnotation(POST.class) != null) {
			return true;
		} else if (method.getAnnotation(PUT.class) != null) {
			return true;
		} else if (method.getAnnotation(DELETE.class) != null) {
			return true;
		}
		return false;
	}

}
