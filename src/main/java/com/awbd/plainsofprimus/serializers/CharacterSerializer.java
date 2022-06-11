package com.awbd.plainsofprimus.serializers;

import com.awbd.plainsofprimus.domain.Character;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

public class CharacterSerializer extends StdSerializer<Character> {
    @Data
    @AllArgsConstructor
    class CharacterIdName {
        private Long id;
        private String name;
    }

    public CharacterSerializer() {
        this(null);
    }

    public CharacterSerializer(Class<Character> t) {
        super(t);
    }

    @Override
    public void serialize(Character character, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        CharacterIdName characterIdName = new CharacterIdName(character.getId(), character.getName());
        jsonGenerator.writeObject(characterIdName);
    }
}
