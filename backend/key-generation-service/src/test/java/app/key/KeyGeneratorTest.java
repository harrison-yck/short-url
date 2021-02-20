package app.key;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyGeneratorTest {
    @Test
    void generator() {
        var generator = new KeyGenerator();
        assertThat(generator.generate(6, 1)).startsWith("B0");
        assertThat(generator.generate(6, 63)).startsWith("BB0");
    }
}
