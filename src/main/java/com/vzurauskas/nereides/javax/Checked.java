package com.vzurauskas.nereides.javax;

interface Checked<T> {
    T value() throws Exception;
}
