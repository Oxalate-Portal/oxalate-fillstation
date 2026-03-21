package io.oxalate.fillstation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ApplicationITC {

    @Test
    void contextLoads_defaultContext_Ok() {
    }

}

