classdef OctNode
    %OCTNODE Summary of this class goes here
    %   Detailed explanation goes here
    
    properties
        data;
    end
    
    methods
        function tf = isInternal(obj)
            validateattributes(obj, {'OctNode'}, {'nonempty'});
            tf = strcmp(class(obj.data), 'OctNode');
        end
        function tf = isLeaf(obj)
            validateattributes(obj, {'OctNode'}, {'nonempty'});
            tf = strcmp(class(obj.data), 'vPoint');
        end
        function tf = isEmpty(obj)
            validateattributes(obj, {'OctNode'}, {'nonempty'});
            tf = strcmp(class(obj.data), 'double');
        end            
        function obj = makeInternal(obj)
            validateattributes(obj, {'OctNode'}, {'nonempty'});
            obj.data = OctNode.empty(8, 0);
            obj.data(1:8) = OctNode;
        end
        function obj = makeEmpty(obj)
            validateattributes(obj, {'OctNode'}, {'nonempty'});
            obj.data = OctNode;
        end
        function obj = makeLeaf(obj, sample)
            validateattributes(obj, {'OctNode'}, {'nonempty'});
            validateattributes(sample, {'vPoint'}, {'nonempty'});
            obj.data = sample;
        end
        function obj = insertHelper(obj, sample, xSup, xInf, ySup, yInf, zSup, zInf)
            validateattributes(obj, {'OctNode'},{'nonempty'})
            validateattributes(sample, {'vPoint'}, {'nonempty'})
            validateattributes(xSup, {'numeric'}, {'size', [1,1]});
            validateattributes(xInf, {'numeric'}, {'size', [1,1]});
            validateattributes(ySup, {'numeric'}, {'size', [1,1]});
            validateattributes(yInf, {'numeric'}, {'size', [1,1]});
            validateattributes(zSup, {'numeric'}, {'size', [1,1]});
            validateattributes(zInf, {'numeric'}, {'size', [1,1]});
            if(~inBox(getCoord(sample), xSup, xInf, ySup, yInf, zSup, zInf))
                return;
            end
            if(isEmpty(obj))
                obj = makeLeaf(obj, sample);
            elseif(isLeaf(obj))
                temp = obj.data;
                obj = makeInternal(obj);
                obj = insertHelper(obj, temp, xSup, xInf, ySup, yInf, zSup, zInf);
                obj = insertHelper(obj, sample, xSup, xInf, ySup, yInf, zSup, zInf);
            elseif(isInternal(obj))
                xAvg = (xSup + xInf) / 2;
                yAvg = (ySup + yInf) / 2;
                zAvg = (zSup + zInf) / 2;
                direction = quadFrom(Point([xAvg, yAvg, zAvg]), getCoord(sample));
                switch(direction)
                    case 'UNE'
                        obj.data(1) = insertHelper(obj.data(1), sample, xSup, xAvg, ySup, yAvg, zSup, zAvg);
                    case 'UNW'
                        obj.data(2) = insertHelper(obj.data(2), sample, xAvg, xInf, ySup, yAvg, zSup, zAvg);
                    case 'USW'
                        obj.data(3) = insertHelper(obj.data(3), sample, xAvg, xInf, yAvg, yInf, zSup, zAvg);
                    case 'USE'
                        obj.data(4) = insertHelper(obj.data(4), sample, xSup, xAvg, yAvg, yInf, zSup, zAvg);
                    case 'DNE'
                        obj.data(5) = insertHelper(obj.data(5), sample, xSup, xAvg, ySup, yAvg, zAvg, zInf);
                    case 'DNW'
                        obj.data(6) = insertHelper(obj.data(6), sample, xAvg, xInf, ySup, yAvg, zAvg, zInf);
                    case 'DSW'
                        obj.data(7) = insertHelper(obj.data(7), sample, xAvg, xInf, yAvg, yInf, zAvg, zInf);
                    case 'DSE'
                        obj.data(8) = insertHelper(obj.data(8), sample, xSup, xAvg, yAvg, yInf, zAvg, zInf);
                    case 'NOD'
                end
            end
        end  
        function obj = findHelper(root, sample, xSup, xInf, ySup, yInf, zSup, zInf)
            validateattributes(root, {'OctNode'},{'nonempty'})
            validateattributes(sample, {'Point'}, {'nonempty'})
            validateattributes(xSup, {'numeric'}, {'size', [1,1]});
            validateattributes(xInf, {'numeric'}, {'size', [1,1]});
            validateattributes(ySup, {'numeric'}, {'size', [1,1]});
            validateattributes(yInf, {'numeric'}, {'size', [1,1]});
            validateattributes(zSup, {'numeric'}, {'size', [1,1]});
            validateattributes(zInf, {'numeric'}, {'size', [1,1]});
            if(~inBox(sample, xSup, xInf, ySup, yInf, zSup, zInf))
                obj = 0;
            end
            if(isEmpty(root))
                obj = 0;
            elseif(isLeaf(root))
                if(distanceFrom(getCoord(root.data), sample) == 0)
                    obj = root.data;
                else
                    obj = 0;
                end
            elseif(isInternal(root))
                xAvg = (xSup + xInf) / 2;
                yAvg = (ySup + yInf) / 2;
                zAvg = (zSup + zInf) / 2;
                direction = quadFrom(Point([xAvg, yAvg, zAvg]), sample);
                switch(direction)
                    case 'UNE'
                        obj = findHelper(root.data(1), sample, xSup, xAvg, ySup, yAvg, zSup, zAvg);
                    case 'UNW'
                        obj = findHelper(root.data(2), sample, xAvg, xInf, ySup, yAvg, zSup, zAvg);
                    case 'USW'
                        obj = findHelper(root.data(3), sample, xAvg, xInf, yAvg, yInf, zSup, zAvg);
                    case 'USE'
                        obj = findHelper(root.data(4), sample, xSup, xAvg, yAvg, yInf, zSup, zAvg);
                    case 'DNE'
                        obj = findHelper(root.data(5), sample, xSup, xAvg, ySup, yAvg, zAvg, zInf);
                    case 'DNW'
                        obj = findHelper(root.data(6), sample, xAvg, xInf, ySup, yAvg, zAvg, zInf);
                    case 'DSW'
                        obj = findHelper(root.data(7), sample, xAvg, xInf, yAvg, yInf, zAvg, zInf);
                    case 'DSE'
                        obj = findHelper(root.data(8), sample, xSup, xAvg, yAvg, yInf, zAvg, zInf);
                    case 'NOD'
                        obj = 0;
                end
            end
        end
    end
    
end

