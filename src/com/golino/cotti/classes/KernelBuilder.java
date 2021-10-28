package com.golino.cotti.classes;

import java.util.List;

public interface KernelBuilder {
    Kernel build(List<Item> items, Configuration config);
}