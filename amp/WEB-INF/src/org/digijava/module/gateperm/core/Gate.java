/**
 * Gate.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.PropertyListable;
import org.digijava.module.gateperm.exception.NotBoundGateInputException;

/**
 * Gate.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 23.08.2007
 */
public abstract class Gate extends PropertyListable {
    
    
    public static Logger    logger = Logger.getLogger(Gate.class);

    protected Queue<String> parameters;

    protected Map	   scope;
    
    protected Map<String,String> state;

    public Gate(Map scope, Queue<String> parameters) {
	this.scope = scope;
	this.parameters = parameters;
	state=new HashMap<String, String>();
    }
    
    public Gate() {
	state=new HashMap<String, String>();
    }

    @Override
    public String getBeanName() {
	return this.getClass().getSimpleName();
    }
    
   
	
   
    

    public static Gate instantiateGate( String gateTypeName) {
	try {
	    Class gateType = Class.forName(gateTypeName);
	    Constructor constructor = gateType.getConstructor(new Class[] {  });
	    Object object = constructor.newInstance(new Object[] {});
	    return (Gate) object;
	} catch (SecurityException e) {
	    logger.error(e);
	} catch (NoSuchMethodException e) {
	    logger.error(e);
	} catch (IllegalArgumentException e) {
	    logger.error(e);
	} catch (InstantiationException e) {
	    logger.error(e);
	} catch (IllegalAccessException e) {
	    logger.error(e);
	} catch (InvocationTargetException e) {
	    logger.error(e);
	} catch (ClassNotFoundException e) {
	    logger.error(e);
	}
	return null;
    }
    
    public static Gate instantiateGate(Map scope, Queue<String> parameters, String gateTypeName) {
	Gate gate = Gate.instantiateGate(gateTypeName);
	gate.setScope(scope);
	gate.setParameters(parameters);
	return gate;
    }

    /**
         * overriden by subclasses to implement Gate logic
         * 
         * @return true if the gate is open for the given inputs + parameters
     * @throws NotBoundGateInputException 
         */
    public boolean isOpen() throws NotBoundGateInputException {
	
	if(parameterNames()!=null && (parameters==null || parameters.size()<parameterNames().length))
	    throw new NotBoundGateInputException("Not enough gate parameters. Gate "+ this.getClass()+" needs "+parameterNames().length+" parameters");
	if(scope==null) throw new NotBoundGateInputException("Scope cannot be null");
	
	//saving the mandatory keys along with their values
	state.clear();
	for (int i = 0; mandatoryScopeKeys()!=null && i < mandatoryScopeKeys().length; i++) {
	    Object object = (Cloneable) scope.get(mandatoryScopeKeys()[i]);
	    if(object==null) throw new NotBoundGateInputException("Mandatory scope parameter '"+
		    mandatoryScopeKeys()[i]+"' missing for Gate "+this.getClass());
	    state.put(mandatoryScopeKeys()[i], object.toString());
	}
	
	return logic();
    }
    	
    
    public abstract boolean logic();

    public abstract String[] parameterNames();
    
    public abstract String[] mandatoryScopeKeys();

    public abstract String description();

    
    public Queue<String> getParameters() {
        return parameters;
    }

    public void setParameters(Queue<String> parameters) {
        this.parameters = parameters;
    }

    public Map getScope() {
        return scope;
    }

    public void setScope(Map scope) {
        this.scope = scope;
    }

}
