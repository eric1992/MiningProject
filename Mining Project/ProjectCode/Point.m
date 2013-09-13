classdef Point
    %POINT Used to store 3D coordinate data.
    %   Adds functionality not inherint to a 1X3 vector.
    
    properties
        coords = [];
    end
    
    methods
        function obj = Point(coord)
            validateattributes(coord, {'numeric'}, {'size', [1, 3]});
            obj.coords(1) = coord(1);
            obj.coords(2) = coord(2);
            obj.coords(3) = coord(3);
        end
        function out = getCoord(in)
            validateattributes(in, {'Point'}, {'nonempty'});
            out = in.coords;
        end
        function obj = setCoord(obj, newCoord)
            validateattributes(obj, {'Point'}, {'nonempty'});
            validateattributes(newCoord, {'numeric'}, {'size', [1,3]});
            obj.coords = newCoord;
        end
        function out = getVelocity(in)
            validateattributes(in, {'Point'}, {'nonempty'});
            out = in.velocity;
        end
        function obj = setVelocity(obj, newVelocity)
            validateattributes(obj, {'Point'}, {'nonempty'});
            validateattributes(newVelocity, {'numeric'}, {'size', [1,1]});
            obj.velocity = newVelocity;
        end
        %{
            Used to determine which quadrant a 3d point lies in from
            a Point object.
            @PARAM
                A   A 1X3 matrix of numerics.
                B   A 1X3 matrix of numerics.
            @RETURN
                Three characters representing the quadrant in which B lies
                from A.
                    UNE- Up and to the north east.
                    UNW- Up and to the north west.
                    USW- Up and to the south west.
                    USE- Up and to the south east.
                    DNE- Down and to the north east.
                    DNW- Down and to the north west.
                    DSW- Down and to the south west.
                    DSE- Down and to the south east.
                    NOD- Collision with the origin.
        `       Note on XYZ plane collision
                    Collision with the z-plane, zero difference in z
                    coordinates cause B to be considered to be part of one
                    of the Up quadrants
                    Collisions with the X or Y axis will be considered to
                    be pushed counter-clockwise on the XY-plane.
                    Collisions with the XY plane origin that do not collide
                    with the Z origin will be pushed to the NE quadrant and
                    then follow the z-plane collision rule.
        %}
        function direction = quadFrom(A, B)
            validateattributes(A, {'Point'}, {'nonempty'});
            validateattributes(B, {'Point'}, {'nonempty'});
            difX = B.coords(1) - A.coords(1);
            difY = B.coords(2) - A.coords(2);
            difZ = B.coords(3) - A.coords(3);
            if difX == 0 && difY == 0 && difZ == 0
                direction = 'NOD';
            elseif difZ >= 0
                if (difX > 0 && difY >= 0) || (difX == 0 && difY == 0)
                    direction = 'UNE';
                elseif difX <= 0 && difY >  0
                    direction = 'UNW';
                elseif difX < 0 && difY <= 0
                    direction = 'USW';
                elseif difX >= 0 && difY < 0
                    direction = 'USE';
                end
            elseif difZ < 0
                if (difX > 0 && difY >= 0) || (difX == 0 && difY == 0)
                    direction = 'DNE';
                elseif difX <= 0 && difY >  0
                    direction = 'DNW';
                elseif difX < 0 && difY <= 0
                    direction = 'DSW';
                elseif difX >= 0 && difY < 0
                    direction = 'DSE';
                end
            end
        end
        %{
            Used to determine which quadrant a 3d point lies in from
            a Point object.
            @PARAM
                A   A 1X3 matrix of numerics.
                B   A 1X3 matrix of numerics.
        %}
        function distance = distanceFrom(A,B)
            validateattributes(A, {'Point'}, {'nonempty'});
            validateattributes(B, {'Point'}, {'nonempty'});
            distance = sqrt((A.coords(1)-B.coords(1))^2+(A.coords(2)-B.coords(2))^2+(A.coords(3)-B.coords(3))^2);
        end
        function tf = inBox(A, xSup, xInf, ySup, yInf, zSup, zInf)
            validateattributes(A, {'Point'}, {'nonempty'});
            validateattributes(xSup, {'numeric'}, {'size', [1,1]});
            validateattributes(xInf, {'numeric'}, {'size', [1,1]});
            validateattributes(ySup, {'numeric'}, {'size', [1,1]});
            validateattributes(yInf, {'numeric'}, {'size', [1,1]});
            validateattributes(zSup, {'numeric'}, {'size', [1,1]});
            validateattributes(zInf, {'numeric'}, {'size', [1,1]});
            temp = getCoord(A);
            tf = ((temp(1) <= xSup) && (temp(1) >= xInf)) && ((temp(1) <= ySup) && (temp(1) >= yInf)) && ((temp(1) <= zSup) && (temp(1) >= zInf));
        end
    end
end

