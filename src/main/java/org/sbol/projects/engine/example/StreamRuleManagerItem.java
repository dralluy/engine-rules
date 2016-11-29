package org.sbol.projects.engine.example;

import org.sbol.projects.engine.rules.StreamRuleManager;
import org.springframework.stereotype.Component;

/**
 * Example of stream rule manager for Item processing.
 *
 * @author david.ralluy
 *
 */
@Component
public class StreamRuleManagerItem extends StreamRuleManager<Item> {

    @Override
    protected Class<Item> getTargetClass() {
        return Item.class;
    }

}
