package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import com.fasterxml.jackson.core.JsonToken;

public class ExpressionDeserializer extends JsonDeserializer<Expression>
{
	private final SpelExpressionParser parser = new SpelExpressionParser();

	@Override
	public Expression deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
	{
		String conditionString = "";

		if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL || jsonParser.getText().isEmpty())
		{
			conditionString = "true";
		}
		else
		{
			conditionString = jsonParser.getText();
		}

		return parser.parseExpression(conditionString);
	}
}
