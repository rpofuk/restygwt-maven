package __package__.server;

import java.text.SimpleDateFormat;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
@Provider
@Produces("application/json")
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

    private ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("deprecation")
	public JacksonConfigurator() {
        SerializationConfig serConfig = mapper.getSerializationConfig();
        serConfig.setDateFormat(new SimpleDateFormat("yyy-MM-dd HH:mm:ss"));
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        deserializationConfig.setDateFormat(new SimpleDateFormat("yyy-MM-dd HH:mm:ss"));
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> arg0) {
        return mapper;
    }

}