package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class ExpressionDeserializer extends JsonDeserializer<Expression>
{
	private final SpelExpressionParser parser = new SpelExpressionParser();

	@Override
	public Expression deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
	{
		String conditionString = jsonParser.getText();
		return parser.parseExpression(conditionString);
	}
}
