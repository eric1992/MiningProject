classdef eventRecord
    %EVENTRECORD Summary of this class goes here
    %   Detailed explanation goes here
    
    properties(SetAccess = private)
        eventCoord = Point([0,0,0]);
        receiverCoord = Point([0,0,0]);
        deltaT = 0;
    end
    
    methods
        %method constructor
        function record = eventRecord(event, receiver, deltaT)
            record = setEvent(record, event);
            record = setReceiver(record, receiver);
            record = setDeltaT(record, deltaT);
        end
        %{
            Used to safely set the event property of an event record
            @PARAM
                obj         Must be a valid eventRecord object
                newEvent    Must be a valid 1x3 vector
            @WARNINGS
                If conditions for parameters are not met an error will be
                thrown explaining the error.
        %}
        function obj = setEvent(obj, newEvent)
            validateattributes(obj, {'eventRecord'}, {'nonempty'});
            validateattributes(newEvent,{'numeric'},{'size', [1,3]});
            obj.eventCoord = Point(newEvent);
        end
        %{
            Used to safely get the event property of an eventRecord object
            @PARAM
                obj     Must be a valid eventRecord object
            @WARNINGS
                If conditions for the parameter is not met an error will be
                thrown explaining the error
        %}
        function event = getEvent(obj)
            validateattributes(obj, {'eventRecord'}, {'nonempty'});
            event = obj.eventCoord;
        end
        %{
            Used to safely set the receiver property of an event record
            @PARAM
                obj             Must be a valid eventRecord object
                newReceiver     Must be a valid 1x3 vector
            @WARNINGS
                If conditions for parameters are not met an error will be
                thrown explaining the error.
        %}
        function obj = setReceiver(obj, newReceiver)
            validateattributes(obj, {'eventRecord'}, {'nonempty'});
            validateattributes(newReceiver,{'numeric'},{'size', [1,3]})
            obj.receiverCoord = Point(newReceiver);
        end
        %{
            Used to safely get the receiver property of an eventRecord object
            @PARAM
                obj     Must be a valid eventRecord object
            @WARNINGS
                If conditions for the parameter is not met an error will be
                thrown explaining the error
        %}
        function receiver = getReceiver(obj)
            validateattributes(obj, {'eventRecord'}, {'nonempty'});
            receiver = obj.receiverCoord;            
        end
        %{
            Used to safely set the event property of an event record
            @PARAM
                obj         Must be a valid eventRecord object
                newEvent    Must be a valid 1x1 vector with a value greater
                            than 0
            @WARNINGS
                If conditions for parameters are not met an error will be
                thrown explaining the error.
        %}
        function obj = setDeltaT(obj, newDT)
            validateattributes(obj, {'eventRecord'}, {'nonempty'});
            validateattributes(newDT,{'numeric'},{'>', 0, 'size',[1,1]})
            obj.deltaT = newDT;
        end
        %{
            Used to safely get the deltaT property of an eventRecord object
            @PARAM
                obj     Must be a valid eventRecord object
            @WARNINGS
                If conditions for the parameter is not met an error will be
                thrown explaining the error
        %}
        function dT = getDeltaT(obj)
            validateattributes(obj, {'eventRecord'}, {'nonempty'});
            dT = obj.deltaT;
        end
        %{
            Used to what properties of two eventRecord objects are equal.
            @PARAM
                a   Must be a valid eventRecord object.
                b   Must be a valid eventRecord object.
            @WARNINGS
                a and b must satisfy the requirements otherwises an error
                will be thrown explaining what is wrong
            @RETURN
                eqEvent     1 if a.eventCoord equals b.eventCoord, 0
                            otherwise.
                eqReceiver  1 if a.receiverCoord equals b.receiverCoord, 0
                            otherwise.
                eqDeltaT    1 if a.deltaT equals b.deltaT, 0 otherwise.
        %}
        function array = recordsEqualPartial(a, b)
            validateattributes(a, {'eventRecord'}, {'nonempty'});
            validateattributes(b, {'eventRecord'}, {'nonempty'});
            array(1) = quadFrom(a.eventCoord, b.eventCoord) == 'NOD';
            array(2) = quadFrom(a.receiverCoord, b.receiverCoord) == 'NOD';
            array(3) = a.deltaT == b.deltaT;
        end
        %{
            Used to determine if two event records are exactly equal.
            @PARAM
                a   Must be a valid eventRecord object.
                b   Must be a valid eventRecord object.
            @WARNINGS
                a and b must satisfy the requirements otherwises an error
                will be thrown explaining what is wrong
            @RETURN
                eq  1 if all properties of a are equal to their counter
                parts in b, 0 otherwise.
        %}
        function equal = recordsEqualExact(a, b)
            validateattributes(a, {'eventRecord'}, {'nonempty'});
            validateattributes(b, {'eventRecord'}, {'nonempty'});
            temp = recordsEqualPartial(a,b);
            equal = all([1,1,1] == temp);
        end
    end
end
    


