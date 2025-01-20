package com.chen.bitten.common.utils;

import org.springframework.util.PropertiesPersister;
import org.springframework.util.PropertyPlaceholderHelper;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;

/**
 * 用于处理占位符
 */
public class ContentHolderUtil {

    private static final String PLACEHOLDER_PREFIX = "${";

    private static final String PLACEHOLDER_SUFFIX = "}";

    private static final PropertyPlaceholderHelper PROPERTY_PLACEHOLDER_HELPER = new PropertyPlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX);

    private ContentHolderUtil() {

    }

    public static String replacePlaceholder(String originValue, Map<String, String> variables) {
        return PROPERTY_PLACEHOLDER_HELPER.replacePlaceholders(originValue, new BittenPlaceholderResolver(originValue, variables));
    }

    private static class BittenPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        private final String originValue;
        private final Map<String, String> variables;

        public BittenPlaceholderResolver(String originValue, Map<String, String> variables) {
            super();
            this.originValue = originValue;
            this.variables = variables;
        }

        @Override
        public String resolvePlaceholder(String placeholderName) {
            if (Objects.isNull(variables)) {
                String errorMessage = MessageFormat.format("OriginValue: {0} require {1}, but not exits. Variables: {2}", originValue, placeholderName, null);
                throw new IllegalArgumentException(errorMessage);
            }
            String value = variables.get(placeholderName);
            if (value.isBlank()) {
                String errorMessage = MessageFormat.format("OriginValue: {0} require {1}, but not exits. Variables: {2}", originValue, placeholderName, variables);
                throw new IllegalArgumentException(errorMessage);
            }
            return value;
        }
    }


}
