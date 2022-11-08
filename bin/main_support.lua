package.cpath = package.cpath .. ';/home/diegeijo/Project/lib/mylib.so'
ml = require 'mylib'

function print(s)
    ml.execute_callback(s)
end