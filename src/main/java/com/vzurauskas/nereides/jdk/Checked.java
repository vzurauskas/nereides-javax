package com.vzurauskas.nereides.jdk;

interface Checked<T> {
    T value() throws Exception;
}
