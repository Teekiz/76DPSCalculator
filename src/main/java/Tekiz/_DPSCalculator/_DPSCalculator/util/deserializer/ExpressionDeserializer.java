package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * A utility service used to allow a {@link JsonParser} to convert a {@link String} object into an {@link Expression} object.
 */
public class ExpressionDeserializer extends JsonDeserializer<Expression>
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
		return parser.parseExpression(conditionString);
	}
}
