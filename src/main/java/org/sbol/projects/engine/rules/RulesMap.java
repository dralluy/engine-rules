package org.sbol.projects.engine.rules;

import java.util.HashMap;
import java.util.List;

import org.sbol.projects.engine.constants.Channel;

/**
 * Map for storing the business rules per channel.
 *
 * @author david.ralluy
 *
 * @param <T>
 */
public class RulesMap<T> extends HashMap<Channel, List<StreamRule<T>>> {

    private static final long serialVersionUID = 4758176037085223553L;

}
