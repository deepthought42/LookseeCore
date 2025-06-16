package com.looksee.models.serializer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.looksee.models.rules.Rule;
import com.looksee.models.rules.RuleFactory;

import lombok.NoArgsConstructor;

/**
 * A deserializer for {@link Rule} objects
 */
@NoArgsConstructor
public class RuleDeserializer extends JsonDeserializer<Rule> {
	private static Logger log = LoggerFactory.getLogger(RuleDeserializer.class);
 
    /**
     * {@inheritDoc}
     */
    @Override
    public Rule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		final String type = node.get("type").asText();
		final String value = node.get("value").asText();
		log.warn("type :: "+type);
		log.warn("value  ::   " + value);
		return RuleFactory.build(type, value);
	}
}