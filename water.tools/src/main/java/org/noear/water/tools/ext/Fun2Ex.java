package org.noear.water.tools.ext;

public interface Fun2Ex<T1,T2,R> {
    R run(T1 t1, T2 t2) throws Exception;
}