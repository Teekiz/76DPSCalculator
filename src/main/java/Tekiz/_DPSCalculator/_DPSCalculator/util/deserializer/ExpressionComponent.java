package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * A utility service used to allow a {@link JsonParser} to convert a {@link String} object into an {@link Expression} object.
 */
@Slf4j
@JsonComponent
public class ExpressionComponent
{
	public static class ExpressionSerializer extends JsonSerializer<Expression>
	{
		/**
		 * A method used to convert a {@link Expression} object into an {@link String} object.
		 * @param expression The {@link Expression} object to be converted into a string.
		 * @param jsonGenerator An object used to convert the resulted string into Json.
		 * @throws IOException If expression cannot be converted or the {@link JsonGenerator} cannot write the string to Json.
		 */
		@Override
		public void serialize(Expression expression, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException
		{
			String conditionString = expression.getExpressionString();
			log.debug("Serializing expression: '{}'", conditionString);
			jsonGenerator.writeString(conditionString);
		}
	}
	public static class ExpressionDeserializer extends JsonDeserializer<Expression>
	{
		private final SpelExpressionParser parser = new SpelExpressionParser();

		/**
		 * A method used to convert a {@link String} object into an {@link Expression} object.
		 * @param jsonParser The {@link JsonParser} providing the JSON input as a string.
		 * @param deserializationContext The context for deserialization (not used in this implementation).
		 * @return An {@link Expression} object representing the parsed {@link String} condition.
		 * @throws IOException If there is an error during the parsing process or reading input.
		 */
		@Override
		public Expression deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
		{
			String conditionString = jsonParser.getText();
			log.debug("Deserializing expression: '{}'", conditionString);
			return parser.parseExpression(conditionString);
		}
	}
}
