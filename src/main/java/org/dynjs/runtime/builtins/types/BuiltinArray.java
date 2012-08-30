package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.array.prototype.Join;
import org.dynjs.runtime.builtins.types.array.prototype.ToString;

public class BuiltinArray extends AbstractNativeFunction {

    public BuiltinArray(final GlobalObject globalObject) {
        super(globalObject);

        final DynArray proto = new DynArray();
        proto.put(null, "constructor", this, false );
        proto.put(null, "toString", new ToString( globalObject ), false );
        proto.put( null, "join", new Join(globalObject), false );
        proto.setPrototype( globalObject.getPrototypeFor( "Object" ));
        put(null, "prototype", proto, false );
        
        setPrototype( globalObject.getPrototypeFor( "Function" ));
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {
        DynArray arraySelf = (DynArray) self;

        if (self != Types.UNDEFINED) {
            if (args.length == 1) {
                final Number possiblyLen = Types.toNumber(args[0]);
                if ((possiblyLen instanceof Double) && ((Double) possiblyLen).isNaN()) {
                    arraySelf.setLength(1);
                    arraySelf.setElement(0, args[0]);
                } else {
                    arraySelf.setLength(possiblyLen.intValue());
                }
            } else {
                arraySelf.setLength(args.length);
                for (int i = 0; i < args.length; ++i) {
                    arraySelf.setElement(i, args[i]);
                }
            }
            return null;
        }
        return null;
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        DynArray o = new DynArray();
        o.setPrototype(getPrototype());
        return o;
    }

    // ----------------------------------------------------------------------

    public static DynArray newArray(ExecutionContext context) {
        BuiltinArray ctor = (BuiltinArray) context.getGlobalObject().get(context, "Array");
        return (DynArray) context.construct(ctor);
    }
}