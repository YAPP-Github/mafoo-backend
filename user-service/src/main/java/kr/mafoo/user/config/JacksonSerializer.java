package kr.mafoo.user.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.jsonwebtoken.io.AbstractSerializer;
import io.jsonwebtoken.lang.Assert;

import java.io.OutputStream;

public class JacksonSerializer<T> extends AbstractSerializer<T> {

    static final String MODULE_ID = "jjwt-jackson";
    static final Module MODULE;

    static {
        SimpleModule module = new SimpleModule(MODULE_ID);
      //  module.addSerializer(JacksonSupplierSerializer.INSTANCE);
        MODULE = module;
    }

    static final ObjectMapper DEFAULT_OBJECT_MAPPER = newObjectMapper();

    /**
     * Creates and returns a new ObjectMapper with the {@code jjwt-jackson} module registered and
     * {@code JsonParser.Feature.STRICT_DUPLICATE_DETECTION} enabled (set to true) and
     * {@code DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES} disabled (set to false).
     *
     * @return a new ObjectMapper with the {@code jjwt-jackson} module registered and
     * {@code JsonParser.Feature.STRICT_DUPLICATE_DETECTION} enabled (set to true) and
     * {@code DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES} disabled (set to false).
     *
     * @since 0.12.4
     */
    // package protected on purpose, do not expose to the public API
    static ObjectMapper newObjectMapper() {
        return new ObjectMapper()
                .registerModule(MODULE)
                .configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true) // https://github.com/jwtk/jjwt/issues/877
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // https://github.com/jwtk/jjwt/issues/893
    }

    protected final ObjectMapper objectMapper;

    /**
     * Constructor using JJWT's default {@link ObjectMapper} singleton for serialization.
     */
    public JacksonSerializer() {
        this(DEFAULT_OBJECT_MAPPER);
    }

    /**
     * Creates a new Jackson Serializer that uses the specified {@link ObjectMapper} for serialization.
     *
     * @param objectMapper the ObjectMapper to use for serialization.
     */
    public JacksonSerializer(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper cannot be null.");
        this.objectMapper = objectMapper.registerModule(MODULE);
    }

    @Override
    protected void doSerialize(T t, OutputStream out) throws Exception {
        Assert.notNull(out, "OutputStream cannot be null.");
        ObjectWriter writer = this.objectMapper.writer().without(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        writer.writeValue(out, t);
    }
}
