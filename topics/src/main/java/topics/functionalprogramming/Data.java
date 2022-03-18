package topics.functionalprogramming;

import java.util.Arrays;

/**
 * toy class to test the ternary operator
 */
class Data {
    private final String key;
    private final String[] values;

    public String getKey() {
        return key;
    }

    public String[] getValues() {
        return values;
    }

    public Data(String key, String[] values) {
        this.key = key;
        this.values = values;
    }

    @Override
    public String toString() {
        return "key: " + getKey() + " values: " + Arrays.toString(getValues());
    }
}
