package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;

@Slf4j
public class ExpressionSerializer extends JsonSerializer<Expression>
{
	@Override
	public void serialize(Expression expression, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException
	{
		String conditionString = expression.getExpressionString();
		log.debug("Serializing expression: '{}'", conditionString);
		jsonGenerator.writeString(conditionString);
	}
}
