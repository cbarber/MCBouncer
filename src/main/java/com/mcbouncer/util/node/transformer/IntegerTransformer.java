package com.mcbouncer.util.node.transformer;

import org.apache.commons.collections.Transformer;

/**
 * Apache Commons collection transformer
 * Transforms objects to integers.
 * 
 */
public class IntegerTransformer implements Transformer {

    public Object transform(Object o) {
        if (o == null) {
            return null;
        }

        return Integer.parseInt(o.toString());
    }
}
