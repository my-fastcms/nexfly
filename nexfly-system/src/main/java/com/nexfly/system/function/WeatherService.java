package com.nexfly.system.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.nexfly.ai.common.function.NexflyFunction;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

@Service
@Description("某个城市的天气怎么样")
public class WeatherService implements NexflyFunction<WeatherService.Request, WeatherService.Response> {

    /**
     * Weather Function request.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("天气查询接口")
    public record Request(@JsonProperty(required = true, value = "location") @JsonPropertyDescription("城市，如北京、广州、上海") String location,
                          @JsonProperty("unit") @JsonPropertyDescription("温度单位") Unit unit) {
    }

    /**
     * Temperature units.
     */
    public enum Unit {

        /**
         * Celsius.
         */
        C("metric"),
        /**
         * Fahrenheit.
         */
        F("imperial");

        /**
         * Human readable unit name.
         */
        public final String unitName;

        private Unit(String text) {
            this.unitName = text;
        }

    }

    /**
     * Weather Function response.
     */
    public record Response(double temp, double feels_like, double temp_min, double temp_max, int pressure, int humidity, Unit unit) {
    }

    @Override
    public Response apply(Request request) {

        double temperature = 0;
        if (request.location().contains("北京")) {
            temperature = 15;
        }
        else if (request.location().contains("广州")) {
            temperature = 10;
        }
        else if (request.location().contains("上海")) {
            temperature = 30;
        }

        return new Response(temperature, 15, 20, 2, 53, 45, request.unit);
    }

}
