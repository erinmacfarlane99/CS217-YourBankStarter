package your.bank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

public class DatabaseTestsExt {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @ExtendWith(H2Ext.class)
    public @interface DatabaseTest {}
}
