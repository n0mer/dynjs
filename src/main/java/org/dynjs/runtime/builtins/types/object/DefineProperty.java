package org.dynjs.runtime.builtins.types.object;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class DefineProperty extends AbstractNativeFunction {

    public DefineProperty(GlobalObject globalObject) {
        super(globalObject, "o");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.3.6
        Object o = args[0];

        if (!(o instanceof JSObject)) {
            throw new ThrowException(context.createTypeError("must be an object"));
        }
        
        JSObject jsObj = (JSObject) o;
        
        String name = Types.toString( args[1] );
        Object attrs = args[2];
        
        PropertyDescriptor d = PropertyDescriptor.toPropertyDescriptor(context, attrs);
        jsObj.defineOwnProperty(context, name, d, true);
        
        return jsObj;
    }
}
