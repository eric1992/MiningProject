function output = matToArrayEventRec(ex)
    validateattributes(ex, {'numeric'}, {'size', [NaN, 7]});
    dimensions = size(ex);
    dimensions = dimensions(1);
    output = eventRecord.empty(dimensions, 0);
    output(1:dimensions) = eventRecord([0,0,0],[0,0,0],1);
    for i = 1:dimensions
        temp = ex(i,1:7);
        output(i) = eventRecord(temp(1:3),temp(4:6), temp(7));
    end
end
