package api.json;

import java.io.Serializable;

/**
 * Marca uma classe como passível de ser convertida em json
 */
public interface Jsonable extends Serializable {

    String type();
}
