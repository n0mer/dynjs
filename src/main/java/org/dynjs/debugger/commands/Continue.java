package org.dynjs.debugger.commands;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.requests.ContinueRequest;
import org.dynjs.debugger.requests.ContinueResponse;

/**
 * @author Bob McWhirter
 */
public class Continue extends AbstractCommand<ContinueRequest, ContinueResponse> {

    public Continue(Debugger debugger) {
        super(debugger, ContinueRequest.class, ContinueResponse.class);
    }

    @Override
    public ContinueResponse handle(ContinueRequest request) {
        if ( request.getArguments() == null ) {
            this.debugger.run();
        } else {
            String action = request.getArguments().getStepAction();

            if ( action.equals( "next" ) ) {
                this.debugger.stepNext();
            } else if ( action.equals( "in" ) ) {
                this.debugger.stepIn();
            } else if ( action.equals( "out" ) ) {
                this.debugger.stepOut();
            }
        }
        return new ContinueResponse(request, true, true);
    }
}
