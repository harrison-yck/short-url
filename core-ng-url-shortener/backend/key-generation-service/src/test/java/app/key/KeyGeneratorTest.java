package app.key;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyGeneratorTest {
    @Test
    void generator() {
        var generator = new KeyGenerator();
        assertThat(generator.generate(1, 6)).startsWith("B0");
        assertThat(generator.generate(63, 6)).startsWith("BB0");
    }
}
