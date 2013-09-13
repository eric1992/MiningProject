classdef vPoint
    %VPOINT Used to store velocity data at various points in space.
    %   Adds functionality needed to perform SIRT on a set of points.  This
    %   includes storing a matrix of values indicating the error of the
    %   assumed velocity based on the error of assumed velocity data at
    %   other points. Also includes methods to correct this error.
    
    properties
        %A Point object.
        coord;
        assumedV = 1;
        error = zeros(1,10);
    end
    methods
        function obj = vPoint(pCoords)
            validateattributes(pCoords, {'Point'}, {'nonempty'});
            obj.error(1:10) = -1;
            obj.coord = pCoords;
        end
        function obj = setCoord(obj, newCoord)
            validateattributes(obj, {'vPoint'}, {'nonempty'});
            validateattributes(newCoord, {'Point'}, {'nonempty'});
            obj.coord = newCoord;
        end
        function xyz = getCoord(obj)
            validateattributes(obj, {'vPoint'}, {'nonempty'});
            xyz = obj.coord;
        end
        function obj = setVelocity(obj, newV)
            validateattributes(newV, {'numeric'}, {'size', [1,1]});
            validateattributes(obj, {'vPoint'}, {'nonempty'});
            obj.assumedV = newV;
        end
        function obj = resetError(obj)
            validateattributes(obj, {'vPoint'}, {'nonempty'});
            Size = size(obj.error);
            Size = Size(2);
            obj.error(1:Size) = -1;
        end
        %{
            Corrects the velocity to be the current velocity time the
            average error up until this function is called.
        %}
        function obj = correctVelocity(obj)
            validateattributes(obj, {'vPoint'}, {'nonempty'});
            errorList = obj.error;
            sizeError = numel(errorList);
            sumTotal = [0,0];
            for i = 1:sizeError
                if errorList(i) ~= -1
                    sumTotal(1) = sumTotal(1) + 1;
                    sumTotal(2) = sumTotal(2) + errorList(i);
                end
            end
            avgError = sumTotal(2) / sumTotal(1);
            obj.assumedV = obj.assumedV * avgError;
            obj = resetError(obj);
        end
        function obj = addError(obj, newE)
            validateattributes(obj, {'vPoint'}, {'nonempty'});
            validateattributes(newE, {'numeric'}, {'size', [1,1]});
            sizeError = size(obj.error);    
            for i = 1:sizeError(2)
                if(i == sizeError+1)
                    obj.error(i) = newE;
                    return
                elseif(obj.error(i) == -1)
                    obj.error(i) = newE;
                    return
                end
            end
            obj.error(sizeError(2)+1) = newE;
        end
    end
    
end

