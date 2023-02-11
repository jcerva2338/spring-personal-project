package johncervantes.springproject.entity;

import java.util.HashMap;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HashMapConverter implements AttributeConverter<HashMap<String, Integer>, String> {

	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public String convertToDatabaseColumn(HashMap<String, Integer> attribute) {
		String hashMapJson = null;
		
		try {
			hashMapJson = objectMapper.writeValueAsString(attribute);
		} catch (Exception e) {
		}
		
		return hashMapJson;
	}

	@Override
	public HashMap<String, Integer> convertToEntityAttribute(String hashMapJson) {
		HashMap<String, Integer> hashMap = null;
		
		try {
			hashMap = objectMapper.readValue(hashMapJson, new TypeReference<HashMap<String, Integer>>(){});
		} catch (Exception e) {
		}
		
		return hashMap;
	}

}
