classdef OctTree
    %OCTTREE Summary of this class goes here
    %   Detailed explanation goes here
    
    properties
        root;
        xMax;
        xMin;
        yMax;
        yMin;
        zMax;
        zMin;
    end
    
    methods
        function obj = OctTree(xSup, xInf, ySup, yInf, zSup, zInf)
            obj.root = OctNode;
            obj.xMax = xSup;
            obj.xMin = xInf;
            obj.yMax = ySup;
            obj.yMin = yInf;
            obj.zMax = zSup;
            obj.zMin = zInf;
        end
        function obj = insert(obj, sample)
            validateattributes(obj, {'OctTree'},{'nonempty'})
            validateattributes(sample, {'vPoint'}, {'nonempty'})
            obj.root = insertHelper(obj.root, sample, obj.xMax, obj.xMin, obj.yMax, obj.yMin, obj.zMax, obj.zMin);
        end
        function obj = find(top, sample)
            validateattributes(top, {'OctTree'},{'nonempty'})
            validateattributes(sample, {'Point'}, {'nonempty'})
            obj = findHelper(top, sample, top.xMax, top.xMin, top.yMax, top.yMin, top.zMax, top.zMin);
        end
    end
    
end

